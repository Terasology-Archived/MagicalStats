/*
 * Copyright 2016 MovingBlocks
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.terasology.magicalstats.system;

import gnu.trove.iterator.TFloatIterator;
import gnu.trove.iterator.TIntIterator;
import gnu.trove.list.TFloatList;
import gnu.trove.list.TIntList;
import org.junit.Before;
import org.terasology.entitySystem.entity.EntityManager;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.event.ReceiveEvent;
import org.terasology.entitySystem.prefab.Prefab;
import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.RegisterMode;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.entitySystem.systems.UpdateSubscriberSystem;
import org.terasology.logic.characters.CharacterMovementComponent;
import org.terasology.logic.characters.MovementMode;
import org.terasology.logic.characters.events.AttackEvent;
import org.terasology.logic.health.EngineDamageTypes;
import org.terasology.logic.inventory.ItemComponent;
import org.terasology.math.TeraMath;
import org.terasology.protobuf.EntityData;
import org.terasology.registry.In;
import org.terasology.utilities.random.FastRandom;
import org.terasology.utilities.random.Random;
import org.terasology.magicalstats.component.ManaComponent;
import org.terasology.magicalstats.event.BeforeManaRegenEvent;
import org.terasology.magicalstats.event.BeforeManaRefillEvent;
import org.terasology.magicalstats.event.OnDrainedEvent;
import org.terasology.magicalstats.event.DoManaRefillEvent;
import org.terasology.magicalstats.event.DoManaRegenEvent;
import org.terasology.magicalstats.event.DoDrainEvent;
import org.terasology.magicalstats.event.ManaChangedEvent;
import org.terasology.magicalstats.event.OnManaRefillEvent;
import org.terasology.magicalstats.event.FullManaEvent;
import org.terasology.magicalstats.event.OnManaRegenEvent;
import org.terasology.magicalstats.event.BeforeDrainedEvent;



@RegisterSystem(RegisterMode.AUTHORITY)
public class ManaAuthoritySystem extends BaseComponentSystem implements UpdateSubscriberSystem {
    @In
    private EntityManager entityManager;

    @In
    private org.terasology.engine.Time time;

    private Random random = new FastRandom();

    @Override
    public void update(float delta) {
        for (EntityRef entity : entityManager.getEntitiesWith(ManaComponent.class)) {
            ManaComponent mana = entity.getComponent(ManaComponent.class);
            if (mana.currentMana <= 0) {
                continue;
            }
            if (mana.currentMana == mana.maxMana || mana.manaRegenRate == 0) {
                continue;
            }

            int manaRegenAmount = 0;
            manaRegenAmount = manaRegen(mana, manaRegenAmount);

            checkRefilled(entity, mana, manaRegenAmount);
        }
    }

    /**
     * Override the default behavior for an attack
     */
    @ReceiveEvent(components = ManaComponent.class, netFilter = RegisterMode.AUTHORITY)
    public void onDrainEntity(AttackEvent event, EntityRef targetEntity) {
        DrainEntity(event, targetEntity);
    }

    public void DrainEntity(AttackEvent event, EntityRef targetEntity) {
        int drain = 1;
        //TODO:Fix this
        Prefab damageType = EngineDamageTypes.PHYSICAL.get();
        // Calculate drain from item
        ItemComponent item = event.getDirectCause().getComponent(ItemComponent.class);
        if (item != null) {
            drain = item.baseDamage;
            if (item.damageType != null) {
                damageType = item.damageType;
            }
        }
    }

    private int manaRegen(ManaComponent mana, int fillAmount) {
        int newFill = fillAmount;
        while (time.getGameTimeInMs() >= mana.nextRegenTick) {
            newFill++;
            mana.nextRegenTick = mana.nextRegenTick + (long) (1000 / mana.manaRegenRate);
        }
        return newFill;
    }

    private void checkRefilled(EntityRef entity, ManaComponent mana, int fillAmount) {
        if (fillAmount > 0) {
            checkRefill(entity, fillAmount, entity);
        }
    }

    private void checkRefill(EntityRef entity, int manaFillAmount, EntityRef instigator) {
        BeforeManaRefillEvent beforeRefill = entity.send(new BeforeManaRefillEvent(manaFillAmount, instigator));
        if (!beforeRefill.isConsumed()) {
            int modifiedAmount = calculateTotal(beforeRefill.getBaseManaRegen(), beforeRefill.getMultipliers(), beforeRefill.getModifiers());
            if (modifiedAmount > 0) {
                doFill(entity, modifiedAmount, instigator);
            } else if (modifiedAmount > 0) {
                doDrain(entity, -modifiedAmount, EngineDamageTypes.HEALING.get(), instigator, EntityRef.NULL);
            }
        }
    }

    private void doFill(EntityRef entity, int fillAmount, EntityRef instigator) {
        ManaComponent mana = entity.getComponent(ManaComponent.class);
        if (mana != null) {
            int filledAmount = Math.min(mana.currentMana + fillAmount, mana.maxMana) - mana.currentMana;
            mana.currentMana += filledAmount;
            entity.saveComponent(mana);
            entity.send(new OnManaRegenEvent(fillAmount, filledAmount, instigator));
            if (mana.currentMana == mana.maxMana) {
                entity.send(new FullManaEvent(instigator));
            }
        }
    }

    private void doDrain(EntityRef entity, int drainAmount, Prefab damageType, EntityRef instigator, EntityRef directCause) {
        ManaComponent mana = entity.getComponent(ManaComponent.class);
        CharacterMovementComponent characterMovementComponent = entity.getComponent(CharacterMovementComponent.class);
        boolean ghost = false;
        if (characterMovementComponent != null) {
            ghost = (characterMovementComponent.mode == MovementMode.GHOSTING);
        }
        if ((mana != null) && !ghost) {
            int drainedAmount = mana.currentMana - Math.max(mana.currentMana - drainAmount, 0);
            mana.currentMana -= drainedAmount;
            mana.nextRegenTick = time.getGameTimeInMs() + TeraMath.floorToInt(mana.waitBeforeManaRegen * 1000);
            entity.saveComponent(mana);
            entity.send(new OnDrainedEvent(drainAmount, drainedAmount, damageType, instigator));
        }
    }

    @ReceiveEvent
    public void onDrain(DoDrainEvent event, EntityRef entity) {
        checkDrain(entity, event.getAmount(), event.getDamageType(), event.getInstigator(), event.getDirectCause());
    }

    private void checkDrain(EntityRef entity, int amount, Prefab damageType, EntityRef instigator, EntityRef directCause) {
        BeforeDrainedEvent beforeDrain = entity.send(new BeforeDrainedEvent(amount, damageType, instigator, directCause));
        if (!beforeDrain.isConsumed()) {
            int drainAmount = TeraMath.floorToInt(beforeDrain.getResultValue());
            if (drainAmount > 0) {
                doDrain(entity, drainAmount, damageType, instigator, directCause);
            } else {
                doFill(entity, -drainAmount, instigator);
            }
        }
    }

    private int calculateTotal(int base, TFloatList multipliers, TIntList modifiers) {
        // All modifiers and multipliers are added and multiplied respectively. Negative modifiers are capped to zero, multipliers remain
        float total = base;
        TIntIterator modifierIter = modifiers.iterator();
        while (modifierIter.hasNext()) {
            total += modifierIter.next();
        }
        total = Math.max(0, total);
        if (total == 0) {
            return 0;
        }
        TFloatIterator multiplierIter = multipliers.iterator();
        while (multiplierIter.hasNext()) {
            total *= multiplierIter.next();
        }
        return TeraMath.floorToInt(total);
    }

}

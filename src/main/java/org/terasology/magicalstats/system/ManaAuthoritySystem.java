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

import org.junit.Before;
import org.terasology.entitySystem.entity.EntityManager;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.event.ReceiveEvent;
import org.terasology.entitySystem.prefab.Prefab;
import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.RegisterMode;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.entitySystem.systems.UpdateSubscriberSystem;
import org.terasology.logic.health.EngineDamageTypes;
import org.terasology.magicalstats.component.ManaComponent;
import org.terasology.magicalstats.event.BeforeManaRefillEvent;
import org.terasology.protobuf.EntityData;
import org.terasology.registry.In;
import org.terasology.utilities.random.FastRandom;
import org.terasology.utilities.random.Random;

/**
 * Created by monkey on 12/5/16.
 */
@RegisterSystem(RegisterMode.AUTHORITY)
public class ManaAuthoritySystem extends BaseComponentSystem implements UpdateSubscriberSystem {
    @In
    private EntityManager entityManager;

    @In
    private org.terasology.engine.Time time;

    private Random random = new FastRandom();

    @Override
    public  void update(float delta) {
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

            checkRefilled(entity, mana, refillAmount);
        }
    }

    /**
     * Override the default behavior for an attack
     */
    @ReceiveEvent(components = ManaComponent.class, netFilter = RegisterMode.AUTHORITY)
    public void onDrainEntity(DrainEvent event, EntityRef targetEntity) {
        int drain = 1;
        //TODO:Fix this
    }

    private int manaRegen(ManaComponent mana, int fillAmount) {
        int newFill = fillAmount;
        while (time.getGameTimeInMs() >= mana.nextRegenTick) {
            newFill++;
            mana.nextRegenTick = mana.nextRegenTick + (long) (1000 / mana.manaRegenRate);
        }
        return newFill;
    }

    private void checkRefilled(EntityRef entity, ManaComponent mana, int manaFillAmount) {
        if (manaFillAmount > 0) {
            checkRefilled(entity, manaFillAmount, entity);
        }
    }
    private void checkRefill(EntityRef entity, ManaComponent mana, int manaFillAmount) {
        if (manaFillAmount > 0) {
            checkRefilled(entity, refillAmount, entity);
        }
    }
    private void doFill(EntityRef entity, int fillAmount, EntityRef instigator) {
        BeforeManaRefillEvent beforeRefill = entity.send(new BeforeManaRefillEvent(fillAmount, instigator));
        if (!beforeRefill.isConsumed()) {
            int modifiedAmount = calculateTotal(beforeRefill.getBaseManaRegen(), beforeRefill.getMultipliers(), beforeRefill.getModifiers());
            if (modifiedAmount > 0) {
                doFill(entity, modifiedAmount, instigator);
            } else if (modifiedAmount < 0) {
                doDrain(entity, -modifiedAmount, EngineDamageTypes.HEALING.get(), instigator, EntityRef.NULL);
            }
        }
    }
}

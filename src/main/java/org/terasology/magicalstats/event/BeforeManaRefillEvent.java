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
package org.terasology.magicalstats.event;

import gnu.trove.list.TFloatList;
import gnu.trove.list.TIntList;
import gnu.trove.list.array.TFloatArrayList;
import gnu.trove.list.array.TIntArrayList;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.event.ConsumableEvent;
/**
 * Created by monkey on 12/5/16.
 */
public class BeforeManaRefillEvent implements ConsumableEvent {
    private int baseManaRegen;
    private EntityRef instigator;

    private boolean consumed;
    private TFloatList multipliers = new TFloatArrayList();
    private TIntList modifiers = new TIntArrayList();

    public BeforeManaRefillEvent(int amount, EntityRef instigator) {
        this.baseManaRegen = amount;
        this.instigator = instigator;
    }
    public int getBaseManaRegen() {
        return baseManaRegen;
    }
    public EntityRef getInstigator() {
        return instigator;
    }
    public TFloatList getMultipliers() {
        return multipliers;
    }
    public TIntList getModifiers() {
        return modifiers;
    }
    public void multiply(float amount) {
        multipliers.add(amount);
    }
    public void add(int amount) {
        modifiers.add(amount);
    }
    public void subtract(int amount) {
        modifiers.add(-amount);
    }

    @Override
    public boolean isConsumed() {
        return consumed;
    }
    @Override
    public void consume() {
        consumed = true;
    }
}

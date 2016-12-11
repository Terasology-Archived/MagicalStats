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

import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.event.AbstractConsumableValueModifiableEvent;
import org.terasology.entitySystem.prefab.Prefab;


public class BeforeDrainedEvent extends AbstractConsumableValueModifiableEvent {
    private Prefab damageType;
    private EntityRef instigator;
    private EntityRef directCause;

    public BeforeDrainedEvent(int baseDamage, Prefab damageType, EntityRef instigator, EntityRef directCause) {
        super(baseDamage);
        this.damageType = damageType;
        this.instigator = instigator;
        this.directCause = directCause;
    }

    public Prefab getDamageType() {
        return damageType;
    }

    public EntityRef getInstigator() {
        return instigator;
    }

    public EntityRef getDirectCause() {
        return directCause;
    }
}

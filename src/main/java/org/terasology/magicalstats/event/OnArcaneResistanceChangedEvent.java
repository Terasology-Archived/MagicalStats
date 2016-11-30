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
import org.terasology.entitySystem.event.Event;

/**
 * This event is sent to an entity when there's a change in its arcane resistance attribute.
 */
public class OnArcaneResistanceChangedEvent implements Event {
    /** A reference to the entity who caused the arcane resistance attribute change. */
    private EntityRef instigator;

    /** A reference to the entity whose arcane resistance attribute has been affected. */
    private EntityRef character;

    /** The old arcane resistance attribute value before the change. */
    private int originalValue;

    /** The new arcane resistance attribute value following the change. */
    private int newValue;

    /**
     * Create an instance of this event with the given instigator and the affected entity.
     *
     * @param instigator    Entity who caused the arcane resistance attribute changes in the character.
     * @param character     Entity whose arcane resistance attribute was modified.
     */
    public OnArcaneResistanceChangedEvent(EntityRef instigator, EntityRef character) {
        this.instigator = instigator;
        this.character = character;
    }

    /**
     * Create an instance of this event with the given instigator and the affected entity, as well as the old and new
     * arcane resistance attribute values.
     *
     * @param instigator     Entity who caused the arcane resistance attribute changes in the character.
     * @param character      Entity whose arcane resistance attribute was modified.
     * @param originalValue  The original arcane resistance attribute value before the modification.
     * @param newValue       The new arcane resistance attribute value after the modification.
     */
    public OnArcaneResistanceChangedEvent(EntityRef instigator, EntityRef character, int originalValue, int newValue) {
        this.instigator = instigator;
        this.character = character;
        this.originalValue = originalValue;
        this.newValue = newValue;
    }

    /**
     * Get the entity who instigated this arcane resistance attribute changed event.
     *
     * @return  A reference to the instigator entity.
     */
    public EntityRef getInstigator() {
        return instigator;
    }

    /**
     * Get the entity who had its arcane resistance changed.
     *
     * @return  A reference to the character entity.
     */
    public EntityRef getCharacter() {
        return character;
    }

    /**
     * Get the (character) entity's original arcane resistance value.
     *
     * @return  The old arcane resistance attribute.
     */
    public int getOriginalValue() {
        return originalValue;
    }

    /**
     * Get the (character) entity's new arcane resistance value.
     *
     * @return  The new arcane resistance attribute.
     */
    public int getNewValue() {
        return newValue;
    }
}

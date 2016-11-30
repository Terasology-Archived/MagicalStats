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
package org.terasology.magicalstats.component;

import org.terasology.entitySystem.Component;
import org.terasology.network.Replicate;
import org.terasology.reflection.MappedContainer;

/**
 * This component is used for storing information about a particular magical stats modifier. This modifier can be
 * temporary or permanent, and from an entity or item. This is intended to be applied to items or buffs, and not
 * (directly) to character entities like players. For entities like those, use MagicalStatsModifiersList, and add
 * instances of this to that class.
 *
 * Note: Only one of this kind of modifier should be applied to either items or buffs at a time.
 */
@MappedContainer
public class MagicalStatsModifierComponent implements Component {
    /**
     * This stores this particular modifier's ID. Each modifier is normally intended to have a different ID, barring
     * the scenario where certain effects can replace older ones.
     */
    @Replicate
    public String id;

    /** The intelligence stat affects how much magical damage an entity does upon striking a target. */
    @Replicate
    public int intelligence;

    /** The wisdom stat will be implemented in the future. */
    @Replicate
    public int wisdom;

    /** The willpower stat will be implemented in the future. */
    @Replicate
    public int willpower;

    /** The arcaneResistance stat affects an entity's resistance towards magical damage. */
    @Replicate
    public int arcaneResistance;
}

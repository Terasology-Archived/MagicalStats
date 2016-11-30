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

/**
 * This component is used for storing information about an entity's base magical stats or attributes.
 * It's up to other modules to define how exactly each stat is used - the descriptions given here are guidelines.
 */
public class MagicalStatsComponent implements Component {
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
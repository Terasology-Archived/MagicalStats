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
import org.terasology.rendering.nui.properties.TextField;

public class ManaComponent implements Component {
    //Configuration options
    @Replicate
    public int maxMana = 20;
    @Replicate
    public float manaRegenRate = 1;
    @Replicate
    public float waitBeforeManaRegen;

    //ManaDrain is the multiplier for how much mana something will take.
    @Replicate
    public double manaDrain = 1;

    @Replicate
    @TextField
    public  int currentMana = 20;

    // ManaRegen information
    public long nextRegenTick;


}

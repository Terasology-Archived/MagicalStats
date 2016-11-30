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

import org.terasology.context.Context;
import org.terasology.entitySystem.entity.EntityManager;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.event.ReceiveEvent;
import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.terasology.logic.players.event.OnPlayerSpawnedEvent;
import org.terasology.magicalstats.component.MagicalStatsComponent;
import org.terasology.magicalstats.event.OnArcaneResistanceChangedEvent;
import org.terasology.registry.In;

/**
 * This system handles the initialization of magical stats and the impact they have on certain actions.
 */
@RegisterSystem
public class MagicalStatsSystem extends BaseComponentSystem {
    /**
     * Define a logger for logging debug information about this system.
     */
    private static final Logger logger = LoggerFactory.getLogger(MagicalStatsSystem.class);

    @In
    private EntityManager entityManager;

    @In
    private Context context;

    /**
     * For every entity that has the MagicalStatsComponent, initialize several characterstics that are affected by
     * having this component. This includes the max health for example.
     */
    @Override
    public void initialise() {
        //TODO: Change the following to affect player Mana from the Magic Module
        /*for (EntityRef clientEntity : entityManager.getEntitiesWith(MagicalStatsComponent.class)) {
            // For every entity that has a mana component, set their max health equal to CON * 10.
            if (clientEntity.hasComponent(HealthComponent.class)) {
                HealthComponent h = clientEntity.getComponent(HealthComponent.class);

                // This check is in place so that the current health isn't reset to max health when reloading a game
                // save. It should only be done when the two are already equal.
                if (h.currentHealth == h.maxHealth) {
                    h.maxHealth = clientEntity.getComponent(MagicalStatsComponent.class).constitution * 10;
                    h.currentHealth = h.maxHealth;
                }
            }
        }*/

        super.initialise();
    }

    /**
     * When an entity (with magical stats) has been spawned following world generation or respawned following death,
     * perform some initialization tasks related to their stats.
     *
     * @param event     Event indicating the player has just been spawned.
     * @param player    Reference to the player entity that has been spawned.
     * @param magicStats  The magical stats of the player entity.
     */
    @ReceiveEvent
    public void onPlayerSpawn(OnPlayerSpawnedEvent event, EntityRef player, MagicalStatsComponent magicStats) {
        //TODO: Change the following to affect player Mana from the Magic Module
        // If the player entity has a health component, make sure that the max health is equal to CON * 10, and the
        // current health is equal to the maximum.
        /*if (player.hasComponent(HealthComponent.class)) {
            HealthComponent h = player.getComponent(HealthComponent.class);
            h.maxHealth = magicStats.constitution * 10;
            h.currentHealth = h.maxHealth;
        }*/
    }

    /**
     * When a character entity's (with magical stats) constitution attribute is changed, update the related stats like
     * health.
     *
     * @param event     Event indicating the character's constitution has been altered.
     * @param player    Reference to the character entity that was affected.
     * @param magicStats  The magical stats of the character entity.
     */
    @ReceiveEvent
    public void onArcaneRESChanged(OnArcaneResistanceChangedEvent event, EntityRef player, MagicalStatsComponent magicStats) {
        //TODO: Change the following to affect player Mana from the Magic Module
        // If the player entity has a health component, make sure that the max health is equal to CON * 10, and if the
        // current health is above the maximum health, set the current equal to the max.
        /*if (player.hasComponent(HealthComponent.class)) {
            HealthComponent h = player.getComponent(HealthComponent.class);
            h.maxHealth = magicStats.constitution * 10;

            if (h.currentHealth > h.maxHealth) {
                h.currentHealth = h.maxHealth;
            }
        }*/
    }

}
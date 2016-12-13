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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.terasology.entitySystem.entity.EntityManager;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.logic.console.commandSystem.annotations.Command;
import org.terasology.logic.console.commandSystem.annotations.CommandParam;
import org.terasology.logic.console.commandSystem.annotations.Sender;
import org.terasology.logic.health.EngineDamageTypes;
import org.terasology.logic.permission.PermissionManager;
import org.terasology.logic.players.LocalPlayer;
import org.terasology.magicalstats.component.MagicalStatsComponent;
import org.terasology.magicalstats.component.ManaComponent;
import org.terasology.magicalstats.event.*;
import org.terasology.network.ClientComponent;
import org.terasology.registry.CoreRegistry;
import org.terasology.registry.In;

/**
 * This system handles cheat or debug commands related to the magical stats system.
 */
@RegisterSystem
public class MagicalStatsSystemCommands extends BaseComponentSystem {
    /**
     * Define a logger for logging debug information about this system.
     */
    private static final Logger logger = LoggerFactory.getLogger(MagicalStatsSystemCommands.class);

    @In
    private EntityManager entityManager;

    /**
     * Print all of the current base magical stats of the local player to the console window.
     */
    @Command(shortDescription = "Show all magical stats of the local player", requiredPermission = PermissionManager.CHEAT_PERMISSION)
    public void getPlayerMagicalStats() {
        // Get the local player's character entity, and get the MagicalStatsComponent from it to print the base stat
        // attributes.
        if (CoreRegistry.get(LocalPlayer.class).getCharacterEntity().hasComponent(MagicalStatsComponent.class)) {
            MagicalStatsComponent p = CoreRegistry.get(LocalPlayer.class).getCharacterEntity().
                    getComponent(MagicalStatsComponent.class);

            logger.info("INT: " + p.intelligence + " WIS: " + p.wisdom + " WIL: " + p.willpower + " RES: " + p.arcaneResistance);
        }
    }

    /**
     * Set the local player's base intelligence attribute to the given amount.
     *
     * @param amount    The new intelligence value.
     */
    @Command(shortDescription = "Set magical INT stat.", requiredPermission = PermissionManager.CHEAT_PERMISSION)
    public String setMagicalIntelligence(@CommandParam("amount") int amount) {
        // Get the local player's character entity, and check to see whether it has magical stats. If so, continue in
        // this if-block.
        String output;
        if (CoreRegistry.get(LocalPlayer.class).getCharacterEntity().hasComponent(MagicalStatsComponent.class)) {
            EntityRef player = CoreRegistry.get(LocalPlayer.class).getCharacterEntity();

            // Store the old strength value, and replace it with the new one.
            int oldValue = player.getComponent(MagicalStatsComponent.class).intelligence;
            player.getComponent(MagicalStatsComponent.class).intelligence = amount;

            // Print the change to the console.
            output = "INT changed from " + oldValue + " to " + amount;
            logger.info(output);

            // Send two events to the player entity. The first for indicating that the strength has been changed.
            // The second for indicating that a magical stat has been changed.
            player.send(new OnIntelligenceChangedEvent(player, player, oldValue, amount));
            player.send(new OnMagicalStatChangedEvent(player, player));
        } else {
            output = "No magical stats found!";
        }

        return output;
    }

    /**
     * Set the local player's base wisdom attribute to the given amount.
     *
     * @param amount    The new wisdom value.
     */
    @Command(shortDescription = "Set magical WIS stat.", requiredPermission = PermissionManager.CHEAT_PERMISSION)
    public String setMagicalWisdom(@CommandParam("amount") int amount) {
        // Get the local player's character entity, and check to see whether it has magical stats. If so, continue in
        // this if-block.
        String output;
        if (CoreRegistry.get(LocalPlayer.class).getCharacterEntity().hasComponent(MagicalStatsComponent.class)) {
            EntityRef player = CoreRegistry.get(LocalPlayer.class).getCharacterEntity();

            // Store the old dexterity value, and replace it with the new one.
            int oldValue = player.getComponent(MagicalStatsComponent.class).wisdom;
            CoreRegistry.get(LocalPlayer.class).getCharacterEntity().getComponent
                    (MagicalStatsComponent.class).wisdom = amount;

            // Print the change to the console.
            output = "WIS changed from " + oldValue + " to " + amount;
            logger.info(output);

            // Send an event to the player entity indicating that a magical stat has been changed.
            player.send(new OnWisdomChangedEvent(player, player, oldValue, amount));
            player.send(new OnMagicalStatChangedEvent(player, player));
        } else {
            output = "No magical stats found!";
        }

        return output;
    }

    /**
     * Set the local player's base willpower attribute to the given amount.
     *
     * @param amount    The new willpower value.
     */
    @Command(shortDescription = "Set magical WIL stat.", requiredPermission = PermissionManager.CHEAT_PERMISSION)
    public String setMagicalWillpower(@CommandParam("amount") int amount) {
        // Get the local player's character entity, and check to see whether it has magical stats. If so, continue in
        // this if-block.
        String output;
        if (CoreRegistry.get(LocalPlayer.class).getCharacterEntity().hasComponent(MagicalStatsComponent.class)) {
            EntityRef player = CoreRegistry.get(LocalPlayer.class).getCharacterEntity();

            // Store the old constitution value, and replace it with the new one.
            int oldValue = player.getComponent(MagicalStatsComponent.class).willpower;
            player.getComponent(MagicalStatsComponent.class).willpower = amount;

            // Print the change to the console.
            output = "WIL changed from " + oldValue + " to " + amount;
            logger.info(output);

            // Send two events to the player entity. The first for indicating that the constitution has been changed.
            // The second for indicating that a magical stat has been changed.
            player.send(new OnWillpowerChangedEvent(player, player, oldValue, amount));
            player.send(new OnMagicalStatChangedEvent(player, player));
        } else {
            output = "No magical stats found!";
        }

        return output;
    }

    /**
     * Set the local player's base arcane resistance attribute to the given amount.
     *
     * @param amount    The new arcane resistance value.
     */
    @Command(shortDescription = "Set magical RES stat.", requiredPermission = PermissionManager.CHEAT_PERMISSION)
    public String setMagicalArcaneResistance(@CommandParam("amount") int amount) {
        // Get the local player's character entity, and check to see whether it has magical stats. If so, continue in
        // this if-block.
        String output;
        if (CoreRegistry.get(LocalPlayer.class).getCharacterEntity().hasComponent(MagicalStatsComponent.class)) {
            EntityRef player = CoreRegistry.get(LocalPlayer.class).getCharacterEntity();

            // Store the old agility value, and replace it with the new one.
            int oldValue = player.getComponent(MagicalStatsComponent.class).arcaneResistance;
            player.getComponent(MagicalStatsComponent.class).arcaneResistance = amount;

            // Print the change to the console.
            output = "RES changed from " + oldValue + " to " + amount;
            logger.info(output);

            // Send two events to the player entity. The first for indicating that the agility has been changed.
            // The second for indicating that a magical stat has been changed.
            player.send(new OnArcaneResistanceChangedEvent(player, player, oldValue, amount));
            player.send(new OnMagicalStatChangedEvent(player, player));
        } else {
            output = "No magical stats found!";
        }

        return output;
    }

    /**
     * Returns the current amount of mana in a character
     */
    @Command(shortDescription = "Returns amount of mana", requiredPermission= PermissionManager.DEBUG_PERMISSION)
    public String showMana(@Sender EntityRef clientEntity) {
        ClientComponent clientComp = clientEntity.getComponent(ClientComponent.class);
        ManaComponent mana = clientComp.character.getComponent(ManaComponent.class);
        if (mana != null) {
            return "Your current mana is: " + mana.currentMana + " its max is: " + mana.maxMana + " and its regeneration rate is: " + mana.manaRegenRate;
        } else {
            return "No mana to be found.";
        }
    }
    /**
     * Sets the amount of mana regen
     */
    @Command(shortDescription = "Sets Mana regen rate", requiredPermission = PermissionManager.SERVER_MANAGEMENT_PERMISSION)
    public String setManaRegen(@Sender EntityRef client, @CommandParam("rate") float rate) {
        ClientComponent clientComp = client.getComponent(ClientComponent.class);
        ManaComponent mana = clientComp.character.getComponent(ManaComponent.class);
        float oldRegenRate = mana.manaRegenRate;
        if (mana != null) {
            mana.manaRegenRate = rate;
            clientComp.character.saveComponent(mana);
        }
        return "Mana regen changed from " + oldRegenRate + " to " + rate;
    }

    @Command(shortDescription = "Reduces mana", requiredPermission = PermissionManager.CHEAT_PERMISSION)
    public String reduceMana(@Sender EntityRef client, @CommandParam("amount") int amount) {
        ClientComponent clientComp = client.getComponent(ClientComponent.class);
        clientComp.character.send(new DoDrainEvent(amount, EngineDamageTypes.DIRECT.get(), clientComp.character));
        return "mana reduced by " + amount;
    }
}

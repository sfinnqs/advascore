/**
 * Advascore - A Bukkit plugin for scoring players by advancements
 * Copyright (C) 2019 sfinnqs
 *
 * This file is part of Advascore.
 *
 * Advascore is free software; you can redistribute it and/or modify it under
 * the terms of version 3 of the GNU General Public License as published by the
 * Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, see <https://www.gnu.org/licenses>.
 */
package com.voichick.advascore

import org.bukkit.NamespacedKey
import org.bukkit.advancement.Advancement
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerAdvancementDoneEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.scoreboard.Objective

class AdvListener(private val objective: Objective) : Listener {

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) = updatePlayer(event.player)

    @EventHandler
    fun onPlayerAdvancementDone(event: PlayerAdvancementDoneEvent) {
        if (event.advancement.isRecipe) return
        updatePlayer(event.player, event.advancement)
    }

    fun updatePlayer(player: Player, included: Advancement? = null) {
        objective.getScore(player.name).score = player.getAdvancements(included)
    }

    private fun Player.getAdvancements(included: Advancement?) = server.advancementIterator().asSequence().count {
        !it.isRecipe && (it == included || getAdvancementProgress(it).isDone)
    }

    private val Advancement.isRecipe: Boolean
        get() = key.namespace == NamespacedKey.MINECRAFT && key.key.startsWith("recipes/")

}

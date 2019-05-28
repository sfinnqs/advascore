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
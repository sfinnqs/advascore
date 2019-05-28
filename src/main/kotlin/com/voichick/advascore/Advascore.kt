package com.voichick.advascore

import org.bstats.bukkit.Metrics
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scoreboard.DisplaySlot.PLAYER_LIST
import org.bukkit.scoreboard.Objective
import org.bukkit.scoreboard.RenderType

@Suppress("unused")
class Advascore : JavaPlugin() {

    private var objective: Objective? = null

    override fun onEnable() {
        val boardManager = server.scoreboardManager
                ?: return logger.severe("Scoreboard manager not found")
        val board = boardManager.mainScoreboard
        val listObjective = board.getObjective(PLAYER_LIST)
        if (listObjective != null)
            return logger.severe {
                "There is already an objective in the \"list\" display slot: ${listObjective.name}"
            }
        val newObjective = board.registerNewObjective("advascore", "dummy", "Advancements", RenderType.INTEGER)
        newObjective.displaySlot = PLAYER_LIST
        val listener = AdvListener(newObjective)
        server.pluginManager.registerEvents(listener, this)
        server.onlinePlayers.forEach { listener.updatePlayer(it) }
        objective = newObjective

        Metrics(this)
    }

    override fun onDisable() {
        objective?.unregister()
    }

}
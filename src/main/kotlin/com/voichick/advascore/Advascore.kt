package com.voichick.advascore

import org.bstats.bukkit.Metrics
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scoreboard.DisplaySlot.PLAYER_LIST
import org.bukkit.scoreboard.Objective
import org.bukkit.scoreboard.RenderType
import org.bukkit.scoreboard.Scoreboard

@Suppress("unused")
class Advascore : JavaPlugin() {

    private var objective: Objective? = null

    override fun onEnable() {
        val boardManager = server.scoreboardManager
                ?: return logger.severe("Scoreboard manager not found")
        val board = boardManager.mainScoreboard
        val newObjective = createObjective(board) ?: return
        val listener = AdvListener(newObjective)
        server.pluginManager.registerEvents(listener, this)
        server.onlinePlayers.forEach { listener.updatePlayer(it) }
        objective = newObjective

        Metrics(this)
    }

    private fun createObjective(board: Scoreboard): Objective? {
        val listObj = board.getObjective(PLAYER_LIST)
        if (listObj == null) {
            val result = board.registerNewObjective("advascore", "dummy", "Advancements", RenderType.INTEGER)
            result.displaySlot = PLAYER_LIST
            return result
        }
        if (listObj.name == "advascore")
            return listObj
        logger.severe {
            "There is already an objective in the \"list\" display slot: ${listObj.name}"
        }
        return null
    }

    override fun onDisable() {
        objective?.unregister()
    }

}
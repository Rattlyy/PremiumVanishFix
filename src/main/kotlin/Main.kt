package it.rattly

import de.myzelyam.api.vanish.VanishAPI
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerCommandPreprocessEvent
import org.bukkit.plugin.java.JavaPlugin

class PremiumVanishFix : JavaPlugin(), Listener {
    override fun onEnable() {
        server.pluginManager.registerEvents(this, this)
        saveDefaultConfig()

        logger.info("PremiumVanishFix has been enabled.")
    }

    @EventHandler
    fun onCmd(e: PlayerCommandPreprocessEvent) {
        if (e.player.hasPermission("pv.see")) return
        val vanishedNames = VanishAPI.getInvisiblePlayers().mapNotNull { Bukkit.getPlayer(it)?.name }
        if (
            config.getStringList("commands").any { e.message.startsWith(it) } &&
            vanishedNames.any { e.message.contains(it) || it.startsWith(e.message.split(" ").getOrNull(1) ?: "", true) }
        ) {

            e.isCancelled = true
            e.player.sendMessage(
                MiniMessage.miniMessage().deserialize(
                    config.getString("message") ?: "<dark_red>Player not found."
                )
            )
        }
    }
}
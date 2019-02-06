package net.benwoodworth.fastcraft.bukkit.player

import net.benwoodworth.fastcraft.bukkit.text.BukkitFcRawTextFactory
import net.benwoodworth.fastcraft.platform.player.FcPlayer
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.util.*
import javax.inject.Inject

class BukkitFcPlayerProvider_1_13_00_R01 @Inject constructor(
    private val rawTextFactory: BukkitFcRawTextFactory
) : BukkitFcPlayerProvider {

    override fun getOnlinePlayers(): List<FcPlayer> {
        return Bukkit.getOnlinePlayers().map { player ->
            BukkitFcPlayer_1_13_00_R01(player, rawTextFactory)
        }
    }

    override fun getPlayer(uuid: UUID): FcPlayer? {
        return Bukkit.getPlayer(uuid)?.let { player ->
            BukkitFcPlayer_1_13_00_R01(player, rawTextFactory)
        }
    }

    override fun getPlayer(player: Player): FcPlayer {
        return BukkitFcPlayer_1_13_00_R01(player, rawTextFactory)
    }
}

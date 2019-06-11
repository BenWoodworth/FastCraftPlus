package net.benwoodworth.fastcraft.bukkit.item

import net.benwoodworth.fastcraft.platform.item.FcItem
import org.bukkit.inventory.ItemStack

interface BukkitFcItemFactory {

    fun createFcItem(itemStack: ItemStack): FcItem
}

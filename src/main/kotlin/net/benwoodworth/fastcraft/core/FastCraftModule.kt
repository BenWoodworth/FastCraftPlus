package net.benwoodworth.fastcraft.core

import net.benwoodworth.fastcraft.core.dependencies.event.FcEventListenerRegistry
import net.benwoodworth.fastcraft.core.dependencies.inventory.FcItem

/**
 * Created by ben on 6/4/17.
 */
interface FastCraftModule<TItem : FcItem<*>> {

    fun eventListeners(): FcEventListenerRegistry<TItem>
}
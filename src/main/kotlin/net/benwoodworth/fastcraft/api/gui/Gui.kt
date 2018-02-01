package net.benwoodworth.fastcraft.api.gui

import net.benwoodworth.fastcraft.api.Listener
import net.benwoodworth.fastcraft.api.gui.event.EventGuiClose
import net.benwoodworth.fastcraft.dependencies.player.Player
import net.benwoodworth.fastcraft.dependencies.text.Text

/**
 * A user interface for in-game players.
 */
interface Gui {

    /**
     * A listener for the inventory closing.
     */
    val closeListener: Listener<EventGuiClose>

    /**
     * The title of this [Gui].
     */
    val title: Text?

    /**
     * Open this [Gui] for the given players.
     *
     * @param players the players to open the inventory for
     */
    fun open(vararg players: Player)

    /**
     * Get the players viewing the inventory.
     *
     * @return the players viewing the inventory
     */
    fun getViewers(): List<Player>

    /**
     * Update the Gui's layout.
     */
    fun updateLayout()
}

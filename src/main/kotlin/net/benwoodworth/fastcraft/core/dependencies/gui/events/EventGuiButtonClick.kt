package net.benwoodworth.fastcraft.core.dependencies.gui.events

import net.benwoodworth.fastcraft.core.dependencies.gui.Gui
import net.benwoodworth.fastcraft.core.dependencies.gui.GuiButton
import net.benwoodworth.fastcraft.core.dependencies.player.Player
import net.benwoodworth.fastcraft.util.Cancellable

/**
 * An event triggered when a [GuiButton] is clicked.
 */
class EventGuiButtonClick(
        /** The [Gui] in which the button was clicked. */
        val gui: Gui,

        /** The [GuiButton] that was clicked. */
        val button: GuiButton,

        /** The [Player] who clicked the button. */
        val player: Player
) : Cancellable {

    override var cancelled = false
}

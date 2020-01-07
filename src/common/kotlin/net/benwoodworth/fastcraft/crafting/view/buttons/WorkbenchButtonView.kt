package net.benwoodworth.fastcraft.crafting.view.buttons

import com.google.auto.factory.AutoFactory
import com.google.auto.factory.Provided
import net.benwoodworth.fastcraft.platform.gui.FcGuiButton
import net.benwoodworth.fastcraft.platform.gui.FcGuiClick
import net.benwoodworth.fastcraft.platform.item.FcItemTypes
import net.benwoodworth.fastcraft.platform.text.FcTextColors
import net.benwoodworth.fastcraft.platform.text.FcTextFactory

@AutoFactory
class WorkbenchButtonView(
    private val button: FcGuiButton,
    @Provided private val itemTypes: FcItemTypes,
    @Provided private val textFactory: FcTextFactory,
    @Provided private val textColors: FcTextColors
) {
    var eventListener: EventListener? = null

    private companion object {
        val CLICK_OPEN_WORKBENCH = FcGuiClick.Primary()
    }

    init {
        button.apply {
            onClick = { event ->
                when (event.click) {
                    CLICK_OPEN_WORKBENCH -> eventListener?.onOpenWorkbench()
                }
            }

            itemType = itemTypes.craftingTable

            text = textFactory.createFcText("Crafting Grid", color = textColors.green)

            description = listOf(
                textFactory.createFcText("Open a 3x3 crafting grid", color = textColors.aqua),
                textFactory.createFcText(
                    color = textColors.green,
                    extra = listOf(
                        textFactory.createFcText("Use "),
                        textFactory.createFcText("/fc toggle", color = textColors.aqua, italic = true),
                        textFactory.createFcText(" to disable FastCraft")
                    )
                )
            )

            hideItemDetails()
        }
    }

    interface EventListener {
        fun onOpenWorkbench()
    }
}

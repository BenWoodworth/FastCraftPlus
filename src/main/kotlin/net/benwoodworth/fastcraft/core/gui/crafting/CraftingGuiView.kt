package net.benwoodworth.fastcraft.core.gui.crafting

import net.benwoodworth.fastcraft.core.lang.FastCraftLang
import net.benwoodworth.fastcraft.dependencies.api.gui.GuiFactory
import net.benwoodworth.fastcraft.dependencies.api.gui.GuiLocation
import net.benwoodworth.fastcraft.dependencies.api.gui.button.GuiButtonAbstract
import net.benwoodworth.fastcraft.dependencies.api.gui.element.GuiLayoutChanger
import net.benwoodworth.fastcraft.dependencies.api.item.FcItem
import net.benwoodworth.fastcraft.dependencies.api.item.FcItemBuilder
import net.benwoodworth.fastcraft.dependencies.api.item.FcItemTypeFactory
import net.benwoodworth.fastcraft.dependencies.api.mvp.MvpView
import net.benwoodworth.fastcraft.dependencies.recipe.FcCraftingRecipe
import javax.inject.Inject
import javax.inject.Provider

class CraftingGuiView @Inject constructor(
        guiFactory: GuiFactory,
        private val fastCraftLang: FastCraftLang,
        private val itemBuilder: Provider<FcItemBuilder>,
        private val itemTypeFactory: FcItemTypeFactory
) : MvpView {


    val gui = guiFactory.withSize(9, 6, fastCraftLang.guiTitle())

    inner class ButtonWorkbench : GuiButtonAbstract(GuiLocation(0, 0)) {
        override fun getItem(location: GuiLocation) = itemBuilder.get()
                .type(itemTypeFactory.getCraftingTable())
                .displayName(fastCraftLang.guiToolbarWorkbenchTitle())
                .lore(fastCraftLang.guiToolbarWorkbenchDescription())
                .build()
    }

    inner class ButtonMultiplier : GuiButtonAbstract(GuiLocation(0, 2)) {
        var multiplier by GuiLayoutChanger(1)

        override fun getItem(location: GuiLocation) = itemBuilder.get()
                .type(itemTypeFactory.getAnvil())
                .amount(multiplier)
                .displayName(fastCraftLang.guiToolbarMultiplierTitle(multiplier))
                .lore(fastCraftLang.guiToolbarMultiplierDescription(multiplier))
                .build()
    }

    inner class ButtonRefresh : GuiButtonAbstract(GuiLocation(0, 3)) {
        override fun getItem(location: GuiLocation) = itemBuilder.get()
                .type(itemTypeFactory.getAnvil())
                .displayName(fastCraftLang.guiToolbarWorkbenchTitle())
                .lore(fastCraftLang.guiToolbarWorkbenchDescription())
                .build()
    }

    inner class ButtonPage : GuiButtonAbstract(GuiLocation(0, 4)) {
        var pageFirst by GuiLayoutChanger(0)
        var pageLast by GuiLayoutChanger(0)
        var pageCurrent by GuiLayoutChanger(0)

        override fun getItem(location: GuiLocation) = itemBuilder.get()
                .type(itemTypeFactory.getAnvil())
                .displayName(fastCraftLang.guiToolbarWorkbenchTitle())
                .lore(fastCraftLang.guiToolbarWorkbenchDescription())
                .build()
    }

    inner class ButtonRecipe(
            location: GuiLocation
    ) : GuiButtonAbstract(location) {
        var recipe: FcCraftingRecipe.Prepared? by GuiLayoutChanger(null)

        override fun getItem(location: GuiLocation): FcItem? = recipe.let { recipe ->
            if (recipe == null || recipe.results.isEmpty()) {
                return null
            }

            return itemBuilder.get()
                    .from(recipe.results.first())
                    .build()
        }
    }
}
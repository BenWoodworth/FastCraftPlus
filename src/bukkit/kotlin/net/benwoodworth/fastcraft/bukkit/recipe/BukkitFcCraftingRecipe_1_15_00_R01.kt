package net.benwoodworth.fastcraft.bukkit.recipe

import com.google.auto.factory.AutoFactory
import com.google.auto.factory.Provided
import net.benwoodworth.fastcraft.bukkit.item.toItemStack
import net.benwoodworth.fastcraft.bukkit.player.player
import net.benwoodworth.fastcraft.platform.item.FcItem
import net.benwoodworth.fastcraft.platform.player.FcPlayer
import net.benwoodworth.fastcraft.platform.recipe.FcCraftingRecipe
import net.benwoodworth.fastcraft.platform.recipe.FcCraftingRecipePrepared
import net.benwoodworth.fastcraft.platform.recipe.FcIngredient
import org.bukkit.Keyed
import org.bukkit.Server
import org.bukkit.event.inventory.InventoryType
import org.bukkit.event.inventory.PrepareItemCraftEvent
import org.bukkit.inventory.CraftingInventory
import org.bukkit.inventory.Recipe
import org.bukkit.inventory.ShapedRecipe
import org.bukkit.inventory.ShapelessRecipe

@AutoFactory
class BukkitFcCraftingRecipe_1_15_00_R01(
    private val recipe: Recipe,
    @Provided val server: Server,
    @Provided val preparedRecipeFactory: BukkitFcCraftingRecipePrepared_1_15_00_R01Factory
) : BukkitFcCraftingRecipe {
    init {
        require(recipe is ShapedRecipe || recipe is ShapelessRecipe)
    }

    override val id: String
        get() = (recipe as Keyed).key.toString()

    override val ingredients: List<FcIngredient> = when (recipe) {
        is ShapedRecipe -> recipe.shape
            .mapIndexed { row, rowStr ->
                rowStr.mapIndexed { column, char ->
                    BukkitFcIngredient_1_15_00_R01(row * 3 + column, recipe.choiceMap[char]!!)
                }
            }
            .flatten()

        is ShapelessRecipe -> recipe.choiceList
            .mapIndexed { i, recipeChoice ->
                BukkitFcIngredient_1_15_00_R01(i, recipeChoice)
            }

        else -> throw IllegalStateException()
    }

    override fun prepare(player: FcPlayer, ingredients: Map<FcIngredient, FcItem>): FcCraftingRecipePrepared {
        val craftingGrid = server.createInventory(null, InventoryType.CRAFTING) as CraftingInventory

        ingredients.forEach { (ingredient, item) ->
            ingredient as BukkitFcIngredient_1_15_00_R01

            craftingGrid.setItem(ingredient.slotIndex, item.toItemStack())
        }

        if (ingredients.all { (ingredient, item) -> ingredient.matches(item) }) {
            craftingGrid.result = recipe.result
        }

        val prepareView = PrepareCraftInventoryView_1_15_00_R01(player.player, craftingGrid)
        val prepareEvent = PrepareItemCraftEvent(craftingGrid, prepareView, false)
        server.pluginManager.callEvent(prepareEvent)

        return preparedRecipeFactory.create(this, craftingGrid)
    }

    override fun equals(other: Any?): Boolean {
        return other is FcCraftingRecipe &&
                id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}


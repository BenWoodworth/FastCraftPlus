package net.benwoodworth.fastcraft

import net.benwoodworth.fastcraft.platform.config.FcConfig
import net.benwoodworth.fastcraft.platform.config.FcConfigNode
import net.benwoodworth.fastcraft.platform.server.FcLogger
import net.benwoodworth.fastcraft.platform.server.FcPluginData
import net.benwoodworth.fastcraft.platform.world.FcItem
import net.benwoodworth.fastcraft.platform.world.FcItemStack
import java.nio.file.Files
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FastCraftConfig @Inject constructor(
    private val configFactory: FcConfig.Factory,
    private val pluginData: FcPluginData,
    private val items: FcItem.Factory,
    private val itemStackFactory: FcItemStack.Factory,
    private val logger: FcLogger,
) {
    private val matchNothing = Regex("(?" + "!)")

    private var config: FcConfig = configFactory.create()
    private var modified: Boolean = false
    private var newFile: Boolean = false

    private val header: String = """
        https://github.com/BenWoodworth/FastCraft/wiki/Configuration
    """.trimIndent()

    private fun wildcardToRegex(expression: String): String {
        return Regex.escape(expression)
            .replace("*", """\E.*\Q""")
    }

    private var disabledRecipeIds: List<String> = emptyList()
        set(values) {
            field = values

            disabledRecipes = values
                .map { wildcardToRegex(it) }
                .takeIf { it.any() }
                ?.let { Regex(it.joinToString("|")) }
                ?: matchNothing
        }

    var disabledRecipes: Regex = matchNothing
        private set

    val fastCraftUi = FastCraftUi()

    inner class FastCraftUi {
        private val node: FcConfigNode
            get() = config["fastcraft-ui"]

        var height: Int = 6
            private set

        val recipes = Recipes()

        inner class Recipes {
            private val node: FcConfigNode
                get() = this@FastCraftUi.node["recipes"]

            var row: Int = 0
                private set

            var column: Int = 0
                private set

            var width: Int = 7
                private set

            var height: Int = 6
                private set

            //region fun load()
            fun load() {
                node["row"].run {
                    val rowRange = 0 until this@FastCraftUi.height
                    row = when (val newRow = getInt()) {
                        null -> modify(row.coerceIn(rowRange))
                        !in rowRange -> {
                            rowRange.first.also {
                                logErr("$newRow is not in $rowRange. Defaulting to $it.")
                            }
                        }
                        else -> newRow
                    }
                }

                node["column"].run {
                    val columnRange = 0..8
                    column = when (val newColumn = getInt()) {
                        null -> modify(column.coerceIn(columnRange))
                        !in columnRange -> {
                            columnRange.first.also {
                                logErr("$newColumn is not in $columnRange. Defaulting to $it.")
                            }
                        }
                        else -> newColumn
                    }
                }

                node["width"].run {
                    val widthRange = 1..9 - column
                    width = when (val newWidth = getInt()) {
                        null -> modify(width.coerceIn(widthRange))
                        !in widthRange -> {
                            widthRange.first.also {
                                logErr("$newWidth is not in $widthRange. Defaulting to $it.")
                            }
                        }
                        else -> newWidth
                    }
                }

                node["height"].run {
                    val heightRange = 1..height - row
                    height = when (val newHeight = getInt()) {
                        null -> modify(height.coerceIn(heightRange))
                        !in heightRange -> {
                            heightRange.first.also {
                                logErr("$newHeight is not in $heightRange. Defaulting to $it.")
                            }
                        }
                        else -> newHeight
                    }
                }
            }
            //endregion
        }

        val buttons = Buttons()

        inner class Buttons {
            private val node: FcConfigNode
                get() = this@FastCraftUi.node["buttons"]

            val craftingGrid = Button(
                key = "crafting-grid",
                enabled = true,
                item = items.craftingTable,
                row = 0,
                column = 8,
            )

            val craftAmount = Button(
                key = "craft-amount",
                enabled = true,
                item = items.anvil,
                row = 1,
                column = 8,
            )

            val refresh = Button(
                key = "refresh",
                enabled = true,
                item = items.netherStar,
                row = 2,
                column = 8,
            )

            val page = Button(
                key = "page",
                enabled = true,
                item = items.ironSword,
                row = 5,
                column = 8,
            )

            inner class Button(
                private val key: String,
                enabled: Boolean,
                item: FcItem,
                row: Int,
                column: Int,
            ) {
                private val node: FcConfigNode
                    get() = this@Buttons.node[key]

                var enabled: Boolean = enabled
                    private set

                var item: FcItem = item
                    private set

                var row: Int = row
                    private set

                var column: Int = column
                    private set

                //region fun load()
                fun load() {
                    node["enabled"].run {
                        enabled = when (val newEnabled = getBoolean()) {
                            null -> modify(enabled)
                            else -> newEnabled
                        }
                    }

                    node["row"].run {
                        val rowRange = 0 until this@FastCraftUi.height
                        row = when (val newRow = getInt()) {
                            null -> modify(row.coerceIn(rowRange))
                            !in rowRange -> {
                                rowRange.first.also {
                                    logErr("$newRow is not in $rowRange. Defaulting to $it.")
                                }
                            }
                            else -> newRow
                        }
                    }

                    node["column"].run {
                        val columnRange = 0..8
                        column = when (val newColumn = getInt()) {
                            null -> modify(column.coerceIn(columnRange))
                            !in columnRange -> {
                                columnRange.first.also {
                                    logErr("$newColumn is not in $columnRange. Defaulting to $it.")
                                }
                            }
                            else -> newColumn
                        }
                    }

                    node["item"].run {
                        item = when (val newItemId = getString()) {
                            null -> {
                                modify(item.id)
                                item
                            }
                            else -> when (val newItem = items.parseOrNull(newItemId)) {
                                null -> {
                                    item.also {
                                        logErr("Invalid item id: $newItemId. Defaulting to ${it.id}.")
                                    }
                                }
                                else -> newItem
                            }
                        }
                    }
                }
                //endregion
            }

            //region fun load()
            fun load() {
                craftingGrid.load()
                craftAmount.load()
                refresh.load()
                page.load()
            }
            //endregion
        }

        val background = Background()

        inner class Background : EnabledItem(
            enabled = false,
            item = itemStackFactory.create(items.lightGrayStainedGlassPane),
        ) {
            override val node: FcConfigNode
                get() = this@FastCraftUi.node["background"]
        }

        abstract inner class EnabledItem(
            enabled: Boolean,
            item: FcItemStack,
        ) {
            protected abstract val node: FcConfigNode

            var enabled: Boolean = enabled
                private set

            var item: FcItemStack = item
                private set

            private var itemId: String = item.type.id

            open fun load() {
                node["enabled"].run {
                    enabled = when (val newEnabled = getBoolean()) {
                        null -> modify(enabled)
                        else -> newEnabled
                    }
                }

                node["item"].run {
                    item = when (val newItemId = getString()) {
                        null -> {
                            modify(itemId)
                            item
                        }
                        else -> when (val newItem = itemStackFactory.parseOrNull(newItemId)) {
                            null -> {
                                item.also {
                                    logErr("Invalid item id: $newItemId. Defaulting to ${itemId}.")
                                }
                            }
                            else -> {
                                itemId = newItemId
                                newItem
                            }
                        }
                    }
                }
            }
        }

        //region fun load()
        fun load() {
            height = node["height"].run {
                val heightRange = 1..6
                when (val newHeight = getInt()) {
                    null -> {
                        modify(height)
                    }
                    !in heightRange -> {
                        height.also {
                            logErr("Invalid height: $newHeight. Must be in $heightRange. Defaulting to $it")
                        }
                    }
                    else -> newHeight
                }
            }

            recipes.load()
            buttons.load()
            background.load()
        }
        //endregion
    }

    //region fun load()
    fun load() {
        val file = pluginData.configFile
        Files.createDirectories(file.parent)

        config = if (Files.exists(file)) {
            modified = false
            newFile = false
            try {
                configFactory.load(file)
            } catch (e: Exception) {
                logger.error("Error loading ${file.fileName}: ${e.message}")
                logger.info("Using default configuration")
                return
            }
        } else {
            modified = true
            newFile = true
            configFactory.create()
        }

        if (config.headerComment != header) {
            config.headerComment = header
            modified = true
        }

        config["disabled-recipes"].run {
            disabledRecipeIds = when (val newDisabledRecipes = getStringList()) {
                null -> modify(disabledRecipeIds)
                else -> {
                    val nonNulls = newDisabledRecipes.filterNotNull()
                    if (nonNulls.size != newDisabledRecipes.size) {
                        modify(nonNulls) { "Removed null entries" }
                    } else {
                        nonNulls
                    }
                }
            }
        }

        fastCraftUi.load()

        if (modified) {
            config.save(file)
        }

        if (newFile) {
            logger.info("Created ${file.fileName}")
        }

        modified = false
        newFile = false
    }
    //endregion

    init {
        load()
    }

    private inline fun <T> FcConfigNode.modify(
        newValue: T,
        message: (newValue: T) -> String = { "Set value to $it" },
    ): T {
        if (!newFile) {
            logger.info("${pluginData.configFile.fileName} [$path]: ${message(newValue)}")
        }

        set(newValue)
        modified = true
        return newValue
    }

    private fun FcConfigNode.logErr(message: String) {
        logger.error("${pluginData.configFile.fileName} [$path]: $message")
    }
}

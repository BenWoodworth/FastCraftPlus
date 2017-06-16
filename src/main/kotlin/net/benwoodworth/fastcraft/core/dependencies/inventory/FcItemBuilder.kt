package net.benwoodworth.fastcraft.core.dependencies.inventory


/**
 * A builder that creates Minecraft items.
 */
interface FcItemBuilder {

    /**
     * Builds a new instance the item.
     *
     * @Return A new instance of the item
     */
    fun build(): ItemAdapter

    /**
     * Resets this builder and uses the values from this item.
     *
     * @Return This builder, for chaining
     */
    fun from(item: ItemAdapter): FcItemBuilder

    /**
     * Set the item type.
     *
     * @param typeId The Minecraft item type ID
     * @Return This builder, for chaining
     */
    fun setType(typeId: String): FcItemBuilder

    /**
     * Set the item amount.
     *
     * @param amount The item amount
     * @Return This builder, for chaining
     */
    fun setAmount(amount: Int): FcItemBuilder

    /**
     * Set the item's display name.
     *
     * @param displayName The display name
     * @Return This builder, for chaining
     */
    fun setDisplayName(displayName: String?): FcItemBuilder

    /**
     * Set the item's lore.
     *
     * @param lore The lore
     * @Return This builder, for chaining
     */
    fun setLore(vararg lore: List<String?>?): FcItemBuilder

    /**
     * Add an enchantment.
     *
     * @param enchantmentId The Minecraft enchantment type ID
     * @param level         The enchantment level
     * @Return This builder, for chaining
     */
    fun addEnchantment(enchantmentId: String, level: Int): FcItemBuilder
}

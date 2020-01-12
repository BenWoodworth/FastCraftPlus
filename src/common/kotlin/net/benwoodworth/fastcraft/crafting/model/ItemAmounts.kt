package net.benwoodworth.fastcraft.crafting.model

import net.benwoodworth.fastcraft.platform.item.FcItem
import net.benwoodworth.fastcraft.platform.item.FcItemFactory
import java.util.*
import javax.inject.Inject

class ItemAmounts private constructor(
    private val amounts: MutableMap<FcItem, Int>,
    private val itemFactory: FcItemFactory
) {
    private companion object {
        val keys = WeakHashMap<FcItem, FcItem>()
    }

    @Inject
    constructor(itemFactory: FcItemFactory) : this(mutableMapOf(), itemFactory)

    private fun FcItem.asKey(): FcItem {
        return when (amount) {
            1 -> this
            else -> keys.getOrPut(this) {
                itemFactory.copyItem(this, 1)
            }
        }
    }

    operator fun get(item: FcItem): Int {
        return amounts.getOrDefault(item, 0)
    }

    operator fun set(item: FcItem, amount: Int) {
        when (amount) {
            0 -> amounts.remove(item.asKey())
            else -> amounts[item.asKey()] = amount
        }
    }

    operator fun plusAssign(item: FcItem) {
        if (item.amount != 0) {
            val key = item.asKey()
            amounts[key] = amounts.getOrDefault(key, 0) + item.amount
        }
    }

    operator fun minusAssign(item: FcItem) {
        if (item.amount != 0) {
            val key = item.asKey()
            amounts[key] = amounts.getOrDefault(key, 0) - item.amount
        }
    }

    fun clear() {
        amounts.clear()
    }

    fun copy(): ItemAmounts {
        return ItemAmounts(amounts.toMutableMap(), itemFactory)
    }

    fun asMap(): Map<FcItem, Int> {
        return amounts
    }

    fun isEmpty(): Boolean {
        return amounts.isEmpty()
    }
}

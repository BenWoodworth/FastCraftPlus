package net.benwoodworth.fastcraft.sponge

import dagger.Component
import net.benwoodworth.fastcraft.core.FastCraftComponent
import net.benwoodworth.fastcraft.sponge.dependencies.inventory.SpongeItem
import javax.inject.Singleton

/**
 * Created by ben on 6/5/17.
 */
@Singleton @Component(modules = arrayOf(SpongeFastCraftModule::class))
interface SpongeFastCraftComponent : FastCraftComponent<SpongeItem>

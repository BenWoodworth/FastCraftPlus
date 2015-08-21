package co.kepler.fastcraft.recipe;

import java.util.HashSet;

import net.minecraft.server.v1_7_R2.CraftingManager;
import net.minecraft.server.v1_7_R2.IRecipe;
import net.minecraft.server.v1_7_R2.RecipeArmorDye;
import net.minecraft.server.v1_7_R2.RecipeBookClone;
import net.minecraft.server.v1_7_R2.RecipeFireworks;
import net.minecraft.server.v1_7_R2.RecipeMapClone;
import net.minecraft.server.v1_7_R2.RecipeMapExtend;
import net.minecraft.server.v1_7_R2.ShapedRecipes;
import net.minecraft.server.v1_7_R2.ShapelessRecipes;

public class RecipeUtil_v1_7_R2 extends RecipeUtil {

	public RecipeUtil_v1_7_R2() {
		badHashes = new HashSet<String>();
		for (Object o : CraftingManager.getInstance().getRecipes()) {
			IRecipe r = (IRecipe) o;
			if ((r instanceof ShapedRecipes || r instanceof ShapelessRecipes) && (
					(r instanceof RecipeArmorDye) ||
					(r instanceof RecipeBookClone) ||
					(r instanceof RecipeMapClone) ||
					(r instanceof RecipeMapExtend) ||
					(r instanceof RecipeFireworks))) {
				badHashes.add(new FastRecipe(r.toBukkitRecipe()).getHash());
			}
		}
	}
}

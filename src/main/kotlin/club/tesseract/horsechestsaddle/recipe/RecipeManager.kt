package club.tesseract.horsechestsaddle.recipe

import club.tesseract.horsechestsaddle.config.ConfigManager
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ShapedRecipe

object RecipeManager {

    private val registeredRecipes = mutableListOf<NamespacedKey>()

    fun loadRecipes(){
        unloadRecipes()
        ConfigManager.getGeneralConfig().saddleRecipe.let { recipeConfig ->
            if(recipeConfig.enabled) {
                val recipe = ShapedRecipe(saddleRecipeKey,recipeConfig.result.toItemStack())
                recipe.shape(*recipeConfig.shape.toTypedArray())
                recipeConfig.shape.forEach { row ->
                    row.forEach { character ->
                        Material.getMaterial(recipeConfig.ingredients[character]!!)?.let { material ->
                            recipe.setIngredient(character, material)
                        }
                    }
                }
                Bukkit.addRecipe(recipe)
                registeredRecipes.add(recipe.key)
            }
        }
    }


    fun unloadRecipes(){
        registeredRecipes.forEach(Bukkit::removeRecipe)
    }

    val saddleRecipeKey = NamespacedKey.fromString("horse_chest_saddle:saddle_recipe")!!

}
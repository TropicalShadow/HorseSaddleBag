package club.tesseract.horsechestsaddle.config.impl

import kotlinx.serialization.Serializable

@Serializable
data class GeneralConfig(
    val saddleRecipe: CustomCraftConfig = CustomCraftConfig(),
    val onlyOwnersAccessChest: Boolean = true,
)
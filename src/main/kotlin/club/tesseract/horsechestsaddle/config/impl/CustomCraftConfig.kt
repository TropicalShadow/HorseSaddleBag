package club.tesseract.horsechestsaddle.config.impl

import kotlinx.serialization.Serializable

@Serializable
data class CustomCraftConfig(
    val enabled: Boolean = true,
    val shape: List<String> = listOf("LLL", "LCL", "LLL"),
    val ingredients: Map<Char, String> = mapOf(
        'L' to "LEATHER",
        'C' to "CHEST",
    ),
    val result: CustomItemConfig = CustomItemConfig()
)
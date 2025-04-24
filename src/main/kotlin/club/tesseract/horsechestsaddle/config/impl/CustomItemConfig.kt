package club.tesseract.horsechestsaddle.config.impl

import club.tesseract.horsechestsaddle.HorseSaddleBag
import io.papermc.paper.datacomponent.item.CustomModelData
import kotlinx.serialization.Serializable
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.components.CustomModelDataComponent
import org.bukkit.persistence.PersistentDataType

@Serializable
data class CustomItemConfig(
    val displayName: String = "<gradient:red:aqua>Horse Chest Saddle</gradient>",
    val lore: List<String> = listOf("<white>A saddle that can be placed on a horse's chest</white>"),
    val material: String = "SADDLE",
    val amount: Int = 1,
    val textureData: Int = 0,
    val slots: Int = 27,
){

    fun toItemStack(): ItemStack{
        Material.getMaterial(material)?.let { material ->
            val item = ItemStack(material, amount)
            val meta = item.itemMeta
            meta.displayName(MiniMessage.miniMessage().deserialize(displayName).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE))
            meta.lore(lore.map { MiniMessage.miniMessage().deserialize(it).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE) })
            meta.setCustomModelData(textureData)
            meta.persistentDataContainer[customItemKey, PersistentDataType.INTEGER] = slots
            item.itemMeta = meta
            return item
        }?: let{
            HorseSaddleBag.getPlugin()
            return ItemStack(Material.AIR)
        }

    }


    companion object{
        val customItemKey = NamespacedKey.fromString("horse_chest_saddle:item")!!
    }
}

package club.tesseract.horsechestsaddle.utils

import org.bukkit.inventory.ItemStack

object ItemStackUtils {

    fun serializeItemStack(itemStack: ItemStack?): ByteArray? {
        return itemStack?.serializeAsBytes()
    }

   fun deserializeItemStack(bytes: ByteArray?): ItemStack? {
       if(bytes == null) return null
       return ItemStack.deserializeBytes(bytes)
   }

}
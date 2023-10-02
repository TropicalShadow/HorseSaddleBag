package club.tesseract.horsechestsaddle

import club.tesseract.horsechestsaddle.utils.ByteArraySerializer
import club.tesseract.horsechestsaddle.utils.ItemStackUtils
import org.bukkit.inventory.ItemStack

class InventoryData(capacity: Int) {
    val inventorySize: Int = capacity
    var inventory: Array<ItemStack?> = arrayOfNulls(inventorySize)

    fun serialize(): ByteArray{
        val serializedMap = inventory.map{ ItemStackUtils.serializeItemStack(it) }
        return ByteArraySerializer.serializeNullableByteArrays(serializedMap)
    }

    companion object{
        fun deserialize(capacity: Int, bytes: ByteArray): InventoryData{
            val serializedItems = ByteArraySerializer.deserializeNullableByteArrays(bytes)
            val items = serializedItems.map { ItemStackUtils.deserializeItemStack(it) }

            val inventoryData = InventoryData(capacity)
            inventoryData.inventory = items.toTypedArray()
            return inventoryData
        }
    }

}
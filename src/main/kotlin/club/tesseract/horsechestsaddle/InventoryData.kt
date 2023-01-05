package club.tesseract.horsechestsaddle

import club.tesseract.horsechestsaddle.utils.ItemStackUtils
import org.bukkit.inventory.ItemStack
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

class InventoryData(capacity: Int) {
    val inventorySize: Int = capacity
    var inventory: Array<ItemStack?> = arrayOfNulls(inventorySize)

    fun serialize(): ByteArray{
        val outputStream = ByteArrayOutputStream()
        val objectOutputStream = ObjectOutputStream(outputStream)
        objectOutputStream.writeObject(inventory.map{ ItemStackUtils.serializeItemStack(it) })
        objectOutputStream.close()
        return outputStream.toByteArray()
    }

    companion object{
        fun deserialize(capacity: Int, bytes: ByteArray): InventoryData{
            val inputStream = ByteArrayInputStream(bytes)
            val objectInputStream = ObjectInputStream(inputStream)
            val list = objectInputStream.readObject() as List<ByteArray>
            objectInputStream.close()
            val inventoryData = InventoryData(capacity)
            inventoryData.inventory = list.map { ItemStackUtils.deserializeItemStack(it) }.toTypedArray()
            return inventoryData
        }
    }

}
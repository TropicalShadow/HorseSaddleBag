package club.tesseract.horsechestsaddle.holder

import club.tesseract.horsechestsaddle.InventoryData
import club.tesseract.horsechestsaddle.database.models.EntityInventoryData
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder
import org.jetbrains.exposed.sql.statements.api.ExposedBlob
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*


class SaddleBag(private val data: EntityInventoryData): InventoryHolder {
        private var inventory: Inventory? = null


    /**
     * Get the inventory of the saddlebag, if it is null, create a new one
     *
     * @return The inventory.
     */
    override fun getInventory(): Inventory {
        if (this.inventory == null) {
            this.inventory = Bukkit.createInventory(this, this.data.capacity, Component.text("Saddle Bag"))
            this.loadInventory()
        }

        return this.inventory!!
    }

    /**
     * Load the inventory from the database values.
     */
    private fun loadInventory() {
        val inventoryData = InventoryData.deserialize(data.capacity,data.inventory.bytes)
        if(inventoryData.inventory.size > inventory!!.size){
            inventoryData.inventory  = inventoryData.inventory.copyOfRange(0,inventory!!.size)
            println("Inventory size is too big, resizing to ${inventory!!.size}. data loss may occur!!!")
        }
        inventory!!.storageContents = inventoryData.inventory
    }

    /**
     * Open the inventory for a player.
     *
     * @param player The player to open the inventory for.
     */
    fun open(player: Player) {
        player.openInventory(getInventory())
    }

    /**
     * Drop content of the inventory at the location.
     *
     * @param location The location to drop the items at.
     */
    fun dropContent(location: Location){
        val copy = getInventory().contents.copyOf()
        getInventory().clear()
        copy.forEach {
            if(it != null){
                location.world!!.dropItemNaturally(location, it)
            }
        }
    }

    /**
     * Save the inventory to the database.
     */
    fun save(){
        val inventoryData = InventoryData(data.capacity)
        inventoryData.inventory = inventory?.contents?: arrayOfNulls(0)
        transaction {
            data.inventory = ExposedBlob(inventoryData.serialize())
            data.lastUpdated = System.currentTimeMillis()
        }
    }

    /**
     * deletes the inventory from the database,
     * while removing the viewers from the inventory.
     */
    fun delete() {
        cache.remove(data.id.value)
        val viewers = inventory?.viewers
        if(viewers != null){
            ArrayList(viewers).forEach {
                it.closeInventory()
            }
        }
        transaction {
            inventory?.clear()
            data.delete()
        }
        this.inventory = null
    }


    companion object{
        val cache = mutableMapOf<UUID, SaddleBag>()

        fun createNew(entity: Entity, player: Player, invCapacity: Int): SaddleBag{
            val inventoryData = InventoryData(invCapacity)
            val data = transaction {
                return@transaction EntityInventoryData.new(entity.uniqueId) {
                    ownerUUID = player.uniqueId
                    capacity = inventoryData.inventorySize
                    inventory = ExposedBlob(inventoryData.serialize())
                    lastUpdated = System.currentTimeMillis()
                }
            }
            return SaddleBag(data).also { cache[entity.uniqueId] = it }
        }


        fun getExisting(entity: Entity): SaddleBag?{
            return cache[entity.uniqueId] ?: transaction {
                EntityInventoryData.findById(entity.uniqueId)?.let {
                    return@transaction SaddleBag(it).also { saddleBag -> cache[entity.uniqueId] = saddleBag }
                }
            }
        }

    }

}
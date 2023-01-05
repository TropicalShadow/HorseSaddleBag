package club.tesseract.horsechestsaddle.database.models

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.util.*

class EntityInventoryData(id: EntityID<UUID>): UUIDEntity(id) {
    companion object : UUIDEntityClass<EntityInventoryData>(EntityInventory)
    var lastUpdated by EntityInventory.last_updated
    var ownerUUID by EntityInventory.owner_uuid
    var capacity by EntityInventory.capacity
    var inventory by EntityInventory.inventory

    override fun equals(other: Any?): Boolean {
        if(other == null) return false
        if(other !is EntityInventoryData) return false
        return other.id == this.id
    }

    override fun hashCode(): Int {
        var result = lastUpdated.hashCode()
        result = 31 * result + ownerUUID.hashCode()
        result = 31 * result + capacity
        result = 31 * result + inventory.hashCode()
        return result
    }

}
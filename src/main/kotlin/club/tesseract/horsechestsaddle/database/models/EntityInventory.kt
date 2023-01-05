package club.tesseract.horsechestsaddle.database.models

import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.statements.api.ExposedBlob
import java.util.*

/**
 * Database table for storing inventory data
 */
object EntityInventory: UUIDTable() {
    val last_updated: Column<Long> = long("last_updated")
    val owner_uuid: Column<UUID> = uuid("owner_uuid")
    val capacity: Column<Int> = integer("capacity")
    val inventory: Column<ExposedBlob> = blob("inventory")
}
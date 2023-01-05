package club.tesseract.horsechestsaddle.database

import club.tesseract.horsechestsaddle.database.models.EntityInventory
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseManager {

    fun connect(){
        Database.connect("jdbc:sqlite:horse_chest_saddle.db", "org.sqlite.JDBC")

        transaction {
            SchemaUtils.create(EntityInventory)
        }
    }

}
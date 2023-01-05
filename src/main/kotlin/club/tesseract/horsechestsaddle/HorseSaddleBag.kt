package club.tesseract.horsechestsaddle

import club.tesseract.horsechestsaddle.command.HorseChestCommand
import club.tesseract.horsechestsaddle.config.ConfigManager
import club.tesseract.horsechestsaddle.database.DatabaseManager
import club.tesseract.horsechestsaddle.listener.HorseInteractionListener
import club.tesseract.horsechestsaddle.recipe.RecipeManager
import org.bukkit.Bukkit
import org.bukkit.event.HandlerList
import org.bukkit.plugin.java.JavaPlugin

@Suppress("unused")
class HorseSaddleBag: JavaPlugin() {


    override fun onEnable() {
        ConfigManager.loadConfigs(this)
        try {
            DatabaseManager.connect()
        }catch (e: Exception){
            e.printStackTrace()
            logger.severe("Failed to connect to database, disabling plugin!")
            Bukkit.getPluginManager().disablePlugin(this)
            return
        }

        RecipeManager.loadRecipes()

        Bukkit.getPluginManager().registerEvents(HorseInteractionListener, this)
        getCommand("horsechest")?.setExecutor(HorseChestCommand)
        getCommand("horsechest")?.tabCompleter = HorseChestCommand

        logger.info("HorseChestSaddle has been enabled!")
    }

    override fun onDisable() {
        HandlerList.unregisterAll(this)
        RecipeManager.unloadRecipes()
        logger.info("HorseChestSaddle has been disabled!")
    }

    companion object{
        fun getPlugin(): HorseSaddleBag{
            return getPlugin(HorseSaddleBag::class.java)
        }
    }

}
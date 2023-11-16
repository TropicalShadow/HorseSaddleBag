package club.tesseract.horsechestsaddle

import club.tesseract.horsechestsaddle.command.HorseChestCommand
import club.tesseract.horsechestsaddle.config.ConfigManager
import club.tesseract.horsechestsaddle.database.DatabaseManager
import club.tesseract.horsechestsaddle.listener.HorseInteractionListener
import club.tesseract.horsechestsaddle.recipe.RecipeManager
import org.bstats.bukkit.Metrics
import org.bukkit.Bukkit
import org.bukkit.event.HandlerList
import org.bukkit.plugin.java.JavaPlugin

@Suppress("unused")
class HorseSaddleBag: JavaPlugin() {

    private var metrics: Metrics? = null

    override fun onEnable() {
        ConfigManager.loadConfigs(this)
        metrics = Metrics(this, 20308)
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
        metrics?.shutdown()
    }

    companion object{
        fun getPlugin(): HorseSaddleBag{
            return getPlugin(HorseSaddleBag::class.java)
        }
    }

}
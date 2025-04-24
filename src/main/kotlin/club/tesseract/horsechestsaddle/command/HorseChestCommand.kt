package club.tesseract.horsechestsaddle.command

import club.tesseract.horsechestsaddle.HorseSaddleBag
import club.tesseract.horsechestsaddle.config.ConfigManager
import club.tesseract.horsechestsaddle.listener.HorseInteractionListener
import club.tesseract.horsechestsaddle.recipe.RecipeManager
import club.tesseract.horsechestsaddle.utils.ComponentUtils.toMini
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.command.TabExecutor
import org.bukkit.entity.Player
import org.bukkit.event.HandlerList

object HorseChestCommand: TabExecutor {

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>
    ): MutableList<String> {
        val result = mutableListOf<String>()
        if(!sender.hasPermission("horsechestsaddle.command")) return result
        if(args.isEmpty()){
            result.add("reload")
            result.add("give")
            return result
        }
        return result
    }


    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if(!sender.hasPermission("horsechestsaddle.command")){
            sender.sendMessage("<red>You do not have permission to use this command!".toMini())
            return true
        }
        if(args.isEmpty()){
            sender.sendMessage("<red>Invalid usage! /horsechest <reload|give>".toMini())
            return true
        }
        when(args.first()){
            "reload" -> {
                sender.sendMessage("<green>Reloading config...".toMini())
                val plugin = HorseSaddleBag.getPlugin()
                HandlerList.unregisterAll(plugin)
                RecipeManager.unloadRecipes()
                sender.sendMessage("<green>Unregistered listeners and recipes!".toMini())
                ConfigManager.loadConfigs(HorseSaddleBag.getPlugin())
                RecipeManager.loadRecipes()
                Bukkit.getPluginManager().registerEvents(HorseInteractionListener, plugin)
                sender.sendMessage("<green>Reloaded config!".toMini())
            }
            "give" -> {
                if(sender is ConsoleCommandSender)return false
                val item = ConfigManager.getGeneralConfig().saddleRecipe.result.toItemStack()
                (sender as Player).inventory.addItem(item)
                sender.sendMessage("<green>Gave you a saddle!".toMini())
            }
        }

        return true
    }
}
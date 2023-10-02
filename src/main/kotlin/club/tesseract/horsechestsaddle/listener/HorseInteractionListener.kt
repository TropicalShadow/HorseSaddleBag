package club.tesseract.horsechestsaddle.listener

import club.tesseract.horsechestsaddle.HorseSaddleBag
import club.tesseract.horsechestsaddle.config.ConfigManager
import club.tesseract.horsechestsaddle.config.impl.CustomItemConfig
import club.tesseract.horsechestsaddle.holder.SaddleBag
import club.tesseract.horsechestsaddle.utils.ComponentUtils.toMini
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Horse
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.player.PlayerInteractEntityEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.HorseInventory
import org.bukkit.persistence.PersistentDataType


object HorseInteractionListener: Listener {

    @EventHandler
    fun onAttachSaddle(event: PlayerInteractEntityEvent){
        if(event.hand != EquipmentSlot.HAND) return
        val itemStack = event.player.inventory.itemInMainHand
        if(itemStack.type != Material.SADDLE) return
        if(!itemStack.itemMeta.persistentDataContainer.has(CustomItemConfig.customItemKey))return
        val horse = event.rightClicked as? Horse ?: return
        horse.ownerUniqueId?: return event.player.sendMessage("<red>This horse does not have an owner!</red>".toMini())
        val currentSaddle = horse.inventory.saddle
        if(currentSaddle != null && currentSaddle.type != Material.AIR)return
        event.isCancelled = true
        horse.inventory.saddle = itemStack
        event.player.inventory.setItemInMainHand(null)
    }

    @EventHandler
    fun onInteraction(event: PlayerInteractEntityEvent){
        if(event.hand != EquipmentSlot.HAND)return
        if(!event.player.isSneaking) return
        val horse = event.rightClicked
        if(horse !is Horse) return
        val ownerUUID = horse.ownerUniqueId ?: return
        if(ownerUUID != event.player.uniqueId && ConfigManager.getGeneralConfig().onlyOwnersAccessChest) return
        val saddle = horse.inventory.saddle ?: return
        if(!saddle.itemMeta.persistentDataContainer.has(CustomItemConfig.customItemKey))return
        val invSize = saddle.itemMeta.persistentDataContainer.getOrDefault(CustomItemConfig.customItemKey, PersistentDataType.INTEGER, 27)
        event.isCancelled = true
        val saddleBag = SaddleBag.getExisting(horse)?: let{
            SaddleBag.createNew(horse, event.player, invSize)
        }
        saddleBag.open(event.player)
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    fun onEntityDeath(event: EntityDeathEvent) {
        val entity = event.entity
        if (entity !is Horse) return

        val horse: Horse = entity
        val saddle = horse.inventory.saddle ?: return
        if (!saddle.itemMeta.persistentDataContainer.has(CustomItemConfig.customItemKey)) return
        Bukkit.getScheduler().runTask(HorseSaddleBag.getPlugin()) { _ ->
            val bag = SaddleBag.cache.remove(horse.uniqueId) ?: return@runTask
            try {
                bag.dropContent(horse.location)
            } catch (_: Exception) {
            }
            bag.delete()
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    fun onInventoryClose(event: InventoryCloseEvent) {
        (event.inventory.holder as? SaddleBag)?.save()
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    fun onUnArmour(event: InventoryClickEvent){
        if(event.inventory !is HorseInventory)return
        val horse = event.inventory.holder as? Horse ?: return
        if((event.slot != 0) || (event.currentItem == null))return
        val ownerUUID = horse.ownerUniqueId?: return
        if(ownerUUID != event.whoClicked.uniqueId && ConfigManager.getGeneralConfig().onlyOwnersAccessChest) {
            event.isCancelled = true
            return
        }
        val saddleBag = SaddleBag.getExisting(horse)?: return
        Bukkit.getScheduler().runTask(HorseSaddleBag.getPlugin()) { _ ->
            saddleBag.dropContent(horse.location)
            saddleBag.delete()
        }
    }

}
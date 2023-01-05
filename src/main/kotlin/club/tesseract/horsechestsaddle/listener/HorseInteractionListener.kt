package club.tesseract.horsechestsaddle.listener

import club.tesseract.horsechestsaddle.config.ConfigManager
import club.tesseract.horsechestsaddle.config.impl.CustomItemConfig
import club.tesseract.horsechestsaddle.holder.SaddleBag
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
        val horse = event.rightClicked as? Horse?: return
        val ownerUUID = horse.ownerUniqueId?: return
        if(ownerUUID != event.player.uniqueId && ConfigManager.getGeneralConfig().onlyOwnersAccessChest) return
        val saddle = horse.inventory.saddle?: return
        if(!saddle.itemMeta.persistentDataContainer.has(CustomItemConfig.customItemKey))return
        val invSize = saddle.itemMeta.persistentDataContainer.getOrDefault(CustomItemConfig.customItemKey, PersistentDataType.INTEGER, 27)

        event.isCancelled = true
        val saddleBag = SaddleBag.getExisting(horse)?: let{
            SaddleBag.createNew(horse, event.player, invSize)
        }
        saddleBag.open(event.player)
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    fun onEntityDeath(event: EntityDeathEvent){
        val entity = event.entity as? Horse?: return
        val saddle = entity.inventory.saddle?: return
        if(!saddle.itemMeta.persistentDataContainer.has(CustomItemConfig.customItemKey))return
        val bag = SaddleBag.cache.remove(entity.uniqueId)?: return
        try { bag.dropContent(entity.location) }catch (_: Exception){}
        bag.delete()
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
        if(horse.ownerUniqueId != event.whoClicked.uniqueId && ConfigManager.getGeneralConfig().onlyOwnersAccessChest) {
            event.isCancelled = true
            return
        }
        val saddleBag = SaddleBag.getExisting(horse)?: return
        saddleBag.dropContent(horse.location)
        saddleBag.delete()
    }

}
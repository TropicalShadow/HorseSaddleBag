package club.tesseract.horsechestsaddle.utils

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage

object ComponentUtils {

    fun String.toMini(): Component {
        return MiniMessage.miniMessage().deserialize(this)
    }

}
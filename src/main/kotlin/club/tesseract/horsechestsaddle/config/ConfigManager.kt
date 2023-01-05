package club.tesseract.horsechestsaddle.config

import club.tesseract.horsechestsaddle.HorseSaddleBag
import club.tesseract.horsechestsaddle.config.impl.GeneralConfig
import java.nio.file.Path
import kotlin.io.path.createDirectories
import kotlin.io.path.exists

object ConfigManager {

    private lateinit var configFolder: Path
    private lateinit var generalConfig: GeneralConfig

    fun loadConfigs(plugin: HorseSaddleBag) {
        configFolder = plugin.dataFolder.toPath()
        if(!configFolder.exists()) configFolder.createDirectories()

        generalConfig = ConfigHelper.initConfigFile(
            configFolder.resolve("general.json"),
            GeneralConfig()
        )
    }


    fun getGeneralConfig(): GeneralConfig {
        return generalConfig
    }


}
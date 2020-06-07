package com.github.vprimachenko.colorcoder.settings

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.ServiceManager.getService
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.util.xmlb.XmlSerializerUtil.copyBean
import org.jetbrains.annotations.Nullable


@State(name = "Colorcoder", storages = [(Storage("colorcoder_settings.xml"))])
class ColorcoderSettings : PersistentStateComponent<ColorcoderSettings> {
    /**
     * default value
     */
    var colorcode = true
    var colorSaturation = defaultColorLightness
    var colorLightness = defaultColorValue

    var scopes = defaultScopes

    @Nullable
    override fun getState() = this

    override fun loadState(state: ColorcoderSettings) {
        copyBean(state, this)
    }

    companion object {
        val instance: ColorcoderSettings
            get() = getService(ColorcoderSettings::class.java)

        const val defaultColorLightness = 70.0
        const val defaultColorValue = 50.0
        val defaultScopes = listOf("IDENTIFIER")

    }
}
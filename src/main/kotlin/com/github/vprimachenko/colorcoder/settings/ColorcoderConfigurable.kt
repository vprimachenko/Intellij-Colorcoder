package com.github.vprimachenko.colorcoder.settings

import com.github.vprimachenko.colorcoder.settings.form.ColorcoderSettingsForm
import com.intellij.openapi.options.Configurable
import com.intellij.openapi.options.ConfigurationException
import org.jetbrains.annotations.Nls
import javax.swing.JComponent

class ColorcoderConfigurable : Configurable {
    var settingsForm: ColorcoderSettingsForm? = null

    override fun createComponent(): JComponent? {
        settingsForm = settingsForm ?: ColorcoderSettingsForm()
        return settingsForm?.component()
    }

    override fun isModified(): Boolean {
        return settingsForm?.isModified ?: return false
    }

    @Throws(ConfigurationException::class)
    override fun apply() {
        val settings = ColorcoderSettings.instance
        settings.colorSaturation = settingsForm?.colorSaturation() ?: ColorcoderSettings.defaultColorLightness
        settings.colorLightness = settingsForm?.colorLightness() ?: ColorcoderSettings.defaultColorValue
        settings.scopes = settingsForm?.scopes() ?: ColorcoderSettings.defaultScopes
    }

    override fun reset() {
        settingsForm?.loadSettings()
    }

    override fun disposeUIResources() {
        settingsForm = null
    }

    @Nls
    override fun getDisplayName() = "Colorcoder"
}
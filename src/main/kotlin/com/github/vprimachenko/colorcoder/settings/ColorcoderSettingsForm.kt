package com.github.vprimachenko.colorcoder.settings.form

import com.github.vprimachenko.colorcoder.settings.ColorcoderSettings
import javax.swing.*

class ColorcoderSettingsForm {
    private lateinit var panel: JPanel
    private lateinit var colorcode: JCheckBox
    private lateinit var sSaturation: JSlider
    private lateinit var sLightness: JSlider
    private lateinit var taScopes: JTextArea

    private lateinit var saturationLabel: JLabel
    private lateinit var lightnessLabel: JLabel
    private lateinit var scopeLabel: JLabel

    private val settings: ColorcoderSettings = ColorcoderSettings.instance

    fun component(): JComponent? = panel

    fun colorcode() = colorcode.isSelected
    fun colorSaturation() = sSaturation.value.toDouble()
    fun colorLightness() = sLightness.value.toDouble()
    fun scopes() = taScopes.text?.lines()


    val isModified: Boolean
        get() = (colorcode() != settings.colorcode
                || colorSaturation() != settings.colorSaturation
                || colorLightness() != settings.colorLightness
                || scopes() != settings.scopes
                )

    init {
        loadSettings()
    }

    fun loadSettings() {
        colorcode.isSelected = settings.colorcode
        sSaturation.value = settings.colorSaturation.toInt()
        sLightness.value = settings.colorLightness.toInt()
        taScopes.text = settings.scopes.joinToString("\n")
    }
}

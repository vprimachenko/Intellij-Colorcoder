package com.github.vprimachenko.colorcoder

import com.github.vprimachenko.colorcoder.settings.ColorcoderSettings
import com.github.vprimachenko.colorcoder.util.CRC8
import com.intellij.codeInsight.daemon.impl.HighlightInfo
import com.intellij.codeInsight.daemon.impl.HighlightInfoType
import com.intellij.codeInsight.daemon.impl.HighlightVisitor
import com.intellij.codeInsight.daemon.impl.analysis.HighlightInfoHolder
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.markup.TextAttributes
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.impl.source.tree.LeafPsiElement
import org.hsluv.HUSLColorConverter
import java.awt.Color


class Colorcoder : HighlightVisitor {

    var highlightInfoHolder: HighlightInfoHolder? = null

    override fun suitableForFile(file: PsiFile): Boolean {
        return true
    }

    override fun analyze(file: PsiFile, updateWholeFile: Boolean, holder: HighlightInfoHolder, action: Runnable)
            : Boolean {
        highlightInfoHolder = holder
        onBeforeAnalyze(file, updateWholeFile)
        action.run()
        onAfterAnalyze()
        return true
    }

    private fun onBeforeAnalyze(file: PsiFile, updateWholeFile: Boolean) = Unit

    private fun onAfterAnalyze() {
        highlightInfoHolder = null
    }

    private val colordoderInfo: HighlightInfoType = HighlightInfoType
            .HighlightInfoTypeImpl(HighlightSeverity.INFORMATION, DefaultLanguageHighlighterColors.CONSTANT)

    private var crc = CRC8(0x9C, 0xFF)

    override fun clone(): HighlightVisitor = Colorcoder()

    override fun visit(element: PsiElement) {
        (element as? LeafPsiElement)?.elementType.toString().also { scope ->
            // CONSIDER: change this if to a check if `elementType` instance of fully qualified name e.g.
            // com.intellij.psi.xml.XmlTokenType.XML_NAME
            // com.intellij.psi.JavaTokenType.IDENTIFIER
            // that would need to get translated into instances via reflexion
            // c.f. https://upsource.jetbrains.com/idea-ce/file/idea-ce-49db9518f97861f7761bb28ed773537ae41e5dae/java/java-psi-api/src/com/intellij/psi/JavaTokenType.java?nav=395:412:focused&line=8&preview=false
            if (!ColorcoderSettings.instance.scopes.contains(scope)) return

            val color = Color.decode(colorcode(element.text))

            highlightInfoHolder?.add(HighlightInfo
                    .newHighlightInfo(colordoderInfo)
                    .textAttributes(TextAttributes(color, null, null, null, 0))
                    .range(element)
                    .create())
        }
    }

    private fun colorcode(content: String): String {
        crc.reset()
        val utf8buffer = content.toByteArray(Charsets.UTF_8)
        crc.update(utf8buffer, 0, utf8buffer.size)

        val colorHue = crc.value.toByte() * 360.0 / 256.0

        val colorSaturation = ColorcoderSettings.instance.colorSaturation
        val colorValue = ColorcoderSettings.instance.colorLightness

        return HUSLColorConverter.hsluvToHex(doubleArrayOf(colorHue, colorSaturation, colorValue))
    }
}
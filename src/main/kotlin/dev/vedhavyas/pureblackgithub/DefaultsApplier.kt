package dev.vedhavyas.pureblackgithub

import com.intellij.ide.ui.LafManager
import com.intellij.ide.ui.laf.UIThemeLookAndFeelInfo
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.editor.colors.EditorColorsManager
import com.intellij.openapi.editor.ex.EditorSettingsExternalizable
import com.intellij.openapi.keymap.KeymapManager
import com.intellij.openapi.keymap.ex.KeymapManagerEx

/**
 * One-shot switchover: applies this plugin's theme, editor scheme, keymap,
 * and flips block cursor on / indent guides off / intention bulb off.
 * Each step is independent — one failure doesn't abort the rest, and
 * everything is wrapped in try/catch since IntelliJ's API does drift
 * between versions.
 */
object DefaultsApplier {
    private val log = Logger.getInstance(DefaultsApplier::class.java)

    private const val THEME_NAME = "GitHub Dark Pure Black"
    private const val SCHEME_NAME = "GitHub Dark Pure Black"
    private const val KEYMAP_NAME = "Vedhavyas Mac"

    fun applyAll() {
        ApplicationManager.getApplication().invokeLater {
            applyTheme()
            applyEditorScheme()
            applyKeymap()
            applyEditorBehavior()
            log.info("Vedhavyas defaults applied")
        }
    }

    private fun applyTheme() {
        try {
            val lafManager = LafManager.getInstance()
            val target = lafManager.installedLookAndFeels
                .filterIsInstance<UIThemeLookAndFeelInfo>()
                .firstOrNull { it.name == THEME_NAME }
            if (target != null) {
                lafManager.setCurrentUIThemeLookAndFeel(target)
                lafManager.updateUI()
                log.info("Applied theme: $THEME_NAME")
            } else {
                log.warn("Theme not found: $THEME_NAME")
            }
        } catch (t: Throwable) {
            log.warn("Failed to apply theme", t)
        }
    }

    private fun applyEditorScheme() {
        try {
            val ecm = EditorColorsManager.getInstance()
            val scheme = ecm.allSchemes.firstOrNull { it.name == SCHEME_NAME }
            if (scheme != null) {
                ecm.setGlobalScheme(scheme)
                log.info("Applied editor scheme: $SCHEME_NAME")
            } else {
                log.warn("Editor scheme not found: $SCHEME_NAME")
            }
        } catch (t: Throwable) {
            log.warn("Failed to apply editor scheme", t)
        }
    }

    private fun applyKeymap() {
        try {
            val km = KeymapManager.getInstance() as KeymapManagerEx
            val keymap = km.allKeymaps.firstOrNull { it.name == KEYMAP_NAME }
            if (keymap != null) {
                km.setActiveKeymap(keymap)
                log.info("Applied keymap: $KEYMAP_NAME")
            } else {
                log.warn("Keymap not found: $KEYMAP_NAME")
            }
        } catch (t: Throwable) {
            log.warn("Failed to apply keymap", t)
        }
    }

    private fun applyEditorBehavior() {
        val s = EditorSettingsExternalizable.getInstance()
        // Each setter can potentially throw on API drift; isolate them.
        trySet("block cursor") { s.isBlockCursor = true }
        trySet("indent guides hidden") { s.isIndentGuidesShown = false }
        trySet("intention bulb off") { s.isShowIntentionBulb = false }
        log.info("Applied editor behavior flags")
    }

    private inline fun trySet(label: String, block: () -> Unit) {
        try {
            block()
        } catch (t: Throwable) {
            log.warn("Failed to set: $label", t)
        }
    }
}

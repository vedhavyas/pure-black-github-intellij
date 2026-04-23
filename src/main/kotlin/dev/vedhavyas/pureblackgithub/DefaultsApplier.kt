package dev.vedhavyas.pureblackgithub

import com.intellij.ide.ui.LafManager
import com.intellij.ide.ui.laf.UIThemeLookAndFeelInfo
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.editor.colors.EditorColorsManager
import com.intellij.openapi.editor.ex.EditorSettingsExternalizable
import com.intellij.openapi.keymap.KeymapManager
import com.intellij.openapi.keymap.ex.KeymapManagerEx
import com.intellij.openapi.project.ProjectManager
import com.intellij.ui.FileColorManager

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
    private const val KEYMAP_NAME = "Pure Black Mac"

    fun applyAll() {
        ApplicationManager.getApplication().invokeLater {
            applyTheme()
            applyEditorScheme()
            applyKeymap()
            applyEditorBehavior()
            applyFileColorsOff()
            log.info("Pure Black defaults applied")
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
            // Direct lookup by name — doesn't rely on getAllSchemes() being
            // fully populated yet. Fall back to iterating if getScheme returns
            // null (older platform versions, exotic registrations).
            val scheme = ecm.getScheme(SCHEME_NAME)
                ?: ecm.allSchemes.firstOrNull { it.name == SCHEME_NAME }
            if (scheme != null) {
                ecm.setGlobalScheme(scheme)
                log.info("Applied editor scheme: $SCHEME_NAME")
            } else {
                val known = ecm.allSchemes.joinToString { it.name }
                log.warn("Editor scheme not found: $SCHEME_NAME — allSchemes: [$known]")
            }
        } catch (t: Throwable) {
            log.warn("Failed to apply editor scheme", t)
        }
    }

    private fun applyKeymap() {
        try {
            val km = KeymapManager.getInstance() as KeymapManagerEx
            // Bundled keymaps register with internal name = resource path
            // (e.g. "/keymaps/PureBlackMac"), not the XML `name` attribute.
            // The user-facing display lives on presentableName — match there
            // first, then try internal name and direct by-name lookup.
            val keymap = km.allKeymaps.firstOrNull { it.presentableName == KEYMAP_NAME }
                ?: km.allKeymaps.firstOrNull { it.name == KEYMAP_NAME }
                ?: km.getKeymap(KEYMAP_NAME)
            if (keymap != null) {
                km.setActiveKeymap(keymap)
                log.info("Applied keymap: $KEYMAP_NAME (internal name: ${keymap.name})")
            } else {
                val known = km.allKeymaps.joinToString { "${it.presentableName} (${it.name})" }
                log.warn("Keymap not found: $KEYMAP_NAME — allKeymaps: [$known]")
            }
        } catch (t: Throwable) {
            log.warn("Failed to apply keymap", t)
        }
    }

    private fun applyFileColorsOff() {
        // Master toggle off on FileColorManager — kills all scope-based
        // tinting in the project view + editor tabs (Tests green, Non-Project
        // Files grey, etc.). FileColorManager is project-scoped and its
        // public API only exposes setEnabled() as a setter, so we sweep every
        // open project. Folder role markers (Excluded, Generated Sources) are
        // unaffected — those come from project structure, not File Colors.
        try {
            val projects = ProjectManager.getInstance().openProjects
            if (projects.isEmpty()) {
                log.info("No open projects — File Colors master toggle left unchanged")
                return
            }
            for (project in projects) {
                trySet("file colors off (${project.name})") {
                    FileColorManager.getInstance(project).isEnabled = false
                }
            }
            log.info("Disabled File Colors master toggle on ${projects.size} project(s)")
        } catch (t: Throwable) {
            log.warn("Failed to disable File Colors", t)
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

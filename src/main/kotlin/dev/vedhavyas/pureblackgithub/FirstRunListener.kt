package dev.vedhavyas.pureblackgithub

import com.intellij.ide.AppLifecycleListener
import com.intellij.notification.Notification
import com.intellij.notification.NotificationAction
import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.notification.Notifications
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.service

/**
 * On the first app frame creation after this plugin is installed, show a
 * notification offering to apply the full set of Pure Black defaults
 * (theme, editor scheme, keymap, editor behavior flags). Idempotent — once
 * shown (whether applied or dismissed), never surfaces again until the
 * state file is deleted.
 */
class FirstRunListener : AppLifecycleListener {
    override fun appFrameCreated(commandLineArgs: MutableList<String>) {
        val service = service<FirstRunService>()
        if (service.shown) return
        // Mark as shown immediately so the notification doesn't double-fire
        // if the user opens multiple projects simultaneously.
        service.shown = true

        ApplicationManager.getApplication().invokeLater {
            val notification: Notification = NotificationGroupManager.getInstance()
                .getNotificationGroup("PureBlackGithub")
                .createNotification(
                    title = "GitHub Dark Pure Black",
                    content = "Apply all Pure Black defaults? " +
                        "Switches the UI theme, editor color scheme, and keymap; " +
                        "flips block cursor on, indent guides off, intention bulb off.",
                    type = NotificationType.INFORMATION,
                )

            notification.addAction(object : NotificationAction("Apply all") {
                override fun actionPerformed(e: AnActionEvent, n: Notification) {
                    DefaultsApplier.applyAll()
                    n.expire()
                }
            })
            notification.addAction(object : NotificationAction("Dismiss") {
                override fun actionPerformed(e: AnActionEvent, n: Notification) {
                    n.expire()
                }
            })

            Notifications.Bus.notify(notification)
        }
    }
}

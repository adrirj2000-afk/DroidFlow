package com.droidflow.domain.engine

import android.app.Notification
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class NotificationInterceptorService : NotificationListenerService() {

    @Inject
    lateinit var flowEngine: FlowEngine

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        super.onNotificationPosted(sbn)
        sbn?.let {
            val packageName = it.packageName
            val extras = it.notification.extras
            val title = extras.getString(Notification.EXTRA_TITLE) ?: ""
            val text = extras.getCharSequence(Notification.EXTRA_TEXT)?.toString() ?: ""

            val params = mapOf(
                "package" to packageName,
                "title" to title,
                "text" to text
            )

            CoroutineScope(Dispatchers.IO).launch {
                flowEngine.evaluateFlows("NOTIFICATION", params)
            }
        }
    }
}

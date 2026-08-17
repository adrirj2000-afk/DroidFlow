/*
 * Developed by ZortVibes
 * Copyright (c) 2026. All rights reserved.
 */
package com.droidflow.domain.engine

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.firstOrNull

@AndroidEntryPoint
class BootReceiver : BroadcastReceiver() {

    @javax.inject.Inject lateinit var flowDao: com.droidflow.data.local.FlowDao
    @javax.inject.Inject lateinit var alarmScheduler: AlarmScheduler

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            val pendingResult = goAsync()

            scope.launch {
                try {
                    val flows = flowDao.getAllFlows().firstOrNull() ?: emptyList()
                    val timeFlows = flows.filter { it.triggerType == "TIME" && it.isEnabled }
                    for (flow in timeFlows) {
                        alarmScheduler.scheduleFlow(flow)
                    }
                } finally {
                    pendingResult.finish()
                }
            }
        }
    }
}

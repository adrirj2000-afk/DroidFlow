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
import javax.inject.Inject

@AndroidEntryPoint
class AlarmReceiver : BroadcastReceiver() {

    @Inject
    lateinit var flowEngine: FlowEngine

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    override fun onReceive(context: Context, intent: Intent) {
        val pendingResult = goAsync()
        val flowId = intent.getLongExtra("FLOW_ID", -1L)

        if (flowId == -1L) {
            pendingResult.finish()
            return
        }

        scope.launch {
            try {
                flowEngine.evaluateAndExecute(flowId.toString())
            } finally {
                pendingResult.finish()
            }
        }
    }
}

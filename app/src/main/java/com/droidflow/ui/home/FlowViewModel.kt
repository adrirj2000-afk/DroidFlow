/*
 * Developed by ZortVibes
 * Copyright (c) 2026. All rights reserved.
 */
package com.droidflow.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.droidflow.data.local.FlowEntity
import com.droidflow.data.local.FlowRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

import com.droidflow.domain.engine.FlowEngine
import com.droidflow.domain.engine.AlarmScheduler

@HiltViewModel
class FlowViewModel @Inject constructor(
    private val repository: FlowRepository,
    private val alarmScheduler: AlarmScheduler,
    private val flowEngine: FlowEngine
) : ViewModel() {

    private val _flows = MutableStateFlow<List<FlowEntity>>(emptyList())
    val flows: StateFlow<List<FlowEntity>> = _flows.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getAllFlows().collect { flowList ->
                _flows.value = flowList
            }
        }
    }

    fun toggleFlow(flow: FlowEntity, isEnabled: Boolean) {
        viewModelScope.launch {
            val updated = flow.copy(isEnabled = isEnabled)
            repository.updateFlow(updated)
            if (isEnabled) {
                alarmScheduler.scheduleFlow(updated)
            } else {
                alarmScheduler.cancelFlow(updated)
            }
        }
    }

    fun deleteFlow(flow: FlowEntity) {
        viewModelScope.launch {
            alarmScheduler.cancelFlow(flow)
            repository.deleteFlow(flow)
        }
    }

    fun insertFlow(flow: FlowEntity) {
        viewModelScope.launch {
            repository.insertFlow(flow)
            if (flow.isEnabled) {
                alarmScheduler.scheduleFlow(flow)
            }
        }
    }

    fun executeFlow(flowId: String) {
        viewModelScope.launch {
            flowEngine.evaluateAndExecute(flowId)
        }
    }
}

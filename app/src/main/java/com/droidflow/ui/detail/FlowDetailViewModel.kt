/*
 * Developed by ZortVibes
 * Copyright (c) 2026. All rights reserved.
 */
package com.droidflow.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.droidflow.data.local.FlowEntity
import com.droidflow.data.local.FlowRepository
import com.droidflow.data.local.HistoryEntity
import com.droidflow.domain.engine.FlowEngine
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FlowDetailViewModel @Inject constructor(
    private val repository: FlowRepository,
    private val flowEngine: FlowEngine,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val flowId: Long = savedStateHandle.get<String>("flowId")?.toLongOrNull() ?: -1L

    val flow: StateFlow<FlowEntity?> = repository.getFlowById(flowId)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    val history: StateFlow<List<HistoryEntity>> = repository.getHistoryForFlow(flowId)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun testFlow() {
        viewModelScope.launch {
            if (flowId != -1L) {
                flowEngine.evaluateAndExecute(flowId.toString())
            }
        }
    }
}

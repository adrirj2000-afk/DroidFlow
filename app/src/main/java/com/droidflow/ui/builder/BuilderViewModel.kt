/*
 * Developed by ZortVibes
 * Copyright (c) 2026. All rights reserved.
 */
package com.droidflow.ui.builder

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.droidflow.data.local.FlowEntity
import com.droidflow.data.local.FlowRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BuilderViewModel @Inject constructor(
    private val repository: FlowRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val flowId: String? = savedStateHandle.get<String>("flowId")

    private val _loadedFlow = MutableStateFlow<FlowEntity?>(null)
    val loadedFlow: StateFlow<FlowEntity?> = _loadedFlow

    init {
        flowId?.toLongOrNull()?.let { id ->
            viewModelScope.launch {
                _loadedFlow.value = repository.getFlowById(id).firstOrNull()
            }
        }
    }

    fun saveFlow(name: String, triggerType: String, conditionsJson: String, actionsJson: String) {
        viewModelScope.launch {
            val existing = _loadedFlow.value
            if (existing != null) {
                repository.updateFlow(
                    existing.copy(
                        name = name,
                        triggerType = triggerType,
                        conditionsJson = conditionsJson,
                        actionsJson = actionsJson
                    )
                )
            } else {
                val newFlow = FlowEntity(
                    name = name,
                    description = "Flujo creado automáticamente",
                    isEnabled = false,
                    triggerType = triggerType,
                    conditionsJson = conditionsJson,
                    actionsJson = actionsJson
                )
                repository.insertFlow(newFlow)
            }
        }
    }

    fun deleteFlow() {
        viewModelScope.launch {
            _loadedFlow.value?.let { flow ->
                repository.deleteFlow(flow)
            }
        }
    }
}

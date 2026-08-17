/*
 * Developed by ZortVibes
 * Copyright (c) 2026. All rights reserved.
 */
package com.droidflow.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.droidflow.data.local.FlowRepository
import com.droidflow.data.local.HistoryWithFlowName
import com.droidflow.core.preferences.PreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val repository: FlowRepository,
    private val preferencesManager: PreferencesManager
) : ViewModel() {

    val allHistory: StateFlow<List<HistoryWithFlowName>> = repository.getAllHistoryWithFlowName()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val themeMode = preferencesManager.themeMode

    fun setThemeMode(mode: String) {
        preferencesManager.setThemeMode(mode)
    }
}

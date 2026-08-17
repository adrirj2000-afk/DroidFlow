/*
 * Developed by ZortVibes
 * Copyright (c) 2026. All rights reserved.
 */
package com.droidflow.data.local

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FlowRepository @Inject constructor(
    private val flowDao: FlowDao,
    private val historyDao: HistoryDao
) {
    fun getAllFlows(): Flow<List<FlowEntity>> = flowDao.getAllFlows()

    fun getFlowById(id: Long): Flow<FlowEntity?> = flowDao.getFlowById(id)

    suspend fun insertFlow(flow: FlowEntity): Long = flowDao.insertFlow(flow)

    suspend fun updateFlow(flow: FlowEntity) = flowDao.updateFlow(flow)

    suspend fun deleteFlow(flow: FlowEntity) = flowDao.deleteFlow(flow)

    fun getHistoryForFlow(flowId: Long): Flow<List<HistoryEntity>> = historyDao.getHistoryForFlow(flowId)

    fun getAllHistoryWithFlowName(): Flow<List<HistoryWithFlowName>> = historyDao.getAllHistoryWithFlowName()

    suspend fun insertHistory(history: HistoryEntity): Long = historyDao.insertHistory(history)
}

/*
 * Developed by ZortVibes
 * Copyright (c) 2026. All rights reserved.
 */
package com.droidflow.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

data class HistoryWithFlowName(
    val id: Long,
    val flowName: String?,
    val timestamp: Long,
    val isSuccess: Boolean,
    val durationMs: Long,
    val errorMessage: String?
)

@Dao
interface HistoryDao {
    @Query("SELECT * FROM history WHERE flowId = :flowId ORDER BY timestamp DESC")
    fun getHistoryForFlow(flowId: Long): Flow<List<HistoryEntity>>

    @Query("SELECT * FROM history ORDER BY timestamp DESC")
    fun getAllHistory(): Flow<List<HistoryEntity>>

    @Query("SELECT h.id, f.name as flowName, h.timestamp, h.isSuccess, h.durationMs, h.errorMessage FROM history h LEFT JOIN flows f ON h.flowId = f.id ORDER BY h.timestamp DESC LIMIT 5")
    fun getAllHistoryWithFlowName(): Flow<List<HistoryWithFlowName>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHistory(history: HistoryEntity): Long
}

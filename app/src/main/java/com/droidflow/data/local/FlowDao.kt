/*
 * Developed by ZortVibes
 * Copyright (c) 2026. All rights reserved.
 */
package com.droidflow.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface FlowDao {
    @Query("SELECT * FROM flows")
    fun getAllFlows(): Flow<List<FlowEntity>>

    @Query("SELECT * FROM flows WHERE id = :id")
    fun getFlowById(id: Long): Flow<FlowEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFlow(flow: FlowEntity): Long

    @Update
    suspend fun updateFlow(flow: FlowEntity)

    @Delete
    suspend fun deleteFlow(flow: FlowEntity)
}

package com.example.project5

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface WaterEntryDao {
    @Insert
    suspend fun insert(waterEntry: WaterEntry)

    @Query("SELECT * FROM water_entries ORDER BY date DESC")
    fun getAllEntries(): LiveData<List<WaterEntry>>

    @Query("SELECT AVG(volume) FROM water_entries")
    suspend fun getAverageWaterIntake(): Float?
}

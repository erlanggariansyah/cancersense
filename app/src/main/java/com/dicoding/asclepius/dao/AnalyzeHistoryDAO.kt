package com.dicoding.asclepius.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dicoding.asclepius.model.AnalyzeHistory

@Dao
interface AnalyzeHistoryDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(analyzeHistory: AnalyzeHistory)

    @Query("SELECT * from analyzehistory ORDER BY id ASC")
    fun getAll():List<AnalyzeHistory>

    @Query("SELECT * from analyzehistory WHERE id = :id")
    fun getById(id: Int): AnalyzeHistory

    @Delete
    fun delete(analyzeHistory: AnalyzeHistory)
}

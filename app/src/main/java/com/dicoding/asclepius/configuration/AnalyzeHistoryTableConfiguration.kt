package com.dicoding.asclepius.configuration

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dicoding.asclepius.dao.AnalyzeHistoryDAO
import com.dicoding.asclepius.model.AnalyzeHistory

@Database(entities = [AnalyzeHistory::class], version = 1)
abstract class AnalyzeHistoryTableConfiguration: RoomDatabase() {
    abstract fun analyzeHistoryDAO(): AnalyzeHistoryDAO

    companion object {
        @Volatile
        private var INSTANCE: AnalyzeHistoryTableConfiguration? = null

        @JvmStatic
        fun getDatabase(context: Context): AnalyzeHistoryTableConfiguration {
            if (INSTANCE == null) {
                synchronized(AnalyzeHistoryTableConfiguration::class.java) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext, AnalyzeHistoryTableConfiguration::class.java, "cancersense").allowMainThreadQueries().build()
                }
            }

            return INSTANCE as AnalyzeHistoryTableConfiguration
        }
    }
}

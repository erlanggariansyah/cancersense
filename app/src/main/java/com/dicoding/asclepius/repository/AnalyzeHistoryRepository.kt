package com.dicoding.asclepius.repository

import android.app.Application
import com.dicoding.asclepius.configuration.AnalyzeHistoryTableConfiguration
import com.dicoding.asclepius.dao.AnalyzeHistoryDAO
import com.dicoding.asclepius.model.AnalyzeHistory
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class AnalyzeHistoryRepository(application: Application) {
    private val analyzeHistoryDAO: AnalyzeHistoryDAO
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = AnalyzeHistoryTableConfiguration.getDatabase(application)
        analyzeHistoryDAO = db.analyzeHistoryDAO()
    }

    fun getAll(): List<AnalyzeHistory> = analyzeHistoryDAO.getAll()

    fun getById(id: Int): AnalyzeHistory = analyzeHistoryDAO.getById(id)

    fun insert(analyzeHistory: AnalyzeHistory) {
        executorService.execute { analyzeHistoryDAO.insert(analyzeHistory) }
    }

    fun delete(analyzeHistory: AnalyzeHistory) {
        executorService.execute { analyzeHistoryDAO.delete(analyzeHistory) }
    }
}

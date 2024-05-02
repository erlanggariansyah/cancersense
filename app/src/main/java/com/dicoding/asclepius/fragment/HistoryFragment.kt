package com.dicoding.asclepius.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.asclepius.R
import com.dicoding.asclepius.adapter.AnalyzeAdapter
import com.dicoding.asclepius.model.AnalyzeHistory
import com.dicoding.asclepius.repository.AnalyzeHistoryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HistoryFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_history, container, false)
        val progressBar: ProgressBar = view.findViewById(R.id.historyProgessBar)
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerHistory)
        val repository: AnalyzeHistoryRepository = this.activity.let { AnalyzeHistoryRepository(it?.application!!) }

        recyclerView.layoutManager = LinearLayoutManager(view.context)

        progressBar.visibility = View.VISIBLE

        lifecycleScope.launch {
            val listAnalyzeHistory: List<AnalyzeHistory> = repository.getAll()

            withContext(Dispatchers.Main) {
                progressBar.visibility = View.INVISIBLE
                recyclerView.adapter = AnalyzeAdapter(listAnalyzeHistory, activity?.application!!)
            }
        }

        return view
    }
}
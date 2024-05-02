package com.dicoding.asclepius.adapter

import android.app.Application
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.asclepius.R
import com.dicoding.asclepius.model.AnalyzeHistory
import com.dicoding.asclepius.repository.AnalyzeHistoryRepository

class AnalyzeAdapter(val listAnalyze: List<AnalyzeHistory>, val application: Application): RecyclerView.Adapter<AnalyzeAdapter.AnalyzeHolder>() {
    private val repository: AnalyzeHistoryRepository = AnalyzeHistoryRepository(application)

    class AnalyzeHolder(analyzeView: View): RecyclerView.ViewHolder(analyzeView) {
        val analyzeImage: ImageView = analyzeView.findViewById(R.id.analyze_image)
        val analyzeId: TextView = analyzeView.findViewById(R.id.analyze_id)
        val analyzeDate: TextView = analyzeView.findViewById(R.id.analyze_date)
        val analyzeDelete: ImageView = analyzeView.findViewById(R.id.analyze_delete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnalyzeHolder = AnalyzeHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_analyze_history, parent, false))

    override fun getItemCount(): Int = listAnalyze.size

    override fun onBindViewHolder(holder: AnalyzeHolder, position: Int) {
        val (id, uri, result, date) = listAnalyze[position]

        holder.analyzeId.text = "Attempt " + id
        holder.analyzeImage.setImageURI(Uri.parse(uri))
        holder.analyzeDate.text = date

        holder.analyzeDelete.setOnClickListener {
            repository.delete(listAnalyze[position])
            this.notifyDataSetChanged()
        }

        holder.itemView.setOnClickListener {
            Toast.makeText(this.application, result, Toast.LENGTH_LONG).show()
        }
    }
}

package com.dicoding.asclepius.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.asclepius.R
import com.dicoding.asclepius.dto.news.News

class NewsAdapter(val news: List<News>): RecyclerView.Adapter<NewsAdapter.NewsHolder>() {
    class NewsHolder(newsView: View): RecyclerView.ViewHolder(newsView) {
        val newsImage: ImageView = newsView.findViewById(R.id.news_image)
        val newsAuthor: TextView = newsView.findViewById(R.id.news_author)
        val newsTitle: TextView = newsView.findViewById(R.id.news_title)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsHolder =
        NewsHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_news, parent, false))

    override fun getItemCount(): Int = news.size

    override fun onBindViewHolder(holder: NewsHolder, position: Int) {
        val (source, author, title, description, url, urlToImage, publishedAt, content) = news[position]

        holder.newsAuthor.text = author
        holder.newsTitle.text = title

        Glide.with(holder.itemView).load(urlToImage).into(holder.newsImage)

        holder.itemView.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setData(Uri.parse(url))

            holder.itemView.context.startActivity(intent)
        }
    }
}

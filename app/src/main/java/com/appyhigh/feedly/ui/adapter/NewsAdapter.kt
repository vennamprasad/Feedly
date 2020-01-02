package com.appyhigh.feedly.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.appyhigh.feedly.R
import com.appyhigh.feedly.data.model.News
import com.squareup.picasso.Picasso
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class NewsAdapter(private val newsList: List<News>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder?>() {
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): RecyclerView.ViewHolder {
        val view =
            LayoutInflater.from(viewGroup.context).inflate(R.layout.news_item, viewGroup, false)
        return NewsWithImageViewHolder(view)
    }

    @SuppressLint("SimpleDateFormat")
    override fun onBindViewHolder(@NonNull viewHolder: RecyclerView.ViewHolder, i: Int) {
        val news: News = newsList[i]
        (viewHolder as NewsWithImageViewHolder).title.text = news.title
        viewHolder.description.text = news.description
//        val df = SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'")
        try {
            val myFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
            myFormat.setTimeZone(TimeZone.getTimeZone("GMT+5.30"));
            val displayformat = "EEE dd MMM, hh:mm"
            val destFormat = SimpleDateFormat(displayformat)

            try {
                val myDate = myFormat.parse(news.publishedAt)
                viewHolder.publishedAt.setText(destFormat.format(myDate!!))
            } catch (e: ParseException) {
                e.printStackTrace()
            }

        } catch (exc: Exception) {
            exc.printStackTrace()
        }
        Picasso.get()
            .load(news.urlToImage)
            .placeholder(R.drawable.ic_placeholder_image_wide)
            .resize(600, 200)
            .centerInside()
            .into(viewHolder.iv_news)
    }


    inner class NewsWithImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var title: TextView = itemView.findViewById(R.id.title)
        var description: TextView = itemView.findViewById(R.id.description)
        var iv_news: ImageView = itemView.findViewById(R.id.urlToImage)
        var publishedAt: TextView = itemView.findViewById(R.id.publishedAt)
    }

    override fun getItemCount(): Int {
        return newsList.size
    }
}
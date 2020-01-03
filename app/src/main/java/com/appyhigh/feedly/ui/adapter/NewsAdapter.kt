package com.appyhigh.feedly.ui.adapter

import android.annotation.SuppressLint
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.appyhigh.feedly.R
import com.appyhigh.feedly.data.model.News
import com.appyhigh.feedly.data.model.UnifiedNativeAdViewHolder
import com.google.android.gms.ads.formats.UnifiedNativeAd
import com.google.android.gms.ads.formats.UnifiedNativeAdView
import com.squareup.picasso.Picasso
import java.util.*


class NewsAdapter(private val newsList: ArrayList<News>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder?>() {
    // A menu item view type.
    private val MENU_ITEM_VIEW_TYPE = 0

    // The unified native ad view type.
    private val UNIFIED_NATIVE_AD_VIEW_TYPE = 1

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val unifiedNativeLayoutView: View?
        val menuItemLayoutView: View?
        if (UNIFIED_NATIVE_AD_VIEW_TYPE == viewType) {
            unifiedNativeLayoutView = LayoutInflater.from(viewGroup.context).inflate(R.layout.ad_unified, viewGroup, false)
            return UnifiedNativeAdViewHolder(view = unifiedNativeLayoutView)
        } else if (MENU_ITEM_VIEW_TYPE == viewType) {
            menuItemLayoutView =
                LayoutInflater.from(viewGroup.context)
                    .inflate(R.layout.news_item, viewGroup, false)
            return NewsWithImageViewHolder(itemView = menuItemLayoutView)
        } else {
            menuItemLayoutView =
                LayoutInflater.from(viewGroup.context)
                    .inflate(R.layout.news_item, viewGroup, false)
            return NewsWithImageViewHolder(itemView = menuItemLayoutView)
        }

    }

    private fun populateNativeAdView(
        nativeAd: UnifiedNativeAd,
        adView: UnifiedNativeAdView
    ) { // Some assets are guaranteed to be in every UnifiedNativeAd.
        try {
            (adView.headlineView as TextView).text = nativeAd.headline
            (adView.bodyView as TextView).text = nativeAd.body
            (adView.callToActionView as Button).setText(nativeAd.callToAction)
            // These assets aren't guaranteed to be in every UnifiedNativeAd, so it's important to
            // check before trying to display them.
            val icon = nativeAd.icon
            if (icon == null) {
                adView.iconView.visibility = View.INVISIBLE
            } else {
                (adView.iconView as ImageView).setImageDrawable(icon.drawable)
                adView.iconView.visibility = View.VISIBLE
            }
            if (nativeAd.price == null) {
                adView.priceView.visibility = View.INVISIBLE
            } else {
                adView.priceView.visibility = View.VISIBLE
                (adView.priceView as TextView).text = nativeAd.price
            }
            if (nativeAd.store == null) {
                adView.storeView.visibility = View.INVISIBLE
            } else {
                adView.storeView.visibility = View.VISIBLE
                (adView.storeView as TextView).text = nativeAd.store
            }
            if (nativeAd.starRating == null) {
                adView.starRatingView.visibility = View.INVISIBLE
            } else {
                (adView.starRatingView as RatingBar).rating = nativeAd.starRating.toFloat()
                adView.starRatingView.visibility = View.VISIBLE
            }
            if (nativeAd.advertiser == null) {
                adView.advertiserView.visibility = View.INVISIBLE
            } else {
                (adView.advertiserView as TextView).text = nativeAd.advertiser
                adView.advertiserView.visibility = View.VISIBLE
            }
            // Assign native ad object to the native view.
            adView.setNativeAd(nativeAd)
        } catch (exception: java.lang.Exception) {
            exception.printStackTrace()
        }
    }

    @SuppressLint("SimpleDateFormat")
    override fun onBindViewHolder(@NonNull viewHolder: RecyclerView.ViewHolder, position: Int) {
        try {
            val viewType = getItemViewType(position)
            when (viewType) {
                UNIFIED_NATIVE_AD_VIEW_TYPE -> {
                    val nativeAd = newsList.get(position).nativeAd as UnifiedNativeAd
                    populateNativeAdView(nativeAd, (viewHolder as UnifiedNativeAdViewHolder).adView)
                }
                MENU_ITEM_VIEW_TYPE -> {
                    loadNewsFeed(viewHolder, position)
                }
                else -> {
                    loadNewsFeed(viewHolder, position)
                }
            }
        } catch (ex: java.lang.Exception) {
            ex.printStackTrace()
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun loadNewsFeed(viewHolder: RecyclerView.ViewHolder, position: Int) {
        try {
            val menuItemHolder: NewsWithImageViewHolder = viewHolder as NewsWithImageViewHolder
            val topic: News = newsList[position]
            /*val anyObj: Any = newsList[position]
            val jsonany: JsonObject = Gson().toJsonTree(anyObj).asJsonObject
            val topic: News = Gson().fromJson(jsonany.toString(), News::class.java)*/
            menuItemHolder.title.text = topic.title
            menuItemHolder.description.text = topic.description
            if (topic.publishedAt != null) {
                val now = System.currentTimeMillis()
                val relativeTime = DateUtils.getRelativeTimeSpanString(
                    topic.publishedAt.getTime(), now, DateUtils.SECOND_IN_MILLIS
                ).toString()
                menuItemHolder.publishedAt.text = relativeTime
            } else {
                menuItemHolder.publishedAt.text = ""
            }
            Picasso.get()
                .load(topic.urlToImage)
                .placeholder(R.drawable.ic_placeholder_image_wide)
                .resize(600, 200)
                .centerInside()
                .into(menuItemHolder.iv_news)
        } catch (exc: Exception) {
            exc.printStackTrace()
        }
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

    override fun getItemViewType(position: Int): Int {
        val recyclerViewItem: News = newsList.get(position)
        return if (recyclerViewItem.nativeAd != null) {
            UNIFIED_NATIVE_AD_VIEW_TYPE
        } else MENU_ITEM_VIEW_TYPE
    }

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        super.onViewDetachedFromWindow(holder)

    }
}
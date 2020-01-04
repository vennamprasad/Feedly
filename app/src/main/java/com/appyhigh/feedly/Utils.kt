package com.appyhigh.feedly

import android.content.Context
import android.net.ConnectivityManager
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.appyhigh.feedly.data.model.News
import com.appyhigh.feedly.ui.adapter.NewsAdapter
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.formats.NativeAdOptions
import com.google.android.gms.ads.formats.UnifiedNativeAd
import java.text.SimpleDateFormat
import java.util.*

/* URLS*/
const val BASE_URL = "https://newsapi.org/v2/"

//API Key
const val API_KEY = "15b744f890084f54b6f1141cf2e12e84"

const val SOURCES =
    "Pinkvilla.com,Ndtv.com,Moneycontrol.com,The Times of India,Morungexpress.com,Indiablooms.com,Hindustantimes.com,Metro.co.uk,Deccanherald.com,Koimoi.com," +
            "CNN,Bigblueview.com,Chicagotribune.com,Youtube.com,The Wall Street Journal,TechRadar,Fox News,Cheatsheet.com,NBC News,CBS News,Redskins.com,Ars Technica,Express.co.uk,Yahoo.com,Associated Press"

const val CATEGORY_BUSINESS = "business"

const val CATEGORY_TECHNOLOGY = "technology"

const val CATEGORY_ENTERTAINMENT = "entertainment"

const val CATEGORY_SCIENCE = "science"

const val CATEGORY_SPORTS = "sports"

//connections
fun checkConnection(context: Context?): Boolean {
    return (context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).activeNetworkInfo != null
}

fun String.getStringDate(
    initialFormat: String,
    requiredFormat: String,
    locale: Locale = Locale.getDefault()
): String? {
    return this.toDate(initialFormat, locale)?.toString(requiredFormat, locale)
}

fun String.toDate(format: String, locale: Locale = Locale.getDefault()): Date? =
    SimpleDateFormat(format, locale).parse(this)

fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String {
    val formatter = SimpleDateFormat(format, locale)
    return formatter.format(this)
}

const val WELCOME_MESSAGE_KEY = "welcome_message"
const val WELCOME_MESSAGE_CAPS_KEY = "welcome_message_caps"
const val ADD_ENABLE = "add_enable"

var adStatus: Boolean = false

fun loadNativeAds(
    activity: FragmentActivity?,
    mNativeAds: ArrayList<UnifiedNativeAd>,
    newsArrayList: ArrayList<News>,
    swipeRefreshLayout: SwipeRefreshLayout,
    mAdapter: NewsAdapter?,
    recyclerView: RecyclerView?
) {
    try {
        var unitId: String? = ""
        lateinit var adLoader: AdLoader
        //test key
        unitId = "ca-app-pub-3940256099942544/2247696110"
        adLoader = AdLoader.Builder(activity, unitId)
            .forUnifiedNativeAd { ad: UnifiedNativeAd ->
                // Show the ad.
                mNativeAds.add(ad)
                if (!adLoader.isLoading) {
                    insertAdsInMenuItems(
                        mNativeAds,
                        newsArrayList,
                        swipeRefreshLayout,
                        mAdapter,
                        recyclerView
                    )
                }
            }
            .withAdListener(object : AdListener() {
                override fun onAdFailedToLoad(errorCode: Int) {
                    // Handle the failure by logging, altering the UI, and so on.
                    if (!adLoader.isLoading) {
                        insertAdsInMenuItems(
                            mNativeAds,
                            newsArrayList,
                            swipeRefreshLayout,
                            mAdapter,
                            recyclerView
                        )
                    }
                }
            })
            .withNativeAdOptions(
                NativeAdOptions.Builder()
                    // Methods in the NativeAdOptions.Builder class can be
                    // used here to specify individual options settings.
                    .build()
            )
            .build()
        adLoader.loadAds(AdRequest.Builder().build(), 5)
    } catch (exception: Exception) {
        exception.printStackTrace()
    }
}

fun insertAdsInMenuItems(
    mNativeAds: ArrayList<UnifiedNativeAd>,
    newsArrayList: ArrayList<News>,
    swipeRefreshLayout: SwipeRefreshLayout,
    mAdapter: NewsAdapter?,
    recyclerView: RecyclerView?
) {
    try {
        if (mNativeAds.size <= 0) {
            return
        }
        val offset: Int = (newsArrayList.size / mNativeAds.size) + 1
        var index = 0
        for (ad: UnifiedNativeAd in mNativeAds) {
            val news = News()
            news.nativeAd = ad
            newsArrayList.add(index, news)
            index = index + offset
        }
        swipeRefreshLayout.isRefreshing = false
        recyclerView!!.adapter = mAdapter
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun show_msg(context: Context?, message: String) {
    Toast.makeText(context, "Internet connection not Available", Toast.LENGTH_SHORT).show()
}

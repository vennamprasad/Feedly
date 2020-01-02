package com.appyhigh.feedly.ui.menu

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.provider.UserDictionary
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.appyhigh.feedly.API_KEY
import com.appyhigh.feedly.BuildConfig
import com.appyhigh.feedly.R
import com.appyhigh.feedly.checkConnection
import com.appyhigh.feedly.data.model.News
import com.appyhigh.feedly.data.model.NewsResource
import com.appyhigh.feedly.retrofit.ApiClient.apiService
import com.appyhigh.feedly.ui.adapter.NewsAdapter
import com.appyhigh.feedly.ui.web.WebViewActivity
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.formats.UnifiedNativeAd
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList


class Top_HeadlinesFragment : Fragment(), OnRefreshListener {
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private var newsArrayList: ArrayList<News> = ArrayList()
    private var mAdapter: NewsAdapter? = null
    private var recyclerView: RecyclerView? = null
    // The number of native ads to load and display.
    val NUMBER_OF_ADS = 5
    // The AdLoader used to load ads.
    private var adLoader: AdLoader? = null
    // List of native ads that have been successfully loaded.
    private val mNativeAds: ArrayList<UnifiedNativeAd> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_news, container, false)
        initViews(view)
        return view
    }

    private fun initViews(view: View) {
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout)!!
        swipeRefreshLayout.setOnRefreshListener(this)
        swipeRefreshLayout.setColorSchemeResources(
            R.color.colorAccent,
            R.color.colorGreen,
            R.color.colorRed,
            R.color.colorOrange
        )
        MobileAds.initialize(context, UserDictionary.Words.APP_ID)
        // Initialize the Google Mobile Ads SDK
        MobileAds.initialize(activity, "ca-app-pub-5798395965775186/7710844532")
        if (BuildConfig.DEBUG) {
            AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build()
        }
        recyclerView = view.findViewById(R.id.recyclerView)
        loadJSON()
        val mLayoutManager: RecyclerView.LayoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        recyclerView!!.layoutManager = mLayoutManager
        recyclerView!!.addItemDecoration(
            DividerItemDecoration(activity, LinearLayoutManager.VERTICAL)
        )
        mAdapter = NewsAdapter(newsArrayList)
        recyclerView!!.adapter = mAdapter
        recyclerView!!.addOnItemTouchListener(
            RecyclerTouchListener(
                activity,
                recyclerView!!,
                object : RecyclerTouchListener.ClickListener {
                    override fun onClick(view: View?, position: Int) {
                        val news: News = newsArrayList[position]
                        val title_Intent = Intent(activity, WebViewActivity::class.java)
                        title_Intent.putExtra("url", news.url)
                        startActivity(title_Intent)
                    }
                    override fun onLongClick(view: View?, position: Int) {}
                })
        )

    }

    @SuppressLint("DefaultLocale")
    private fun loadJSON() {
        swipeRefreshLayout.isRefreshing = true
        if (checkConnection(context)) {
            val request = apiService
            val call: Call<NewsResource?>? = request.getTopHeadlines(
                Locale.getDefault().country.toString().toLowerCase(),
                API_KEY
            )
            call?.enqueue(object : Callback<NewsResource?> {
                override fun onResponse(
                    call: Call<NewsResource?>?,
                    response: Response<NewsResource?>
                ) {
                    if (response.isSuccessful && response.body()?.articles != null) {
                        swipeRefreshLayout.isRefreshing = false
                        if (!newsArrayList.isEmpty()) {
                            newsArrayList.clear()
                        }
                        newsArrayList = response.body()!!.articles!!
                        loadNativeAds()
                    }
                }

                override fun onFailure(call: Call<NewsResource?>?, t: Throwable?) {
                    swipeRefreshLayout.isRefreshing = false
                    Toast.makeText(activity, "Something went wrong...", Toast.LENGTH_SHORT).show()
                }
            })

        } else {
            Toast.makeText(activity, "Internet connection not Available", Toast.LENGTH_SHORT)
                .show()
            swipeRefreshLayout.isRefreshing = false
        }
    }

    override fun onRefresh() {
        swipeRefreshLayout.isRefreshing = true
        loadJSON()
    }

    private fun insertAdsInMenuItems() {
        if (mNativeAds.size <= 0) {
            return;
        }
        val offset = (newsArrayList.size / mNativeAds.size) + 1;
        var index = 0;
        for (ad: UnifiedNativeAd in mNativeAds) {
            val news: News? = null
            news?.nativeAd = ad;
            newsArrayList.add(news!!);
            index = index + offset;
        }
    }

    private fun loadNativeAds() {

        val builder: AdLoader.Builder =
            AdLoader.Builder(activity, getString(R.string.admob_ad_unit_id))
        val adLoader = builder.forUnifiedNativeAd { ad ->
            // A native ad loaded successfully, check if the ad loader has finished loading
            // and if so, insert the ads into the list.
            mNativeAds.add(ad)
            if (!adLoader?.isLoading()!!) {
                insertAdsInMenuItems()
            }
        }.withAdListener(
            object : AdListener() {
                override fun onAdFailedToLoad(errorCode: Int) { // A native ad failed to load, check if the ad loader has finished loading
// and if so, insert the ads into the list.
                    Log.e(
                        "MainActivity", "The previous native ad failed to load. Attempting to"
                                + " load another."
                    )
                    if (!adLoader?.isLoading()!!) {
                        insertAdsInMenuItems()
                    }
                }
            }).build()
        // Load the Native Express ad.
        adLoader.loadAds(AdRequest.Builder().build(), NUMBER_OF_ADS)
    }

}
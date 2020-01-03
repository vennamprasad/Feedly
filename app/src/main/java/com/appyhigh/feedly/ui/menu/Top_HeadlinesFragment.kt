package com.appyhigh.feedly.ui.menu

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.appyhigh.feedly.*
import com.appyhigh.feedly.data.model.News
import com.appyhigh.feedly.data.model.NewsResource
import com.appyhigh.feedly.retrofit.ApiClient
import com.appyhigh.feedly.ui.adapter.NewsAdapter
import com.appyhigh.feedly.ui.web.WebViewActivity
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.formats.UnifiedNativeAd
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class Top_HeadlinesFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private var newsArrayList: ArrayList<News> = ArrayList()
    private var mAdapter: NewsAdapter? = null
    private var recyclerView: RecyclerView? = null
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
        // Initialize the Google Mobile Ads SDK
        MobileAds.initialize(activity, getString(R.string.admob_app_id))
        recyclerView = view.findViewById(R.id.recyclerView)
        loadJSON()
        val mLayoutManager: RecyclerView.LayoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        recyclerView!!.layoutManager = mLayoutManager
        recyclerView!!.addItemDecoration(
            DividerItemDecoration(activity, LinearLayoutManager.VERTICAL)
        )
        recyclerView!!.addOnItemTouchListener(
            RecyclerTouchListener(
                activity,
                recyclerView!!,
                object : RecyclerTouchListener.ClickListener {
                    override fun onClick(view: View?, position: Int) {
                        val news: News = newsArrayList[position]
                        if (news.nativeAd == null) {
                            val title_Intent = Intent(activity, WebViewActivity::class.java)
                            title_Intent.putExtra("url", news.url)
                            startActivity(title_Intent)
                        }
                    }

                    override fun onLongClick(view: View?, position: Int) {}
                })
        )

    }


    override fun onRefresh() {
        loadJSON()
    }

    @SuppressLint("DefaultLocale")
    private fun loadJSON() {
        swipeRefreshLayout.isRefreshing = true
        swipeRefreshLayout.isRefreshing = true
        if (checkConnection(activity)) {
            val request = ApiClient.apiService
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
                        if (!newsArrayList.isEmpty()) {
                            newsArrayList.clear()
                        }
                        newsArrayList = response.body()!!.articles!!
                        mAdapter = NewsAdapter(newsArrayList)
                        if (!adStatus) {
                            swipeRefreshLayout.isRefreshing = false
                            recyclerView!!.adapter = mAdapter
                        } else {
                            loadNativeAds(
                                activity,
                                mNativeAds,
                                newsArrayList,
                                swipeRefreshLayout,
                                mAdapter,
                                recyclerView
                            )
                        }
                    }
                }

                override fun onFailure(call: Call<NewsResource?>?, t: Throwable?) {
                    swipeRefreshLayout.isRefreshing = false
                    show_msg(activity, "Something went wrong...")
                }
            })

        } else {
            show_msg(activity, "Internet connection not Available")
            swipeRefreshLayout.isRefreshing = false
        }
    }
}
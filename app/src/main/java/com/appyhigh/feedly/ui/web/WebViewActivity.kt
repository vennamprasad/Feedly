package com.appyhigh.feedly.ui.web

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.appyhigh.feedly.R
import kotlinx.android.synthetic.main.activity_web_view.*


class WebViewActivity : AppCompatActivity(), OnRefreshListener {
    var url: String? = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)
        url = intent.getStringExtra("url")
        swipe_refresh.isRefreshing = true
        swipe_refresh.setColorSchemeResources(
            R.color.colorAccent,
            R.color.colorGreen,
            R.color.colorRed,
            R.color.colorOrange
        )
        loadPage()
        swipe_refresh.setOnRefreshListener(this)
        fab_share.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_TEXT, url)
            startActivity(Intent.createChooser(shareIntent, "Share link using"))
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun loadPage() {
        webView.getSettings().javaScriptEnabled = true
        webView.getSettings().builtInZoomControls = true
        webView.getSettings().displayZoomControls = true
        webView.setWebViewClient(WebViewClient())
        webView.loadUrl(url)
        webView.setWebChromeClient(object : WebChromeClient() {
            override fun onProgressChanged(view: WebView, progress: Int) {
                if (progress == 100) {
                    swipe_refresh.setRefreshing(false)
                } else {
                    swipe_refresh.setRefreshing(true)
                }
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        cacheDir.deleteOnExit()
        webView!!.clearCache(true)
    }

    override fun onRefresh() {
        swipe_refresh.isRefreshing = true
        loadPage()
    }
}

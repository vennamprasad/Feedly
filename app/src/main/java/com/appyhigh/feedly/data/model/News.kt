package com.appyhigh.feedly.data.model

import com.google.android.gms.ads.formats.UnifiedNativeAd
import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName


class News {
    @SerializedName("source")
    @Expose
    val source: Source? = null
    @SerializedName("author")
    @Expose
    val author: String = ""
    @SerializedName("title")
    @Expose
    val title: String = ""
    @SerializedName("description")
    @Expose
    val description: String = ""
    @SerializedName("url")
    @Expose
    val url: String? = null
    @SerializedName("urlToImage")
    @Expose
    val urlToImage: String = ""
    @SerializedName("publishedAt")
    @Expose
    val publishedAt: String = ""
    @SerializedName("content")
    @Expose
    val content: String = ""

    var nativeAd:UnifiedNativeAd?=null
}

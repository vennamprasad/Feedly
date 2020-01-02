package com.appyhigh.feedly.data.model

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName


class NewsResource() {
    @SerializedName("status")
    @Expose
    public val status: String? = null
    @SerializedName("totalResults")
    @Expose
    public val totalResults: Int? = null
    @SerializedName("articles")
    @Expose
    public val articles: ArrayList<News>? = null
}

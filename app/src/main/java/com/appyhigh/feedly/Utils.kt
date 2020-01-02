package com.appyhigh.feedly

import android.content.Context
import android.net.ConnectivityManager
import java.text.SimpleDateFormat
import java.util.*

/* URLS*/
const val BASE_URL = "https://newsapi.org/v2/"

//API Key
const val API_KEY = "15b744f890084f54b6f1141cf2e12e84"

const val SOURCES = "Pinkvilla.com,Ndtv.com,Moneycontrol.com,The Times of India,Morungexpress.com,Indiablooms.com,Hindustantimes.com,Metro.co.uk,Deccanherald.com,Koimoi.com,"+
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
fun String.getStringDate(initialFormat: String, requiredFormat: String, locale: Locale = Locale.getDefault()): String? {
    return this.toDate(initialFormat, locale)?.toString(requiredFormat, locale)
}

fun String.toDate(format: String, locale: Locale = Locale.getDefault()): Date? = SimpleDateFormat(format, locale).parse(this)

fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String {
    val formatter = SimpleDateFormat(format, locale)
    return formatter.format(this)
}
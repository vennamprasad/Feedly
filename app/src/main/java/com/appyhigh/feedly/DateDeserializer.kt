package com.appyhigh.feedly

import com.google.gson.JsonDeserializer
import kotlin.jvm.Throws
import com.google.gson.JsonParseException
import com.google.gson.JsonElement
import com.google.gson.JsonDeserializationContext
import android.annotation.SuppressLint
import android.util.Log
import java.lang.reflect.Type
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class DateDeserializer : JsonDeserializer<Date?> {
    @Throws(JsonParseException::class)
    override fun deserialize(
        element: JsonElement,
        arg1: Type,
        arg2: JsonDeserializationContext
    ): Date? {
        val date = element.asString
        @SuppressLint("SimpleDateFormat") val formatter =
            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
        formatter.timeZone = TimeZone.getTimeZone("UTC")
        return try {
            formatter.parse(date)
        } catch (e: ParseException) {
            Log.e("DateDeserializer", "Failed to parse Date due to:", e)
            null
        }
    }
}
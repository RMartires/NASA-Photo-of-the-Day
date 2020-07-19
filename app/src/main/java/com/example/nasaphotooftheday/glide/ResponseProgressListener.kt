package com.example.nasaphotooftheday.glide

import okhttp3.HttpUrl

interface ResponseProgressListener {
    fun update(url: HttpUrl, bytesRead: Long, contentLength: Long)
}
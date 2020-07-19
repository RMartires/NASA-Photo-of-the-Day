package com.example.nasaphotooftheday

import android.net.Uri
import android.os.Bundle
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.nasaphotooftheday.glide.GlideApp.with
import com.example.nasaphotooftheday.glide.GlideImageLoader
import com.github.piasy.biv.BigImageViewer
import kotlinx.android.synthetic.main.activity_zoom_image.*


class ZoomImageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_zoom_image)

        //BigImageViewer.initialize(Glide.with(this));
        //GlideImageLoader(mBigImage,progressBar2)
        val url = intent.getStringExtra("URL")
        mBigImage.showImage(Uri.parse(url))

    }
}
package com.example.nasaphotooftheday

import android.net.Uri
import android.os.Bundle
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.leanback.app.ProgressBarManager
import com.github.piasy.biv.BigImageViewer
import com.github.piasy.biv.loader.ImageLoader
import com.github.piasy.biv.loader.fresco.FrescoImageLoader
import com.github.ybq.android.spinkit.sprite.Sprite
import com.github.ybq.android.spinkit.style.CubeGrid
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_zoom_image.*
import java.io.File


class ZoomImageActivity : AppCompatActivity() {
    var mProgressBar2Manager = ProgressBarManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var mBigImageViewer = BigImageViewer.initialize(FrescoImageLoader.with(this));
        setContentView(R.layout.activity_zoom_image)

        val url = intent.getStringExtra("URL")

        val cubeGrid: Sprite = CubeGrid()
        progressBar2.indeterminateDrawable = cubeGrid
        mProgressBar2Manager.setProgressBarView(progressBar2)

        BigImage.setImageLoaderCallback(myImageLoaderCallback)
        BigImage.showImage(Uri.parse(url))
        mProgressBar2Manager.show()


    }

    var myImageLoaderCallback = object : ImageLoader.Callback{


        override fun onStart() {
            // Image download has started
        }

        override fun onFinish() {

        }

        override fun onSuccess(image: File) {
            // Image was retrieved successfully (either from cache or network)
            mProgressBar2Manager.hide()
        }

        override fun onFail(error:Exception) {
            // Image download failed
        }

        override fun onCacheHit(imageType: Int, image: File?) {

        }

        override fun onCacheMiss(imageType: Int, image: File?) {

        }

        override fun onProgress(progress: Int) {

        }

    }
}
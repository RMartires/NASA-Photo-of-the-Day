package com.example.nasaphotooftheday

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.leanback.app.ProgressBarManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.nasaphotooftheday.POD.POD
import com.example.nasaphotooftheday.POD.PODViewModel
import com.facebook.drawee.backends.pipeline.Fresco
import com.github.ybq.android.spinkit.sprite.Sprite
import com.github.ybq.android.spinkit.style.CubeGrid
import kotlinx.android.synthetic.main.activity_splash.*
import java.lang.String


class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Fresco.initialize(this)
        setContentView(R.layout.activity_splash)


        val uri = Uri.Builder()
            .scheme("res") // "res" UriUtil.LOCAL_RESOURCE_SCHEME
            .path(String.valueOf(R.drawable.star_splash))
            .build()
        imageViewsplash.setImageURI(uri)
        //SplashProgressBar..
        var mSplashProgressBarManager = ProgressBarManager()
        val mCubeGrid: Sprite = CubeGrid()
        splashprogressBar.indeterminateDrawable = mCubeGrid
        mSplashProgressBarManager.setProgressBarView(splashprogressBar)
        mSplashProgressBarManager.show()

        if(getConectionState()){
            var mPODSplashViewModel = ViewModelProvider(this).get(PODViewModel::class.java)

            //
            mPODSplashViewModel.pod.observe(this, Observer<List<POD>>() {
                mSplashProgressBarManager.hide()

                val intent = Intent(baseContext, MainActivity::class.java)
                startActivity(intent)
            })

        }else{
            //no internet dialog
            var ErrorConnectionDialogBuilder = AlertDialog.Builder(this)
            ErrorConnectionDialogBuilder.setNegativeButton("close",
                DialogInterface.OnClickListener{ dialog, id ->
                // User clicked close button
                this.finishAffinity();
            })
            ErrorConnectionDialogBuilder.setTitle("Error No network access")
            var mErrorConnectionDialog =ErrorConnectionDialogBuilder.create()
            mErrorConnectionDialog.show()

        }

    }

    private fun getConectionState():Boolean{
        var connected = false
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connected = if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).state == NetworkInfo.State.CONNECTED ||
            connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).state == NetworkInfo.State.CONNECTED) {
            return true
        } else {
            return false
        }
    }
}
package com.example.nasaphotooftheday

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.icu.util.Calendar
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.DatePicker
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.leanback.app.ProgressBarManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.nasaphotooftheday.POD.POD
import com.example.nasaphotooftheday.POD.PODViewModel
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.drawable.ProgressBarDrawable
import com.google.android.youtube.player.YouTubeStandalonePlayer
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private lateinit var mPODViewModel: PODViewModel
    var mImageProgressBarManager = ProgressBarManager()

    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Fresco.initialize(this);
        setContentView(R.layout.activity_main)
        mPODViewModel= ViewModelProvider(this).get(PODViewModel::class.java)

        var mProgressBarManager = ProgressBarManager()
        mProgressBarManager.setProgressBarView(progressBar)

        imageView.hierarchy.setProgressBarImage(ProgressBarDrawable())

        mPODViewModel.pod.observe(this, Observer<List<POD>>() {
            Log.d("res", it.toString())
            mPODViewModel.pods.add(it[it.size-1])
            title_view.setBackgroundResource(R.color.text_backdrop)
            title_view.setText(it[it.size-1].title)
            nestedScrollView.setBackgroundResource(R.color.text_backdrop)
            explanation.setText(it[it.size-1].explanation)
            //play/zoom button
            var url = it[it.size-1].url
            var mediatype =it[it.size-1].media_type
            var hdurl = it[it.size-1].hdurl

            playbutton.setOnClickListener {
                OnPlayOrZoom(url,mediatype,hdurl,this)
            }

            mProgressBarManager.hide()

            if(it[it.size-1].media_type=="video"){
                playbutton.setBackgroundResource(R.drawable.ic_play)
                imageView.setImageURI(Uri.parse(getVideoThumbnail(it[it.size-1].url)))
            }else{
                playbutton.setBackgroundResource(R.drawable.ic_zoom_unpressed)
                imageView.setImageURI(Uri.parse(it[it.size-1].hdurl))
            }

            //textView2.setText(it.discription)
        })


        calenderButton.setOnClickListener {
            calendar(it,mProgressBarManager)
        }



    }

    private fun OnPlayOrZoom(url:String,meidatype:String,hdurl:String,activity: MainActivity){
        if(meidatype=="video"){
            Log.d("poz",getVideoID(url))
            val intent = YouTubeStandalonePlayer.createVideoIntent(
                activity,
                YoutubeConfig().getApiKey(),
                getVideoID(url)
            )
            startActivity(intent)
        }else{
            val intent = Intent(baseContext, ZoomImageActivity::class.java)
            intent.putExtra("URL", hdurl)
            startActivity(intent)
        }
    }

    private fun getVideoThumbnail(str:String):String{
        try{
            var tempstr = str.split("/embed/")[1]
            tempstr = tempstr.split('?')[0]
            return "https://img.youtube.com/vi/${tempstr}/maxresdefault.jpg"
        }catch (error:Error){
            return "https://img.youtube.com/vi/ERROR/maxresdefault.jpg"
        }
    }

    private fun getVideoID(str: String):String{
     try{
         var tempstr = str.split("/embed/")[1]
         tempstr = tempstr.split('?')[0]
         return tempstr
     }   catch (error:Error){
         return "ERROR"
     }

    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun getDialogBuilder(it: View,mProgressBarManager: ProgressBarManager,msg:String):AlertDialog.Builder{
        var wrongDateDialogBuilder = AlertDialog.Builder(this)
        wrongDateDialogBuilder.setPositiveButton("pick another date",DialogInterface.OnClickListener{dialog, id ->
            // User clicked OK button
            calendar(it,mProgressBarManager)
        })
        wrongDateDialogBuilder.setNegativeButton("cancel",DialogInterface.OnClickListener{dialog, id ->
            // User clicked OK button
            var pods = mPODViewModel.pods
            mPODViewModel.getWithDate(pods[pods.size-1].date)
        })
        wrongDateDialogBuilder.setTitle(msg)
        return wrongDateDialogBuilder
    }


    @RequiresApi(Build.VERSION_CODES.N)
    private fun calendar(it:View,mProgressBarManager:ProgressBarManager){
        var cldr: Calendar  = Calendar.getInstance();
        var day = cldr.get(Calendar.DAY_OF_MONTH);
        var month = cldr.get(Calendar.MONTH);
        var year = cldr.get(Calendar.YEAR);
        // date picker dialog

        var picker = DatePickerDialog(it.context, DatePickerDialog.OnDateSetListener() {
                datePicker: DatePicker, i: Int, i1: Int, i2: Int ->

            if(isDateValid(day,month+1,year,i2,i1+1,i)){
                var date =(i.toString()+"-"+(i1+1).toString()+"-"+i2.toString())
                //Log.d("datel",date)
                mPODViewModel.getWithDate(date)
                mProgressBarManager.show()
            }else{
                var mWrongDateDialog = getDialogBuilder(it,mProgressBarManager,"Timetravel module Error 😜").create()
                mWrongDateDialog.show()
            }

        },year,month,day)
        picker.show()
    }

    private fun isDateValid(day:Int,month:Int,year:Int,i:Int,i1:Int,i2:Int):Boolean{
        if(i2==year){
            if(i1==month){
                if(i<=day){
                    return true
                }else{
                    return false
                }
            }else if(i1<month){
                return true
            }else{
                return false
            }
        }else if(i2<year){
            return true
        }else{
            return false
        }

    }



}

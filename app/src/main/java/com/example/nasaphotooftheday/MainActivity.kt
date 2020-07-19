package com.example.nasaphotooftheday

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.DialogInterface
import android.content.Intent
import android.icu.util.Calendar
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
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions
import com.example.nasaphotooftheday.POD.POD
import com.example.nasaphotooftheday.POD.PODViewModel
import com.example.nasaphotooftheday.glide.GlideImageLoader
import com.github.ybq.android.spinkit.sprite.Sprite
import com.github.ybq.android.spinkit.style.CubeGrid
import com.google.android.youtube.player.YouTubeStandalonePlayer
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private lateinit var mPODViewModel: PODViewModel
    @SuppressLint("ResourceType")
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mPODViewModel= ViewModelProvider(this).get(PODViewModel::class.java)

        var mProgressBarManager = ProgressBarManager()
        mProgressBarManager.setProgressBarView(progressBar)
        //mProgressBarManager.show()

        val cubeGrid: Sprite = CubeGrid()
        imageProgressBar.indeterminateDrawable = cubeGrid

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

            playbutton.setOnClickListener {
                OnPlayOrZoom(url,mediatype,this)
            }

            mProgressBarManager.hide()

            var glideImageLoader: GlideImageLoader? = null

            if(it[it.size-1].media_type=="video"){

                playbutton.setBackgroundResource(R.drawable.ic_play)
                glideImageLoader = GlideImageLoader(imageView, imageProgressBar)
                glideImageLoader.load(getVideoThumbnail(it[it.size-1].url),
                    RequestOptions().transform(CenterCrop())
                        .error(R.drawable.ic_error))

            }else{

                playbutton.setBackgroundResource(R.drawable.ic_zoom_unpressed)
                glideImageLoader = GlideImageLoader(imageView, imageProgressBar)
                glideImageLoader.load(it[it.size-1].hdurl,
                    RequestOptions().transform(CenterCrop())
                        .error(R.drawable.ic_error))

            }

            //textView2.setText(it.discription)
        })

        calenderButton.setOnClickListener {
            calendar(it,mProgressBarManager)
        }



    }

    private fun OnPlayOrZoom(url:String,meidatype:String,activity: MainActivity){
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
            intent.putExtra("URL", url)
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

            Log.d("datee",isDateValid(day,month+1,year,i,i1+1,i2).toString())
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

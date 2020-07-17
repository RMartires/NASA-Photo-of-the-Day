package com.example.nasaphotooftheday

import android.app.AlertDialog.THEME_DEVICE_DEFAULT_LIGHT
import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.DatePicker
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.nasaphotooftheday.POD.POD
import com.example.nasaphotooftheday.POD.PODViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var mPODViewModel: PODViewModel
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mPODViewModel= ViewModelProvider(this).get(PODViewModel::class.java)

        mPODViewModel.pod.observe(this, Observer<List<POD>>() {
            Log.d("res", it.toString())
            title_view.setText(it[it.size-1].title)
            explanation.setText(it[it.size-1].explanation)

            Glide.with(this)
                .load(it[it.size-1].url)
                .into(imageView)

            //textView2.setText(it.discription)
        })

        calenderButton.setOnClickListener {
            var cldr: Calendar  = Calendar.getInstance();
            var day = cldr.get(Calendar.DAY_OF_MONTH);
            var month = cldr.get(Calendar.MONTH);
            var year = cldr.get(Calendar.YEAR);
            // date picker dialog

            var picker = DatePickerDialog(it.context, DatePickerDialog.OnDateSetListener() {
                    datePicker: DatePicker, i: Int, i1: Int, i2: Int ->
                var date =(i.toString()+"-"+(i1+1).toString()+"-"+i2.toString())
                Log.d("datel",date)
                mPODViewModel.getWithDate(date)

            },year,month,day)
            picker.show()
        }


    }

}

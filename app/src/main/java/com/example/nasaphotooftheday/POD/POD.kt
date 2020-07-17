package com.example.nasaphotooftheday.POD
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class POD (
    @PrimaryKey(autoGenerate = true) val Id:Int ,
    val title : String,
    val explanation : String,
    val url : String,
    val hdurl:String,
    val media_type: String
    )
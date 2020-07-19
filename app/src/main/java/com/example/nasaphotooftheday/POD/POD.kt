package com.example.nasaphotooftheday.POD
import androidx.annotation.Nullable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class POD (
    @PrimaryKey(autoGenerate = true) val Id:Int,
    val title : String,
    val explanation : String,
    val url : String,
    @ColumnInfo(defaultValue = "NAN") val hdurl:String,
    val date:String,
    val media_type: String
    )
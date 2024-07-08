package com.example.spirala1.Models

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.spirala1.DAO.BitmapConverter

@Entity(
    tableName = "BiljkaBitmap",
    foreignKeys = [ForeignKey(
        entity = Biljka::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("idBiljke"),
        onDelete = ForeignKey.CASCADE
    )]
)
data class BiljkaBitmap(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val idBiljke: Long,
    @TypeConverters(BitmapConverter::class)
    val bitmap: Bitmap
)

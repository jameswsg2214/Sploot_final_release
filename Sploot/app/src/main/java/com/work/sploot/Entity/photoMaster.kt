package com.work.sploot.Entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
@Entity(tableName = "TblphotoMaster")
data class photoMaster(
    @ColumnInfo(name = "PhotoId")
    @PrimaryKey(autoGenerate = true)
    var PhotoId: Long? =null,
    @ColumnInfo(name = "photo1") var photo1: String? =null,
    @ColumnInfo(name = "photo2") var photo2: String? =null,
    @ColumnInfo(name = "photo3") var photo3: String? =null,
    @ColumnInfo(name = "prescription1") var prescription1: String? =null,
    @ColumnInfo(name = "prescription2") var prescription2: String? =null,
    @ColumnInfo(name = "upload_date")
    @TypeConverters(DateConverter::class)
    var upload_date: Date? =null,
    @ColumnInfo(name = "userId") var userId: Int? =null,
    @ColumnInfo(name = "petId") var petId: Long? =null
): Parcelable
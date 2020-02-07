package com.work.sploot.Entity


import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
@Entity(tableName = "TblgalleryImage")
data class photoagalley(
    @ColumnInfo(name = "ImageId")
    @PrimaryKey(autoGenerate = true)
    var PhotoId: Long? =null,
    @ColumnInfo(name = "photopath") var photopath: String? =null,
    @ColumnInfo(name = "type") var photoType: Int? =null,
    @ColumnInfo(name = "upload_date")
    @TypeConverters(DateConverter::class)
    var upload_date: Date? =null,
    @ColumnInfo(name = "userId") var userId: Int? =null,
    @ColumnInfo(name = "petId") var petId: Long? =null
): Parcelable
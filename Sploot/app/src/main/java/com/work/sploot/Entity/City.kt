package com.work.sploot.Entity

import android.os.Parcelable

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
@Parcelize
@Entity(tableName = "City")
data class City(

    @ColumnInfo(name = "City")
    @PrimaryKey(autoGenerate = true)
    var CityId: Long? =null,
    @ColumnInfo(name = "City") var Cityname: String? =null,
    @ColumnInfo(name = "active") var active: String? =null,
    @ColumnInfo(name = "syncStatus") var syncStatus: Int? =null
): Parcelable

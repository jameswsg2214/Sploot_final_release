package com.work.sploot.Entity

import android.os.Parcelable

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
@Parcelize
@Entity(tableName = "Country")
data class Country(

    @ColumnInfo(name = "Country")
    @PrimaryKey(autoGenerate = true)
    var  Countryid : Long? =null,
    @ColumnInfo(name = "Country") var Countryname: String? =null,
    @ColumnInfo(name = "active") var active: String? =null,
    @ColumnInfo(name = "syncStatus") var syncStatus: Int? =null
): Parcelable


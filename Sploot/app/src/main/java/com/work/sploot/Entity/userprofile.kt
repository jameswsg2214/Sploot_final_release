package com.work.sploot.Entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.sql.Date

@Parcelize
@Entity(tableName = "TblUserprofile")
data class userprofile(
    @ColumnInfo(name = "userId")
    @PrimaryKey(autoGenerate = true)
    var userId: Long? =null,
    @ColumnInfo(name = "userName") var userName: String? =null,
    @ColumnInfo(name = "number") var number: String? =null,
    @ColumnInfo(name = "email") var email: String? =null,
    @ColumnInfo(name = "address") var address: String? =null,
    @ColumnInfo(name = "country") var country: String? =null,
    @ColumnInfo(name = "state") var state: String? =null,
    @ColumnInfo(name = "city") var city: String? =null,
    @ColumnInfo(name = "pin") var pin: Int? =null,
    @ColumnInfo(name = "imagepath") var imagepath: String? =null,
    @ColumnInfo(name = "syncStatus") var syncStatus: Int? =null
): Parcelable
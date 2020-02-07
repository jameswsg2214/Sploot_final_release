package com.work.sploot.Entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.util.*


@Parcelize
@Entity(tableName = "Tblother")
data class other(
    @ColumnInfo(name = "otherId")
    @PrimaryKey(autoGenerate = true)
    var weightId: Long? =null,
    @ColumnInfo(name = "userId") var userId: String? =null,
    @ColumnInfo(name = "petId") var petId: String? =null,
    @ColumnInfo(name = "date") var date: Date? =null,
    @ColumnInfo(name = "other") var other: String? =null

): Parcelable

package com.work.sploot.Entity

import android.os.Parcelable

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
@Entity(tableName = "TblAlarmReminder")
data class alarmdata(

    @ColumnInfo(name = "alarmId")
    @PrimaryKey(autoGenerate = true)
    var alarmId: Long? =null,
    @ColumnInfo(name = "userId") var userId: String? =null,
    @ColumnInfo(name = "petId") var petId: String? =null,
    @ColumnInfo(name = "startdate") var startdate: Date? =null,
    @ColumnInfo(name = "endate") var endate: Date? =null,
    @ColumnInfo(name = "time") var time: Date? =null,
    @ColumnInfo(name = "notified_data") var notified_data: String? =null,
    @ColumnInfo(name = "reminder_type") var reminder_type: Long? =null,
    @ColumnInfo(name = "active") var active: String? =null

): Parcelable
package com.work.sploot.Entity



import android.os.Parcelable

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
@Entity(tableName = "TblAlarmManager")
data class alarmmanager(

    @ColumnInfo(name = "alarm_manger_Id")
    @PrimaryKey(autoGenerate = true)
    var alarm_manger_Id: Long? =null,
    @ColumnInfo(name = "date") var date: Date? =null,
    @ColumnInfo(name = "alarm_id") var alarm_id: Long? =null,
    @ColumnInfo(name = "active") var active: Int? =null

): Parcelable
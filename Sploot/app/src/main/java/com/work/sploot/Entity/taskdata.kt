package com.work.sploot.Entity

import android.os.Parcelable

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
@Entity(tableName = "Tbltask")
data class taskdata(
    @ColumnInfo(name = "TaskId")
    @PrimaryKey(autoGenerate = true)
    var TaskId: Long? =null,
    @ColumnInfo(name = "userId") var userId: String? =null,
    @ColumnInfo(name = "petId") var petId: String? =null,
    @ColumnInfo(name = "task_name") var task_name: String? =null,
    @ColumnInfo(name = "startdate") var startdate: Date? =null,
    @ColumnInfo(name = "enddate") var enddate: Date? =null,
    @ColumnInfo(name = "dailyxtime") var dailyxtime: String? =null,
    @ColumnInfo(name = "dailyxhours") var dailyxhours: String? =null,
    @ColumnInfo(name = "xtimeaday") var xtimeaday: String? =null,
    @ColumnInfo(name = "dosagedata") var dosage: String? =null,
    @ColumnInfo(name = "hoursreminderdata") var hoursreminderdata: String? =null,
    @ColumnInfo(name = "firstintake") var firstintake: String? =null,
    @ColumnInfo(name = "lastintake") var lastintake: String? =null,
    @ColumnInfo(name = "weekdayselect") var weekdayselect: String? =null,
    @ColumnInfo(name = "active") var active: String? =null,
    @ColumnInfo(name = "paused") var paused: String? =null,
    @ColumnInfo(name = "cycle") var cycle: String? =null
    ): Parcelable
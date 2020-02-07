package com.work.sploot.Entity

import android.os.Parcelable

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
@Entity(tableName = "Tbldayupdate")
data class taskCategory(
    @ColumnInfo(name = "CategoryId")
    @PrimaryKey(autoGenerate = true)
    var CategoryId: Long? =null,
    @ColumnInfo(name = "vacinationtypename") var vname: String? =null,
    @ColumnInfo(name = "vacinationtypedata") var vtype: String? =null,
    @ColumnInfo(name = "partoftheday") var partoftheday: String? =null,
    @ColumnInfo(name = "startdate")
    @TypeConverters(DateConverter::class)
    var startdate: Date? =null,
    @ColumnInfo(name = "enddate") @TypeConverters(DateConverter::class) var enddate: Date? =null,
    @ColumnInfo(name = "dailyxtime") var dailyxtime: String? =null,
    @ColumnInfo(name = "dailyxhours") var dailyxhours: String? =null,
    @ColumnInfo(name = "xtimeaday") var xtimeaday: String? =null,
    @ColumnInfo(name = "xday") var xday: String? =null,
    @ColumnInfo(name = "dosagedata") var dosage: String? =null,
    @ColumnInfo(name = "hoursreminderdata") var hoursreminderdata: String? =null,
    @ColumnInfo(name = "firstintake") var firstintake: String? =null,
    @ColumnInfo(name = "lastintake") var lastintake: String? =null,
    @ColumnInfo(name = "weekdayselect") var weekdayselect: String? =null,
    @ColumnInfo(name = "active") var active: String? =null,
    @ColumnInfo(name = "paused") var paused: String? =null,
    @ColumnInfo(name = "cycle") var cycle: String? =null,
    @ColumnInfo(name = "userId") var userId: String? =null,
    @ColumnInfo(name = "petId") var petId: String? =null,
    @ColumnInfo(name = "medicinetype") var medicineType: Int? =null,
    @ColumnInfo(name = "note") var note: String? =null,
    @ColumnInfo(name = "reminderid") var reminderid: String? =null,
    @ColumnInfo(name = "v_dosage") var v_dosage: String? =null
): Parcelable
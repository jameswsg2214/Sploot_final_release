package com.work.sploot.Entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.util.*


@Parcelize
@Entity(tableName = "TblallCatagory")
data class madicineType(
    @ColumnInfo(name = "allTypeId")
    @PrimaryKey(autoGenerate = true)
    var allTypeId: Long? =null,
    @ColumnInfo(name = "userId") var userId: String? =null,
    @ColumnInfo(name = "petId") var petId: String? =null,
    @ColumnInfo(name = "task_name") var task_name: String? =null,
    @ColumnInfo(name = "start_date") var start_date: Date? =null,
    @ColumnInfo(name = "end_date") var end_date: Date? =null,
    @ColumnInfo(name = "repeat_type") var repeat_type: Int? =null,
    @ColumnInfo(name = "frequency_type_id") var frequency_type_id: Int? =null,
    @ColumnInfo(name = "every_frequency") var every_frequency: Int? =null,
    @ColumnInfo(name = "selective_week") var selective_week: String? =null,
    @ColumnInfo(name = "reminder_time") var reminder_time: Date? =null,
    @ColumnInfo(name = "active") var active: Int? =null,
    @ColumnInfo(name = "cat_type") var cat_type: Int? =null,
    @ColumnInfo(name = "status") var status: Int? =null,
    @ColumnInfo(name = "delete_status") var delete_status: Int? =null
): Parcelable



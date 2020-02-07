package com.work.sploot.Entity


import android.os.Parcelable

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "TblDoctor")
data class doctor_list(
    @ColumnInfo(name = "Doctor_Id")
    @PrimaryKey(autoGenerate = true)
    var Doctor_Id: Long? =null,
    @ColumnInfo(name = "userId") var userId: Int? =null,
    @ColumnInfo(name = "petType") var petTpye: String? =null,
    @ColumnInfo(name = "doctor_name") var doctor_name: String? =null,
    @ColumnInfo(name = "hospital_name") var hospital_name: String? =null,
    @ColumnInfo(name = "doctor_no") var doctor_no: String? =null,
    @ColumnInfo(name = "email_id") var email_id: String? =null,
    @ColumnInfo(name = "doctor_Address") var doctor_Address: String? =null,
    @ColumnInfo(name = "city") var city: String? =null,
    @ColumnInfo(name = "country") var country: String? =null,
    @ColumnInfo(name = "state") var state: String? =null,
    @ColumnInfo(name = "pin") var pin: String? =null



): Parcelable
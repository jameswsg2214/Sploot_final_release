package com.work.sploot.Entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.util.*


@Parcelize
@Entity(tableName = "TblNodes")
data class activitynotes(
    @ColumnInfo(name = "notesId")
    @PrimaryKey(autoGenerate = true)
    var weightId: Long? =null,
    @ColumnInfo(name = "userId") var userId: Int? =null,
    @ColumnInfo(name = "petId") var petId: String? =null,
    @ColumnInfo(name = "date") var date: Date? =null,
    @ColumnInfo(name = "notes") var notes: String? =null

): Parcelable


package com.work.sploot.Entity

import android.os.Parcelable

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "TblBreedMaster")
data class breedMaster(

    @ColumnInfo(name = "breedId")
    @PrimaryKey(autoGenerate = true)
    var breedId: Long? =null,
    @ColumnInfo(name = "petCategoryId") var petCategoryId: Int? =null,
    @ColumnInfo(name = "breedName") var breedName: String? =null,
    @ColumnInfo(name = "active") var active: String? =null

): Parcelable
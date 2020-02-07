package com.work.sploot.Entity

import android.os.Parcelable

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

//Annotation Parcelize for make the data class parcelable
@Parcelize
//Annotation Entity to declare that this class is a table with name = students
@Entity(tableName = "TblPetMaster")
data class petMasterEntity(
    @ColumnInfo(name = "petId")
    @PrimaryKey(autoGenerate = true)
    var petId: Long? =null,
    @ColumnInfo(name = "userId") var userId: Int?,
    @ColumnInfo(name = "petCategoryId") var petCategoryId: String? =null,
    @ColumnInfo(name = "photo") var photo: String? =null,
    @ColumnInfo(name = "petName") var petName: String? =null,
    @ColumnInfo(name = "breedId") var breedId: String? =null,
    @ColumnInfo(name = "sex") var sex: String? =null,
    @ColumnInfo(name = "age") var age: String? =null,
    @ColumnInfo(name = "monthlyCycle") var monthlyCycle: String? =null,
    @ColumnInfo(name = "period") var period: String? =null,
    @ColumnInfo(name = "parentFatherName") var parentFatherName: String? =null,
    @ColumnInfo(name = "parentFatherBreedName") var parentFatherBreedName: String? =null,
    @ColumnInfo(name = "parentMotherName") var parentMotherName: String? =null,
    @ColumnInfo(name = "parentMotherBreedName") var parentMotherBreedName: String? =null,
    @ColumnInfo(name = "parentAddress") var parentAddress: String? =null,
    @ColumnInfo(name = "certificatepath") var certificatepath: String? =null,
    @ColumnInfo(name = "parenOwnerName") var parenOwnerName: String? =null,
    @ColumnInfo(name = "parenMobileNumber") var parenMobileNumber: String? =null,
    @ColumnInfo(name = "parentOwnerAddress") var parentOwnerAddress: String? =null,
    @ColumnInfo(name = "drName") var drName: String? =null,
    @ColumnInfo(name = "drhospitalName") var drhospitalName: String? =null,
    @ColumnInfo(name = "drMobile") var drMobile: String? =null,
    @ColumnInfo(name = "drEmail") var drEmail: String? =null,
    @ColumnInfo(name = "drAddress") var drAddress: String? =null,
    @ColumnInfo(name = "status") var status: String? =null,
    @ColumnInfo(name = "syncStatus") var syncStatus: Int? =null,
    @ColumnInfo(name = "active") var active: Int? =null
    ): Parcelable


//fun setname(petame: String){
//    this.petName=petame
//}


//collumn use @CollumnInfo Annotation
//You can aslo declare the primary key by adding @PrimaryKey Annotation
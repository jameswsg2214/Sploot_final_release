package com.work.sploot.DAO

import androidx.room.Dao
import androidx.room.Query
import com.work.sploot.Entity.petMasterEntity
import com.work.sploot.Entity.taskCategory

@Dao
interface petdayupdate {

    @Query("SELECT * FROM Tbldayupdate  order by CategoryId desc")
    fun getAll(): List<taskCategory>
}
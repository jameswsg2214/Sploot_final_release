package com.work.sploot;

import android.content.Context
import android.telecom.Call
import androidx.lifecycle.LiveData
import androidx.room.*
import com.work.sploot.DAO.petMasterDao
import com.work.sploot.DAO.petdayupdate
import com.work.sploot.Entity.*

//This annotation to tell room what is the entity/table of the database
@Database(entities = [
    petMasterEntity::class,
    taskCategory::class,
    alarmdata::class,
    weightdata::class,
    taskdata::class,
    doctor_list::class,
    userprofile::class,
    activitynotes::class,
    other::class,
    photoagalley::class,
    madicineType::class,
    photoMaster::class,
    alarmmanager::class
], version = 1, exportSchema = true)

@TypeConverters(DateConverter::class)

abstract class SplootAppDB : RoomDatabase() {

    abstract fun petMasterDao(): petMasterDao


    companion object {
        private var INSTANCE: SplootAppDB? = null

        fun getInstance(context: Context): SplootAppDB? {
            if (INSTANCE == null) {
                synchronized(SplootAppDB::class) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                        SplootAppDB::class.java, Constants.DB_NAME)
                        .build()
                }
            }
            return INSTANCE
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}
package com.work.sploot.DAO;

import androidx.room.*
import com.work.sploot.Entity.*
import java.util.*
import java.util.function.BinaryOperator
import kotlin.collections.ArrayList


@Dao
interface petMasterDao {


    @Query("SELECT * FROM TblPetMaster order by petId desc")
    fun getAll(): List<petMasterEntity>


    @Query("SELECT * FROM TblPetMaster where userId=:userid order by petId desc")
    fun getAll(userid: String): List<petMasterEntity>


    @Query("SELECT * FROM TblPetMaster where petId=:petId order by petId desc")
    fun getSelect(petId:String): petMasterEntity
    @Query("SELECT * FROM TblPetMaster ORDER BY petId desc LIMIT 1")
    fun getparticulerpet(): petMasterEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg todo: petMasterEntity)

    //weight
    @Insert
    fun insertweight(vararg todo: weightdata)

    @Update
    fun update_weight(vararg todos: weightdata)


    @Query("SELECT * FROM Tblweight where (petId=:petId and userId=:userid and date=:date)")
    fun check_weight(petId:String,userid:String,date: Date): Boolean

    @Query("SELECT * FROM Tblweight where (petId=:petId and userId=:userid and date=:date)")
    fun get_weight(petId:String,userid:String,date: Date): weightdata


    @Query("SELECT * FROM TblPetMaster where (userId=:userid and active=1) order by petId desc")
    fun checkAll(userid:String): Boolean

    @Query("SELECT * FROM TblPetMaster where (userId=:userid and active=1) order by petId desc")
    fun view_one(userid:String): petMasterEntity


    @Query("delete from TblPetMaster")
    fun delete()
    @Update
    fun update(vararg todos: petMasterEntity)
    @Query("SELECT * FROM TblPetMaster where (petId=:petId and userId=:userid)")
    fun getSelectdata(petId:String,userid:String): petMasterEntity

    @Query("SELECT * FROM TblPetMaster  where userId=:userid and active=1 ORDER BY petId desc")

    fun getpetid(userid: String):List<petMasterEntity>

    @Query("SELECT * FROM TblPetMaster where userId=:userid and active=1 order by petId desc")
    fun findpet(userid: String): Boolean

    //day update
    @Query("SELECT * FROM Tbldayupdate order by CategoryId desc")
    fun get(): List<taskCategory>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertdayupdate(vararg todo: taskCategory)

    @Query("SELECT CategoryId FROM Tbldayupdate  where (startdate=:Selectdate and petId=:petId and userId=:userid)")
    fun finddata(Selectdate:String,petId:String,userid:String): Boolean

    @Query("SELECT * FROM Tbldayupdate  where (petId=:petId and userId=:userid and startdate<=:startdate or enddate>=:enddate)")
    fun getcatmonth(startdate:Date,enddate: Date,petId:String,userid:String): List<taskCategory>

    @Query("SELECT * FROM Tbldayupdate  where (petId=:petId and userId=:userid and startdate<=:startdate or enddate>=:enddate)")
    fun getcatmonth_check(startdate:Date,enddate: Date,petId:String,userid:String): Boolean


    @Query("SELECT * FROM Tbldayupdate  where (startdate=:Selectdate and petId=:petId and userId=:userid and medicinetype=:type)")
    fun getcat(Selectdate:String,petId:String,userid:String,type:Int): taskCategory

    @Query("SELECT * FROM Tbldayupdate  where startdate=:Selectdate")
    fun getcatid(Selectdate:String): taskCategory


    @Query("SELECT * FROM Tbldayupdate  where ((startdate>=:selectdate or enddate<=:selectdate) and petId=:petId and userId=:userid and medicinetype=:type)")
    fun find_date(selectdate:Date,petId:String,userid:String,type:Int): Boolean

    @Query("SELECT * FROM Tbldayupdate  where ((startdate>=:selectdate or enddate<=:selectdate)and petId=:petId and userId=:userid and medicinetype=:type)")
    fun view_date(selectdate:Date,petId:String,userid:String,type:Int): taskCategory


    @Query("SELECT * FROM Tbldayupdate  where (medicinetype=:medicType and petId=:petId and userId=:userid and (startdate<=:startdate and enddate>=:startdate))")
    fun get(medicType:Int,petId:String,userid:String,startdate:Date): List<taskCategory>


    @Query("SELECT * FROM Tbldayupdate  where (medicinetype=:medicType and petId=:petId and userId=:userid and (startdate<=:startdate and enddate>=:startdate))")
    fun check(medicType:Int,petId:String,userid:String,startdate:Date): Boolean

//    @Query("SELECT CategoryId FROM Tbldayupdate ORDER BY CategoryId desc LIMIT 1")
//    fun getcatid(): taskCategory

    @Update
    fun updateTbldayupdate(vararg todos: taskCategory)

    @Query("SELECT 1 FROM Tbldayupdate LIMIT 1")
    fun dayupdateTblempty():Boolean


    ///alarm
    @Query("SELECT * FROM TblAlarmReminder order by alarmId desc")
    fun getalarmdata(): List<alarmdata>

    @Query("SELECT * FROM TblAlarmReminder where (userId=:userId and petId=:petId)")
    fun getdataalarm(userId:String,petId: String): List<alarmdata>

    @Query("SELECT * FROM TblAlarmReminder where (userId=:userId and petId=:petId and active=:active)")
    fun getdataalarm(userId:String,petId: String,active:String): List<alarmdata>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertalaramdata(vararg todo: alarmdata)

    @Update
    fun updatealarmdata(vararg todos: alarmdata)

    @Query("SELECT * FROM TblAlarmReminder  where (startdate=:Selectdate and petId=:petId and userId=:userid and endate=:enddate and time=:time)")
    fun findcatid(Selectdate:String,petId:String,userid:String,enddate:String,time:String): alarmdata

    @Query("SELECT * FROM TblAlarmReminder  where (userId=:userid and time=:time)")
    fun alaramcheck(userid:String,time:String): Boolean

    @Query("SELECT * FROM TblAlarmReminder where (alarmId=:alarmId)")
    fun getforalarm(alarmId:String): alarmdata

    @Query("SELECT * FROM TblAlarmReminder where (userId=:user)")
    fun getalram(user:String): List<alarmdata>

    @Query("SELECT * FROM TblAlarmReminder where (userId=:user)")
    fun checkalram(user:String): Boolean

    @Query("delete from TblAlarmReminder where (alarmId=:Id)")
    fun alarmdelete(Id:Long)

    //Weight insert
//
    @Query("SELECT 1 FROM TblWeight where (petId=:petId and userId=:userid) LIMIT 1")
    fun weightTblempty(petId: String,userid: String):Boolean

    @Query("SELECT * FROM TblWeight where (petId=:petId and userId=:userid and date<:date) ORDER BY weightId ")
    fun getlastweight(petId: String,userid: String,date: Date): List<weightdata>


    @Query("SELECT * FROM TblWeight where (petId=:petId and userId=:userid and date=:date) ORDER BY weightId desc LIMIT 1 ")
    fun checkweightat_day(petId: String,userid: String,date: Date): Boolean

    @Query("SELECT * FROM TblWeight where (petId=:petId and userId=:userid and date=:date) ORDER BY weightId desc LIMIT 1 ")
    fun getweightat_day(petId: String,userid: String,date: Date): weightdata

    @Query("SELECT * FROM TblWeight where (petId=:petId and userId=:userid)")
    fun weightdata(petId: String,userid: String):weightdata

    @Query("SELECT * FROM TblWeight where (petId=:petId and userId=:userid and date=:selectdate)")
    fun getweight(petId: String,userid: String,selectdate: Date):weightdata

    @Query("SELECT * FROM TblWeight where (petId=:petId and userId=:userid and date=:selectdate)")
    fun checkweight(petId: String,userid: String,selectdate: Date):Boolean

    @Query("SELECT * FROM TblWeight order by weightId desc")
    fun weightdataall():List<weightdata>

    //Task
    @Query("SELECT * FROM Tbltask order by petId desc")
    fun getAlltask(): List<taskdata>

    @Query("SELECT * FROM Tbltask where (petId=:petId and userId=:userid) order by TaskId desc")
    fun getSelectTask(petId:String,userid: String): taskdata

    @Query("SELECT * FROM Tbltask where (petId=:petId and userId=:userid) order by TaskId desc")
    fun findtask(petId:String,userid: String): Boolean

    @Query("SELECT * FROM Tbltask where (active= '0') order by TaskId desc")
    fun findinactive(): Boolean

    @Query("SELECT * FROM Tbltask where (TaskId=:taskId) order by TaskId desc")
    fun getSelectTask(taskId:Long): taskdata

    @Update
    fun updatetask(vararg todos: taskdata)

    @Query("delete from Tbltask where active='0'")
    fun deletetask()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun taskinsertAll(vararg todo: taskdata)

    //doctors

    @Query("SELECT * FROM TblDoctor where (petType=:petpe) order by Doctor_Id desc")
    fun getAlldoctor(petpe:String): List<doctor_list>

    @Query("SELECT * FROM TblDoctor order by Doctor_Id desc")
    fun getAll_doctor(): List<doctor_list>

    @Query("SELECT * FROM TblDoctor where userId=:userId order by Doctor_Id desc")
    fun getAll_doctor_user(userId:String): List<doctor_list>

    @Query("SELECT * FROM TblDoctor order by Doctor_Id desc")
    fun ispresent_doctor(): Boolean

    @Query("SELECT * FROM TblDoctor where (Doctor_Id=:docId and userId=:userid) order by Doctor_Id desc")
    fun getSelectdoctor(docId:String,userid: String): taskdata

    @Query("SELECT * FROM TblDoctor where (Doctor_Id=:docId and userId=:userid) order by Doctor_Id desc")
    fun finddoctor(docId:String,userid: String): Boolean


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun doctor_insert(vararg todo: doctor_list)

    //user profile
    @Query("SELECT * FROM TblUserprofile order by userId desc")
    fun getAll_user(): List<userprofile>

    @Query("SELECT * FROM TblUserprofile where userId=:userid order by userId desc")
    fun ispresent_user(userid: Long): Boolean

    @Query("SELECT * FROM TblUserprofile where userId=:userid order by userId desc")
    fun get_user(userid: Long): userprofile

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun user_insert(vararg todo: userprofile)

    @Update
    fun update_user(vararg todos: userprofile)

    //note
//
    @Query("SELECT * FROM TblNodes order by notesId desc")
    fun getnote(): List<activitynotes>
    @Query("SELECT * FROM TblNodes where date=:create order by notesId desc")
    fun isnote(create: Date): Boolean
    @Query("SELECT * FROM TblNodes where date=:create order by notesId desc")
    fun get_note(create: Date): activitynotes

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun note_insert(vararg todo: activitynotes)

    //photo

    @Query("SELECT * FROM TblphotoMaster order by PhotoId desc")
    fun getphoto(): List<photoMaster>

    @Query("SELECT * FROM TblphotoMaster where (upload_date=:create  and userId=:userid and petId=:petId) order by PhotoId desc")
    fun check_seleted_date(create: Date,userid: Int,petId: Long): Boolean

    @Query("SELECT * FROM TblphotoMaster where (upload_date=:create and userId=:userid and petId=:petId) order by PhotoId desc")
    fun get_selected_date(create: Date,userid: Int,petId: Long): photoMaster

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun photo_insert(vararg todo: photoMaster)

    @Update
    fun update_photo(vararg todos: photoMaster)

    //others

    @Query("SELECT * FROM Tblother order by otherId desc")
    fun getother(): List<other>

    @Query("SELECT * FROM Tblother where (date=:create and userId=:userid and petId=:petid) order by otherId desc")
    fun get_others(create: Date,userid: Int,petid:String): other

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert_other(vararg todo: other)

//gallery

    @Query("SELECT * FROM TblgalleryImage order by ImageId desc")
    fun getimage(): List<photoagalley>


    @Query("SELECT * FROM TblgalleryImage where (upload_date=:create  and userId=:userid and petId=:petId) order by ImageId desc")
    fun check_seleted_image(create: Date,userid: Int,petId: Long): Boolean


    @Query("SELECT * FROM TblgalleryImage where (upload_date=:create  and userId=:userid and petId=:petId and type=:type) order by ImageId desc")
    fun check_seleted_Type(create: Date,userid: Int,petId: Long,type: Int): Boolean


    @Query("SELECT * FROM TblgalleryImage where (upload_date=:create and userId=:userid and petId=:petId and type=:type) order by ImageId desc")
    fun get_seleted_Type(create: Date,userid: Int,petId: Long,type: Int): photoagalley


    @Query("SELECT * FROM TblgalleryImage where (upload_date=:create and userId=:userid and petId=:petId) order by ImageId desc")
    fun check_selected_image(create: Date,userid: Int,petId: Long): Boolean


    @Query("SELECT * FROM TblgalleryImage where (upload_date=:create and userId=:userid and petId=:petId) order by ImageId asc")
    fun get_selected_image(create: Date,userid: Int,petId: Long): List<photoagalley>

    //delete image

    @Query("delete from TblgalleryImage where ImageId=:imageid")
    fun image_delete(imageid:Long)



    @Query("SELECT * FROM TblgalleryImage where ImageId=:type order by ImageId asc")
    fun get_seleted_Data(type: Long): photoagalley

    @Query("SELECT * FROM TblgalleryImage where ImageId=:type order by ImageId asc")
    fun check_seleted_Data(type: Long): Boolean




    @Query("SELECT * FROM TblgalleryImage where (upload_date<:create and userId=:userid and petId=:petId) order by ImageId desc")
    fun get_previous_image(create: Date,userid: Int,petId: Long): List<photoagalley>

    @Query("SELECT * FROM TblgalleryImage where (upload_date<:create and userId=:userid and petId=:petId) order by ImageId desc")
    fun check_previous_image(create: Date,userid: Int,petId: Long): Boolean


    //image filter previous
    @Query("SELECT * FROM TblgalleryImage where (upload_date<:create and userId=:userid and petId=:petId and (type=1 or type=2 or type=3)) order by ImageId desc")
    fun get_previous_filter_image(create: Date,userid: Int,petId: Long): List<photoagalley>

    @Query("SELECT * FROM TblgalleryImage where (upload_date<:create and userId=:userid and petId=:petId and (type=1 or type=2 or type=3)) order by ImageId desc")
    fun check_previous_filter_image(create: Date,userid: Int,petId: Long): Boolean

// image filter

    @Query("SELECT * FROM TblgalleryImage where (upload_date=:create and userId=:userid and petId=:petId and (type=1 or type=2 or type=3)) order by ImageId desc")
    fun get_date_filter_image(create: Date,userid: Int,petId: Long): List<photoagalley>

    @Query("SELECT * FROM TblgalleryImage where (upload_date=:create and userId=:userid and petId=:petId and (type=1 or type=2 or type=3)) order by ImageId desc")
    fun check_date_filter_image(create: Date,userid: Int,petId: Long): Boolean


    //image filter previous

    @Query("SELECT * FROM TblgalleryImage where (upload_date<:create and userId=:userid and petId=:petId and (type=4 or type=5)) order by ImageId desc")
    fun get_previous_filter_pric(create: Date,userid: Int,petId: Long): List<photoagalley>

    @Query("SELECT * FROM TblgalleryImage where (upload_date<:create and userId=:userid and petId=:petId and (type=4 or type=5)) order by ImageId desc")
    fun check_previous_filter_prc(create: Date,userid: Int,petId: Long): Boolean

    // image filter

    @Query("SELECT * FROM TblgalleryImage where (upload_date=:create and userId=:userid and petId=:petId and (type=4 or type=5)) order by ImageId desc")
    fun get_date_filter_prc(create: Date,userid: Int,petId: Long): List<photoagalley>

    @Query("SELECT * FROM TblgalleryImage where (upload_date=:create and userId=:userid and petId=:petId and (type=4 or type=5)) order by ImageId desc")
    fun check_date_filter_prc(create: Date,userid: Int,petId: Long): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun image_insert(vararg todo: photoagalley)

    @Update
    fun update_image(vararg todos: photoagalley)

    //all cat


    @Query("SELECT * FROM TblallCatagory where (userId=:userid and petId=:petId) order by allTypeId desc")
    fun getAll_cat(userid: String,petId: String): List<madicineType>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun cat_insert(vararg todo: madicineType)

    @Query("SELECT * FROM TblallCatagory ORDER BY allTypeId desc LIMIT 1")
    fun getlast_one(): madicineType

    @Query("SELECT * FROM TblallCatagory where allTypeId=:alltype ORDER BY allTypeId")
    fun get_one(alltype:Long): madicineType


    @Query("SELECT * FROM TblallCatagory where allTypeId=:alltype ORDER BY allTypeId")
    fun check_one(alltype:Long): Boolean



    @Query("SELECT * FROM TblallCatagory  where ((start_date>=:selectdate or end_date<=:selectdate) and petId=:petId and userId=:userid and cat_type=:type)")
    fun find_date_dv(selectdate:Date,petId:String,userid:String,type:Int): Boolean

    @Query("SELECT * FROM TblallCatagory  where ((start_date>=:selectdate or end_date<=:selectdate)and petId=:petId and userId=:userid and cat_type=:type)")
    fun view_date_dv(selectdate:Date,petId:String,userid:String,type:Int): madicineType


    @Update
    fun update_insert(vararg todos: madicineType)



    @Query("delete from TblallCatagory where allTypeId=:id")
    fun delete_petday(id:Long)


    @Query("SELECT * FROM TblallCatagory where (petId=:petId and userId=:userid and start_date=:date) order by allTypeId desc")
    fun find_task(petId:String,userid: String,date: Date): Boolean


    @Query("SELECT * FROM TblallCatagory where (userId=:userid and petId=:petId and start_date=:date) order by allTypeId desc")
    fun getAll_Task(userid: String,petId: String,date: Date): List<madicineType>


    @Query("SELECT * FROM TblallCatagory where (allTypeId=:taskId) order by allTypeId desc")
    fun get_Select_Task(taskId:Long): madicineType


    @Update
    fun update_task(vararg todos: madicineType)

    @Query("delete from TblallCatagory where delete_status=0")
    fun delete_task()

    @Query("SELECT * FROM TblallCatagory where delete_status= 0 order by allTypeId desc")
    fun find_in_active(): Boolean


    @Query("SELECT * FROM TblallCatagory order by allTypeId desc")
    fun getAll_cat_(): List<madicineType>

    @Query("SELECT * FROM TblallCatagory order by allTypeId desc")
    fun getAll_cat_check(): Boolean

    @Query("SELECT * FROM TblallCatagory  where (petId=:petId and userId=:userid and start_date<=:startdate or end_date>=:enddate)")
    fun cal_check(startdate:Date,enddate: Date,petId:String,userid:String): Boolean

    @Query("SELECT * FROM TblallCatagory  where (cat_type=:medicType and petId=:petId and userId=:userid and (start_date<=:startdate and end_date>=:startdate))")
    fun cal_get(medicType:Int,petId:String,userid:String,startdate:Date): List<madicineType>


    @Query("SELECT * FROM TblallCatagory  where (cat_type=:medicType and petId=:petId and userId=:userid and (start_date<=:startdate and end_date>=:startdate))")
    fun cal_get_click(medicType:Int,petId:String,userid:String,startdate:Date): Boolean

    //alaram manger

    @Query("SELECT * FROM TblAlarmManager where (alarm_id=:alarm_id and date=:date)")
    fun check_alaram_status(alarm_id:Long,date: Date): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert_alaram_status(vararg todo: alarmmanager)

    @Query("SELECT * FROM TblAlarmManager where (alarm_id=:alarm_id and date=:date)")
    fun get_alaram_status(alarm_id:Long,date: Date): alarmmanager



    @Query("SELECT * FROM TblAlarmManager where (alarm_id=:alarm_id)")
    fun cleck_alaram_in_db(alarm_id:Long): Boolean

    @Query("SELECT * FROM TblAlarmManager where (alarm_id=:alarm_id and active=0)")
    fun cleck_alaram_status(alarm_id:Long): Boolean
}
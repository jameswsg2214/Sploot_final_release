package com.work.sploot.activities

import android.app.AlarmManager
import android.app.Dialog
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.CalendarView
import android.widget.NumberPicker
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.work.sploot.Entity.madicineType
import com.work.sploot.R
import com.work.sploot.SplootAppDB
import com.work.sploot.api.ApiProduction
import com.work.sploot.api.request.appointmentreq
import com.work.sploot.api.response.appontmentres
import com.work.sploot.api.service.CommonServices
import com.work.sploot.data.ConstantMethods
import com.work.sploot.data.stringPref
import com.work.sploot.rx.RxAPICallHelper
import com.work.sploot.rx.RxAPICallback
import com.work.sploot.services.AlarmReceiver
import kotlinx.android.synthetic.main.calender.*
import kotlinx.android.synthetic.main.medicine_update.view.*
import kotlinx.android.synthetic.main.medicine_update.view.task_close
import kotlinx.android.synthetic.main.select_frequency_dialog.*
import kotlinx.android.synthetic.main.week_dialog_picker.*
import java.text.SimpleDateFormat
import java.util.*


class add_task_fregment:Fragment() {

    private var splootDB: SplootAppDB? = null

    var Startdate:Date?=null

    var Enddate:Date?=null

    var viewers:View?=null

    var reminder_time:Date?=null

    var repeat_type:Int?=1

    var frequency_type_id:Int?=null

    var every_frequency:Int?=null

    var selective_week:String?=null

    var dialog:Dialog?=null


    companion object {

        var viewdata: View? =null

        var cont: FragmentActivity?=null

        var cat_type:Int?=null

        fun newInstance(view: View?, mContext: FragmentActivity?, type: Int): add_task_fregment
        {
            viewdata=view

            cont=mContext

            cat_type=type

            return add_task_fregment()
        }
    }






    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        val curentdate=Date()

        var ischeck_custom=true

        val formatter = SimpleDateFormat("dd/MM/yyyy")

        val date_curent=formatter.format(curentdate)

        Startdate=formatter.parse(date_curent)

        val dateformated=formatter.parse(formatter.format(curentdate))

        Enddate=dateformated

        reminder_time=dateformated

        val views = inflater.inflate(R.layout.medicine_update, container, false)

        views.reminder_time.text=get_time_form_date(curentdate)

        views.add_task_viewer.visibility=View.VISIBLE

        dialog= Dialog(views.context)

        views.task_close.setOnClickListener {

            val inputMethodManager =
                views.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(
                activity?.currentFocus?.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
            )


                try {

                    val mContext = activity

                    val fm = cont?.supportFragmentManager

                    val transaction = fm?.beginTransaction()

                    transaction?.replace(
                        R.id.task_frame,
                        taskfragmentActivity.newInstance(viewdata!!, cont!!)
                    )

                    transaction?.commit()
                } catch (e: Exception) {


                }

        }

        views.medicine_name.hint="Enter Task Name"

        splootDB = SplootAppDB.getInstance(views.context)

        viewers=views


        //reminder time picker peroformed time to save reminder time variable

        views.get_time.addOnDateChangedListener { displayed, date ->

            reminder_time=date

            val str=get_time_form_date(date)

            var dates=date

            var hours=dates.hours

            var min=dates.minutes

            var hr=""

            var mi=""

            var day="am"

            if(hours>12){
                day="pm"
                hours =hours- 12
            } else{
                hours=hours

                if(hours==12){

                    day="pm"

                }
                else{
                    day="am"
                }

            }
            if(hours<10)
            {
                hr="0"+hours
            }else
            {
                hr=""+hours
            }

            if(min<10)
            {
                mi  = "0$min"
            }else
            {
                mi=""+min
            }

            if(hr=="00"){

                hr="12"

            }


            views.reminder_time.text="$hr:$mi $day"
        }

        var isrepeat=true

        //repeat list can view

        views.repeat_time.setOnClickListener {

            if(isrepeat){

                views.repeat_list_view_layout.visibility=View.VISIBLE

                isrepeat=false
            }

            else{

                views.repeat_list_view_layout.visibility=View.GONE

                isrepeat=true
            }


        }


        //never function tick mark

        views.never_check.setOnClickListener {

            repeat_type=1

            Enddate=Startdate

            ischeck_custom=true

            views.selected_Type.text="Never"

            views.end_repeat_layout.visibility=View.GONE

            views.never_select.visibility=View.VISIBLE

            views.hourly_select.visibility=View.INVISIBLE

            views.Daily_select.visibility=View.INVISIBLE

            views.weekly_select.visibility=View.INVISIBLE

            views.Fortnightly_select.visibility=View.INVISIBLE

            views.Monthly_select.visibility=View.INVISIBLE

            views.Every3m_select.visibility=View.INVISIBLE

            views.every6m_select.visibility=View.INVISIBLE

            views.yearly_select.visibility=View.INVISIBLE

            views.custom_layout_view.visibility=View.GONE

        }

        //Hourly function tick mark

        views.hourly_check.setOnClickListener {

            repeat_type=2

            views.selected_Type.text="Hourly"

            ischeck_custom=true

            views.end_repeat_layout.visibility=View.VISIBLE

            views.never_select.visibility=View.INVISIBLE

            views.hourly_select.visibility=View.VISIBLE

            views.Daily_select.visibility=View.INVISIBLE

            views.weekly_select.visibility=View.INVISIBLE

            views.Fortnightly_select.visibility=View.INVISIBLE

            views.Monthly_select.visibility=View.INVISIBLE

            views.Every3m_select.visibility=View.INVISIBLE

            views.every6m_select.visibility=View.INVISIBLE

            views.yearly_select.visibility=View.INVISIBLE

            views.custom_layout_view.visibility=View.GONE

        }

        //Daily function tick mark
        views.Daily_check.setOnClickListener {

            repeat_type=3

            views.selected_Type.text="Daily"

            ischeck_custom=true

            views.end_repeat_layout.visibility=View.VISIBLE

            views.never_select.visibility=View.INVISIBLE

            views.hourly_select.visibility=View.INVISIBLE

            views.Daily_select.visibility=View.VISIBLE

            views.weekly_select.visibility=View.INVISIBLE

            views.Fortnightly_select.visibility=View.INVISIBLE

            views.Monthly_select.visibility=View.INVISIBLE

            views.Every3m_select.visibility=View.INVISIBLE

            views.every6m_select.visibility=View.INVISIBLE

            views.yearly_select.visibility=View.INVISIBLE

            views.custom_layout_view.visibility=View.GONE

        }

        //weekly function tick mark

        views.Weekly_check.setOnClickListener {

            repeat_type=4

            ischeck_custom=true

            views.selected_Type.text="Weekly"

            views.end_repeat_layout.visibility=View.VISIBLE

            views.never_select.visibility=View.INVISIBLE

            views.hourly_select.visibility=View.INVISIBLE

            views.Daily_select.visibility=View.INVISIBLE

            views.weekly_select.visibility=View.VISIBLE

            views.Fortnightly_select.visibility=View.INVISIBLE

            views.Monthly_select.visibility=View.INVISIBLE

            views.Every3m_select.visibility=View.INVISIBLE

            views.every6m_select.visibility=View.INVISIBLE

            views.yearly_select.visibility=View.INVISIBLE

            views.custom_layout_view.visibility=View.GONE

        }

        //Fortnightly function tick mark

        views.fortnightly_check.setOnClickListener {

            repeat_type=5

            views.selected_Type.text="Fortnightly"

            ischeck_custom=true

            views.end_repeat_layout.visibility=View.VISIBLE

            views.never_select.visibility=View.INVISIBLE

            views.hourly_select.visibility=View.INVISIBLE

            views.Daily_select.visibility=View.INVISIBLE

            views.weekly_select.visibility=View.INVISIBLE

            views.Fortnightly_select.visibility=View.VISIBLE

            views.Monthly_select.visibility=View.INVISIBLE

            views.Every3m_select.visibility=View.INVISIBLE

            views.every6m_select.visibility=View.INVISIBLE

            views.yearly_select.visibility=View.INVISIBLE

            views.custom_layout_view.visibility=View.GONE
        }

        //Monthly function tick mark

        views.Monthly_check.setOnClickListener {

            repeat_type=6

            views.selected_Type.text="Monthly"

            ischeck_custom=true

            views.end_repeat_layout.visibility=View.VISIBLE

            views.never_select.visibility=View.INVISIBLE

            views.hourly_select.visibility=View.INVISIBLE

            views.Daily_select.visibility=View.INVISIBLE

            views.weekly_select.visibility=View.INVISIBLE

            views.Fortnightly_select.visibility=View.INVISIBLE

            views.Monthly_select.visibility=View.VISIBLE

            views.Every3m_select.visibility=View.INVISIBLE

            views.every6m_select.visibility=View.INVISIBLE

            views.yearly_select.visibility=View.INVISIBLE

            views.custom_layout_view.visibility=View.GONE

        }

        //Every 3 Months function tick mark

        views.every3m_check.setOnClickListener {

            repeat_type=7

            views.selected_Type.text="Every 3 Months"

            ischeck_custom=true

            views.end_repeat_layout.visibility=View.VISIBLE

            views.never_select.visibility=View.INVISIBLE

            views.hourly_select.visibility=View.INVISIBLE

            views.Daily_select.visibility=View.INVISIBLE

            views.weekly_select.visibility=View.INVISIBLE

            views.Fortnightly_select.visibility=View.INVISIBLE

            views.Monthly_select.visibility=View.INVISIBLE

            views.Every3m_select.visibility=View.VISIBLE

            views.every6m_select.visibility=View.INVISIBLE

            views.yearly_select.visibility=View.INVISIBLE

            views.custom_layout_view.visibility=View.GONE

        }

        //Every 6 Months function tick mark

        views.every6m_check.setOnClickListener {

            repeat_type=8

            views.selected_Type.text="Every 6 Months"

            ischeck_custom=true

            views.end_repeat_layout.visibility=View.VISIBLE

            views.never_select.visibility=View.INVISIBLE

            views.hourly_select.visibility=View.INVISIBLE

            views.Daily_select.visibility=View.INVISIBLE

            views.weekly_select.visibility=View.INVISIBLE

            views.Fortnightly_select.visibility=View.INVISIBLE

            views.Monthly_select.visibility=View.INVISIBLE

            views.Every3m_select.visibility=View.INVISIBLE

            views.every6m_select.visibility=View.VISIBLE

            views.yearly_select.visibility=View.INVISIBLE

            views.custom_layout_view.visibility=View.GONE

        }

        //Yearly function tick mark

        views.yearly_check.setOnClickListener {

            repeat_type=9

            views.selected_Type.text="Yearly"

            ischeck_custom=true

            views.end_repeat_layout.visibility=View.VISIBLE

            views.never_select.visibility=View.INVISIBLE

            views.hourly_select.visibility=View.INVISIBLE

            views.Daily_select.visibility=View.INVISIBLE

            views.weekly_select.visibility=View.INVISIBLE

            views.Fortnightly_select.visibility=View.INVISIBLE

            views.Monthly_select.visibility=View.INVISIBLE

            views.Every3m_select.visibility=View.INVISIBLE

            views.every6m_select.visibility=View.INVISIBLE

            views.yearly_select.visibility=View.VISIBLE

            views.custom_layout_view.visibility=View.GONE

        }
        var isrepeat_check=true

        views.end_repeat_layout.setOnClickListener {


            if(isrepeat_check) {

                views.end_repeat_list_view_layout.visibility = View.VISIBLE

                isrepeat_check=false

            }
            else{

                views.end_repeat_list_view_layout.visibility = View.GONE

                isrepeat_check=true

            }

        }

        //No end date function tick mark and set end date to three year from current date

        views.no_end_check.setOnClickListener {

            val cal = Calendar.getInstance()

            cal . setTime (Startdate)

            cal . add (Calendar.YEAR, 3)

            Enddate=cal.time

            views.no_end_select.visibility=View.VISIBLE

            views.end_repeat_select.visibility=View.INVISIBLE

            viewers!!.end_repeat_data.setText("")

        }

        //End repeat function tick mark

        views.end_repeat_check.setOnClickListener {


            views.no_end_select.visibility=View.INVISIBLE

            views.end_repeat_select.visibility=View.VISIBLE

            Untildate(views.context,"End Repeat")

        }

        //Custom function tick mark

        views.custom_layout.setOnClickListener {

            views.end_repeat_layout.visibility=View.VISIBLE

            if(ischeck_custom){

                repeat_type=10

                views.selected_Type.text="Custom"

                views.never_select.visibility=View.INVISIBLE

                views.hourly_select.visibility=View.INVISIBLE

                views.Daily_select.visibility=View.INVISIBLE

                views.weekly_select.visibility=View.INVISIBLE

                views.Fortnightly_select.visibility=View.INVISIBLE

                views.Monthly_select.visibility=View.INVISIBLE

                views.Every3m_select.visibility=View.INVISIBLE

                views.every6m_select.visibility=View.INVISIBLE

                views.yearly_select.visibility=View.INVISIBLE

                views.custom_layout_view.visibility=View.VISIBLE

                ischeck_custom=false
            }
            else{

                repeat_type=1

                views.selected_Type.text=""

                views.never_select.visibility=View.VISIBLE

                views.custom_layout_view.visibility=View.GONE

                ischeck_custom=true
            }

        }

        val name  =views.findViewById<NumberPicker>(R.id.custom_type_select)

        val values = arrayOf( "Daily", "Weekly", "Monthly", "Yearly")

        name.minValue = 0

        name.maxValue = values.size - 1

        name.displayedValues = values

        name.wrapSelectorWheel = true

        name.setOnScrollListener { numberPicker, i2 ->

            Log.e("selected","$i2")

            if(i2==0){


                var number=name.value

                Log.e("number","....$number")

                if(number==0){

                    if(dialog!=null){

                        dialog!!.dismiss()

                    }

                    views.frequency_Type.text=values[number]

                    dialog_days_picker(views.context,1)

                }
                else if(number==1){

                    if(dialog!=null){

                        dialog!!.dismiss()
                    }


                    views.frequency_Type.text=values[number]

                    dialog_week_picker(views.context)


                }
                else if(number==2){

                    if(dialog!=null){

                        dialog!!.dismiss()
                    }

                    views.frequency_Type.text=values[number]

                    dialog_days_picker(views.context,2)

                }
                else if(number==3){

                    if(dialog!=null){

                        dialog!!.dismiss()
                    }

                    views.frequency_Type.text=values[number]

                    dialog_days_picker(views.context,3)

                }

            }


        }

        //Save function and check

        var date_check=false

        views.save_data_update.setOnClickListener {

            Log.e("CLicked save","waesrdtfgyhujiko")

            val curent=Date()

            val formatter = SimpleDateFormat("dd/MM/yyyy")

            var date=formatter.format(curent)

            val selective= formatter.parse(date)

            Log.e("qwertyu","$selective $Startdate")

            if(repeat_type!=1 && repeat_type !=2){

                Log.e("repecct","$repeat_type")

                if(Startdate==Enddate){

                    Log.e("repecct","equal")

                    date_check=true

                }
                else{

                    Log.e("repecct","not equal")

                    date_check=false
                }

            }


            when{

                views.medicine_name.text.trim().isNullOrEmpty()-> Toast.makeText(views.context,"Please enter task name", Toast.LENGTH_LONG).show()

                reminder_time==null-> Toast.makeText(views.context,"Please set reminder", Toast.LENGTH_LONG).show()

                (repeat_type==1 && (reminder_time!! < Date()))-> Toast.makeText(views.context,"Reminder Time is invalid",Toast.LENGTH_LONG).show()

                date_check-> Toast.makeText(views.context,"Please select end date",Toast.LENGTH_LONG).show()

                else ->{

                    val name=views.medicine_name.text.trim().toString()

                    val ck=name.contains("appointment")

                    if(ConstantMethods().checkNetwork(this.context!!)){

                        if(ck){

                            Log.e("api","api perform")

                            var userId by stringPref("userId", null)

                            var petid by stringPref("petid", null)

                            var username by stringPref("name", null)

                            Log.e("qwerty","username $username")

                            val userName=""

                            var alertDialog = ConstantMethods().setProgressDialog(this.context!!)

                            alertDialog.show()

                            var commService: CommonServices = ApiProduction(this.context!!).provideService(CommonServices::class.java)

                            val req = appointmentreq()

                            req.setuserId(userId!!.toInt())

                            req.setpetId(petid!!.toInt())

                            req.setName(username!!)

                            val formatter = SimpleDateFormat("dd/MM/yyyy")

                            val start=formatter.format(Startdate)

                            val end=formatter.format(Enddate)

                            req.settask_name(views.medicine_name.text.toString().trim())

                            req.setstart_date(start)

                            req.setend_date(end)

                            req.setrepeat_type(repeat_type!!)


                            if(frequency_type_id!=null) {

                                req.setfrequency_type_id(frequency_type_id!!)
                            }

                            if(every_frequency!=null) {

                                req.setevery_frequency(every_frequency!!)

                            }



                            if(selective_week!=null) {

                                req.setselective_week(selective_week!!)

                            }

                            req.setactive(1)

                            req.setcat_type(cat_type!!)

                            var apiCall = commService.appointment(req)

                            RxAPICallHelper().call(apiCall, object : RxAPICallback<appontmentres> {

                                override fun onSuccess(loginResponse: appontmentres) {

                                    if(loginResponse.getStatus()!!)
                                    {

                                        Log.e("Insert","Appointment Sucessfully")

                                        alertDialog.dismiss()

                                    }
                                    else
                                    {

                                        Log.e("Insert","Appointment status false")

                                        alertDialog.dismiss()

                                    }
                                }
                                override fun onFailed(throwable: Throwable) {

                                    Log.e("Insert","Status failed $throwable")

                                    alertDialog.dismiss()

                                }
                            })



                        }
                        else{

                            Log.e("api","api not perform")
                        }

                    }
                    else{

                        Log.e("api","no internet")

                    }

                    AsyncTask.execute {
                        var userId by stringPref("userId", null)
                        var petid by stringPref("petid", null)
                       // var user = userId?.toInt()
                        //var petId = petid?.toLong()
                        try {
                            val callDetails = splootDB!!.petMasterDao()

                            val insert_data = madicineType(
                                userId = userId,
                                petId = petid,
                                task_name = views.medicine_name.text.toString().trim(),
                                start_date = Startdate,
                                end_date = Enddate,
                                repeat_type = repeat_type,
                                frequency_type_id = frequency_type_id,
                                every_frequency = every_frequency,
                                selective_week = selective_week,
                                reminder_time = reminder_time,
                                cat_type = cat_type,
                                active = 1,
                                status = 1
                            )

                            val fun_insert = callDetails.cat_insert(insert_data)

                            val viewall_date=callDetails.getAll_cat(userId!!,petid!!)

                            Log.e("display all data","$viewall_date")

                            val last_data=callDetails.getlast_one()

                            Log.e("last_data","$last_data")

                            val RQS_1=last_data.allTypeId

                            val calNow = Calendar . getInstance ()

                            val calSet:Calendar = calNow . clone () as Calendar

                            calSet.set(Calendar.HOUR_OF_DAY, reminder_time!!.hours)

                            calSet.set(Calendar.MINUTE, reminder_time!!.minutes)

                            calSet.set(Calendar.SECOND, 0)

                            calSet.set(Calendar.MILLISECOND, 0)

                            if (calSet.compareTo(calNow) <= 0) {

                                calSet.add(Calendar.DATE, 1)

                            }

                            setAlarm(calSet,RQS_1!!.toInt())


                                val mContext = activity

                                val fm = mContext?.supportFragmentManager

                                val transaction = fm?.beginTransaction()

                                transaction?.replace(
                                    R.id.task_frame,
                                    taskfragmentActivity.newInstance(viewdata!!, cont!!)
                                )

                                transaction?.commit()



                        } catch (e: Exception) {
                            val s = e.message
                            Log.e("Error", s)
                        }
                    }
                    Toast.makeText(views.context,"Saved", Toast.LENGTH_LONG).show()
                }

            }

        }

        return views
    }

    private fun get_time_form_date(dates: Date?):String {

        var hours=dates!!.hours

        var min=dates!!.minutes

        var hr=""

        var mi=""

        var day="am"

        if(hours>12){
            day="pm"
            hours =hours- 12
        } else{
            hours=hours

            if(hours==12){

                day="pm"

            }
            else{
                day="am"
            }

        }
        if(hours<10)
        {
            hr="0"+hours
        }else
        {
            hr=""+hours
        }

        if(min<10)
        {
            mi  = "0$min"
        }else
        {
            mi=""+min
        }

        if(hr=="00"){

            hr="12"

        }

        //  textViewName.text ="$hr:$mi $day"

        val rtr_data="$hr:$mi $day"

        return rtr_data

    }


    // Alaram Setup Sunction

    private fun setAlarm(targetCal: Calendar, id: Int)
    {

        val intent = Intent(viewers!!.context, AlarmReceiver::class.java)

        Log.e("alaram data","${targetCal.time}")

        intent.putExtra("name", "$id")

        intent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND)

        var DAY = 86400000L

        var millisec = System.currentTimeMillis().toInt()

        val pendingIntent = PendingIntent.getBroadcast(activity, millisec, intent, 0)

        val alarmManager: AlarmManager =activity?.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        if (repeat_type==1){

            val formatter = SimpleDateFormat("dd/MM/yyyy")

            val output = formatter.format(Date())

            val out=formatter.parse(output)

            if(Startdate!!.compareTo(out)<0 )
            {
                Log.e("alaram","check ${out}")
            }
            else {


                val valide=  Startdate!!.time  - Date().time

                Log.e("alaram validate","$valide")

                val data_responce = (valide / (1000 * 3600 * 24)).toInt()

                var tar=data_responce

                Log.e("alaram","$tar")

                if(valide<0)
                {

                    if (Build.VERSION.SDK_INT >= 23)
                    {
                        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, targetCal.getTimeInMillis(), pendingIntent)
                    }
                    else if (Build.VERSION.SDK_INT >= 19)
                    {
                        alarmManager.setExact(AlarmManager.RTC_WAKEUP,  targetCal.getTimeInMillis(), pendingIntent)
                    } else
                    {
                        alarmManager.set(AlarmManager.RTC_WAKEUP,  targetCal.getTimeInMillis(), pendingIntent)
                    }

                }
                else
                {

                    val time1=(tar+1)*DAY

                    targetCal.add(Calendar.DATE,(tar))

                    if (Build.VERSION.SDK_INT >= 23)
                    {
                        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, targetCal.getTimeInMillis(), pendingIntent)
                    }
                    else if (Build.VERSION.SDK_INT >= 19)
                    {
                        alarmManager.setExact(AlarmManager.RTC_WAKEUP,  targetCal.getTimeInMillis(), pendingIntent)
                    }
                    else
                    {
                        alarmManager.set(AlarmManager.RTC_WAKEUP,  targetCal.getTimeInMillis(), pendingIntent)
                    }

                }

                Log.e("talat",""+tar)

            }

        }
        else if(repeat_type==2){


            val formatter = SimpleDateFormat("dd/MM/yyyy")

            val output = formatter.format(Date())

            val out=formatter.parse(output)

            if(Startdate!!.compareTo(out)<0 )
            {

                Log.e("hour1","check ${out}")

                if (Build.VERSION.SDK_INT >= 23) {


                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, targetCal.getTimeInMillis(), pendingIntent)

                    alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, targetCal.getTimeInMillis(), AlarmManager.INTERVAL_HOUR, pendingIntent)

                }
                else if (Build.VERSION.SDK_INT >= 19) {

                    alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, targetCal.getTimeInMillis(), AlarmManager.INTERVAL_HOUR, pendingIntent)

                }
                else
                {
                    alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, targetCal.getTimeInMillis(), AlarmManager.INTERVAL_HOUR, pendingIntent)
                }


            }
            else {


                val valide=  Startdate!!.time  - Date().time

                Log.e("alaram validate","$valide")

                val data_responce = (valide / (1000 * 3600 * 24)).toInt()

                var tar=data_responce

                Log.e("alaram","$tar")

                if(valide<0)
                {
                    if (Build.VERSION.SDK_INT >= 23) {


                        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,
                            targetCal.getTimeInMillis(), pendingIntent)

                        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, targetCal.getTimeInMillis(), AlarmManager.INTERVAL_HOUR, pendingIntent)

                    } else if (Build.VERSION.SDK_INT >= 19) {



                        alarmManager.setInexactRepeating(
                            AlarmManager.RTC_WAKEUP, targetCal.getTimeInMillis(),
                            AlarmManager.INTERVAL_HOUR, pendingIntent
                        )


                    } else {

                        alarmManager.setInexactRepeating(
                            AlarmManager.RTC_WAKEUP, targetCal.getTimeInMillis(),
                            AlarmManager.INTERVAL_HOUR, pendingIntent
                        )
                    }
                }
                else {

                    val time1=(tar+1)*DAY

                    targetCal.add(Calendar.DATE,(tar))

                    if (Build.VERSION.SDK_INT >= 23) {

                        alarmManager.setRepeating( AlarmManager.RTC_WAKEUP, targetCal.getTimeInMillis(),
                            AlarmManager.INTERVAL_HOUR, pendingIntent)


                        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,
                            targetCal.getTimeInMillis(), pendingIntent)

                    } else if (Build.VERSION.SDK_INT >= 19) {



                        alarmManager.setInexactRepeating(
                            AlarmManager.RTC_WAKEUP, targetCal.getTimeInMillis(),
                            AlarmManager.INTERVAL_HOUR, pendingIntent
                        )


                    } else {

                        alarmManager.setInexactRepeating(
                            AlarmManager.RTC_WAKEUP, targetCal.getTimeInMillis(),
                            AlarmManager.INTERVAL_HOUR, pendingIntent
                        )
                    }


                }

                Log.e("talat",""+tar)

            }



        }
        else {

            if (Build.VERSION.SDK_INT >= 23) {

                alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, targetCal.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent)

                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,
                    targetCal.getTimeInMillis(), pendingIntent)

            } else if (Build.VERSION.SDK_INT >= 19) {


                alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, targetCal.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent)


            } else {

                alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, targetCal.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent)
            }

        }

    }



    fun dialog_days_picker(commandAdapter: Context, type:Int)
    {
        dialog = Dialog(commandAdapter)
        Log.e("diloge", "------------------------------------------>")
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog!!.setCancelable(false)
        dialog!!.setContentView(R.layout.select_frequency_dialog)

        val name =dialog!!.findViewById<NumberPicker>(R.id.dialog_date_picker)

        name.minValue=1

        name.maxValue=999

        var seleted_data=1

        if(type==1) {

            dialog!!.picker_header.text="Every Day"

            dialog!!.type_pickers.text="Day"


        }
        else if(type==2){

            dialog!!.picker_header.text="Every Month"

            dialog!!.type_pickers.text="Month"


        }
        else if(type==3){

            dialog!!.picker_header.text="Every Year"

            dialog!!.type_pickers.text="Year"

        }


        dialog!!.frequency_cancel.setOnClickListener {

            dialog!!.dismiss()
        }

        name.setOnValueChangedListener { _, _, i2 ->


            if(type==1) {

                if(i2==1)
                {
                    dialog!!.type_pickers.text="Day"
                }
                else{

                    dialog!!.type_pickers.text="Days"
                }


            }
            else if(type==2){

                if(i2==1)
                {
                    dialog!!.type_pickers.text="Month"
                }
                else{
                    dialog!!.type_pickers.text="Months"
                }

            }
            else if(type==3){

                if(i2==1)
                {
                    dialog!!.type_pickers.text="Year"
                }
                else{
                    dialog!!.type_pickers.text="Years"
                }
            }


            seleted_data=i2

        }


        dialog!!.frequency_ok.setOnClickListener {

            every_frequency=seleted_data

            if(type==1) {

                frequency_type_id=1


                if(seleted_data==1){

                    viewers!!.custom_data_view.text="$seleted_data Day"

                }
                else{

                    viewers!!.custom_data_view.text="$seleted_data Days"
                }



            }
            else if(type==2){

                frequency_type_id=3

                if(seleted_data==1){

                    viewers!!.custom_data_view.text="$seleted_data Month"

                }
                else{

                    viewers!!.custom_data_view.text="$seleted_data Months"

                }



            }
            else if(type==3){

                frequency_type_id=4

                if(seleted_data==1){

                    viewers!!.custom_data_view.text="$seleted_data Year"

                }
                else{

                    viewers!!.custom_data_view.text="$seleted_data Years"

                }
            }

            dialog!!.dismiss()


        }

        dialog!!.show()

    }

    fun dialog_week_picker(commandAdapter: Context)
    {

        dialog = Dialog(commandAdapter)

        Log.e("diloge", "------------------------------------------>")

        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)

        dialog!!.setCancelable(false)

        dialog!!.setContentView(R.layout.week_dialog_picker)

        val name =dialog!!.findViewById<NumberPicker>(R.id.dialog_week_picker)

        name.minValue=1

        name.maxValue=999

        var seleted_data=1

        var weekdayselect:String?=""

        dialog!!.week_picker_header.setText("Every Week")


        dialog!!.week_Monday?.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                weekdayselect+="Monday"

            }
            else{
                weekdayselect = weekdayselect?.replace("Monday", "")

            }
        }
        dialog!!.week_tueasdy?.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                weekdayselect+="Tuesday"

            }
            else{
                weekdayselect = weekdayselect?.replace("Tuesday", "")

            }
        }
        dialog!!.week_wednesday?.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                weekdayselect+="Wednesday"

            }
            else{
                weekdayselect = weekdayselect?.replace("Wednesday", "")

            }
        }
        dialog!!.week_thuesday?.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                weekdayselect+="Thursday"

            }
            else{
                weekdayselect = weekdayselect?.replace("Thursday", "")

            }
        }
        dialog!!.week_friday?.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                weekdayselect+="Friday"

            }
            else{
                weekdayselect = weekdayselect?.replace("Friday", "")

            }
        }
        dialog!!.week_saterday?.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                weekdayselect+="Saturday"

            }
            else{
                weekdayselect = weekdayselect?.replace("Saturday", "")

            }
        }
        dialog!!.week_sunday?.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                weekdayselect+="Sunday"

            }
            else{
                weekdayselect = weekdayselect?.replace("Sunday", "")

            }
        }



        dialog!!.week_frequency_cancel.setOnClickListener {

            dialog!!.dismiss()
        }

        name.setOnValueChangedListener { _, _, i2 ->


            if(i2==1){

                dialog!!.week_select.text="week"

            }
            else{

                dialog!!.week_select.text="weeks"

            }


            seleted_data=i2

        }


        dialog!!.week_frequency_ok.setOnClickListener {

            frequency_type_id=2

            every_frequency=seleted_data

            selective_week=weekdayselect

            Log.e("work","Week day $seleted_data , $weekdayselect")

            if(seleted_data==1)
            {
                viewers!!.custom_data_view.text="$seleted_data Week"
            }
            else
            {
                viewers!!.custom_data_view.text="$seleted_data Weeks"
            }

            dialog!!.dismiss()

        }

        dialog!!.show()

    }

    fun Untildate(commandAdapter: Context, datas: String): String?
    {

        dialog = Dialog(commandAdapter)

        Log.e("diloge","------------------------------------------>")

        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)

        dialog!!.setCancelable(false)

        dialog!!.setContentView(R.layout.calender)

        var date=""

        var date_1: Date?=null

        val dateget =dialog!!.findViewById<CalendarView>(R.id.getdate)

        dialog!!.headertext.text=datas

        dateget.minDate=Startdate!!.time

        dateget?.setOnDateChangeListener { view, year, month, dayOfMonth ->

            Log.e("Data","...............${Date(view.date)}")

            date = "" + dayOfMonth + "/" + (month + 1) + "/" + year

            val simpledate= SimpleDateFormat("")

            val formatter = SimpleDateFormat("dd/MM/yyyy")

            val output = formatter.parse(date)

            val formatter1 = SimpleDateFormat("dd/MM/yyyy")

            val output1=formatter1.format(output)

            date=output1

            date_1=output

        }
        dialog!!.calendercan.setOnClickListener {

            Enddate=null

            viewers!!.end_repeat_select.visibility=View.INVISIBLE

            viewers!!.end_repeat_data.setText("")

            dialog!!.dismiss()
        }
        dialog!!.calenderokbtn.setOnClickListener {

            Enddate=date_1

            viewers!!.end_repeat_data.setText(date)

            dialog!!.dismiss()
        }

        dialog!!.show()

        return null

    }

}
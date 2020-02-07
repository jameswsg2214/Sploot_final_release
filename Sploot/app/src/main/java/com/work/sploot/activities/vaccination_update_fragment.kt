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
import android.widget.CalendarView
import android.widget.NumberPicker
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.work.sploot.Entity.alarmdata
import com.work.sploot.Entity.madicineType
import com.work.sploot.Entity.taskdata
import com.work.sploot.R
import com.work.sploot.SplootAppDB
import com.work.sploot.data.stringPref
import com.work.sploot.services.AlarmReceiver
import kotlinx.android.synthetic.main.calender.*
import kotlinx.android.synthetic.main.medicine_update.view.*
import kotlinx.android.synthetic.main.myprofile.*
import kotlinx.android.synthetic.main.select_frequency_dialog.*
import kotlinx.android.synthetic.main.select_frequency_dialog.type_pickers
import kotlinx.android.synthetic.main.vaccination.view.*
import kotlinx.android.synthetic.main.week_dialog_picker.*
import java.text.SimpleDateFormat
import java.util.*

class vaccination_update_fragment:Fragment() {

    private var splootDB: SplootAppDB? = null

    var Startdate:Date?=null

    var Enddate:Date?=null

    var viewers:View?=null

    var type_name:String?=null

    var reminder_time:Date?=null

    var repeat_type:Int?=1

    var frequency_type_id:Int?=null

    var every_frequency:Int?=null

    var selective_week:String?=null

    var flow_type:Int?=null

    var dialog:Dialog?=null

    companion object {

        var cat_type:Int?=null

        fun newInstance(type:Int): vaccination_update_fragment {

            cat_type=type

            return vaccination_update_fragment()
        }
    }

    fun dialog_days_picker(commandAdapter: Context, type:Int) {
        dialog = Dialog(commandAdapter)
        Log.e("diloge", "------------------------------------------>")
         dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
         dialog!!.setCancelable(false)
         dialog!!.setContentView(R.layout.select_frequency_dialog)

        val name = dialog!!.findViewById<NumberPicker>(R.id.dialog_date_picker)

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

          dialog!!!!.show()

    }



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var select_date by stringPref("select_date", null)

        Log.e("Selective","$select_date")



        val formatter = SimpleDateFormat("dd/MM/yyyy")

        Startdate=formatter.parse(select_date)

        Enddate=formatter.parse(select_date)

        Log.e("Selective","$select_date $Startdate")

        val views = inflater.inflate(R.layout.medicine_update, container, false)

        dialog= Dialog(views.context)

        if(cat_type==1){

            views.medicine_name.hint="Enter vaccination Name"

        }
        else if(cat_type==2){

            views.medicine_name.hint="Enter medicine Name"

        }
        else if(cat_type==3){

            views.medicine_name.hint="Enter task Name"

        }

        splootDB = SplootAppDB.getInstance(views.context)

        viewers=views

        views.get_time.addOnDateChangedListener { displayed, date ->

            var dates=date

            Log.e("picker time","$dates")

            reminder_time=date

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

        views.never_check.setOnClickListener {

            repeat_type=1

            Enddate=Startdate

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

        views.hourly_check.setOnClickListener {

            repeat_type=2

            views.selected_Type.text="Hourly"

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

        views.Daily_check.setOnClickListener {

            repeat_type=3

            views.selected_Type.text="Daily"

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

        views.Weekly_check.setOnClickListener {

            repeat_type=4

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

        views.fortnightly_check.setOnClickListener {

            repeat_type=5

            views.selected_Type.text="Fortnightly"

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

        views.Monthly_check.setOnClickListener {

            repeat_type=6

            views.selected_Type.text="Monthly"

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

        views.every3m_check.setOnClickListener {

            repeat_type=7

            views.selected_Type.text="Every 3 Month"

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

        views.every6m_check.setOnClickListener {

            repeat_type=8

            views.selected_Type.text="Every 6 Months"

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

        views.yearly_check.setOnClickListener {

            repeat_type=9

            views.selected_Type.text="Yearly"

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

        var no_end=true

        var is_select_date=true

        views.no_end_check.setOnClickListener {


            val cal = Calendar.getInstance()

            cal . setTime (Startdate)

            cal . add (Calendar.YEAR, 3)

            Enddate=cal.time

            views.no_end_select.visibility=View.VISIBLE

            views.end_repeat_select.visibility=View.INVISIBLE

            // views.get_end_date.visibility=View.GONE

            viewers!!.end_repeat_data.setText("")

        }
        views.end_repeat_check.setOnClickListener {


            views.no_end_select.visibility=View.INVISIBLE

            views.end_repeat_select.visibility=View.VISIBLE



            Untildate(views.context,"End Repeat")

        }

        var ischeck_custom=true

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

        val values = arrayOf("Daily", "Weekly", "Monthly", "Yearly")

        name.minValue = 0

        name.maxValue = values.size - 1

        name.displayedValues = values

        name.wrapSelectorWheel = true

        name.setOnScrollListener { numberPicker, i2 ->

            Log.e("selected","$i2")

            if(i2==0){


                var number=name.value

                Log.e("number","....$number")

/*
                if(number==0){

                    frequency_type_id=0

                    Toast.makeText(views.context,"Please select one",Toast.LENGTH_LONG).show()

                    views.frequency_Type.text=values[number]

                }
                else*/
                    if(number==0){

                   // frequency_type_id=1


                        if(dialog!=null){

                            dialog!!.dismiss()

                        }
                    views.frequency_Type.text=values[number]

                    dialog_days_picker(views.context,1)

                }
                else if(number==1){

                    //frequency_type_id=2

                        if(dialog!=null){

                            dialog!!.dismiss()

                        }

                    views.frequency_Type.text=values[number]

                    dialog_week_picker(views.context)


                }
                else if(number==2){

                    //frequency_type_id=3


                        if(dialog!=null){

                            dialog!!.dismiss()

                        }
                    views.frequency_Type.text=values[number]

                    dialog_days_picker(views.context,2)

                }
                else if(number==3){

                   // frequency_type_id=4


                        if(dialog!=null){

                            dialog!!.dismiss()

                        }

                    views.frequency_Type.text=values[number]

                    dialog_days_picker(views.context,3)

                }

            }




        }

        var work_ischeck=false

        views.save_data_update.setOnClickListener {

            Log.e("Selective save","waesrdtfgyhujiko $Startdate")

            val curent=Date()

            val formatter = SimpleDateFormat("dd/MM/yyyy")

            var date=formatter.format(curent)


            val selective= formatter.parse(date)

            Log.e("qwertyu","$selective $Startdate")

            var isfalse=false

            var ischeck2=false

            var custion_check=false

            var date_check=false


            if(reminder_time==null){

             //   Toast.makeText(views.context,"Please set reminder",Toast.LENGTH_LONG).show()
            }
            else {


                if (repeat_type == 1  && reminder_time!! < Date()) {

                    if (Startdate == selective) {

                        isfalse = true

                    }


                }


                if (repeat_type == 2  && reminder_time!! < Date()) {

                    if (Startdate == selective) {

                        ischeck2 = true

                    }


                }
            }

            if(repeat_type==10){


                if(frequency_type_id!=null){

                    if(every_frequency==null){

                        custion_check==true
                    }

                }

                else{

                    custion_check=true

                }

            }

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
                (views.medicine_name.text.trim().isNullOrEmpty() && cat_type==1)->Toast.makeText(views.context,"Please enter Vaccination name",Toast.LENGTH_LONG).show()

                (views.medicine_name.text.trim().isNullOrEmpty() && cat_type==2)->Toast.makeText(views.context,"Please enter medicine name",Toast.LENGTH_LONG).show()

                (views.medicine_name.text.trim().isNullOrEmpty() && cat_type==3)->Toast.makeText(views.context,"Please enter task name",Toast.LENGTH_LONG).show()

                reminder_time==null->Toast.makeText(views.context,"Please set reminder",Toast.LENGTH_LONG).show()

                (isfalse)-> Toast.makeText(views.context,"Reminder Time is invalid",Toast.LENGTH_LONG).show()

                (ischeck2)-> Toast.makeText(views.context,"Reminder Time is invalid",Toast.LENGTH_LONG).show()

                custion_check-> Toast.makeText(views.context,"Please select custom valid data",Toast.LENGTH_LONG).show()

                date_check-> Toast.makeText(views.context,"Please select end date",Toast.LENGTH_LONG).show()

               //((repeat_type==10) && (frequency_type_id==2) && (selective_week==""))-> Toast.makeText(views.context,"Reminder Time is invalid",Toast.LENGTH_LONG).show()

//                    (repeat_type!=1 || repeat_type!=2) && Enddate==null -> Toast.makeText(views.context,"Please select end repeat",Toast.LENGTH_LONG).show()
                else ->{

                    AsyncTask.execute {
                        var userId by stringPref("userId", null)
                        var petid by stringPref("petid", null)
                        var user = userId?.toInt()
                        var petId = petid?.toLong()
                        try {
                            val callDetails = splootDB!!.petMasterDao()

                            val insert_data = madicineType(
                                userId = userId,
                                petId = petid,
                                task_name = views.medicine_name.text.toString().trim(),
                                start_date = Startdate,
                                end_date = Enddate,
                                repeat_type = repeat_type,
                                every_frequency = every_frequency,
                                selective_week = selective_week,
                                frequency_type_id = frequency_type_id,
                                reminder_time = reminder_time,
                                cat_type = cat_type,
                                active = 1
                            )

                            val fun_insert = callDetails.cat_insert(insert_data)

                            val viewall_date=callDetails.getAll_cat(userId!!,petid!!)

                            Log.e("display all data","$viewall_date")


                            val last_data=callDetails.getlast_one()

                            Log.e("last_data","$last_data")

                            val RQS_1=last_data.allTypeId


                            val calNow = Calendar . getInstance ();

                            val calSet:Calendar = calNow . clone () as Calendar;
/*
                            calSet.set(Calendar.DATE, Startdate!!.date);

                            calSet.set(Calendar.MONTH, Startdate!!.month);

                            calSet.set(Calendar.YEAR, Startdate!!.year);*/

                            calNow.set(Calendar.HOUR_OF_DAY, reminder_time!!.hours);

                            calNow.set(Calendar.MINUTE, reminder_time!!.minutes);

                            calNow.set(Calendar.SECOND, 0);

                            calNow.set(Calendar.MILLISECOND, 0);



                            setAlarm(calNow,RQS_1!!.toInt());

                            if(cat_type!=3) {


                                    try {

                                        val mContext = activity
                                        val manager = mContext?.supportFragmentManager
                                        val transaction = manager?.beginTransaction()

                                        transaction?.replace(R.id.medicine_frag, Medicine())

                                        transaction?.commit()

                                    } catch (e: Exception) {

                                    }



                                activity!!.runOnUiThread {

                                    Toast.makeText(views.context,"Saved Successfully",Toast.LENGTH_LONG).show()

                                }

                            }


                        } catch (e: Exception) {
                            val s = e.message
                            Log.e("Error", s)
                        }
                    }

                }
            }
        }

        return views
    }

    private fun setAlarm(
        targetCal: Calendar,
        id: Int
    ) {

        val daua=activity

        val intent = Intent(viewers!!.context, AlarmReceiver::class.java)

        Log.e("alaram data","${targetCal.time}")

        intent.putExtra("name", "$id")

        intent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND)

        var DAY = 86400000L

        var millisec = System.currentTimeMillis().toInt()

        val pendingIntent = PendingIntent.getBroadcast(activity, millisec, intent, 0);

        val alarmManager: AlarmManager =activity?.getSystemService(Context.ALARM_SERVICE) as AlarmManager;

        if (repeat_type==1){

            val formatter = SimpleDateFormat("dd/MM/yyyy")

            val output = formatter.format(Date())

            val out=formatter.parse(output)

            if(Startdate!!.compareTo(out)<0 )
            {

                Log.e("alaram","check ${out}")

              //  Toast.makeText(this.context,"")


            }
            else {


                val valide=  Startdate!!.time  - Date().time

                Log.e("alaram validate","$valide")

                val data_responce = (valide / (1000 * 3600 * 24)).toInt()

                var tar=data_responce

                Log.e("alaram","$tar")

                if(valide<0)
                {


                 //   alarmManager.set(AlarmManager.RTC_WAKEUP, targetCal.getTimeInMillis(), pendingIntent)


                    if (Build.VERSION.SDK_INT >= 23) {
                        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,
                            targetCal.getTimeInMillis(), pendingIntent);
                    } else if (Build.VERSION.SDK_INT >= 19) {
                        alarmManager.setExact(AlarmManager.RTC_WAKEUP,  targetCal.getTimeInMillis(), pendingIntent);
                    } else {
                        alarmManager.set(AlarmManager.RTC_WAKEUP,  targetCal.getTimeInMillis(), pendingIntent);
                    }


                }
                else {

                    val time1=(tar+1)*DAY

                    targetCal.add(Calendar.DATE,(tar))

                 //   alarmManager.set(AlarmManager.RTC_WAKEUP, targetCal.getTimeInMillis(), pendingIntent)


                    if (Build.VERSION.SDK_INT >= 23) {
                        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,
                            targetCal.getTimeInMillis(), pendingIntent);
                    } else if (Build.VERSION.SDK_INT >= 19) {
                        alarmManager.setExact(AlarmManager.RTC_WAKEUP,  targetCal.getTimeInMillis(), pendingIntent);
                    } else {
                        alarmManager.set(AlarmManager.RTC_WAKEUP,  targetCal.getTimeInMillis(), pendingIntent);
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


                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,
                        targetCal.getTimeInMillis(), pendingIntent);

                    alarmManager.setInexactRepeating(
                        AlarmManager.RTC_WAKEUP, targetCal.getTimeInMillis(),
                        AlarmManager.INTERVAL_HOUR, pendingIntent
                    )

                } else if (Build.VERSION.SDK_INT >= 19) {


//

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


                val valide=  Startdate!!.time  - Date().time

                Log.e("alaram validate","$valide")

                val data_responce = (valide / (1000 * 3600 * 24)).toInt()

                var tar=data_responce

                Log.e("alaram","$tar")

                if(valide<0)
                {

                    if (Build.VERSION.SDK_INT >= 23) {


                        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,
                            targetCal.getTimeInMillis(), pendingIntent);

                        alarmManager.setInexactRepeating(
                            AlarmManager.RTC_WAKEUP, targetCal.getTimeInMillis(),
                            AlarmManager.INTERVAL_HOUR, pendingIntent
                        )

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


                        alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,
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

                alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, targetCal.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);

                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,
                    targetCal.getTimeInMillis(), pendingIntent)




            } else if (Build.VERSION.SDK_INT >= 19) {


                alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, targetCal.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);


            } else {

                alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, targetCal.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
            }

        }

    }




    fun dialog_week_picker(commandAdapter: Context) {

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

            when{
                weekdayselect==""-> Toast.makeText(this.context,"Please select one day",Toast.LENGTH_LONG).show()

                else->{

                    Log.e("selected week","$selective_week")

            frequency_type_id=2

            every_frequency=seleted_data

            selective_week=weekdayselect

            Log.e("work","Week day $seleted_data , $weekdayselect")


            if(seleted_data==1){

                viewers!!.custom_data_view.text="$seleted_data Week"

            }
            else{

                viewers!!.custom_data_view.text="$seleted_data Weeks"


            }

            dialog!!.dismiss()


                }
            }


        }

        dialog!!.show()

    }

    fun Untildate(commandAdapter: Context, datas: String): String? {


        dialog = Dialog(commandAdapter)
        Log.e("diloge","------------------------------------------>")
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog!!.setCancelable(false)
        dialog!!.setContentView(R.layout.calender)
        var date=""

        var date_1:Date?=Startdate

        val dateget =dialog!!.findViewById<CalendarView>(R.id.getdate)

         dialog!!.headertext.text=datas

        dateget.minDate=Startdate!!.time

        dateget?.setOnDateChangeListener { view, year, month, dayOfMonth ->

            Log.e("Data","...............${Date(view.date)}")

            date = "" + dayOfMonth + "/" + (month + 1) + "/" + year

            val simpledate=SimpleDateFormat("")

            val formatter = SimpleDateFormat("dd/MM/yyyy")

            val output = formatter.parse(date)

            val formatter1 = SimpleDateFormat("dd/MM/yyyy")

            val output1=formatter1.format(output)

            date=output1

            date_1=output

        }
         dialog!!.calendercan.setOnClickListener {

            Enddate=Startdate

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
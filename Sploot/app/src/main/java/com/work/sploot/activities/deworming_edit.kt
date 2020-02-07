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
import com.work.sploot.Entity.madicineType
import com.work.sploot.R
import com.work.sploot.SplootAppDB
import com.work.sploot.data.stringPref
import com.work.sploot.services.AlarmReceiver
import kotlinx.android.synthetic.main.calender.*
import kotlinx.android.synthetic.main.deworming.view.*
import kotlinx.android.synthetic.main.dosage.*
import kotlinx.android.synthetic.main.gettime.*
import java.text.SimpleDateFormat
import java.util.*

class deworming_edit: Fragment()
{
    var start_date: Date? = null

    var repeat: String? = null

    var end_date: Date? = null

    var name: String? = null

    private var splootDB: SplootAppDB? = null

    var dosage: String? = null

    var viewdatas: View? = null

    var reminder_time: Date? = null

    companion object {

        var selected_date: madicineType?=null

        fun newInstance(user: madicineType): deworming_edit {

            selected_date = user

            return deworming_edit()
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var views = inflater.inflate(R.layout.deworming, container, false)

        Log.e("Selected_data", "$selected_date")

        splootDB = SplootAppDB.getInstance(views.context)

        start_date = selected_date!!.start_date

        end_date = selected_date!!.end_date

        repeat = selected_date!!.repeat_type.toString()

        reminder_time = selected_date!!.reminder_time

        val date_select =  start_date  //selected_date!!.start_date

        val format = SimpleDateFormat("dd/MM/yyyy")

        views.date_data.text = format.format(date_select)

        views.repeat_data.text = repeat + "Days"

        views.Deworming_name.setText(selected_date!!.task_name)

        val dates = reminder_time

        var hours = dates!!.hours

        var min = dates!!.minutes

        var hr = ""

        var mi = ""

        var day = "am"

        if (hours > 12) {
            day = "pm"
            hours = hours - 12
        } else {
            hours = hours

            if (hours == 12) {

                day = "pm"

            } else {
                day = "am"
            }

        }
        if (hours < 10) {
            hr = "0" + hours
        } else {
            hr = "" + hours
        }

        if (min < 10) {
            mi = "0$min"
        } else {
            mi = "" + min
        }

        if (hr == "00") {

            hr = "12"

        }

        //  textViewName.text ="$hr:$mi $

        views.reminder_time_deworming.text = "$hr:$mi $day"


        views.get_time_dewoming.addOnDateChangedListener { displayed, date ->

            var dates=date

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

            //  textViewName.text ="$hr:$mi $day"

            views.reminder_time_deworming.text="$hr:$mi $day"
        }


        views.get_time_dewoming.setDefaultDate(reminder_time)


        val sdf = SimpleDateFormat("dd/MM/yyyy")

        val dateInString = sdf.format(Date())

        val formatter = SimpleDateFormat("dd/MM/yyyy")

        val date = formatter.parse(dateInString)

        start_date = date

        views.select_date_deworming.setOnClickListener {
            getdate(views.context, "Select Date", views)
        }
        views.select_repeat.setOnClickListener {
            dialog_for_repeat(views.context, "Repeat Every", views)
        }

        views.save_deworming.setOnClickListener {

            val cal = Calendar.getInstance()

            cal.time = start_date

            cal.add(Calendar.YEAR, 1)

            end_date = cal.time

            when {

                views.Deworming_name.text.toString().trim().isNullOrEmpty() -> views.Deworming_name.error = "Please enter deworming name"

                //  views.dosage_deworming.text.toString().trim().isNullOrEmpty()-> views.dosage_deworming.error="Please enter deworming name"

                start_date == null -> Toast.makeText(views.context, "Select Start day", Toast.LENGTH_LONG).show()

                reminder_time == null -> Toast.makeText(views.context, "Please set Reminder", Toast.LENGTH_LONG).show()

                repeat == null -> Toast.makeText(views.context, "Select Repeat day", Toast.LENGTH_LONG).show()

                else -> {

                    AsyncTask.execute {


                        Log.e("qwertyuiop","$start_date $end_date repeat time $repeat $reminder_time")

                        var userId by stringPref("userId", null)

                        var petid by stringPref("petid", null)

                        var user = userId?.toInt()

                        var petId = petid?.toLong()

                        try {

                            val callDetails = splootDB!!.petMasterDao()

                            val delete=callDetails.delete_petday(selected_date!!.allTypeId!!)

                            val insert_data = madicineType(
                                userId = userId,
                                petId = petid,
                                task_name = views.Deworming_name.text.toString().trim(),
                                start_date = start_date,
                                end_date = end_date,
                                repeat_type =repeat!!.toInt(),
                                reminder_time = reminder_time,
                                cat_type = 4,
                                active = 1
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

                            if (calSet.compareTo(calNow) <= 0)
                            {

                                calSet.add(Calendar.DATE, 1)

                            }

                            Log.e("Log_to_print","$cal \n$RQS_1")

                            setAlarm(calSet,RQS_1!!.toInt())

                                val mContext = activity
                                val manager = mContext?.supportFragmentManager
                                val transaction = manager?.beginTransaction()
                                transaction!!.replace(R.id.view_pager, calanderLayout.newInstance())
                                transaction!!.commit()


                        } catch (e: Exception) {
                            val s = e.message
                            Log.e("Error", s)
                        }
                    }
                    Toast.makeText(views.context,"Saved successfully",Toast.LENGTH_LONG).show()
                }

            }
            val deworming_name=views.Deworming_name.text.toString().trim()
            dosage=views.dosage_deworming.text.toString()
            Log.e("Date_save","..................$start_date    $deworming_name")
            Log.e("Repeat_save","................$end_date")
            Log.e("Dosage","..................$dosage")
        }
        return views
    }

    private fun setAlarm(targetCal: Calendar, id: Int) {


        val intent = Intent(this.context, AlarmReceiver::class.java)

        intent.putExtra("name", "$id")

        intent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND)

        var millisec = System.currentTimeMillis().toInt()

        val pendingIntent = PendingIntent.getBroadcast(activity, millisec, intent, 0)

        val alarmManager: AlarmManager = activity?.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        if (Build.VERSION.SDK_INT >= 23) {

            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, targetCal.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent)

            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, targetCal.getTimeInMillis(), pendingIntent)


        } else if (Build.VERSION.SDK_INT >= 19) {


            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, targetCal.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent)


        } else {

            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, targetCal.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent)
        }



    }

    fun dialog_for_repeat(commandAdapter: Context, view_data: String, views: View): String?
    {
        val dialog = Dialog(commandAdapter)

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        dialog.setCancelable(false)

        dialog.setContentView(R.layout.dosage)

        var data="15"

        var data_1:Date?=null

        val name =dialog.findViewById<NumberPicker>(R.id.numberpicker)

        val header=dialog.findViewById<TextView>(R.id.headertextfordata)

        dialog.headertextfordata.text=view_data

        val values = arrayOf("15 days", "30 days", "45 days", "60 days", "75 days", "90 days")

        val values_date = arrayOf("15", "30", "45", "60", "75", "90")

        name.minValue = 0

        name.maxValue = values.size - 1

        name.displayedValues = values

        name.wrapSelectorWheel = true

        dialog.calendercancel.setOnClickListener {

            dialog.dismiss()

        }

        name.setOnValueChangedListener { numberPicker, i, i2 ->
            Log.e("Picker",""+(i2+1))
            data= values_date[i2]
        }
        dialog.calenderok.setOnClickListener {
            Log.e("data","zxcvbnm, $data")
            /*  val cal = Calendar.getInstance()
              cal . setTime (start_date)
              cal . add (Calendar.DAY_OF_YEAR, data.toInt())
              data_1=cal.time*/
            //end_date=data_1

            repeat=data

            views.repeat_data.text= "$data Days"

            dialog.dismiss()
        }

        dialog.show()

        return null

    }

    fun getdate(commandAdapter: Context, datas: String, views: View): String?
    {
        val dialog = Dialog(commandAdapter)

        Log.e("diloge","------------------------------------------>")

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        dialog.setCancelable(false)

        dialog.setContentView(R.layout.calender)

        var current=""

        var date_1:Date?=start_date

        val dateget =dialog.findViewById<CalendarView>(R.id.getdate)

        dialog.headertext.text=datas

        var select_date by stringPref("select_date", null)

        Log.e("Selective","$select_date")

        val formatter = SimpleDateFormat("dd/MM/yyyy")

        var start =formatter.parse(select_date)

        val date= start!!.time

        dateget.minDate=start!!.time

        dateget.setDate(start_date!!.time)

        dateget?.setOnDateChangeListener { view, year, month, dayOfMonth ->

            current = "" + dayOfMonth + "/" + (month + 1) + "/" + year

            val simpledate=SimpleDateFormat("")

            // viewdata?.startdateselect?.setText(date)

            val formatter = SimpleDateFormat("dd/MM/yyyy")

            val output = formatter.parse(current)

            val formatter1 = SimpleDateFormat("yyyy-MM-dd")

            val output1=formatter1.format(output)

            date_1=output

        }
        dialog.calendercan.setOnClickListener {
            dialog.dismiss()
        }
        dialog.calenderokbtn.setOnClickListener {

            start_date=date_1

            val formatter = SimpleDateFormat("dd/MM/yyyy")

            views.date_data.text=formatter.format(date_1)

            Log.e("Start_date","$current")

            dialog.dismiss()
        }
        dialog.show()

        return null
    }

    fun remindertime(commandAdapter: Context, datas: String): String? {

        val dialog = Dialog(commandAdapter)
        Log.e("diloge","------------------------------------------>")
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.gettime)
        val header=dialog.findViewById<TextView>(R.id.headertextfortime)
        header.text = datas
        var data=""
        var day="am"
        var hours=0

        var hor=""
        var min=""

        dialog.timecancel.setOnClickListener {
            dialog.dismiss()
        }
        dialog.timeok.setOnClickListener {
            //  reminderInsert(data)
            dialog.dismiss()
        }
        dialog.gettime.setOnTimeChangedListener { timePicker, hour, minute ->
            if(hour>12){
                day="pm"
                hours =hour- 12
            } else{
                hours=hour
                day="am"

            }
            if(hours<10)
            {
                hor="0"+hours
            }else
            {
                hor=""+hours
            }

            if(minute<10)
            {
                min  ="0"+minute
            }else
            {
                min=""+minute
            }
            data="$hor:$min $day"
            Log.e("time", "Hour: "+ hours + " Minute : "+ minute+"day " +day)
        }
        dialog.show()

        return null
    }




}
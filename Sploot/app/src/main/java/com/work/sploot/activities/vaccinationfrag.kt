package com.work.sploot.activities

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.Dialog
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.*
import com.work.sploot.R
import kotlinx.android.synthetic.main.calender.*
import kotlinx.android.synthetic.main.dosage.*
import kotlinx.android.synthetic.main.dosage.calendercancel
import kotlinx.android.synthetic.main.gettime.*
import kotlinx.android.synthetic.main.vaccination.*
import kotlinx.android.synthetic.main.vaccination.view.*
import  androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.work.sploot.Entity.alarmdata
import com.work.sploot.SplootAppDB
import com.work.sploot.data.stringPref
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import android.widget.Toast
import android.view.View.OnFocusChangeListener
import androidx.core.content.getSystemService
import androidx.room.ColumnInfo
import com.github.florent37.singledateandtimepicker.SingleDateAndTimePicker
import com.github.florent37.singledateandtimepicker.dialog.SingleDateAndTimePickerDialog
import com.work.sploot.Entity.taskCategory
import kotlinx.android.synthetic.main.calender.headertext
import kotlinx.android.synthetic.main.time_picker.*
import kotlinx.android.synthetic.main.vaccination.view.add_reminder


class vaccinationfrag  : Fragment() {
    var Startdate:Date?=null
    private var splootDB: SplootAppDB? = null
    var Enddate:Date?=null
    var dailyxtime:String?=null
    var dailyxhours:String?=null
    var xtimeaday:String?=null
    var xday:String?=null
    var dosagedata:String?=null
    var hoursreminderdata:String?=null
    var firstintake:String?=null
    var lastintake:String?=null
    var active:String?=null
    var paused:String?=null
    var cycle:String?=null
    var weekdayselect:String?=""
    var viewers: View? =null
    var alaramcatId:String?=null
    var recyclerView:RecyclerView?=null
    // var medicinetype: Int? =null

    val users = ArrayList<reminderdata>()

    companion object {
        var medicinetype=1

        fun newInstance(type:Int): vaccinationfrag {
            medicinetype=type
            Log.e("type","====>>>>>>>>>>>>>>> $type")
            return vaccinationfrag()
        }
    }
    @SuppressLint("NewApi", "WrongConstant")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?{
        val views = inflater.inflate(R.layout.vaccination, container, false)
        recyclerView=views.findViewById(R.id.reminder_recycler)
        if(medicinetype==1){

            views.vaccinationame.hint = "Enter Vaccination Name"
            //   views.vaccinationtype.hint = "Enter Vaccination Type"
        }
        else  if(medicinetype==2){
            views.vaccinationame.hint = "Enter De-worming Name"
            //  views.vaccinationtype.hint = "Enter De-worming Type"

        }
        else if(medicinetype==3){
            views.vaccinationame.hint = "Enter medication Name"
            //  views.vaccinationtype.hint = "Enter medication Type"

        }


        viewers=views

        splootDB = SplootAppDB.getInstance(views.context)

        val sdf = SimpleDateFormat("dd/MM/yyyy")

        val dateInString = sdf.format(Date())

        val formatter = SimpleDateFormat("dd/MM/yyyy")

        val date = formatter.parse(dateInString)


        Startdate=date

        val vacinationname= views.findViewById<EditText>(R.id.vaccinationame)
        var vacinationnamedatas: String? =null
        // val vacinationtype= views.findViewById<EditText>(R.id.vaccinationtype)
        var vacinationtypedata: String? =null
        var partofthedaydata:String=""
        val durationformaattext=views.findViewById<TextView>(R.id.durationdataformat)
        val durationdatatext=views.findViewById<TextView>(R.id.durationdata)


        val breed = arrayOf("Select Vaccination Type", "Injection", "Syrup","Tablet")
        val breedspinner=views.findViewById<Spinner>(R.id.vaccinationtype)



        vacinationname.setOnFocusChangeListener(OnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                //  calculate()
                val inputMethodManager =
                    views.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(
                    activity?.currentFocus?.windowToken,
                    InputMethodManager.HIDE_NOT_ALWAYS
                )
            }

        })

        if (breedspinner != null) {

            val arrayAdapter = ArrayAdapter(views.context, R.layout.spin_textview, breed)
            breedspinner.adapter = arrayAdapter as SpinnerAdapter?

            breedspinner!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    Log.e("masdfdd", "" + breed[position])

                    if (position!=0) {
                        vacinationtypedata = breed[position]
                    }
                    else{
                        vacinationtypedata =null
                    }

                    when (position) {
                        0 -> {
                            views.dosage_vaccination.visibility= View.GONE
                            views.dosage_vaccination.hint=""
                            views.dosage_vaccination.setText("")
                        }
                        3 -> {
                            views.dosage_vaccination.visibility= View.VISIBLE
                            views.dosage_vaccination.hint="Tablet (Count)"
                            views.dosage_vaccination.setText("")

                        }
                        2 -> {
                            views.dosage_vaccination.visibility= View.VISIBLE
                            views.dosage_vaccination.hint="Syrup (ml)"
                            views.dosage_vaccination.setText("")
                        }
                        1 -> {
                            views.dosage_vaccination.visibility= View.VISIBLE
                            views.dosage_vaccination.hint="Injection (ml)"
                            views.dosage_vaccination.setText("")

                        }
                    }

                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // Code to perform some action when nothing is selected
                    vacinationtypedata=null
                }
            }
        }




        views.savedata.setOnClickListener {
            vacinationnamedatas=vacinationname.text.toString().trim()
            when{
                vacinationname.text.toString().trim().isNullOrEmpty()->{
                    vacinationname.text.clear()
                    vacinationname.error="Name field Can't be Empty"

                }

                vacinationtypedata==null->{
                    Toast.makeText(views.context,"Please select type field ",Toast.LENGTH_SHORT).show()
                }

                else->{
                    savedataprocess(vacinationnamedatas
                        ,vacinationtypedata
                        ,partofthedaydata,
                        Startdate,
                        Enddate,
                        dailyxtime,
                        dailyxhours,
                        xtimeaday,
                        dosagedata,
                        hoursreminderdata,
                        firstintake,
                        lastintake,
                        weekdayselect,
                        active,
                        paused,
                        cycle,
                        views.dosage_vaccination.text.toString().trim(),
                        xday                    )
                    when (medicinetype) {
                        1 -> Toast.makeText(views.context,"Vaccination added successfully",Toast.LENGTH_SHORT).show()
                        2 -> Toast.makeText(views.context,"save successfully",Toast.LENGTH_SHORT).show()
                        3 -> Toast.makeText(views.context,"Others added successfully",Toast.LENGTH_SHORT).show()
                    }

                }
            }
            // vacinationtypedata=vacinationtype.text.toString()

            //  Toast.makeText(views.context,"save successfully",Toast.LENGTH_SHORT).show()
        }

        var duration_click=true
        views.durationclicklayout.setOnClickListener {
            if(duration_click) {
                views.durationlayout.visibility = View.VISIBLE
                views.frequencylayout.visibility = View.GONE
                duration_click=false
            }
            else{
                views.durationlayout.visibility = View.GONE
                views.frequencylayout.visibility = View.GONE
                duration_click=true
            }
        }

        var freqency_click=true
        views.frequencyclicklayout.setOnClickListener {

            if(freqency_click) {
                views.frequencylayout.visibility = View.VISIBLE
                views.durationlayout.visibility = View.GONE
                freqency_click=false
            }
            else{
                views.frequencylayout.visibility = View.GONE
                views.durationlayout.visibility = View.GONE
                freqency_click=true
            }
        }
        var ischeckmrng=true
        views.mrngradio.setOnClickListener {

            views.frequencylayout.visibility=View.GONE
            views.durationlayout.visibility=View.GONE

            if(ischeckmrng){
                views.mrngradio.isChecked = true
                ischeckmrng=false
                partofthedaydata+="M"
            }
            else{
                views.mrngradio.isChecked = false
                ischeckmrng=true
                partofthedaydata = partofthedaydata?.replace("M", "")

            }
            //  partofthedaydata="Morning"
            Log.e("pot......",">>>>>>>>>>>>>>>>>>>>>>.. $partofthedaydata")

        }
        var ischeckaftn=true

        views.aftnradio.setOnClickListener {
            views.frequencylayout.visibility=View.GONE
            views.durationlayout.visibility=View.GONE
            if(ischeckaftn){
                views.aftnradio.isChecked = true
                ischeckaftn=false
                partofthedaydata+="A"
            }
            else{
                views.aftnradio.isChecked = false
                ischeckaftn=true
                partofthedaydata = partofthedaydata?.replace("A", "")

            }
            Log.e("pot......",">>>>>>>>>>>>>>>>>>>>>>.. $partofthedaydata")
        }
        var ischeckevng=true
        views.evngradio.setOnClickListener {

            views.frequencylayout.visibility=View.GONE
            views.durationlayout.visibility=View.GONE
            if(ischeckevng){
                views.evngradio.isChecked = true
                ischeckevng=false
                partofthedaydata+="E"
            }
            else{
                views.evngradio.isChecked = false
                ischeckevng=true
                partofthedaydata = partofthedaydata?.replace("E", "")

            }
            //  partofthedaydata="Morning"
            Log.e("pot......",">>>>>>>>>>>>>>>>>>>>>>.. $partofthedaydata")

        }
        var ischecknyt=true
        views.nightradio.setOnClickListener {

            views.frequencylayout.visibility=View.GONE
            views.durationlayout.visibility=View.GONE
            if(ischecknyt){
                views.nightradio.isChecked = true
                ischecknyt=false
                partofthedaydata+="N"
            }
            else{
                views.nightradio.isChecked = false
                ischecknyt=true
                partofthedaydata = partofthedaydata?.replace("N", "")

            }
            //  partofthedaydata="Morning"
            Log.e("pot......",">>>>>>>>>>>>>>>>>>>>>>.. $partofthedaydata")
        }

        views.startdaylayout.setOnClickListener {
            getdate(views.context,"Start date")
        }
        views.radiogroup1.setOnClickListener {

            val cal = Calendar.getInstance()
            cal . setTime (Startdate)
            cal . add (Calendar.YEAR, 1)
            Enddate=cal.time


            val formatter1 = SimpleDateFormat("dd/MM/yyyy")

            val startdate_view=formatter1.format(Startdate)

            viewers?.durationhint?.setText("$startdate_view to No end date")



            viewers?.durationdataformat?.setText("No end date found")
            viewers?.durationdata?.setText("No end date")

        }
        views.radiogroup2.setOnClickListener {


            Untildate(views.context,"Until Date")





        }
        views.radiogroup3.setOnClickListener {
            // dialogcountpicker(views.context,"Duration")
            dialogdayspicker(views.context,"For X days",4)



        }
        views.radioButton1.setOnClickListener {
            views.multiprocessview.visibility=View.GONE
            views.checkboxlayout.visibility=View.GONE
            views.xyview.visibility=View.GONE

            views.freqency_data_layout.visibility=View.VISIBLE

            dialogtimescountpicker(views.context,"Daily,X times a day")

            views.frequencyhint.setText("Daily,X times a day")


            xday=null
            dosagedata=null
            hoursreminderdata=null
            firstintake=null
            lastintake=null
            active=null
            paused=null
            cycle=null
            weekdayselect=""


        }
        views.radioButton2.setOnClickListener {
            views.multiprocessview.visibility=View.VISIBLE
            views.checkboxlayout.visibility=View.GONE
            views.xyview.visibility=View.GONE
            views.freqency_data_layout.visibility=View.GONE

            views.frequencyhint.setText("Daily, every X hours")

            xtimeaday=null
            xday=null
            active=null
            paused=null
            cycle=null
            weekdayselect=""

        }
        views.radioButton3.setOnClickListener {
            views.multiprocessview.visibility=View.GONE
            views.checkboxlayout.visibility=View.GONE
            views.xyview.visibility=View.GONE
            dialogdayspicker(views.context,"Every X days",0)

            views.frequencyhint.setText("Every X days")

            views.freqency_data_layout.visibility=View.VISIBLE
            xtimeaday=null

            dosagedata=null
            hoursreminderdata=null
            firstintake=null
            lastintake=null
            active=null
            paused=null
            cycle=null
            weekdayselect=""

        }
        views.radioButton5.setOnClickListener {
            views.xyview.visibility=View.VISIBLE
            views.multiprocessview.visibility=View.GONE
            views.freqency_data_layout.visibility=View.GONE
            views.checkboxlayout.visibility=View.GONE

            views.frequencyhint.setText("Cycle X days active Y days Paused ")

            xtimeaday=null
            xday=null
            dosagedata=null
            hoursreminderdata=null
            firstintake=null
            lastintake=null
            weekdayselect=""

        }
        views.radioButton4.setOnClickListener {
            views.xyview.visibility=View.GONE
            views.multiprocessview.visibility=View.GONE
            views.checkboxlayout.visibility=View.VISIBLE
            views.freqency_data_layout.visibility=View.GONE

            views.frequencyhint.setText("Specific day a week ")
            xtimeaday=null
            xday=null
            dosagedata=null
            hoursreminderdata=null
            firstintake=null
            lastintake=null
            active=null
            paused=null
            cycle=null
        }
        views.dosagelayout.setOnClickListener {
            dialogtabletspicker(views.context,"Dosage")

        }
        views.remindereverylayout.setOnClickListener {
            dialoghourreminderspicker(views.context,"Reminder every")
        }
        views.firstintakelayout.setOnClickListener {
            firstintakepicker(views.context,"First Intake")

        }
        views.lastintakelayout.setOnClickListener {
            lastintakepicker(views.context,"Last Intake")
        }


        views.Monday?.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                weekdayselect+="Monday"

            }
            else{
                weekdayselect = weekdayselect?.replace("Monday", "")

            }
        }
        views.tueasdy?.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                weekdayselect+="Tuesday"

            }
            else{
                weekdayselect = weekdayselect?.replace("Tuesday", "")

            }
        }
        views.wednesday?.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                weekdayselect+="Wednesday"

            }
            else{
                weekdayselect = weekdayselect?.replace("Wednesday", "")

            }
        }
        views.thuesday?.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                weekdayselect+="Thursday"

            }
            else{
                weekdayselect = weekdayselect?.replace("Thursday", "")

            }
        }
        views.friday?.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                weekdayselect+="Friday"

            }
            else{
                weekdayselect = weekdayselect?.replace("Friday", "")

            }
        }
        views.saterday?.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                weekdayselect+="Saturday"

            }
            else{
                weekdayselect = weekdayselect?.replace("Saturday", "")

            }
        }
        views.sunday?.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                weekdayselect+="Sunday"

            }
            else{
                weekdayselect = weekdayselect?.replace("Sunday", "")

            }
        }
        views.dayactivelayout.setOnClickListener {
            dialogdayspicker(views.context,"Day Active",1)

        }
        views.cycledaylayout.setOnClickListener {

         //   dialogdayspicker(views.context,"Cycle Day",2)

            dialogtodaycycle(views.context,"Cycle Day")
        }
        views.Daypausedlayout.setOnClickListener {
            dialogdayspicker(views.context,"Day Paused",3)
        }

        val recyclerView=views.findViewById<RecyclerView>(R.id.reminder_recycler)
        views.alaramclicklayout.setOnClickListener {
           // remindertime(views.context,"Set Reminder")

            var datav= SingleDateAndTimePickerDialog.Builder(views.context)
                .bottomSheet()
                .curved()
                .displayMinutes(true)
                .displayHours(true)
                .displayDays(false)
                .displayMonth(false)
                .displayYears(false)
                .title("Set Reminder")
                .displayListener(SingleDateAndTimePickerDialog.DisplayListener { picker ->

                }).listener { date: Date? ->

                    Log.e("date","$date.........")

                    reminderInsert(date!!)

                }.display()

        }
        views.add_reminder.setOnClickListener {
            //            val date = getCurrentDateTime()
//            val currenttimeformatted = date.toString("HH:mm")
//            val alarmid=alarminsert("startdate","enddate",currenttimeformatted,"Tablet 1")
//            Log.e("message","Alarm Id $alaramcatId")
//            recyclerView.layoutManager = LinearLayoutManager(views.context)
//            val adapter = CustomAdapter(users)
//            recyclerView.adapter = adapter
        }
        return views
    }

    /*private fun alarminsert(startdate: String?, enddate: String?, currenttimeformatted: String?, notification_data: String?):String {
        var alarmid=""
        AsyncTask.execute {
            var userId by stringPref("userId", null)
            var petid by stringPref("petid", null)
            try {
                Log.e("check data","statedatre = $startdate,petid=$petid,userid:$userId")

                val insertdata=alarmdata(
                    userId = userId,
                    petId = petid,
                    startdate = startdate,
                    endate = enddate,
                    time =  currenttimeformatted,
                    //notifieddata = notification_data,
                    active = "1"
                )
                val callDetails = splootDB!!.petMasterDao()
                val data=callDetails.insertalaramdata(insertdata)
                val tbldata=callDetails.findcatid(startdate!!,petid!!,userId!!,enddate!!,currenttimeformatted!!)
                Log.e("alaram table"," inserted data---------->$tbldata")
                alaramcatId="${tbldata.alarmId}"
                users.add(reminderdata(currenttimeformatted, "Tablet 1",alaramcatId))
                Log.e("LAransjkan","IN db get $alaramcatId")
            } catch (e: Exception) {
                val s = e.message;
                Log.e("Error",s)
                alarmid="0123"
            }
        }

        return alarmid
    }*/

    private fun savedataprocess(
        vacinationnamedata: String?,
        vacinationtypedata: String?,
        partofthedaydata: String?,
        startdate: Date?,
        enddate: Date?,
        dailyxtime: String?,
        dailyxhours: String?,
        xtimeaday: String?,
        dosagedata: String?,
        hoursreminderdata: String?,
        firstintake: String?,
        lastintake: String?,
        weekdayselect: String?,
        active: String?,
        paused: String?,
        cycle: String?,
        toString: String,
        xday: String?
    ) {
        Log.e("savedata",""+vacinationnamedata+"\n"
                +vacinationtypedata+"\n"
                +partofthedaydata+"\n"+"stAART DATE= "
                +startdate+"\n"
                +enddate+"\n"
                +dailyxtime+"\n"
                +dailyxhours+"\n"
                +xtimeaday+"\n"
                +dosagedata+"\n"
                +hoursreminderdata+"\n"
                +firstintake+"\n"
                +lastintake+"\n"
                +weekdayselect+"\n"+
                active+"\n"+
                paused+"\n"+
                cycle+"\n"+
                medicinetype
        )


        AsyncTask.execute {
            var userId by stringPref("userId", null)
            var user= userId?.toInt()

            var petid by stringPref("petid", null)

            try {

                Log.e("check data","statedatre = $startdate,petid=$petid,userid:$userId")
                val callDetails = splootDB!!.petMasterDao()

                var pet = taskCategory(
                    vname=vacinationnamedata,
                    vtype=vacinationtypedata,
                    partoftheday=partofthedaydata,
                    startdate=startdate,
                    enddate=enddate,
                    dailyxtime=dailyxtime,
                    dailyxhours=dailyxhours,
                    xtimeaday=xtimeaday,
                    dosage=dosagedata,
                    hoursreminderdata=hoursreminderdata,
                    firstintake=firstintake,
                    lastintake=lastintake,
                    weekdayselect=weekdayselect,
                    active=active,
                    paused=paused,
                    cycle=cycle,
                    userId = userId,
                    petId =petid,
                    medicineType = medicinetype,
                    v_dosage = toString,
                    xday = xday
                )

                var repocecreate=callDetails.insertdayupdate(pet)

                var viewdata=callDetails.get()

                Log.e("INseted", "worked  $viewdata")


                    try {
                        val mContext = activity
                        val manager = mContext?.supportFragmentManager
                        val transaction = manager?.beginTransaction()
                        transaction?.addToBackStack(null)
                        transaction?.replace(R.id.medicfragement, Medicine())
                        transaction?.commit()
                    } catch (e: Exception) {

                    }
                val getdata=callDetails.getdataalarm(userId!!, petid!!,"2")

                Log.e("size","${getdata.size}")

                for(i in 0 ..getdata.size-1) {
                    var update = alarmdata(
                        alarmId =getdata[i].alarmId,
                        userId =getdata[i].userId,
                        petId = getdata[i].petId,
                        startdate = getdata[i].startdate,
                        endate = getdata[i].endate,
                        time = getdata[i].time,
                        notified_data = getdata[i].notified_data,
                        reminder_type = getdata[i].reminder_type,
                        active = "1"
                    )
                    var updatdata=callDetails.updatealarmdata(update)


                    //add alara
                }

                var getall=callDetails.getalarmdata()

                Log.e("all","Alaram data $getall")

                var check=callDetails.checkalram(userId!!)

                if(check){

                    var data=callDetails.getalram(userId!!)


                }

            } catch (e: Exception) {
                val s = e.message;
              //  Log.e("Error msg",s)
            }
        }
    }

    fun getdate(commandAdapter: Context, datas: String): String? {
        val dialog = Dialog(commandAdapter)
        Log.e("diloge","------------------------------------------>")
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.calender)
        var date=""
        var date_1:Date?=null
        val dateget =dialog.findViewById<CalendarView>(R.id.getdate)
        dialog.headertext.text=datas


        dateget?.setOnDateChangeListener { view, year, month, dayOfMonth ->


            Log.e("Data","...............${Date(view.date)}")
            date = "" + dayOfMonth + "/" + (month + 1) + "/" + year



            viewers?.startdateselect?.setText(date)

            val formatter = SimpleDateFormat("dd/MM/yyyy")

            val output = formatter.parse(date)

            val formatter1 = SimpleDateFormat("dd/MM/yyyy")

            val output1=formatter1.format(output)

            date_1=output

            Startdate=output

            Log.e("qowow","$Startdate")

            // Startdate=date

            Log.e("Data","...............$output")

        }
        dialog.calendercan.setOnClickListener {

            dialog.dismiss()
        }
        dialog.calenderokbtn.setOnClickListener {
            // dialog.startdateselect.setText(Startdate)

            // Startdate=date
            Log.e("Staetdate","$Startdate")
            dialog.dismiss()
        }


        dialog.show()
        return null
    }
    fun Untildate(commandAdapter: Context, datas: String): String? {
        val dialog = Dialog(commandAdapter)
        Log.e("diloge","------------------------------------------>")
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.calender)
        var date=""

        var date_1:Date?=null

        val dateget =dialog.findViewById<CalendarView>(R.id.getdate)
        dialog.headertext.text=datas
        dateget.minDate=Startdate!!.time
        dateget?.setOnDateChangeListener { view, year, month, dayOfMonth ->

            Log.e("Data","...............${Date(view.date)}")
            date = "" + dayOfMonth + "/" + (month + 1) + "/" + year

            val simpledate=SimpleDateFormat("")

            val formatter = SimpleDateFormat("dd/MM/yyyy")

            val output = formatter.parse(date)

            val formatter1 = SimpleDateFormat("dd/MM/yyyy")

            val output1=formatter1.format(output)

            date_1=output

            Enddate=output

        }
        dialog.calendercan.setOnClickListener {

            dialog.dismiss()
        }
        dialog.calenderokbtn.setOnClickListener {
            //
            viewers?.durationdataformat?.setText("Untill")
            viewers?.durationdata?.setText(date)


            val formatter1 = SimpleDateFormat("yyyy-MM-dd")

            val startdate_view=formatter1.format(Startdate)

            val enddate_view=formatter1.format(Enddate)

            viewers?.durationhint?.setText("$startdate_view to $enddate_view")

            Log.e("End date","$Enddate")

            dialog.dismiss()
        }

        dialog.show()

        return null

    }

    fun dialogtimescountpicker(commandAdapter: Context, datas: String): String? {
        val dialog = Dialog(commandAdapter)
        Log.e("diloge","------------------------------------------>")
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dosage)
        var data="dabsbdjshjhsa"
        val name =dialog.findViewById<NumberPicker>(R.id.numberpicker)
        val header=dialog.findViewById<TextView>(R.id.headertextfordata)
        header.text = datas
        name.minValue=1
        name.maxValue=10
        dialog.calendercancel.setOnClickListener {
            viewers?.freqncey_format?.text = "Daily X times a day"
            viewers?.freqncey_data?.text = "$xtimeaday Times"
            dialog.dismiss()
        }

        name.setOnValueChangedListener { numberPicker, i, i2 ->
            Log.e("Picker","$i2")

            data= i2.toString()
        }
        dialog.calenderok.setOnClickListener {
            if(data==null)
            {
                xtimeaday="1"
                viewers?.freqncey_format?.text = "Daily X times a day"
                viewers?.freqncey_data?.text = "$xtimeaday Times"
                //durationdata.setText(data)
            }
            else{
                xtimeaday=data
                viewers?.freqncey_format?.text = "Daily X times a day"
                viewers?.freqncey_data?.text = xtimeaday
            }
            dialog.dismiss()
        }

        dialog.show()
        return null
    }

    fun dialoghourscountpicker(commandAdapter: Context, datas: String): String? {
        val dialog = Dialog(commandAdapter)
        Log.e("diloge","------------------------------------------>")
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dosage)
        var data="1 hours"
        val name =dialog.findViewById<NumberPicker>(R.id.numberpicker)
        val header=dialog.findViewById<TextView>(R.id.headertextfordata)
        val values = arrayOf("1 hours", "2 hours", "3 hours", "4 hours", "5 hours","6 hours","7 hours","8 hours","9 hours","10 hours","11 hours","12 hours")
        name.minValue = 0
        name.maxValue = values.size - 1
        name.displayedValues = values
        name.wrapSelectorWheel = true
        dialog.calendercancel.setOnClickListener {
            dialog.dismiss()
        }

        name.setOnValueChangedListener { numberPicker, i, i2 ->
            Log.e("Picker",""+(i2+1))
            data= (i2+1).toString()
        }
        dialog.calenderok.setOnClickListener {
            if(data==null)
            {
                xtimeaday="1"
                durationdataformat.setText("Duration")
                //durationdata.setText(data)
            }
            else{
                xtimeaday=data
            }
            dialog.dismiss()
        }

        dialog.show()
        return null
    }
    fun dialogtabletspicker(commandAdapter: Context, datas: String): String? {
        val dialog = Dialog(commandAdapter)
        Log.e("diloge","------------------------------------------>")
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dosage)
        var data="1"
        var data_view="1 Tablet"
        val name =dialog.findViewById<NumberPicker>(R.id.numberpicker)
        val header=dialog.findViewById<TextView>(R.id.headertextfordata)
        header.text = datas

        val values = arrayOf("1 Tablet", "2 Tablet (s)", "3 Tablet (s)", "4 Tablet (s)", "5 Tablet (s)","6 Tablet (s)","7 Tablet (s)","8 Tablet (s)","9 Tablet (s)","10 Tablet (s)")
        name.minValue = 0
        name.maxValue = values.size - 1
        name.displayedValues = values
        name.wrapSelectorWheel = true

        dialog.calendercancel.setOnClickListener {
            dialog.dismiss()
        }
        dialog.calenderok.setOnClickListener {
            dosagedata=data
            viewers?.dosagetext?.text = data_view
            dialog.dismiss()

        }
        name.setOnValueChangedListener { _, _, i2 ->
            Log.e("Picker","hours"+(i2+1))
            data=(i2+1).toString()
            data_view=values[i2]
        }
        dialog.show()
        return null
    }
    fun dialoghourreminderspicker(commandAdapter: Context, datas: String): String? {
        val dialog = Dialog(commandAdapter)
        Log.e("diloge","------------------------------------------>")
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dosage)
        var data="1"
        val name =dialog.findViewById<NumberPicker>(R.id.numberpicker)
        val header=dialog.findViewById<TextView>(R.id.headertextfordata)
        header.text = datas
        var status_data="1 hours"

        val values = arrayOf("1 hours", "2 hours", "3 hours", "4 hours", "5 hours","6 hours","7 hours","8 hours","9 hours","10 hours","11 hours","12 hours")

        name.minValue = 0
        name.maxValue = values.size - 1
        name.displayedValues = values
        name.wrapSelectorWheel = true

        dialog.calendercancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.calenderok.setOnClickListener {

            viewers?.remindereverytext?.setText(status_data)
            hoursreminderdata=data
            dialog.dismiss()
        }
        name.setOnValueChangedListener { _, _, i2 ->
            Log.e("Picker","hours"+(i2+1))
            data=(i2+1).toString()
            status_data=values[i2]
        }
        dialog.show()
        return null
    }




    fun firstintakepicker(commandAdapter: Context, datas: String): String? {
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
        var data_felid=""

        var check=true

        dialog.timecancel.setOnClickListener {
            dialog.dismiss()
        }
        dialog.timeok.setOnClickListener {

            if(check){

                var defult_hr=dialog.gettime.hour

                var defult_min=dialog.gettime.minute

                if(defult_hr>12){
                    day="pm"
                    hours =defult_hr- 12
                } else{
                    hours=defult_hr

                    if(defult_hr==12){

                        day="pm"

                    }
                    else{
                        day="am"
                    }


                }
                if(hours<10)
                {
                    hor="0"+hours
                }else
                {
                    hor=""+hours
                }

                if(defult_min<10)
                {
                    min  = "0$defult_min"
                }else
                {
                    min=""+defult_min
                }

                data="$hor:$min $day"

                firstintake=data
                viewers?.firstintaketext?.text = data
                dialog.dismiss()

            }
            else{

                firstintake=data

                viewers?.firstintaketext?.text = data

                dialog.dismiss()

            }


        }
        dialog.gettime.setOnTimeChangedListener { timePicker, hour, minute ->

            check=false

            if(hour > 12){

                day="pm"

                hours = hour- 12

            } else{
                hours = hour

                if(hours==12){

                    day="pm"

                }
                else{
                    day="am"
                }
            }
            if(hours < 10)
            {
                hor = "0"+hours
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

            Log.e("time", "Hour: "+ hor + " Minute : "+ min+"  am/pm " +day)
        }
        dialog.show()
        return null
    }
    fun lastintakepicker(commandAdapter: Context, datas: String): String? {
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

        var check=true

        dialog.timecancel.setOnClickListener {
            dialog.dismiss()
        }
        dialog.timeok.setOnClickListener {


            if(check){

                var defult_hr=dialog.gettime.hour

                var defult_min=dialog.gettime.minute

                if(defult_hr > 12){

                    day="pm"

                    hours =defult_hr- 12


                } else{

                    hours=defult_hr

                    if(defult_hr==12){

                        day="pm"

                    }
                    else{
                          day="am"
                    }


                }
                if(hours<10)
                {
                    hor="0"+hours
                }else
                {
                    hor=""+hours
                }

                if(defult_min<10)
                {
                    min  = "0$defult_min"
                }else
                {
                    min=""+defult_min
                }

                data="$hor:$min $day"

                lastintake=data

                viewers?.lastintaketext?.text = data

                dialog.dismiss()
            }
            else{

                lastintake=data

                viewers?.lastintaketext?.text = data

                dialog.dismiss()
            }

        }
        dialog.gettime.setOnTimeChangedListener { timePicker, hour, minute ->

            check=false

            if(hour>12){
                day="pm"
                hours =hour- 12
            } else{

                hours=hour

                if(hours==12){

                    day="pm"

                }
                else{

                    day="am"

                }

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

    fun dialogtodaycycle(commandAdapter: Context, datas: String): String? {
        val dialog = Dialog(commandAdapter)
        Log.e("diloge","------------------------------------------>")
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dosage)
        var data:String?=null
        val name =dialog.findViewById<NumberPicker>(R.id.numberpicker)
        val header=dialog.findViewById<TextView>(R.id.headertextfordata)
        header.text = datas
        name.minValue=1
        name.maxValue=90
        dialog.calendercancel.setOnClickListener {

            dialog.dismiss()
        }

        name.setOnValueChangedListener { numberPicker, i, i2 ->

            Log.e("Picker","$i2")

            data= i2.toString()

        }
        dialog.calenderok.setOnClickListener {
            if(data==null)
            {
                cycle="1"

                viewers?.todaycycletext?.setText(data+" Day")

                //durationdata.setText(data)
            }
            else{
                cycle=data
                viewers?.todaycycletext?.setText(data+" Days")

            }
            dialog.dismiss()
        }

        dialog.show()
        return null
    }





    @SuppressLint("SetTextI18n")
    fun dialogdayspicker(commandAdapter: Context, datas: String, type:Int): String? {
        val dialog = Dialog(commandAdapter)
        Log.e("diloge","------------------------------------------>")
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dosage)
        var data="1"
        var data_view="1 Days"
        val name  =dialog.findViewById<NumberPicker>(R.id.numberpicker)
        val header=dialog.findViewById<TextView>(R.id.headertextfordata)
        header.text = datas

        val values = arrayOf("1 Days", "2 Days", "3 Days", "4 Days", "5 Days","6 Days","7 Days","8 Days","9 Days","10 Days","11 Days","12 Days","13 Days","14 Days","15 Days")

        name.minValue = 0
        name.maxValue = values.size - 1
        name.displayedValues = values
        name.wrapSelectorWheel = true

        dialog.calendercancel.setOnClickListener {
            if (type==0){
                xday=data
                viewers?.freqncey_format?.text = "Every X day"
                viewers?.freqncey_data?.text = "$xday Days"
            }

            dialog.dismiss()
        }

        dialog.calenderok.setOnClickListener {
            if (type==0){
                xday=data
                viewers?.freqncey_format?.text = "Every X day"
                viewers?.freqncey_data?.text = data_view

            }
            else if(type==1){
                active=data
                viewers?.dayactiveverytext?.setText(data_view)
            }
            else if(type==2){
                cycle=data
                viewers?.todaycycletext?.setText(data_view)
            }
            else if(type==3){
                paused=data
                viewers?.Daypausedtext?.setText(data_view)
            }
            else if(type==4){

                val cal = Calendar.getInstance()

                cal . setTime (Startdate)

                cal . add (Calendar.DAY_OF_YEAR, data.toInt())

                Enddate=cal.time

                viewers?.durationdataformat?.setText("For X days")

                viewers?.durationdata?.setText(data_view)

                val formatter1 = SimpleDateFormat("dd/MM/yyyy")

                val startdate_view=formatter1.format(Startdate)

                val enddate_view=formatter1.format(Enddate)

                viewers?.durationhint?.setText("$startdate_view to $enddate_view")
                Log.e("Startdate","$Startdate")

                //Startdate
            }
            dialog.dismiss()

        }
        name.setOnValueChangedListener { _, _, i2 ->
            Log.e("Picker","hours"+(i2+1))
            data=(i2+1).toString()
            data_view=values[i2]
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

        var check=true




        dialog.timecancel.setOnClickListener {
            dialog.dismiss()
        }
        dialog.timeok.setOnClickListener {

            if(check){

                var defult_hr=dialog.gettime.hour

                var defult_min=dialog.gettime.minute

                if(defult_hr>12){
                    day="pm"
                    hours =defult_hr- 12
                } else{
                    hours=defult_hr

                    if(hours==12){

                        day="pm"

                    }
                    else{
                        day="am"
                    }

                }
                if(hours<10)
                {
                    hor="0"+hours
                }else
                {
                    hor=""+hours
                }

                if(defult_min<10)
                {
                    min  = "0$defult_min"
                }else
                {
                    min=""+defult_min
                }

                data="$hor:$min $day"

          //      reminderInsert(data)

                dialog.dismiss()
            }
            else{

               // reminderInsert(data)
                dialog.dismiss()
            }

        }


        dialog.gettime.setOnTimeChangedListener { timePicker, hour, minute ->

            check=false
            if(hour>12){
                day="pm"
                hours =hour- 12
            } else{
                hours=hour

                if(hours==12){

                    day="pm"

                }
                else{
                    day="am"
                }

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

    private fun reminderInsert(data: Date) {

        AsyncTask.execute {
            var userId by stringPref("userId", null)
            var petid by stringPref("petid", null)
            var Id= id?.toLong()
            try {
                val callDetails = splootDB!!.petMasterDao()
                Log.e("check data","statedatre = $id,petid=$petid,userid:$data")

                val insertdata= alarmdata(
                    userId = userId,
                    petId = petid,
                    startdate = Startdate,
                    endate = Enddate,
                    time =  data,
                    active = "2"
                )
                val insert_function=callDetails.insertalaramdata(insertdata)
                val getdata=callDetails.getdataalarm(userId!!, petid!!,"2")
                recyclerView?.post(Runnable {
                    recyclerView?.layoutManager = LinearLayoutManager(viewers?.context)
                    val adapter = CustomAdapter(getdata)
                    recyclerView?.adapter = adapter
                })


                Log.e("Reminder table data","$getdata")
            } catch (e: Exception) {
                val s = e.message;
                Log.e("Errors",s)
                // alarmid="0123"
            }
        }
    }



    fun insert_date(commandAdapter: Context): String? {
        val dialog = Dialog(commandAdapter)
        Log.e("diloge","------------------------------------------>")
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.time_picker)

        var date_pickes:Date?=null

        var datav= SingleDateAndTimePickerDialog.Builder(commandAdapter)
            .bottomSheet()
            .curved()
            .displayMinutes(false)
            .displayHours(false)
            .displayDays(false)
            .displayMonth(false)
            .displayYears(true)
            .displayListener(SingleDateAndTimePickerDialog.DisplayListener { picker ->

            }).listener { date: Date? ->

                date_pickes=date
                Log.e("date","$date")

            }.display()

        dialog.time_ok.setOnClickListener {

            Log.e("selectdate","$date_pickes.")

        }

        dialog.show()
        return null
    }

}
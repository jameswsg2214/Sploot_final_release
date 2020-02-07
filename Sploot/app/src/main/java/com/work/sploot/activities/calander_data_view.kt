package com.work.sploot.activities


import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.work.sploot.Entity.madicineType
import com.work.sploot.R
import com.work.sploot.SplootAppDB
import com.work.sploot.data.stringPref
import com.work.sploot.data.taskviewdata
import kotlinx.android.synthetic.main.calander_views_data.view.*
import kotlinx.android.synthetic.main.calender_data_recycler.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class calander_data_view:Fragment() {

    private var splootDB: SplootAppDB? = null

    var localview:View?=null

    var recyclerView1:RecyclerView?=null
    var recyclerView2:RecyclerView?=null
    var recyclerView3:RecyclerView?=null

    var nodata1:TextView?=null
    var nodata2:TextView?=null
    var nodata3:TextView?=null

    var frag:FragmentActivity?=null

    companion object {
        var dates:String?=null
        fun newInstance(date:String): calander_data_view {
            dates=date
            return calander_data_view()
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val views=inflater.inflate(R.layout.calander_views_data, container, false)
        //views.check_data.text= dates
        splootDB = SplootAppDB.getInstance(views.context)
        localview=views
        recyclerView1= views.findViewById<RecyclerView>(R.id.vaccination_recycler)
        recyclerView2= views.findViewById<RecyclerView>(R.id.deworming_recycler)
        recyclerView3= views.findViewById<RecyclerView>(R.id.other_recycler)

        nodata1=views.findViewById(R.id.vaccination_nodata)

        nodata2=views.findViewById(R.id.deworming_nodata)

        nodata3=views.findViewById(R.id.others_nodata)



        frag = activity


        var isvaccinationclick=true
//
        views.vaccination_layout.setOnClickListener {
            if(isvaccinationclick){
                isvaccinationclick=false
            //    dataprocess("1")
                views.vaccination_view.visibility=View.VISIBLE
                dataprocess(1)
            }
            else{
                isvaccinationclick=true
                views.vaccination_view.visibility=View.GONE
            }
        }
        var isclick2=true
        views.deworming_layout.setOnClickListener {
            if(isclick2){
                isclick2=false
                views.demorming_view.visibility=View.VISIBLE
                dataprocess(2)
            }
            else{
                isclick2=true
                views.demorming_view.visibility=View.GONE
            }

        }
        var isclick3=true
        views.other_layout.setOnClickListener {
            if(isclick3){
                isclick3=false

                dataprocess(3)
                views.other_view.visibility=View.VISIBLE
            }
            else{
                isclick3=true
                views.other_view.visibility=View.GONE
            }

        }
        var isclick4=true

        views.weight_layout.setOnClickListener {

            if(isclick4)
            {
                isclick4=false

                views.weight_view.visibility=View.VISIBLE


                AsyncTask.execute {

                    var userId by stringPref("userId", null)

                    var petid by stringPref("petid", null)

                    var calenderdate by stringPref("calenderdate", null)

                    Log.e("get date",calenderdate)

                    val formatter = SimpleDateFormat("dd/MM/yyyy")

                    val start = formatter.parse(calenderdate)

                    try {
                        val callDetails = splootDB!!.petMasterDao()

                        val getall=callDetails.weightdataall()
                        Log.e("Weight","$getall")

                        val check=callDetails.checkweight(petid!!,userId!!,start)

                        if(check){

                            val get=callDetails.getweight(petid!!,userId!!,start)

                            Log.e("weightdata","$get")



                            views.weight_nodata.post(Runnable {
                                views.weight_nodata.visibility=View.GONE
                            })

                            views.post(Runnable {
                                views.weight_data.visibility=View.VISIBLE
                                views.weight_data.text = get.weight+"kg"
                            })


                        }
                        else{
                            Log.e("no data","in date $calenderdate")


                            views.weight_nodata.post(Runnable {
                                views.weight_nodata.visibility=View.VISIBLE
                            })

                            views.weight_data.post(Runnable {
                                views.weight_data.visibility=View.GONE
                            })

                        }


                    } catch (e: Exception) {
                        val s = e.message
                        Log.e("Error list",s)
                    }
                }

            }
            else{
                isclick4=true

                views.weight_view.visibility=View.GONE
            }

        }


        var isclick5=true

        views.note_layout.setOnClickListener{
            if(isclick5)
            {
                isclick5=false
                AsyncTask.execute {
                    var userId by stringPref("userId", null)

                    var petid by stringPref("petid", null)

                    var calenderdate by stringPref("calenderdate", null)

                    Log.e("sharedate",calenderdate)

                    val formatter = SimpleDateFormat("dd/MM/yyyy")

                    val start = formatter.parse(calenderdate)

                    try {
                        val callDetails = splootDB!!.petMasterDao()

                        val getall=callDetails.getother()
                        Log.e("list","$getall")

                        val getdata=callDetails.get_others(start, userId!!.toInt(),petid!!)

                        Log.e("others","$getdata")

                        if (getdata!=null) {

                            views.note.post(Runnable {
                                views.note.text = getdata.other
                            })

                            views.note_view.post(Runnable {

                                views.note_view.visibility=View.VISIBLE

                            })

                            views.note_nodata.post(Runnable {
                                views.note_nodata.visibility=View.GONE
                            })



                        }
                        else{

                            views.note_view.post(Runnable {

                                views.note_view.visibility=View.VISIBLE

                            })

                            views.note_nodata.post(Runnable {
                                views.note_nodata.visibility=View.VISIBLE
                            })

                        }
                    } catch (e: Exception) {
                        val s = e.message
                        Log.e("Error list",s)
                    }
                }

            }
            else{
                isclick5=true
                views.note_view.visibility=View.GONE

            }

        }

        views.photo_layout_view.setOnClickListener {


            var select_date by stringPref("select_date", null)

            var calenderdate by stringPref("calenderdate", null)

            select_date=calenderdate


                try {

                    val mContext = frag

                    val fm = mContext?.supportFragmentManager


                    val transaction = fm?.beginTransaction()

                    transaction?.replace(
                        R.id.view_pager,
                        photo_update_fragement.newInstance(2, calenderdate)
                    )


                    //   transaction?.

                    transaction?.addToBackStack(null)

                    transaction?.commit()

                } catch (e: Exception) {


                }

        }
        return views

    }

    private fun dataprocess(type:Int) {


        AsyncTask.execute {

            var userId by stringPref("userId", null)

            var petid by stringPref("petid", null)

            var calenderdate by stringPref("calenderdate", null)

            Log.e("sharedate",calenderdate)

            val formatter = SimpleDateFormat("dd/MM/yyyy")

            val start = formatter.parse(calenderdate)

            val end = formatter.parse(calenderdate)

            var user= userId?.toInt()

            var petId= petid?.toLong()

            try {
                val callDetails = splootDB!!.petMasterDao()

              //  val viewdata=callDetails.getcatmonth_check(start!!,end!!,userId!!,petid!!)


                var datalist1=ArrayList<madicineType>()

                val viewdata=callDetails.cal_check(start!!,end!!,userId!!,petid!!)

                if(viewdata){


                    Log.e("responce"," data correct")

                    if(type==1){

                        val viewdata=callDetails.cal_get_click(1,petid!!,userId!!,start)

                        if(viewdata){

                            val viewdatas=callDetails.cal_get(1,petid!!,userId!!,start)

                                Log.e("reponce","$viewdatas")

                                Log.e("length",""+viewdatas.size + viewdatas)

                                var data_cke=true

                                for(i in 0 ..viewdatas.size-1) {

                                    Log.e(
                                        "date size",
                                        ""+i
                                    )

                                    if(viewdatas[i].userId==userId){

                                        if(viewdatas[i].petId==petid){

                                            if (viewdatas[i].start_date!! <= start!! ) {

                                                Log.e(
                                                    "start date if",
                                                    "${viewdatas[i].start_date}..............${viewdatas[i].end_date}."
                                                )

                                                if(viewdatas[i].end_date!! >= end!!) {

                                                    if (viewdatas[i].cat_type!=4) {

                                                        if (viewdatas[i].repeat_type == 1) {

                                                            val status=callDetails.check_alaram_status(viewdatas[i].allTypeId!!,start)




                                                            if(status){

                                                                val data=callDetails.get_alaram_status(viewdatas[i].allTypeId!!,start)

                                                                viewdatas[i].active=data.active

                                                            }

                                                            datalist1.add(viewdatas[i])

                                                            data_cke=false


                                                        }
                                                        else if (viewdatas[i].repeat_type == 2) {

                                                            val status=callDetails.check_alaram_status(viewdatas[i].allTypeId!!,start)




                                                            if(status){

                                                                val data=callDetails.get_alaram_status(viewdatas[i].allTypeId!!,start)

                                                                viewdatas[i].active=data.active

                                                            }

                                                            datalist1.add(viewdatas[i])

                                                            data_cke=false


                                                        }
                                                        else if (viewdatas[i].repeat_type == 3) {

                                                            val status=callDetails.check_alaram_status(viewdatas[i].allTypeId!!,start)

                                                            if(status){

                                                                val data=callDetails.get_alaram_status(viewdatas[i].allTypeId!!,start)

                                                                viewdatas!![i].active=data.active

                                                            }

                                                            datalist1.add(viewdatas!![i])

                                                            data_cke=false


                                                        }
                                                        else if (viewdatas[i].repeat_type == 4) {

                                                            if(viewdatas[i].start_date!!.day==start.day){

                                                                val status=callDetails.check_alaram_status(viewdatas[i].allTypeId!!,start)




                                                                if(status){

                                                                    val data=callDetails.get_alaram_status(viewdatas[i].allTypeId!!,start)

                                                                    viewdatas!![i].active=data.active

                                                                }

                                                                datalist1.add(viewdatas!![i])

                                                                data_cke=false

                                                            }



                                                        }
                                                        else if (viewdatas[i].repeat_type == 5) {

                                                            if(viewdatas[i].start_date==start){

                                                                val status=callDetails.check_alaram_status(viewdatas[i].allTypeId!!,start)

                                                                if(status){

                                                                    val data=callDetails.get_alaram_status(viewdatas[i].allTypeId!!,start)

                                                                    viewdatas!![i].active=data.active

                                                                }

                                                                datalist1.add(viewdatas!![i])

                                                                data_cke=false

                                                            }
                                                            else{

                                                                var select_view_data=start.date-viewdatas[i].start_date!!.date

                                                                val week=fun_of_week(start,viewdatas[i].start_date)


                                                                if((week%2==0) && (start.day == viewdatas[i].start_date!!.day)){
                                                                    val status=callDetails.check_alaram_status(viewdatas[i].allTypeId!!,start)




                                                                    if(status){

                                                                        val data=callDetails.get_alaram_status(viewdatas[i].allTypeId!!,start)

                                                                        viewdatas[i].active=data.active

                                                                    }

                                                                    datalist1.add(viewdatas[i])

                                                                    data_cke=false

                                                                }
                                                            }
                                                        }
                                                        else if (viewdatas[i].repeat_type == 6) {

                                                            if(viewdatas[i].start_date!!.date==start.date){

                                                                val status=callDetails.check_alaram_status(viewdatas[i].allTypeId!!,start)




                                                                if(status){

                                                                    val data=callDetails.get_alaram_status(viewdatas[i].allTypeId!!,start)

                                                                    viewdatas!![i].active=data.active

                                                                }

                                                                datalist1.add(viewdatas!![i])

                                                                data_cke=false


                                                            }


                                                        }
                                                        else if (viewdatas[i].repeat_type == 7) {


                                                            var select_view_data=start.month-viewdatas[i].start_date!!.month

                                                            //   if(select_view_data%3==0){

                                                            val months=fun_of_month(start,viewdatas[i].start_date!!)



                                                            if((months %3 ==0) && (viewdatas[i].start_date!!.date == start!!.date)){


                                                                val status=callDetails.check_alaram_status(viewdatas[i].allTypeId!!,start)




                                                                if(status){

                                                                    val data=callDetails.get_alaram_status(viewdatas[i].allTypeId!!,start)

                                                                    viewdatas!![i].active=data.active

                                                                }
                                                                datalist1.add(viewdatas!![i])

                                                                data_cke=false

                                                            }

                                                        }
                                                        else if (viewdatas[i].repeat_type == 8) {


                                                           // var select_view_data=start.month-viewdatas[i].start_date!!.month

                                                            val months=fun_of_month(start,viewdatas[i].start_date!!)

                                                            if((months%6==0 )&& (viewdatas[i].start_date!!.date == start!!.date)){


                                                                val status=callDetails.check_alaram_status(viewdatas[i].allTypeId!!,start)




                                                                if(status){

                                                                    val data=callDetails.get_alaram_status(viewdatas[i].allTypeId!!,start)

                                                                    viewdatas!![i].active=data.active

                                                                }

                                                                datalist1.add(viewdatas!![i])

                                                                data_cke=false

                                                            }



                                                        }
                                                        else if (viewdatas[i].repeat_type == 9) {

                                                            if((viewdatas[i].start_date!!.date == start.date) && (viewdatas[i].start_date!!.month==start.month) ){


                                                                val status=callDetails.check_alaram_status(viewdatas[i].allTypeId!!,start)




                                                                if(status){

                                                                    val data=callDetails.get_alaram_status(viewdatas[i].allTypeId!!,start)

                                                                    viewdatas!![i].active=data.active

                                                                }

                                                                datalist1.add(viewdatas!![i])

                                                                data_cke=false

                                                            }

                                                        }

                                                        else if (viewdatas[i].repeat_type == 10) {

                                                            if(viewdatas[i].frequency_type_id == 1){

                                                             //   val data_responce=(start.date-viewdatas[i].start_date!!.date)

                                                                val valide=  start.time  - viewdatas[i].start_date!!.time

                                                                val data_responce = (valide / (1000 * 3600 * 24)).toInt()

                                                                Log.e("responce_data","$data_responce....  ${start.date}..... ${viewdatas[i].start_date!!.date} ")

                                                                if( (data_responce) % ((viewdatas[i].every_frequency!!)) == 0){


                                                                    Log.e("responce_if_if","$data_responce....  ${start.date}..... ${viewdatas[i].start_date!!.date} ")

                                                                    Log.e("responce_freq","${viewdatas[i].every_frequency!!}")

                                                                    val status=callDetails.check_alaram_status(viewdatas[i].allTypeId!!,start)




                                                                    if(status){

                                                                        val data=callDetails.get_alaram_status(viewdatas[i].allTypeId!!,start)

                                                                        viewdatas!![i].active=data.active

                                                                    }

                                                                    datalist1.add(viewdatas!![i])

                                                                    data_cke=false



                                                                }

                                                                else{

                                                                    Log.e("responce_if_else","$data_responce....  ${start.date}..... ${viewdatas[i].start_date!!.date} ")
                                                                }

                                                            }

                                                            else if(viewdatas[i].frequency_type_id==2){

                                                                var cal =Calendar.getInstance()

                                                                cal.set(start.year,start.month,start.date)

                                                                val i1=cal.get(Calendar.WEEK_OF_YEAR)

                                                                Log.e("validate","data $i1")


                                                                var cal2 =Calendar.getInstance()

                                                                // val yeat=viewdatas


                                                                cal2.set(viewdatas[i].start_date!!.year,viewdatas[i].start_date!!.month,viewdatas[i].start_date!!.date)

//                                                            viewsdata[i].startdate!!.year,viewsdata[i].startdate!!.month,viewsdata[i].startdate!!.date

                                                                val i2=cal2.get(Calendar.WEEK_OF_YEAR)

                                                                Log.e("validate","Startdate $i2")

                                                                val minu_data=i1-i2


                                                                //  Log.e("validate","Startdate $i2  currwent date week $i1 $minu_data")

                                                                /*  var cal =Calendar.getInstance()

                                                                  cal.set(start.year,start.month,start.date)

                                                                  val i1=cal.get(Calendar.WEEK_OF_YEAR)

                                                                  var cal2 =Calendar.getInstance()

                                                                  cal2.set(viewsdata[i].startdate!!.year,viewsdata[i].startdate!!.month,viewsdata[i].startdate!!.date)

                                                                  val i2=cal2.get(Calendar.WEEK_OF_YEAR)

                                                                  Log.e("Week_day","$i1   $i2 $minu_data")*/

                                                           //     val week=fun_of_week(start,viewdatas[i].start_date)

                                                                val week=fun_of_week1(start,viewdatas[i].start_date)

                                                                if(week % (viewdatas[i].every_frequency!!) == 0){

                                                                    Log.e("validate","Startdate $i2 ${viewdatas[i].start_date}  currwent date week $i1 $start.... $minu_data ${(viewdatas[i].every_frequency!! + 1)}  ")

                                                                    val simpleDateformat = SimpleDateFormat("EEEE")

                                                                    val day = simpleDateformat.format(start)

                                                                    Log.e("dat1234", "" + start.date + "-----------" + day)

                                                                    if (viewdatas[i].selective_week!!.contains(day, ignoreCase = true)) {

                                                                        val status=callDetails.check_alaram_status(viewdatas[i].allTypeId!!,start)




                                                                        if(status){

                                                                            val data=callDetails.get_alaram_status(viewdatas[i].allTypeId!!,start)

                                                                            viewdatas!![i].active=data.active

                                                                        }

                                                                        datalist1.add(viewdatas!![i])

                                                                        data_cke=false

                                                                    }

                                                                }

                                                            }

                                                            else if(viewdatas[i].frequency_type_id==3){

                                                                val months=fun_of_month(start,viewdatas[i].start_date!!)

                                                                if(viewdatas[i].start_date==start){

                                                                    val status=callDetails.check_alaram_status(viewdatas[i].allTypeId!!,start)




                                                                    if(status){

                                                                        val data=callDetails.get_alaram_status(viewdatas[i].allTypeId!!,start)

                                                                        viewdatas[i].active=data.active

                                                                    }
                                                                    datalist1.add(viewdatas[i])

                                                                    data_cke=false
                                                                }
                                                                else if(viewdatas[i].start_date!!.date==start.date)
                                                                {

                                                                    if (((months+1) % (viewdatas[i].every_frequency!!) == 0)) {


                                                                        val status=callDetails.check_alaram_status(viewdatas[i].allTypeId!!,start)




                                                                        if(status){

                                                                            val data=callDetails.get_alaram_status(viewdatas[i].allTypeId!!,start)

                                                                            viewdatas[i].active=data.active

                                                                        }

                                                                        datalist1.add(viewdatas!![i])

                                                                        data_cke=false

                                                                    }

                                                                }
                                                                else {


                                                                }

                                                            }

                                                            else if(viewdatas[i].frequency_type_id==4)
                                                            {

                                                                val formater = SimpleDateFormat("yyyy")

                                                                val current_year=(formater.format(start)).toInt()

                                                                val start_year=(formater.format(viewdatas[i].start_date)).toInt()

                                                                if(start.date==viewdatas[i].start_date!!.date && start.month==viewdatas[i].start_date!!.month && start_year==current_year){

                                                                    Log.e("responedata","$start ...${viewdatas[i].every_frequency}")

                                                                    val status=callDetails.check_alaram_status(viewdatas[i].allTypeId!!,start)




                                                                    if(status){

                                                                        val data=callDetails.get_alaram_status(viewdatas[i].allTypeId!!,start)

                                                                        viewdatas!![i].active=data.active

                                                                    }

                                                                    datalist1.add(viewdatas!![i])

                                                                    data_cke=false

                                                                }
                                                                else if(start.date==viewdatas[i].start_date!!.date && start.month==viewdatas[i].start_date!!.month){

                                                                    Log.e("responedata","$start")

                                                                    if((current_year-start_year) % (viewdatas[i].every_frequency!!) == 0){

                                                                        Log.e("responedata","view $start")

                                                                        val status=callDetails.check_alaram_status(viewdatas[i].allTypeId!!,start)




                                                                        if(status){

                                                                            val data=callDetails.get_alaram_status(viewdatas[i].allTypeId!!,start)

                                                                            viewdatas!![i].active=data.active

                                                                        }
                                                                        datalist1.add(viewdatas!![i])

                                                                        data_cke=false

                                                                    }

                                                                }

                                                            }

                                                        }
                                                    }
                                                    else{

                                                        val valide=  start.time  - viewdatas[i].start_date!!.time

                                                        val days = valide / (1000 * 3600 * 24)

                                                        if ((days.toInt()) % (viewdatas[i].repeat_type!!) == 0) {

                                                            val status=callDetails.check_alaram_status(viewdatas[i].allTypeId!!,start)




                                                            if(status){

                                                                val data=callDetails.get_alaram_status(viewdatas[i].allTypeId!!,start)

                                                                viewdatas!![i].active=data.active

                                                            }

                                                            datalist1.add(viewdatas!![i])


                                                        }
                                                    }
                                                }

                                            } else {
                                                Log.e(
                                                    "date else",
                                                    "${viewdatas[i].start_date}..............${viewdatas[i].end_date}."
                                                )

                                            }
                                        }


                                    }


                                }



                            recyclerView1!!.post(Runnable {

                                recyclerView1!!.visibility=View.VISIBLE

                                recyclerView1!!.layoutManager = LinearLayoutManager(localview?.context)
                                val adapter = calanderAdapter(datalist1, frag)
                                recyclerView1!!.adapter = adapter
                            })

                            if(data_cke) {

                                recyclerView1!!.post(Runnable {

                                    recyclerView1!!.visibility=View.GONE

                                })

                                nodata1!!.post(Runnable {

                                    nodata1!!.visibility=View.VISIBLE

                                })

                            }

                            else{
                                nodata1!!.post(Runnable {

                                    nodata1!!.visibility=View.GONE

                                })

                            }


                        }
                        else{

                            recyclerView1!!.post(Runnable {

                                recyclerView1!!.visibility=View.GONE

                            })

                            nodata1!!.post(Runnable {
                                nodata1!!.visibility=View.VISIBLE

                            })

                        }

                    }
                    else if(type==2){


                        val viewdata=callDetails.cal_get_click(4,petid!!,userId!!,start)

                        if(viewdata){

                            val viewdatas=callDetails.cal_get(4,petid!!,userId!!,start)

                            Log.e("reponce","$viewdatas")


                            Log.e("length",""+viewdatas.size + viewdatas)


                            var data_cke=true

                            for(i in 0 ..viewdatas.size-1) {

                                Log.e(
                                    "date size",
                                    ""+i
                                )

                                if(viewdatas[i].userId==userId){

                                    if(viewdatas[i].petId==petid){

                                        if (viewdatas[i].start_date!! <= start!! ) {

                                            Log.e(
                                                "start date if",
                                                "${viewdatas[i].start_date}..............${viewdatas[i].end_date}."
                                            )

                                            if(viewdatas[i].end_date!! >= end!!) {

                                                val valide=  start.time  - viewdatas[i].start_date!!.time

                                                val days = valide / (1000 * 3600 * 24)

                                                if ((days.toInt()) % (viewdatas[i].repeat_type!!) == 0) {

                                                    val status=callDetails.check_alaram_status(viewdatas[i].allTypeId!!,start)




                                                    if(status){

                                                        val data=callDetails.get_alaram_status(viewdatas[i].allTypeId!!,start)

                                                        viewdatas!![i].active=data.active

                                                    }

                                                    datalist1.add(viewdatas!![i])

                                                    data_cke = false

                                                }

                                            }

                                        } else {
                                            Log.e(
                                                "date else",
                                                "${viewdatas[i].start_date}..............${viewdatas[i].end_date}."
                                            )

                                        }
                                    }


                                }


                            }

                            recyclerView2!!.post(Runnable {

                                recyclerView2!!.visibility=View.VISIBLE

                                recyclerView2!!.layoutManager = LinearLayoutManager(localview?.context)

                                val adapter = calanderAdapter(datalist1,frag)

                                recyclerView2!!.adapter = adapter

                            })

                            if(data_cke){

                                recyclerView2!!.post(Runnable {
                                    recyclerView2!!.visibility=View.GONE

                                })

                                nodata2!!.post(Runnable {

                                    nodata2!!.visibility=View.VISIBLE

                                })

                            }
                            else{

                                nodata2!!.post(Runnable {

                                    nodata2!!.visibility=View.GONE

                                })

                            }







                        }
                        else{

                            recyclerView2!!.post(Runnable {
                                recyclerView2!!.visibility=View.GONE

                            })

                            nodata2!!.post(Runnable {
                                nodata2!!.visibility=View.VISIBLE

                            })

                        }
                    }
                    else{

                        val viewdata=callDetails.cal_get_click(2,petid!!,userId!!,start)

                        if(viewdata){
                            val viewdatas=callDetails.cal_get(2,petid!!,userId!!,start)

                            Log.e("reponce","$viewdatas")

                            var data_cke=true

                            Log.e("length",""+viewdatas.size + viewdatas)

                            for(i in 0 ..viewdatas.size-1){

                                Log.e(
                                    "date size",
                                    ""+i
                                )

                                if(viewdatas[i].userId==userId){

                                    if(viewdatas[i].petId==petid){

                                        if (viewdatas[i].start_date!! <= start!! ) {

                                            Log.e(
                                                "start date if",
                                                "${viewdatas[i].start_date}..............${viewdatas[i].end_date}."
                                            )

                                            if(viewdatas[i].end_date!! >= end!!) {

                                                if (viewdatas[i].cat_type!=4) {

                                                    if (viewdatas[i].repeat_type == 1) {

                                                        val status=callDetails.check_alaram_status(viewdatas[i].allTypeId!!,start)




                                                        if(status){

                                                            val data=callDetails.get_alaram_status(viewdatas[i].allTypeId!!,start)

                                                            viewdatas!![i].active=data.active

                                                        }

                                                        datalist1.add(viewdatas!![i])

                                                        data_cke=false


                                                    }
                                                    else if (viewdatas[i].repeat_type == 2) {

                                                        val status=callDetails.check_alaram_status(viewdatas[i].allTypeId!!,start)




                                                        if(status){

                                                            val data=callDetails.get_alaram_status(viewdatas[i].allTypeId!!,start)

                                                            viewdatas!![i].active=data.active

                                                        }

                                                        datalist1.add(viewdatas!![i])

                                                        data_cke=false


                                                    }
                                                    else if (viewdatas[i].repeat_type == 3) {

                                                        val status=callDetails.check_alaram_status(viewdatas[i].allTypeId!!,start)




                                                        if(status){

                                                            val data=callDetails.get_alaram_status(viewdatas[i].allTypeId!!,start)

                                                            viewdatas!![i].active=data.active

                                                        }

                                                        datalist1.add(viewdatas!![i])

                                                        data_cke=false


                                                    }
                                                    else if (viewdatas[i].repeat_type == 4) {

                                                        if(viewdatas[i].start_date!!.day==start.day){

                                                            val status=callDetails.check_alaram_status(viewdatas[i].allTypeId!!,start)




                                                            if(status){

                                                                val data=callDetails.get_alaram_status(viewdatas[i].allTypeId!!,start)

                                                                viewdatas!![i].active=data.active

                                                            }

                                                            datalist1.add(viewdatas!![i])

                                                            data_cke=false

                                                        }



                                                    }
                                                    else if (viewdatas[i].repeat_type == 5) {

                                                        if(viewdatas[i].start_date==start){

                                                            val status=callDetails.check_alaram_status(viewdatas[i].allTypeId!!,start)




                                                            if(status){

                                                                val data=callDetails.get_alaram_status(viewdatas[i].allTypeId!!,start)

                                                                viewdatas!![i].active=data.active

                                                            }

                                                            datalist1.add(viewdatas!![i])

                                                            data_cke=false

                                                        }
                                                        else{

                                                            var select_view_data=start.date-viewdatas[i].start_date!!.date

                                                            val week=fun_of_week(start,viewdatas[i].start_date)


                                                            if((week%2==0) && (start.day == viewdatas[i].start_date!!.day)){
                                                                val status=callDetails.check_alaram_status(viewdatas[i].allTypeId!!,start)




                                                                if(status){

                                                                    val data=callDetails.get_alaram_status(viewdatas[i].allTypeId!!,start)

                                                                    viewdatas!![i].active=data.active

                                                                }

                                                                datalist1.add(viewdatas!![i])

                                                                data_cke=false

                                                            }
                                                        }
                                                    }
                                                    else if (viewdatas[i].repeat_type == 6) {

                                                        if(viewdatas[i].start_date!!.date==start.date){

                                                            val status=callDetails.check_alaram_status(viewdatas[i].allTypeId!!,start)




                                                            if(status){

                                                                val data=callDetails.get_alaram_status(viewdatas[i].allTypeId!!,start)

                                                                viewdatas!![i].active=data.active

                                                            }

                                                            datalist1.add(viewdatas!![i])

                                                            data_cke=false


                                                        }


                                                    }
                                                    else if (viewdatas[i].repeat_type == 7) {


                                                        var select_view_data=start.month-viewdatas[i].start_date!!.month

                                                        //   if(select_view_data%3==0){

                                                        val months=fun_of_month(start,viewdatas[i].start_date!!)



                                                        if((months %3 ==0) && (viewdatas[i].start_date!!.date == start!!.date)){


                                                            val status=callDetails.check_alaram_status(viewdatas[i].allTypeId!!,start)




                                                            if(status){

                                                                val data=callDetails.get_alaram_status(viewdatas[i].allTypeId!!,start)

                                                                viewdatas!![i].active=data.active

                                                            }
                                                            datalist1.add(viewdatas!![i])

                                                            data_cke=false

                                                        }

                                                    }
                                                    else if (viewdatas[i].repeat_type == 8) {


                                                        var select_view_data=start.month-viewdatas[i].start_date!!.month

                                                        val months=fun_of_month(start,viewdatas[i].start_date!!)

                                                        if((months%6==0 )&& (viewdatas[i].start_date!!.date == start!!.date)){


                                                            val status=callDetails.check_alaram_status(viewdatas[i].allTypeId!!,start)




                                                            if(status){

                                                                val data=callDetails.get_alaram_status(viewdatas[i].allTypeId!!,start)

                                                                viewdatas!![i].active=data.active

                                                            }

                                                            datalist1.add(viewdatas!![i])

                                                            data_cke=false

                                                        }



                                                    }
                                                    else if (viewdatas[i].repeat_type == 9) {

                                                        if((viewdatas[i].start_date!!.date == start.date) && (viewdatas[i].start_date!!.month==start.month) ){


                                                            val status=callDetails.check_alaram_status(viewdatas[i].allTypeId!!,start)




                                                            if(status){

                                                                val data=callDetails.get_alaram_status(viewdatas[i].allTypeId!!,start)

                                                                viewdatas!![i].active=data.active

                                                            }

                                                            datalist1.add(viewdatas!![i])

                                                            data_cke=false

                                                        }

                                                    }

                                                    else if (viewdatas[i].repeat_type == 10) {

                                                        if(viewdatas[i].frequency_type_id == 1){

                                                            //   val data_responce=(start.date-viewdatas[i].start_date!!.date)

                                                            val valide=  start.time  - viewdatas[i].start_date!!.time

                                                            val data_responce = (valide / (1000 * 3600 * 24)).toInt()

                                                            Log.e("responce_data","$data_responce....  ${start.date}..... ${viewdatas[i].start_date!!.date} ")

                                                            if( (data_responce) % ((viewdatas[i].every_frequency!!)) == 0){


                                                                Log.e("responce_if_if","$data_responce....  ${start.date}..... ${viewdatas[i].start_date!!.date} ")

                                                                Log.e("responce_freq","${viewdatas[i].every_frequency!!}")

                                                                val status=callDetails.check_alaram_status(viewdatas[i].allTypeId!!,start)




                                                                if(status){

                                                                    val data=callDetails.get_alaram_status(viewdatas[i].allTypeId!!,start)

                                                                    viewdatas!![i].active=data.active

                                                                }

                                                                datalist1.add(viewdatas!![i])

                                                                data_cke=false



                                                            }

                                                            else{

                                                                Log.e("responce_if_else","$data_responce....  ${start.date}..... ${viewdatas[i].start_date!!.date} ")
                                                            }

                                                        }

                                                        else if(viewdatas[i].frequency_type_id==2){

                                                            var cal =Calendar.getInstance()

                                                            cal.set(start.year,start.month,start.date)

                                                            val i1=cal.get(Calendar.WEEK_OF_YEAR)

                                                            Log.e("validate","data $i1")


                                                            var cal2 =Calendar.getInstance()

                                                            // val yeat=viewdatas


                                                            cal2.set(viewdatas[i].start_date!!.year,viewdatas[i].start_date!!.month,viewdatas[i].start_date!!.date)

//                                                            viewsdata[i].startdate!!.year,viewsdata[i].startdate!!.month,viewsdata[i].startdate!!.date

                                                            val i2=cal2.get(Calendar.WEEK_OF_YEAR)

                                                            Log.e("validate","Startdate $i2")

                                                            val minu_data=i1-i2


                                                            //  Log.e("validate","Startdate $i2  currwent date week $i1 $minu_data")

                                                            /*  var cal =Calendar.getInstance()

                                                              cal.set(start.year,start.month,start.date)

                                                              val i1=cal.get(Calendar.WEEK_OF_YEAR)

                                                              var cal2 =Calendar.getInstance()

                                                              cal2.set(viewsdata[i].startdate!!.year,viewsdata[i].startdate!!.month,viewsdata[i].startdate!!.date)

                                                              val i2=cal2.get(Calendar.WEEK_OF_YEAR)

                                                              Log.e("Week_day","$i1   $i2 $minu_data")*/

                                                            //     val week=fun_of_week(start,viewdatas[i].start_date)

                                                            val week=fun_of_week1(start,viewdatas[i].start_date)

                                                            if(week % (viewdatas[i].every_frequency!!) == 0){

                                                                Log.e("validate","Startdate $i2 ${viewdatas[i].start_date}  currwent date week $i1 $start.... $minu_data ${(viewdatas[i].every_frequency!! + 1)}  ")

                                                                val simpleDateformat = SimpleDateFormat("EEEE")

                                                                val day = simpleDateformat.format(start)

                                                                Log.e("dat1234", "" + start.date + "-----------" + day)

                                                                if (viewdatas[i].selective_week!!.contains(day, ignoreCase = true)) {

                                                                    val status=callDetails.check_alaram_status(viewdatas[i].allTypeId!!,start)




                                                                    if(status){

                                                                        val data=callDetails.get_alaram_status(viewdatas[i].allTypeId!!,start)

                                                                        viewdatas!![i].active=data.active

                                                                    }

                                                                    datalist1.add(viewdatas!![i])

                                                                    data_cke=false

                                                                }

                                                            }

                                                        }

                                                        else if(viewdatas[i].frequency_type_id==3){

                                                            val months=fun_of_month(start,viewdatas[i].start_date!!)

                                                            if(viewdatas[i].start_date==start){

                                                                val status=callDetails.check_alaram_status(viewdatas[i].allTypeId!!,start)




                                                                if(status){

                                                                    val data=callDetails.get_alaram_status(viewdatas[i].allTypeId!!,start)

                                                                    viewdatas!![i].active=data.active

                                                                }
                                                                datalist1.add(viewdatas!![i])

                                                                data_cke=false
                                                            }
                                                            else if(viewdatas[i].start_date!!.date==start.date){

                                                                if (((months+1) % (viewdatas[i].every_frequency!!) == 0)) {


                                                                    val status=callDetails.check_alaram_status(viewdatas[i].allTypeId!!,start)




                                                                    if(status){

                                                                        val data=callDetails.get_alaram_status(viewdatas[i].allTypeId!!,start)

                                                                        viewdatas!![i].active=data.active

                                                                    }

                                                                    datalist1.add(viewdatas!![i])

                                                                    data_cke=false

                                                                }

                                                            }
                                                            else {


                                                            }

                                                        }

                                                        else if(viewdatas[i].frequency_type_id==4) {

                                                            val formater = SimpleDateFormat("yyyy")

                                                            val current_year=(formater.format(start)).toInt()

                                                            val start_year=(formater.format(viewdatas[i].start_date)).toInt()

                                                            if(start.date==viewdatas[i].start_date!!.date && start.month==viewdatas[i].start_date!!.month && start_year==current_year){

                                                                Log.e("responedata","$start ...${viewdatas[i].every_frequency}")

                                                                val status=callDetails.check_alaram_status(viewdatas[i].allTypeId!!,start)




                                                                if(status){

                                                                    val data=callDetails.get_alaram_status(viewdatas[i].allTypeId!!,start)

                                                                    viewdatas!![i].active=data.active

                                                                }

                                                                datalist1.add(viewdatas!![i])

                                                                data_cke=false

                                                            }
                                                            else if(start.date==viewdatas[i].start_date!!.date && start.month==viewdatas[i].start_date!!.month){

                                                                Log.e("responedata","$start")

                                                                if((current_year-start_year) % (viewdatas[i].every_frequency!!) == 0){

                                                                    Log.e("responedata","view $start")

                                                                    val status=callDetails.check_alaram_status(viewdatas[i].allTypeId!!,start)




                                                                    if(status){

                                                                        val data=callDetails.get_alaram_status(viewdatas[i].allTypeId!!,start)

                                                                        viewdatas!![i].active=data.active

                                                                    }
                                                                    datalist1.add(viewdatas!![i])

                                                                    data_cke=false

                                                                }

                                                            }

                                                        }

                                                    }
                                                }
                                                else{

                                                    val valide=  start.time  - viewdatas[i].start_date!!.time

                                                    val days = valide / (1000 * 3600 * 24)

                                                    if ((days.toInt()) % (viewdatas[i].repeat_type!!) == 0) {

                                                        val status=callDetails.check_alaram_status(viewdatas[i].allTypeId!!,start)




                                                        if(status){

                                                            val data=callDetails.get_alaram_status(viewdatas[i].allTypeId!!,start)

                                                            viewdatas!![i].active=data.active

                                                        }

                                                        datalist1.add(viewdatas!![i])


                                                    }
                                                }
                                            }

                                        } else {
                                            Log.e(
                                                "date else",
                                                "${viewdatas[i].start_date}..............${viewdatas[i].end_date}."
                                            )

                                        }
                                    }


                                }


                            }


                            recyclerView3!!.post(Runnable {

                                recyclerView3!!.visibility=View.VISIBLE
                                recyclerView3!!.layoutManager = LinearLayoutManager(localview?.context)
                                val adapter = calanderAdapter(datalist1, frag)
                                recyclerView3!!.adapter = adapter
                            })


                            if(data_cke){

                                recyclerView3!!.post(Runnable {
                                    recyclerView3!!.visibility=View.GONE

                                })

                                nodata3!!.post(Runnable {
                                    nodata3!!.visibility=View.VISIBLE

                                })

                            }
                            else{

                                nodata3!!.post(Runnable {
                                    nodata3!!.visibility=View.GONE

                                })

                            }

                        }
                        else{

                            recyclerView3!!.post(Runnable {
                                recyclerView3!!.visibility=View.GONE

                            })
                            nodata3!!.post(Runnable {
                                nodata3!!.visibility=View.VISIBLE

                            })


                        }
                    }
                }
                else{
//
                }
            } catch (e: Exception) {
                val s = e.message
                Log.e("Error list",s)
            }
        }

    }


    private fun fun_of_week(currentDate: Date?, date: Date?): Int {

        var a=date
        var b =currentDate

        if (b!!.before(a)) {
            return -fun_of_week(b, a)
        }
        a = resetTime(a!!)

        b = resetTime(b!!)


        val cal = GregorianCalendar()
        cal.setTime(a)
        var weeks = 0
        while (cal.getTime().before(b)) {
            // add another week
            cal.add(Calendar.WEEK_OF_YEAR, 1)
            weeks++
        }
        return weeks


    }


    private fun resetTime(d: Date?): Date? {

        val cal = GregorianCalendar()
        cal.setTime(d)
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        return cal.getTime()

    }


    private fun fun_of_week1(startdate: Date?, enddate: Date?): Int {

        var a=startdate
        var b =enddate


        val cal = GregorianCalendar()

        cal.setTime(a)

        val wek1=cal.get(Calendar.WEEK_OF_YEAR)

        cal.setTime(b)
        val wek2=cal.get(Calendar.WEEK_OF_YEAR)


        val wek=wek2-wek1
        var weeks = 0

        while (cal.getTime().before(b)) {
            // add another week
            cal.add(Calendar.WEEK_OF_YEAR, 1)
            weeks++
        }

        Log.e("week old",""+weeks)

        Log.e("week new",""+wek)

        return wek



    }


    private fun fun_of_month(date1: Date, date2: Date?): Int {

        var a=date1

        var b=date2

        val cal = Calendar.getInstance()
        if (a.before(b))
        {
            cal.setTime(a)
        } else {
            cal.setTime(b)
            b = a
        }
        var c = 0
        while (cal.getTime().before(b)) {
            cal.add(Calendar.MONTH, 1)
            c++
        }
        return c - 1


    }




    class calanderAdapter(var userList: List<madicineType>, frag: FragmentActivity?) : RecyclerView.Adapter<calanderAdapter.ViewHolder>() {

        var viewesdata:View?=null

        var frageMent=frag


        private var splootDB: SplootAppDB? = null

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

            val v = LayoutInflater.from(parent.context).inflate(R.layout.calender_data_recycler, parent, false)

            viewesdata=v

            return ViewHolder(v)

        }
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {

            holder.bindItems(userList[position])

            holder.itemView.goto_nxt.setOnClickListener {

                val mContext = frageMent

                var fm:FragmentManager?=null


                    try {

                        fm = mContext?.supportFragmentManager

                        val transaction = fm?.beginTransaction()


                        transaction?.replace(
                            R.id.view_pager,
                            calander_edit_page_fragement.newInstance(userList[position])
                        )

                        transaction?.addToBackStack(null)

                        transaction?.commit()
                        //  fm!!.executePendingTransactions()

                    } catch (e: Exception) {


                    }


            Log.e("click","${userList[position]}")

            }

        }
        override fun getItemCount(): Int {

            return userList.size

        }
        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

            fun bindItems(user: madicineType) {

                Log.e("adapter","$user")

                val name = itemView.findViewById(R.id.text_name) as TextView

                val date  = itemView.findViewById(R.id.time_data) as TextView


                val status_  = itemView.findViewById(R.id.status) as TextView

                name.text=user.task_name

                val dates=user.reminder_time

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

                date.text="$hr:$mi $day"

            }
        }
    }
}




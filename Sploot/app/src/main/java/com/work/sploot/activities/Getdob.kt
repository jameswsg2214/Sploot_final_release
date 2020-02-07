package com.work.sploot.activities

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.work.sploot.Entity.petMasterEntity
import com.work.sploot.R
import com.work.sploot.SplootAppDB
import com.work.sploot.data.stringPref
import kotlinx.android.synthetic.main.calender.*
import kotlinx.android.synthetic.main.deworming.view.*
import kotlinx.android.synthetic.main.getdob.*
import kotlinx.android.synthetic.main.getdob.view.*
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.*

class Getdob : Fragment() {
    var cal = Calendar.getInstance()
    private var splootDB: SplootAppDB? = null


    var select_date:Date?=null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?{

        val views = inflater.inflate(R.layout.getdob, container, false)

        splootDB = SplootAppDB.getInstance(views.context)

        val name =views.findViewById<EditText>(R.id.petage)


        val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)




            var select=cal.time

            select_date=select

            val formatter1 = SimpleDateFormat("yyyy/MM/dd")

            var date=formatter1.format(select)

                name.setText(date.toString())
        }


        val date_set= DatePickerDialog.OnDateSetListener { datePicker, i, i2, i3 ->

            Log.e("data"," $i  $i2  $i3 $datePicker")

            val dat=datePicker

            val formatter1 = SimpleDateFormat("dd/MM/yyyy")

            val date=formatter1.parse("$i3/${i2+1}/$i")

            val formatter2 = SimpleDateFormat("yyyy/MM/dd")


            val viewdate=formatter2.format(date)

            name.setText(viewdate)


        }

        views.select_date_dob.setOnClickListener {
            val pick_date= DatePickerDialog(views.context,
                date_set,
                // set DatePickerDialog to point to today's date when it loads up
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH))

            pick_date.datePicker.maxDate = Calendar.getInstance().timeInMillis

            pick_date.show()

        }


        if(name.text.toString()!=null){

            Log.e("Petname","name "+name.text.toString())

        }



        var iswork=true



        views.petage.addTextChangedListener(object : TextWatcher {

            override fun onTextChanged(cs: CharSequence, arg1: Int, arg2: Int, arg3: Int) {




            }
            override fun beforeTextChanged(arg0: CharSequence, arg1: Int, arg2: Int, arg3: Int) {


                if(views.petage.text.toString().contains("/"))
                {
                    if(views.petage.text.toString().length==7)
                    {
                        iswork=false

                    }
                }else
                {
                    if(views.petage.text.toString().length<7)
                    {
                        iswork=true
                    }
                }



            }

            override fun afterTextChanged(arg0: Editable) {

                if (iswork){

                    if(views.petage.text.toString().length==7 || views.petage.text.toString().length==4)
                    {

                        views.petage.append("/")

                    }

                }

                else{


                }


            }
        })


        return views
    }


    fun getdate(
        commandAdapter: Context,
        datas: String,
        views: View
    ): String? {
        val dialog = Dialog(commandAdapter)
        Log.e("diloge","------------------------------------------>")
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.calender)
        var current=""
        var date_1:Date?=null
        val dateget =dialog.findViewById<CalendarView>(R.id.getdate)

        dateget.maxDate=Date().time


        dialog.headertext.text=datas

        dateget?.setOnDateChangeListener { view, year, month, dayOfMonth ->

            current = "" + dayOfMonth + "/" + (month + 1) + "/" + year

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

            select_date=date_1

            views.petage.setText(current)

            Log.e("Start_date","$current")

            dialog.dismiss()

        }
        dialog.show()
        return null
    }



}
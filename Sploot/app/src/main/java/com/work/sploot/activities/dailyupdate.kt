package com.work.sploot.activities

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.annotation.RequiresApi
import androidx.annotation.UiThread
import androidx.fragment.app.Fragment
import com.work.sploot.R
import com.work.sploot.SplootAppDB
import com.work.sploot.data.stringPref
import kotlinx.android.synthetic.main.dayhealthactivity.view.*
import java.text.SimpleDateFormat
import java.util.*

class dailyupdate: Fragment() {

    var viewers:View?=null
    companion object {

        fun newInstance(): dailyupdate {


            var select_date by stringPref("select_date", null)

            if(select_date==null){

                val cur=Date()

                Log.e("dayef","data")

                val formatter = SimpleDateFormat("dd/MM/yyyy")

                val date=formatter.format(cur)

                select_date=date

            }
            else{

                Log.e("dayef","not found  $select_date")

            }


            return dailyupdate()


        }
    }

    private var splootDB: SplootAppDB? = null

    @RequiresApi(Build.VERSION_CODES.O)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {

        val root = inflater.inflate(R.layout.dayhealthactivity, container, false)

        viewers=root

        val data=Date()

        val formatter = SimpleDateFormat("dd/MM/yyyy")

        val selet=formatter.format(data)

        val select=formatter.parse(selet)




        try{

            val mContext = activity

            val fm = mContext?.supportFragmentManager

            val transaction = fm?.beginTransaction()

            transaction?.replace(R.id.frag_for_view, view_pager_fragement.newInstance(selet, 0))

            transaction?.commit()

        }
        catch (e:Exception){


        }



        val mDays = arrayOf(
            "January",
            "February",
            "March",
            "April",
            "May",
            "June",
            "July",
            "August",
            "September",
            "October",
            "November",
            "December"
        )
        var select_date by stringPref("select_date", null)

        val mCalendar = Calendar.getInstance()

        var mToday = mCalendar.get(Calendar.DAY_OF_MONTH)

        var mToday1 = mCalendar.get(Calendar.MONTH)// zero based

        var mToday2 = mCalendar.get(Calendar.YEAR)

        root.calender_month_day.text = mDays[mToday1] + "-" + mToday2

        //root.week_Calendar.reset()

        val cal = Calendar.getInstance()

        root.previous_month_day.setOnClickListener {

            if (cal.get(Calendar.MONTH) == 0) {
                // cal.add(Calendar.YEAR,-1)
            }

            cal.add(Calendar.MONTH, -1)

            mToday = 0

            mToday1 = cal.get(Calendar.MONTH) // zero based

            mToday2 = cal.get(Calendar.YEAR)

            root.calender_month_day.text = mDays[mToday1] + "-" + mToday2

            Log.e("month.....dadad", "next $mToday1")

        }


        root.calender_month_day.setOnClickListener {

          //  root!!.week_Calendar.reset()

            root.week_Calendar.moveToNext()
        }

        root.next_month_day.setOnClickListener {
            if (cal.get(Calendar.MONTH) == 11) {
                // cal.add(Calendar.YEAR,1)
            }
            cal.add(Calendar.MONTH, 1)
            mToday = 0

            mToday1 = cal.get(Calendar.MONTH) // zero based

            mToday2 = cal.get(Calendar.YEAR)


            root.calender_month_day.text = mDays[mToday1] + "-" + mToday2

        }

        splootDB = SplootAppDB.getInstance(root.context)



        root.week_Calendar.setOnDateClickListener { dayOfMonth ->

            val inputMethodManager = root.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

            inputMethodManager.hideSoftInputFromWindow(activity?.currentFocus?.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)

            val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")

            val monthformat = SimpleDateFormat("MM")

            val yearformat = SimpleDateFormat("yyyy")

            val dateformat = SimpleDateFormat("dd")

            val formattedmonth = monthformat.format(parser.parse("$dayOfMonth"))

            val formatyear = yearformat.format(parser.parse("$dayOfMonth"))

            val formatdate = dateformat.format(parser.parse("$dayOfMonth"))

            root.calender_month_day.text = mDays[(formattedmonth.toInt()) - 1] + "-" + formatyear

            cal.set(formatyear.toInt(), formattedmonth.toInt() - 1, formatdate.toInt())

            val formatter = SimpleDateFormat("dd/MM/yyyy")

            val formattedDate = formatter.format(parser.parse("$dayOfMonth"))

            select_date = formattedDate

             try {

                    val mContext = activity

                    val fm = mContext?.supportFragmentManager

                    val transaction = fm?.beginTransaction()

                    transaction?.replace(
                        R.id.frag_for_view,
                        view_pager_fragement.newInstance(formattedDate, 0)
                    )

                    transaction?.commit()

                } catch (e: Exception) {


                }

        }
        return root
    }



    override fun onDestroy() {

       // viewers!!.weekCalendar.reset()
        super.onDestroy()

    }


}

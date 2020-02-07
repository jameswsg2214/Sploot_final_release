package com.work.sploot.activities

import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import com.work.sploot.R
import com.work.sploot.data.stringPref
import kotlinx.android.synthetic.main.calenderfragment.view.*
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class calanderLayout:Fragment() {
    companion object {
        fun newInstance(): calanderLayout {
            return calanderLayout()
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        val views=inflater.inflate(R.layout.calenderfragment, container, false)

        val mDays = arrayOf("January", "February", "March", "April", "May", "June", "July","August","September","October","November","December")


        getActivity()!!.runOnUiThread(java.lang.Runnable {

            try {


                val mContext = activity
                val fm = mContext?.supportFragmentManager
                val transaction = fm?.beginTransaction()
                transaction?.replace(R.id.view_date, calander_data_view.newInstance("Today"))
                transaction?.commit()
            } catch (e: Exception) {


            }
        })

        val mCalendar = Calendar.getInstance()
        var mToday = mCalendar.get(Calendar.DAY_OF_MONTH)
        var mToday1 = mCalendar.get(Calendar.MONTH)// zero based
        var mToday2 = mCalendar.get(Calendar.YEAR)

        views.calender_month.text=mDays[mToday1]+"-"+mToday2

        Log.e("month.....dadada","$mToday1")

// get display metrics
        val metrics =  DisplayMetrics()

        activity?.windowManager?.defaultDisplay?.getMetrics(metrics)
// set adapter

        val adapter=MonthAdapter(views.context, mToday1, mToday2, metrics, mToday, activity,views,true)

        views.gridview.adapter=adapter

        val cal = Calendar.getInstance()


        //curentdate

        var calenderdate by stringPref("calenderdate", null)

        val sdf = SimpleDateFormat("dd/MM/yyyy")

        val dateInString = sdf.format(Date())

        calenderdate=dateInString


        views.previous_month.setOnClickListener {

            if(cal.get(Calendar.MONTH)==0)
            {
               // cal.add(Calendar.YEAR,-1)
            }
            cal.add(Calendar.MONTH,-1)

             mToday = 0
             mToday1 =  cal.get(Calendar.MONTH) // zero based
             mToday2 = cal.get(Calendar.YEAR)

            val adapter=MonthAdapter(views.context,mToday1, mToday2, metrics, mToday, activity, views,false)
            views.gridview.adapter=adapter

            views.calender_month.text=mDays[mToday1]+"-"+mToday2

            Log.e("month.....dadad","next $mToday1")

        }

        views.next_month.setOnClickListener {
            if(cal.get(Calendar.MONTH)==11)
            {
               // cal.add(Calendar.YEAR,1)
            }
            cal.add(Calendar.MONTH,1)
             mToday = 0
             mToday1 =  cal.get(Calendar.MONTH)// zero based
             mToday2 = cal.get(Calendar.YEAR)

            //  cal . add (Calendar.YEAR, 1)

            val adapter=MonthAdapter(views.context, mToday1, mToday2, metrics, mToday, activity, views,false)

            views.gridview.adapter=adapter

            views.calender_month.text=mDays[mToday1]+"-"+mToday2



        }



        views.gridview.onItemClickListener = object : AdapterView.OnItemClickListener
        {
            override fun onItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                // Get the GridView selected/clicked item text
                //val selectedItem = parent.getItemAtPosition(position).toString()

                val day=cal.get(Calendar.DAY_OF_WEEK)

                Log.e("day.....dadad","next $day")

                if(position>6) {
                    mToday = position

                    Log.e("day.....dadad","next $position")

                    val adapter=MonthAdapter(views.context, mToday1, mToday2, metrics, mToday, activity, views,false)
                    views.gridview.adapter=adapter


                        try {


                            val mContext = activity
                            val fm = mContext?.supportFragmentManager
                            val transaction = fm?.beginTransaction()
                            transaction?.replace(
                                R.id.view_date,
                                calander_data_view.newInstance("Today")
                            )
                            transaction?.commit()

                        } catch (e: Exception) {


                        }

                }


            }
        }

        return views

    }


}

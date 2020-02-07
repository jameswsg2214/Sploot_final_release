package com.work.sploot.activities


import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.work.sploot.Entity.madicineType
import com.work.sploot.R
import com.work.sploot.SplootAppDB
import com.work.sploot.data.stringPref
import kotlinx.android.synthetic.main.activity_signup.view.*
import kotlinx.android.synthetic.main.calander_edit_page.view.*
import kotlinx.android.synthetic.main.edit_calander_date.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class calander_edit_page_fragement:Fragment() {

    companion object {

        lateinit var selected_data:madicineType

        fun newInstance(user: madicineType): calander_edit_page_fragement {

            selected_data=user

            return calander_edit_page_fragement()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        var views=inflater.inflate(R.layout.calander_edit_page, container, false)

        var calenderdate by stringPref("calenderdate", null)

        val formatter = SimpleDateFormat("dd/MM/yyyy")

        val output = formatter.parse(calenderdate)

      //  output

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

        val yearformat = SimpleDateFormat("yyyy")

        val year=yearformat.format(output)

        views.editpage_calender_month_day.text=mDays[output.month]+"-"+year

        val date_data=fun_getweek(output)

        val gridView=views.findViewById<RecyclerView>(R.id.edit_cal_recycler)

        gridView?.layoutManager = GridLayoutManager(this.context,7)

        val adapter =calanderviewer(date_data,output.date)

        gridView?.adapter = adapter




        var type=selected_data.cat_type

        if(type==4){

            Log.e("message","type2")

            try {

                    val mContext = activity

                    val fm = mContext?.supportFragmentManager

                    val transaction = fm?.beginTransaction()

                    transaction?.replace(
                        R.id.edit_pager_fragment,
                        deworming_edit.newInstance(selected_data)
                    )

                    //   transaction?.addToBackStack(null)

                    transaction?.commit()


                } catch (e: Exception) {


                }
        }
        else{

            Log.e("message","type 1 & 3")

                try {

                    val mContext = activity

                    val fm = mContext?.supportFragmentManager

                    val transaction = fm?.beginTransaction()

                    transaction?.replace(
                        R.id.edit_pager_fragment,
                        vaccination_edit.newInstance(selected_data)
                    )

                    // transaction?.addToBackStack(null)

                    transaction?.commit()

                } catch (e: Exception) {


                }


        }

        return views
    }

    private fun fun_getweek(date: Date?): ArrayList<Int> {

        var dates:ArrayList<Int> = arrayListOf()

        if(date!!.day==0){

            for(i in 6 downTo 0){

                val mindata=addday(date,i)

                dates!!.add(mindata!!.date)

            }

            return dates!!
        }
        else{

            val mindata=addday(date,-1)

            //  var new_date:Date

            return fun_getweek(mindata)
        }

        //  return dates!!
    }


    private fun addday(d: Date?,day:Int): Date? {

        val cal = GregorianCalendar()
        cal.setTime(d)
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)

        cal.add(Calendar.DAY_OF_YEAR, -day)

        return cal.getTime()

    }

    class calanderviewer(var userList: ArrayList<Int>, date: Int) : RecyclerView.Adapter<calanderviewer.ViewHolder>() {

        var viewesdata:View?=null

        val date=date



        private val mDays = arrayOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")

        private var splootDB: SplootAppDB? = null

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

            val v = LayoutInflater.from(parent.context).inflate(R.layout.edit_calander_date, parent, false)

            viewesdata=v

            return ViewHolder(v)

        }
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {

           // holder.bindItems(userList[position],mDays)

            if(userList[position]==date){


                holder.itemView.cal_date.setTextColor(ContextCompat.getColor(viewesdata!!.context, R.color.black))

                holder.itemView.cal_date.setBackgroundDrawable(ContextCompat.getDrawable(viewesdata!!.context, R.drawable.ic_lens_black_24dp))


            }

            holder.itemView.cal_date.text=userList[position].toString()

            holder.itemView.cal_day.text=mDays[position]



        }
        override fun getItemCount(): Int {

            return userList.size

        }
        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


            fun bindItems(
                user: madicineType,
                mDays: Array<String>
            ) {


            }
        }
    }
}
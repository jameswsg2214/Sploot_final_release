package com.work.sploot.activities

import android.app.PendingIntent.getActivity
import android.content.Context
import android.graphics.Color
import android.os.AsyncTask
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import com.work.sploot.R
import com.work.sploot.SplootAppDB
import com.work.sploot.data.stringPref
import kotlinx.android.synthetic.main.calenderfragment.view.*
import org.joda.time.DateTime
import org.joda.time.Interval
import java.text.SimpleDateFormat
import java.util.*

class MonthAdapter(
    private val mContext: Context,
    private val mMonth: Int,
    private val mYear: Int,
    private val mDisplayMetrics: DisplayMetrics,
    private val day: Int,
    activity: FragmentActivity?,
    private  val views: View,
    private val first: Boolean
) :
    BaseAdapter() {
    private val mCalendar: GregorianCalendar
    private val mCalendarToday: Calendar
    private var mItems: MutableList<String>? = null
    private var mDaysShown: Int = 0
    private var mDaysLastMonth: Int = 0
    private var mDaysNextMonth: Int = 0
    private var mTitleHeight: Int = 0
    private var mDayHeight: Int = 0

    private var activitys: FragmentActivity= activity!!



    private var enddate:Int=0
    private val mDays = arrayOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
    private val mDaysInMonth = intArrayOf(31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)
    private var splootDB: SplootAppDB? = null

    private val barHeight: Int
        get() {
            when (mDisplayMetrics.densityDpi) {
                DisplayMetrics.DENSITY_HIGH -> return 48
                DisplayMetrics.DENSITY_MEDIUM -> return 32
                DisplayMetrics.DENSITY_LOW -> return 24
                else -> return 48
            }
        }

    init {
        mCalendar = GregorianCalendar(mYear, mMonth, 1)
        mCalendarToday = Calendar.getInstance()
        populateMonth()
    }

    /**
     * @param date - null if day title (0 - dd / 1 - mm / 2 - yy)
     * @param position - position in item list
     * @param item - view for date
     */

    protected fun onDate(date: IntArray?, position: Int, item: View) {}

    private fun populateMonth() {

        mItems = ArrayList()

        for (day in mDays) {
            mItems!!.add(day)
            mDaysShown++
        }

        val firstDay = getDay(mCalendar.get(Calendar.DAY_OF_WEEK))



        val prevDay: Int
        if (mMonth == 0)
            prevDay = daysInMonth(11) - firstDay + 1
        else
            prevDay = daysInMonth(mMonth - 1) - firstDay + 1
        for (i in 0 until firstDay) {
            mItems!!.add((prevDay + i).toString())
            mDaysLastMonth++
            mDaysShown++
        }

        val daysInMonth = daysInMonth(mMonth)
        for (i in 1..daysInMonth) {
            mItems!!.add(i.toString())
            mDaysShown++
        }

        mDaysNextMonth = 1
        while (mDaysShown % 7 != 0) {
            mItems!!.add(mDaysNextMonth.toString())
            mDaysShown++
            mDaysNextMonth++
        }

        mTitleHeight = 70
        val rows = mDaysShown / 7
        mDayHeight = 50

        enddate=daysInMonth


        splootDB = SplootAppDB.getInstance(mContext)
       // process(enddate)

    }

    private fun daysInMonth(month: Int): Int {
        var daysInMonth = mDaysInMonth[month]
        if (month == 1 && mCalendar.isLeapYear(mYear))
            daysInMonth++
        return daysInMonth
    }

    private fun getDay(day: Int): Int {
        when (day) {
            Calendar.MONDAY -> return 0
            Calendar.TUESDAY -> return 1
            Calendar.WEDNESDAY -> return 2
            Calendar.THURSDAY -> return 3
            Calendar.FRIDAY -> return 4
            Calendar.SATURDAY -> return 5
            Calendar.SUNDAY -> return 6
            else -> return 0
        }
    }

    private fun isToday(day: Int, month: Int, year: Int): Boolean {
        return if (mCalendarToday.get(Calendar.MONTH) == month
            && mCalendarToday.get(Calendar.YEAR) == year
            && mCalendarToday.get(Calendar.DAY_OF_MONTH) == day
        ) {
            true
        } else false
    }

    private fun getDate(position: Int): IntArray? {
        val date = IntArray(3)
        if (position <= 6) {
            return null // day names
        } else if (position <= mDaysLastMonth + 6) {
            // previous month
            date[0] = Integer.parseInt(mItems!![position])
            if (mMonth == 0) {
                date[1] = 11
                date[2] = mYear - 1
            } else {
                date[1] = mMonth - 1
                date[2] = mYear
            }
        } else if (position <= mDaysShown - mDaysNextMonth) {
            // current month
            date[0] = position - (mDaysLastMonth + 6)
            date[1] = mMonth
            date[2] = mYear
        } else {
            // next month
            date[0] = Integer.parseInt(mItems!![position])
            if (mMonth == 11) {
                date[1] = 0
                date[2] = mYear + 1
            } else {
                date[1] = mMonth + 1
                date[2] = mYear
            }
        }
        return date
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val v = LayoutInflater.from(parent.context).inflate(R.layout.caldenercell, parent, false)

        val view = v.findViewById(R.id.daytext)as TextView

        val view1 = v.findViewById(R.id.daydot1)as ImageView

        val view2 = v.findViewById(R.id.daydot2)as ImageView

        val view3 = v.findViewById(R.id.daydot3)as ImageView

        //view.gravity = Gravity.CENTER_VERTICAL or Gravity.CENTER_HORIZONTAL
        view.text = mItems!![position]



        view.setTextColor(Color.BLACK)


        val date = getDate(position)


        if (date != null) {
            //v.setBackgroundColor(Color.rgb(234, 234, 250))
           // view.height = mDayHeight
            if (date[1] != mMonth) {
                v.setBackgroundColor(Color.rgb(244, 244, 244))
                // previous or next month
                view.setBackgroundColor(Color.rgb(244, 244, 244))
                view1.setBackgroundColor(Color.rgb(244, 244, 244))
                view2.setBackgroundColor(Color.rgb(244, 244, 244))
                view3.setBackgroundColor(Color.rgb(244, 244, 244))
            } else {

               // Log.e("date", ""+date!![0]+"/"+(date[1]+1)+"/"+date[2])



                var startdate=""+date!![0]+"/"+(date[1]+1)+"/"+date[2]
                var endDate=""+date!![0]+"/"+(date[1]+1)+"/"+date[2]
                val formatter = SimpleDateFormat("dd/MM/yyyy")
                val start = formatter.parse(startdate)
                val end = formatter.parse(endDate)


            /*    view.setOnClickListener(View.OnClickListener {


                    var calenderdate by stringPref("calenderdate", null)


                    calenderdate=""+date!![0]+"/"+(date[1]+1)+"/"+date[2]

                    Log.e("clickdate",""+date!![0]+"/"+(date[1]+1)+"/"+date[2])
                })*/

                AsyncTask.execute {

                    var userId by stringPref("userId", null)

                    var petid by stringPref("petid", null)

                    //  petid= 1.toString()

                    var user= userId?.toInt()

                    try {
                        //val callDetails = splootDB!!.petMasterDao().getAll()

                        val callDetails = splootDB!!.petMasterDao()

                        val viewsdata=callDetails.get()

                       // Log.e("viewsdata","$viewsdata")

                     //   val viewdata=callDetails.getcatmonth_check(start!!,end!!,userId!!,petid!!)


                        val viewdata=callDetails.getAll_cat_check()

                        if(viewdata){

                         //   var viewdatas=callDetails.getcatmonth(start!!,end!!,userId!!,petid!!)

                            val viewdatas=callDetails.getAll_cat_()

                            Log.e("length",""+viewdatas.size + viewdatas)

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


                                                        if (viewdatas[i].cat_type == 1) {


                                                            view1.post(Runnable {

                                                                view1.visibility = View.VISIBLE
                                                            })

                                                        } else if (viewdatas[i].cat_type == 2) {
                                                            view3.post(Runnable {
                                                                view3.visibility = View.VISIBLE
                                                            })

                                                        }


                                                    }
                                                    else if (viewdatas[i].repeat_type == 2) {

                                                        if (viewdatas[i].cat_type == 1) {

                                                            view1.post(Runnable {

                                                                view1.visibility = View.VISIBLE

                                                            })

                                                        } else if (viewdatas[i].cat_type == 2) {
                                                            view3.post(Runnable {
                                                                view3.visibility = View.VISIBLE
                                                            })

                                                        }



                                                    }
                                                    else if (viewdatas[i].repeat_type == 3) {

                                                        if (viewdatas[i].cat_type == 1) {


                                                            view1.post(Runnable {

                                                                view1.visibility = View.VISIBLE
                                                            })

                                                        } else if (viewdatas[i].cat_type == 2) {
                                                            view3.post(Runnable {
                                                                view3.visibility = View.VISIBLE
                                                            })

                                                        }

                                                    }
                                                    else if (viewdatas[i].repeat_type == 4) {

                                                        if(viewdatas[i].start_date!!.day==start.day){

                                                            if (viewdatas[i].cat_type == 1) {

                                                                view1.post(Runnable {

                                                                    view1.visibility = View.VISIBLE

                                                                })

                                                            } else if (viewdatas[i].cat_type == 2) {

                                                                view3.post(Runnable {

                                                                    view3.visibility = View.VISIBLE

                                                                })

                                                            }

                                                        }

                                                    }
                                                    else if (viewdatas[i].repeat_type == 5) {

                                                        if(viewdatas[i].start_date==start){

                                                            if (viewdatas[i].cat_type == 1) {

                                                                view1.post(Runnable {

                                                                    view1.visibility = View.VISIBLE
                                                                })

                                                            } else if (viewdatas[i].cat_type == 2) {

                                                                view3.post(Runnable {
                                                                    view3.visibility = View.VISIBLE
                                                                })

                                                            }

                                                        }
                                                        else{

                                                         //   var select_view_data=start.date-viewdatas[i].start_date!!.date

                                                            val valide=  start.time  - viewdatas[i].start_date!!.time


                                                            val week=fun_of_week(start,viewdatas[i].start_date)

                                                            //val select_view_data = (valide / (1000 * 3600 * 24)).toInt()

                                                            if((week % 2 ==0) && (start.day == viewdatas[i].start_date!!.day)){





                                                                if (viewdatas[i].cat_type == 1) {

                                                                    view1.post(Runnable {

                                                                        view1.visibility = View.VISIBLE
                                                                    })

                                                                } else if (viewdatas[i].cat_type == 2) {
                                                                    view3.post(Runnable {
                                                                        view3.visibility = View.VISIBLE
                                                                    })

                                                                }

                                                            }
                                                        }
                                                    }
                                                    else if (viewdatas[i].repeat_type == 6) {

                                                        if(viewdatas[i].start_date!!.date==start.date){

                                                            if (viewdatas[i].cat_type == 1) {

                                                                view1.post(Runnable {

                                                                    view1.visibility = View.VISIBLE
                                                                })

                                                            } else if (viewdatas[i].cat_type == 2) {
                                                                view3.post(Runnable {
                                                                    view3.visibility = View.VISIBLE
                                                                })

                                                            }



                                                        }


                                                    }
                                                    else if (viewdatas[i].repeat_type == 7) {

                                                     //   var select_view_data=start.month-viewdatas[i].start_date!!.month

                                                        val months=fun_of_month(start,viewdatas[i].start_date!!)

                                                     //   if(select_view_data%3==0){


                                                        if(months%3==0 && (viewdatas[i].start_date!!.date == start!!.date)){

                                                            if (viewdatas[i].cat_type == 1) {

                                                                view1.post(Runnable {

                                                                    view1.visibility = View.VISIBLE

                                                                })

                                                            }
                                                            else if (viewdatas[i].cat_type == 2) {

                                                                view3.post(Runnable {

                                                                    view3.visibility = View.VISIBLE

                                                                })

                                                            }

                                                        }

                                                    }
                                                    else if (viewdatas[i].repeat_type == 8) {

                                                        val months=fun_of_month(start,viewdatas[i].start_date!!)

                                                        if(months%6==0  && (viewdatas[i].start_date!!.date == start!!.date)){

                                                            if (viewdatas[i].cat_type == 1) {

                                                                view1.post(Runnable {

                                                                    view1.visibility = View.VISIBLE
                                                                })

                                                            } else if (viewdatas[i].cat_type == 2) {
                                                                view3.post(Runnable {
                                                                    view3.visibility = View.VISIBLE
                                                                })

                                                            }

                                                        }



                                                    }
                                                    else if (viewdatas[i].repeat_type == 9) {


                                                        if((viewdatas[i].start_date!!.date==start.date) && (viewdatas[i].start_date!!.month==start.month) ){

                                                            if (viewdatas[i].cat_type == 1) {

                                                                view1.post(Runnable {

                                                                    view1.visibility = View.VISIBLE
                                                                })

                                                            } else if (viewdatas[i].cat_type == 2) {
                                                                view3.post(Runnable {
                                                                    view3.visibility = View.VISIBLE
                                                                })

                                                            }

                                                        }

                                                    }

                                                    else if (viewdatas[i].repeat_type == 10) {

                                                        if(viewdatas[i].frequency_type_id == 1){

                                                            val valide=  start.time  - viewdatas[i].start_date!!.time

                                                            val data_responce = (valide / (1000 * 3600 * 24)).toInt()

                                                            Log.e("responce_data","$data_responce....  ${start.date}..... ${viewdatas[i].start_date!!.date} ")

                                                            if( (data_responce) % ((viewdatas[i].every_frequency!!)) == 0){

                                                                Log.e("responce_if_if","$data_responce....  ${start.date}..... ${viewdatas[i].start_date!!.date} ")

                                                                Log.e("responce_freq","${viewdatas[i].every_frequency!!}")

                                                                if (viewdatas[i].cat_type == 1) {

                                                                    view1.post(Runnable {

                                                                        view1.visibility = View.VISIBLE
                                                                    })

                                                                } else if (viewdatas[i].cat_type == 2) {
                                                                    view3.post(Runnable {
                                                                        view3.visibility = View.VISIBLE
                                                                    })

                                                                }

                                                            }

                                                            else{

                                                                Log.e("responce_if_else","$data_responce....  ${start.date}..... ${viewdatas[i].start_date!!.date} ")

                                                            }

                                                        }
                                                        else if(viewdatas[i].frequency_type_id==2){



                                                           val week=fun_of_week1(start,viewdatas[i].start_date)


                                                            if( week % (viewdatas[i].every_frequency!!) == 0){

                                                                Log.e("validatesda","Startdate ${viewdatas[i].start_date}  currwent date week $start.... ${(viewdatas[i].every_frequency!!)}  ")

                                                                val simpleDateformat = SimpleDateFormat("EEEE")

                                                                val day = simpleDateformat.format(start)

                                                                Log.e("dat1234", "" + start.date + "-----------" + day)

                                                                if (viewdatas[i].selective_week!!.contains(day, ignoreCase = true)) {

                                                                    if (viewdatas[i].cat_type == 1) {

                                                                        view1.post(Runnable {

                                                                            view1.visibility = View.VISIBLE

                                                                        })

                                                                    } else if (viewdatas[i].cat_type == 2) {

                                                                        view3.post(Runnable {

                                                                            view3.visibility = View.VISIBLE

                                                                        })

                                                                    }

                                                                }

                                                            }
                                                            else{

                                                                Log.e("validatesda","Startdate ${viewdatas[i].start_date}  currwent date week $start.... ${(viewdatas[i].every_frequency!!)}  ")

                                                            }


                                                        }

                                                        else if(viewdatas[i].frequency_type_id==3){

                                                            val months=fun_of_month(start,viewdatas[i].start_date!!)

                                                            if(viewdatas[i].start_date==start){

                                                                if (viewdatas[i].cat_type == 1) {

                                                                    view1.post(Runnable {

                                                                        view1.visibility = View.VISIBLE
                                                                    })

                                                                } else if (viewdatas[i].cat_type == 2) {
                                                                    view3.post(Runnable {
                                                                        view3.visibility = View.VISIBLE
                                                                    })

                                                                }

                                                            }
                                                            else if(viewdatas[i].start_date!!.date==start.date){

                                                               /* if (viewdatas[i].cat_type == 1) {

                                                                    view1.post(Runnable {

                                                                        view1.visibility = View.VISIBLE
                                                                    })

                                                                } else if (viewdatas[i].cat_type == 2) {
                                                                    view3.post(Runnable {
                                                                        view3.visibility = View.VISIBLE
                                                                    })

                                                                }*/


                                                                if (((months+1) % (viewdatas[i].every_frequency!!) == 0)) {

                                                                    if (viewdatas[i].cat_type == 1) {

                                                                        view1.post(Runnable {

                                                                            view1.visibility =
                                                                                View.VISIBLE
                                                                        })

                                                                    } else if (viewdatas[i].cat_type == 2) {
                                                                        view3.post(Runnable {
                                                                            view3.visibility =
                                                                                View.VISIBLE
                                                                        })

                                                                    }

                                                                }

                                                            }
                                                            else {

                                                                /*if (((months) % (viewdatas[i].every_frequency!!) == 0) && (viewdatas[i].start_date!!.date == start!!.date)) {

                                                                    if (viewdatas[i].cat_type == 1) {

                                                                        view1.post(Runnable {

                                                                            view1.visibility =
                                                                                View.VISIBLE
                                                                        })

                                                                    } else if (viewdatas[i].cat_type == 2) {
                                                                        view3.post(Runnable {
                                                                            view3.visibility =
                                                                                View.VISIBLE
                                                                        })

                                                                    }

                                                                }*/

                                                            }

                                                        }
                                                        else if(viewdatas[i].frequency_type_id==4){

                                                            val formater = SimpleDateFormat("yyyy")

                                                            val current_year=(formater.format(start)).toInt()

                                                            val start_year=(formater.format(viewdatas[i].start_date)).toInt()

                                                            if(start.date==viewdatas[i].start_date!!.date && start.month==viewdatas[i].start_date!!.month && start_year==current_year){

                                                                Log.e("responedata","$start ...${viewdatas[i].every_frequency}")

                                                                if (viewdatas[i].cat_type == 1) {

                                                                    view1.post(Runnable {

                                                                        view1.visibility = View.VISIBLE
                                                                    })

                                                                } else if (viewdatas[i].cat_type == 2) {
                                                                    view3.post(Runnable {
                                                                        view3.visibility = View.VISIBLE
                                                                    })

                                                                }

                                                            }
                                                            else if(start.date==viewdatas[i].start_date!!.date && start.month==viewdatas[i].start_date!!.month){

                                                                Log.e("responedata","$start")

                                                            if((current_year-start_year) % (viewdatas[i].every_frequency!!) == 0){

                                                                Log.e("responedata","view $start")


                                                                if (viewdatas[i].cat_type == 1) {

                                                                    view1.post(Runnable {

                                                                        view1.visibility = View.VISIBLE
                                                                    })

                                                                } else if (viewdatas[i].cat_type == 2) {
                                                                    view3.post(Runnable {
                                                                        view3.visibility = View.VISIBLE
                                                                    })

                                                                }
                                                            }


                                                            }

                                                        }
                                                    }
                                                }
                                                else{


                                                    val valide=  start.time  - viewdatas[i].start_date!!.time

                                                    val days = valide / (1000 * 3600 * 24)

                                                    if ((days.toInt()) % (viewdatas[i].repeat_type!!) == 0) {

                                                        view2.post(Runnable {

                                                            view2.visibility = View.VISIBLE
                                                        })

                                                    }
                                                }


                                                /*Log.e("date", "$start..............$end")

                                                if(viewdatas[i].dailyxtime==null && viewdatas[i].dailyxhours==null && viewdatas[i].xday==null && viewdatas[i].weekdayselect=="" && viewdatas[i].active==null && viewdatas[i].partoftheday==""){

                                                    if (viewdatas[i].medicineType == 1) {


                                                        view1.post(Runnable {

                                                            view1.visibility = View.VISIBLE
                                                        })

                                                    } else if (viewdatas[i].medicineType == 2) {
                                                        view3.post(Runnable {
                                                            view3.visibility = View.VISIBLE
                                                        })
                                                        Log.e(
                                                            "date if",
                                                            "${viewdatas[i].startdate}..............${viewdatas[i].enddate}."
                                                        )

                                                    } else if (viewdatas[i].medicineType == 3) {

                                                        view2.post(Runnable {

                                                            view2.visibility = View.VISIBLE
                                                        })

                                                    }

                                                }
                                                else {

                                                    if(viewdatas[i].partoftheday!=""){

                                                        if ((start.date - viewdatas[i].startdate!!.date) % (viewdatas[i].partoftheday!!.toInt()) == 0) {

                                                            if (viewdatas[i].medicineType == 1) {


                                                                view1.post(Runnable {

                                                                    view1.visibility = View.VISIBLE
                                                                })

                                                            } else if (viewdatas[i].medicineType == 2) {
                                                                view3.post(Runnable {
                                                                    view3.visibility = View.VISIBLE
                                                                })

                                                            } else if (viewdatas[i].medicineType == 3) {

                                                                view2.post(Runnable {

                                                                    view2.visibility = View.VISIBLE
                                                                })

                                                            }
                                                        }
                                                    }
                                                    else{
                                                        if (viewdatas[i].dailyxhours == null && viewdatas[i].dailyxtime == null) {

                                                            if (viewdatas[i].xday != null) {

                                                                if (viewdatas[i].startdate == start) {
                                                                    if (viewdatas[i].medicineType == 1) {


                                                                        view1.post(Runnable {

                                                                            view1.visibility = View.VISIBLE
                                                                        })

                                                                    } else if (viewdatas[i].medicineType == 2) {
                                                                        view3.post(Runnable {
                                                                            view3.visibility = View.VISIBLE
                                                                        })

                                                                    } else if (viewdatas[i].medicineType == 3) {

                                                                        view2.post(Runnable {

                                                                            view2.visibility = View.VISIBLE
                                                                        })

                                                                    }
                                                                } else {
                                                                    if ((start.date - viewdatas[i].startdate!!.date) % (viewdatas[i].xday!!.toInt() + 1) == 0) {

                                                                        if (viewdatas[i].medicineType == 1) {


                                                                            view1.post(Runnable {

                                                                                view1.visibility = View.VISIBLE
                                                                            })

                                                                        } else if (viewdatas[i].medicineType == 2) {
                                                                            view3.post(Runnable {
                                                                                view3.visibility = View.VISIBLE
                                                                            })

                                                                        } else if (viewdatas[i].medicineType == 3) {

                                                                            view2.post(Runnable {

                                                                                view2.visibility = View.VISIBLE
                                                                            })

                                                                        }
                                                                    }
                                                                }
                                                            } else if (viewdatas[i].weekdayselect!!.length > 0) {

                                                                //Weekdays
                                                                Log.e(
                                                                    "dat123",
                                                                    "" + start.date + "-----------" + Calendar.SUNDAY
                                                                )


                                                                val simpleDateformat =
                                                                    SimpleDateFormat("EEEE"); // the day of the week spelled out completely
                                                                val day = simpleDateformat.format(start)
                                                                Log.e(
                                                                    "dat1234",
                                                                    "" + start.date + "-----------" + day
                                                                )
                                                                if (viewdatas[i].weekdayselect!!.contains(
                                                                        day,
                                                                        ignoreCase = true
                                                                    )
                                                                ) {


                                                                    if (viewdatas[i].medicineType == 1) {


                                                                        view1.post(Runnable {

                                                                            view1.visibility = View.VISIBLE
                                                                        })

                                                                    } else if (viewdatas[i].medicineType == 2) {
                                                                        view3.post(Runnable {
                                                                            view3.visibility = View.VISIBLE
                                                                        })

                                                                    } else if (viewdatas[i].medicineType == 3) {

                                                                        view2.post(Runnable {

                                                                            view2.visibility = View.VISIBLE
                                                                        })

                                                                    }


                                                                }


                                                            } else if (viewdatas[i].active != null) {

                                                                val total_cycle_day =
                                                                    viewdatas[i].active?.toInt()!! + viewdatas[i].paused?.toInt()!!

                                                                Log.e(
                                                                    "xdayspaused",
                                                                    "Currentday ${start.date} total $total_cycle_day "
                                                                )

                                                                val daycount =
                                                                    (start.date) - (viewdatas[i].startdate!!.date)

                                                                Log.e("xdayspaused", "daycount $daycount")

                                                                if (daycount < total_cycle_day) {

                                                                    if (daycount < viewdatas[i].active?.toInt()!!) {

                                                                        Log.e(
                                                                            "output",
                                                                            " ${start.date} ... day count $daycount"
                                                                        )

                                                                        if (viewdatas[i].medicineType == 1) {


                                                                            view1.post(Runnable {

                                                                                view1.visibility = View.VISIBLE
                                                                            })

                                                                        } else if (viewdatas[i].medicineType == 2) {
                                                                            view3.post(Runnable {
                                                                                view3.visibility = View.VISIBLE
                                                                            })

                                                                        } else if (viewdatas[i].medicineType == 3) {

                                                                            view2.post(Runnable {

                                                                                view2.visibility = View.VISIBLE
                                                                            })

                                                                        }
                                                                    }
                                                                } else {

                                                                    var division = daycount / total_cycle_day

                                                                    Log.e("xdayspaused", "division $division")

                                                                    var minus = total_cycle_day * division

                                                                    Log.e("xdayspaused", "minus $minus")

                                                                    if ((daycount - minus) < total_cycle_day) {

                                                                        if ((daycount - minus) < viewdatas[i].active?.toInt()!!) {

                                                                            Log.e(
                                                                                "output",
                                                                                "day ${start.date} ... daycount $daycount"
                                                                            )

                                                                            if (viewdatas[i].medicineType == 1) {


                                                                                view1.post(Runnable {

                                                                                    view1.visibility =
                                                                                        View.VISIBLE
                                                                                })

                                                                            } else if (viewdatas[i].medicineType == 2) {
                                                                                view3.post(Runnable {
                                                                                    view3.visibility =
                                                                                        View.VISIBLE
                                                                                })

                                                                            } else if (viewdatas[i].medicineType == 3) {

                                                                                view2.post(Runnable {

                                                                                    view2.visibility =
                                                                                        View.VISIBLE
                                                                                })

                                                                            }

                                                                        }

                                                                    }


                                                                }
                                                            }

                                                        }
                                                        else {
                                                            if (viewdatas[i].medicineType == 1) {


                                                                view1.post(Runnable {

                                                                    view1.visibility = View.VISIBLE
                                                                })

                                                            } else if (viewdatas[i].medicineType == 2) {
                                                                view3.post(Runnable {
                                                                    view3.visibility = View.VISIBLE
                                                                })

                                                            } else if (viewdatas[i].medicineType == 3) {

                                                                view2.post(Runnable {

                                                                    view2.visibility = View.VISIBLE
                                                                })

                                                            }
                                                        }

                                                    }

                                                }*/

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

                           // viewdatas.medicineType

                           // Log.e("viewsdata11","$viewdatas")
                        }

                        else{
                            Log.e("no date"," no data in this month")
                        }

                    } catch (e: Exception) {
                        val s = e.message
                        Log.e("Error",s)
                    }
                }

                // current month
                v.setBackgroundColor(Color.rgb(244, 244, 244))

                view.setBackgroundColor(Color.rgb(244, 244, 244))

                view1.setBackgroundColor(Color.rgb(244, 244, 244))

                view2.setBackgroundColor(Color.rgb(244, 244, 244))

                view3.setBackgroundColor(Color.rgb(244, 244, 244))

                /*if(date[0]%3==0)
                {

                   // view

                    Log.e("check","$date")
                    view1.visibility=View.VISIBLE
                    view2.visibility=View.INVISIBLE
                    view3.visibility=View.INVISIBLE
                }else if(date[0]%2==0)
                {
                    Log.e("check2","$date")
                    view1.visibility=View.INVISIBLE
                    view2.visibility=View.VISIBLE
                    view3.visibility=View.INVISIBLE
                }else
                {
                    Log.e("check3","${date}")
                    view1.visibility=View.INVISIBLE
                    view2.visibility=View.INVISIBLE
                    view3.visibility=View.VISIBLE
                }*/

                if (isToday(date[0], date[1], date[2])) {

                    view.setTextColor(Color.WHITE)
                    view.setBackgroundResource(R.drawable.ic_orange)

                }

               // val isclick=false

                if(position==day) {

                    if(isToday(date[0], date[1], date[2])) {
                        view.setTextColor(Color.WHITE)
                        view.setBackgroundResource(R.drawable.ic_orange)

                        var calenderdate by stringPref("calenderdate", null)

                        calenderdate=""+date!![0]+"/"+(date[1]+1)+"/"+date[2]

                        views.select_date.setText(calenderdate)

                    }
                    else {

                            if(first==false) {

                                view.setBackgroundResource(R.drawable.ic_circle_data)

                                var calenderdate by stringPref("calenderdate", null)

                                calenderdate=""+date!![0]+"/"+(date[1]+1)+"/"+date[2]

                                views.select_date.setText(calenderdate)


                            }


                    }




                   // Log.e("sharedate","month"+calenderdate)

                    Log.e("clickdate",""+date!![0]+"/"+(date[1]+1)+"/"+date[2])

                        try {

                            val mContext = activitys

                            val fm = mContext?.supportFragmentManager

                            val transaction = fm?.beginTransaction()

                            transaction?.replace(
                                R.id.view_date,
                                calander_data_view.newInstance("Today")
                            )

                            transaction?.commit()

                        } catch (e: Exception) {


                        }
                    //}
                }
            }
        } else {
            view.setBackgroundColor(Color.rgb(255, 87, 5))
            view.height = mTitleHeight
            view1.visibility=View.INVISIBLE
            view2.visibility=View.INVISIBLE
            view3.visibility=View.INVISIBLE
            view.setTextColor(Color.WHITE)
        }

        onDate(date, position, v)
        return v
    }


    private fun fun_of_week1(startdate: Date?, enddate: Date?): Int {

        var a=startdate
        var b =enddate


        val cal = GregorianCalendar();
        cal.setTime(a);

        val wek1=cal.get(Calendar.WEEK_OF_YEAR)

        cal.setTime(b);
        val wek2=cal.get(Calendar.WEEK_OF_YEAR)


        val wek=wek2-wek1



        var weeks = 0;
        while (cal.getTime().before(b)) {
            // add another week
            cal.add(Calendar.WEEK_OF_YEAR, 1);
            weeks++;
        }

        Log.e("week old",""+weeks)

        Log.e("week new",""+wek)
        return wek;



    }






    private fun fun_of_week(currentDate: Date?, date: Date?): Int {

        var a=date
        var b =currentDate

        if (b!!.before(a)) {
            return -fun_of_week(b, a);
        }
        a = resetTime(a!!);

        b = resetTime(b!!)


        val cal = GregorianCalendar();
        cal.setTime(a);
        var weeks = 0;
        while (cal.getTime().before(b)) {
            // add another week
            cal.add(Calendar.WEEK_OF_YEAR, 1);
            weeks++;
        }
        return weeks;



    }

    private fun resetTime(d: Date?): Date? {

        val cal = GregorianCalendar();
        cal.setTime(d);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime()

    }

    private fun fun_of_month(date1: Date, date2: Date?): Int {

        var a=date1

        var b=date2

        val cal = Calendar.getInstance();
        if (a.before(b)) {
            cal.setTime(a);
        } else {
            cal.setTime(b);
            b = a;
        }
        var c = 0;
        while (cal.getTime().before(b)) {
            cal.add(Calendar.MONTH, 1)
            c++;
        }
        return c - 1


    }


    private fun addday(d: Date?,day:Int): Date? {

        val cal = GregorianCalendar();
        cal.setTime(d);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        cal.add(Calendar.DAY_OF_YEAR, -day)

        return cal.getTime()

    }


    fun process(firstDay1: Int) {

        val firstDay="01"
        var month=""

        if(mMonth<10){
            month="0$mMonth"
        }
        else{
            month="$mMonth"
        }

        Log.e("last day...........", "$firstDay/$mMonth/$mYear $firstDay1/$mMonth/$mYear")

        var startdate="$firstDay/${mMonth+1}/$mYear"
        var endDate="$firstDay1/${mMonth+1}/$mYear"
        val formatter = SimpleDateFormat("dd/MM/yyyy")
        val start = formatter.parse(startdate)
        val end = formatter.parse(endDate)

        Log.e("date","$start..............$end")

        AsyncTask.execute {
            var userId by stringPref("userId", null)

            var petid by stringPref("petid", null)

            //  petid= 1.toString()

            var user= userId?.toInt()

            try {
                //val callDetails = splootDB!!.petMasterDao().getAll()
                val callDetails = splootDB!!.petMasterDao()

                val viewsdata=callDetails.get()

                Log.e("viewsdata","$viewsdata")

                val viewdata=callDetails.getcatmonth_check(start!!,end!!,userId!!,petid!!)

                if(viewdata){
                    val viewdatas=callDetails.getcatmonth(start!!,end!!,userId!!,petid!!)

                    Log.e("viewsdata","$viewdatas")
                }

                else{
                    Log.e("no date"," no data in this month")
                }


            }
            catch (e: Exception) {

                val s = e.message

                Log.e("Error",s)

            }
        }

    }

    override fun getCount(): Int {
        return mItems!!.size
    }

    override fun getItem(position: Int): Any {
        return mItems!![position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
}
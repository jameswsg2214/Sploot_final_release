package com.work.sploot.activities

import android.content.Intent
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.work.sploot.Entity.photoagalley
import com.work.sploot.R
import com.work.sploot.SplootAppDB
import com.work.sploot.data.stringPref
import kotlinx.android.synthetic.main.imageview_galley.view.*
import kotlinx.android.synthetic.main.reminderview.view.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class galley_fragemnt:Fragment() {

    private var splootDB: SplootAppDB? = null

    private  var gridView:RecyclerView?=null

    private  var gridView2:RecyclerView?=null

    private  var gridView3:RecyclerView?=null

    private  var viewsdata:View?=null

    private  var adapterInt:Int?=null

    private var today:TextView?=null

    private var yesterday:TextView?=null

    private var previous:TextView?=null

    companion object {

        fun newInstance(): galley_fragemnt {
            return galley_fragemnt()
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val cityarray = arrayOf("All","Photos","Prescription")

        val views=inflater.inflate(R.layout.imageview_galley, container, false)

        viewsdata=views

        today=views.findViewById(R.id.today_nodata)

        yesterday=views.findViewById(R.id.yesterday_nodata)

        previous=views.findViewById(R.id.previous_nodata)

        splootDB = SplootAppDB.getInstance(views.context)

        gridView = views.findViewById(R.id.today_Image)

        gridView2 = views.findViewById(R.id.yesterday)

        gridView3 = views.findViewById(R.id.previous)

        adapterInt=R.layout.galleryview


        updateimage()


        var city=views.findViewById<Spinner>(R.id.image_type_spinner)
        var citydata=""

        val spinnercity = ArrayAdapter(views.context, R.layout.spinner_text, cityarray)
        spinnercity.setDropDownViewResource(R.layout.spinner_text)
        city.adapter = spinnercity

        city.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {


                Log.e("masdfdd", "" + cityarray[position])

                if(position==0){
                    updateimage()
                }
                else if(position==1)
                {
                    photos_only()
                }
                else if(position==2){

                    priscription_only()

                }


                citydata= cityarray[position]
            }
            override fun onNothingSelected(parent: AdapterView<*>) {
                // Code to perform some action when nothing is selected
                citydata= cityarray[1]
            }
        }

        views.galley_close.setOnClickListener {
            val intent=Intent(views.context, firstpage::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)

        }

        return views
    }



    private fun updateimage() {
        AsyncTask.execute {

            var userId by stringPref("userId", null)

            var user= userId?.toInt()

            var petid by stringPref("petid", null)

            var perId= petid!!.toLong()

            val sdf = SimpleDateFormat("dd/MM/yyyy")

            val dateInString = sdf.format(Date())

            val formatter = SimpleDateFormat("dd/MM/yyyy")

            val date = formatter.parse(dateInString)

            val cal = Calendar.getInstance()

            cal . setTime (date)

            cal . add (Calendar.DATE, -1)

            val perviousday=cal.time

            Log.e("previous","day $perviousday")

            try {

                val callDetails = splootDB!!.petMasterDao()

                val isempty=callDetails.check_seleted_image(date, user!!,perId)

                if(isempty){

                    Log.e("check","date in there")

                    val isview=callDetails.get_selected_image(date, user!!,perId)

                    gridView?.post(Runnable {

                        gridView?.visibility=View.VISIBLE

                        gridView?.layoutManager = GridLayoutManager(viewsdata?.context,3)
                        val adapter = ImageAdapter(isview)
                        gridView?.adapter = adapter

                    })

                    today?.post(Runnable {
                        today?.visibility = View.GONE

                    })

                    Log.e("viewdata","$isview")
                }
                else{

                    gridView?.post(Runnable {
                        gridView?.visibility = View.GONE
                    })

                    today?.post(Runnable {
                        today?.visibility = View.VISIBLE

                    })
                }


              val check_pervious=callDetails.check_previous_image(perviousday, user!!,perId)

                if(check_pervious){

                    Log.e("pervious","data found")
                    val data=callDetails.get_previous_image(perviousday, user!!,perId)

                    gridView3?.post(Runnable {

                        gridView3?.visibility=View.VISIBLE

                        gridView3?.layoutManager = GridLayoutManager(viewsdata?.context,3)

                        val adapter = ImageAdapter(data)

                        gridView3!!.adapter = adapter

                    })

                    previous?.post(Runnable {
                        previous?.visibility = View.GONE

                    })

                }
                else{
                    gridView3?.post(Runnable {
                        gridView3?.visibility = View.GONE
                    })

                    previous?.post(Runnable {
                        previous?.visibility = View.VISIBLE

                    })

                    Log.e("pervious","nodata")
                }

                val isempty_previous=callDetails.check_seleted_image(perviousday, user!!,perId)

                if(isempty_previous){

                    Log.e("check","date in there")

                    val isview=callDetails.get_selected_image(perviousday, user!!,perId)

                    gridView2?.post(Runnable {

                        gridView2?.visibility=View.VISIBLE

                        gridView2?.layoutManager = GridLayoutManager(viewsdata?.context,3)

                        val adapter = ImageAdapter(isview)

                        gridView2!!.adapter = adapter

                    })

                    yesterday?.post(Runnable {
                        yesterday?.visibility = View.GONE

                    })

                    Log.e("viewdata","$isview")
                }

                else{
                    gridView2?.post(Runnable {
                        gridView2?.visibility = View.GONE
                    })

                    yesterday?.post(Runnable {
                        yesterday?.visibility = View.VISIBLE

                    })
                    Log.e("check","Image no data")
                }
            } catch (e: Exception) {
                val s = e.message;
                Log.e("Error photo",s)
            }
        }
    }


    private fun photos_only() {
        AsyncTask.execute {

            var userId by stringPref("userId", null)

            var user= userId?.toInt()

            var petid by stringPref("petid", null)

            var perId= petid!!.toLong()

            val sdf = SimpleDateFormat("dd/MM/yyyy")

            val dateInString = sdf.format(Date())

            val formatter = SimpleDateFormat("dd/MM/yyyy")

            val date = formatter.parse(dateInString)

            val cal = Calendar.getInstance()

            cal . setTime (date)

            cal . add (Calendar.DATE, -1)

            val perviousday=cal.time

            Log.e("previous","day $perviousday")

            try {

                val callDetails = splootDB!!.petMasterDao()

                val isempty=callDetails.check_date_filter_image(date, user!!,perId)

                if(isempty){

                    Log.e("check","date in there")

                    val isview=callDetails.get_date_filter_image(date, user!!,perId)

                    gridView?.post(Runnable {
                        gridView?.visibility=View.VISIBLE

                        gridView?.layoutManager = GridLayoutManager(viewsdata?.context,3)
                        val adapter = ImageAdapter(isview)
                        gridView?.adapter = adapter

                    })

                    today?.post(Runnable {
                        today?.visibility = View.GONE

                    })

                    Log.e("viewdata","$isview")

                }
                else{

                    gridView?.post(Runnable {
                        gridView?.visibility = View.GONE

                    })

                    today?.post(Runnable {
                        today?.visibility = View.VISIBLE

                    })
                }

                val check_pervious=callDetails.check_previous_filter_image(perviousday, user!!,perId)

                if(check_pervious){

                    Log.e("pervious","data found")
                    val data=callDetails.get_previous_filter_image(perviousday, user!!,perId)

                    gridView3?.post(Runnable {

                        gridView3?.visibility=View.VISIBLE

                        gridView3?.layoutManager = GridLayoutManager(viewsdata?.context,3)

                        val adapter = ImageAdapter(data)

                        gridView3!!.adapter = adapter

                    })

                    previous?.post(Runnable {
                        previous?.visibility = View.GONE

                    })

                }
                else{

                    gridView3?.post(Runnable {
                        gridView3?.visibility = View.GONE
                    })

                    previous?.post(Runnable {
                        previous?.visibility = View.VISIBLE

                    })
                    Log.e("pervious","nodata")
                }

                val isempty_previous=callDetails.check_date_filter_image(perviousday, user!!,perId)

                if(isempty_previous){

                    Log.e("check","date in there")

                    val isview=callDetails.get_date_filter_image(perviousday, user!!,perId)

                    gridView2?.post(Runnable {

                        gridView2?.visibility=View.VISIBLE

                        gridView2?.layoutManager = GridLayoutManager(viewsdata?.context,3)

                        val adapter = ImageAdapter(isview)

                        gridView2!!.adapter = adapter

                    })

                    yesterday?.post(Runnable {
                        yesterday?.visibility = View.GONE

                    })

                    Log.e("viewdata","$isview")
                }

                else{

                    gridView2?.post(Runnable {
                        gridView2?.visibility = View.GONE
                    })

                    yesterday?.post(Runnable {
                        yesterday?.visibility = View.VISIBLE

                    })
                    Log.e("check","Image no data")

                }
            } catch (e: Exception) {
                val s = e.message;
                Log.e("Error photo",s)
            }
        }
    }


    private fun priscription_only() {
        AsyncTask.execute {

            var userId by stringPref("userId", null)

            var user= userId?.toInt()

            var petid by stringPref("petid", null)

            var perId= petid!!.toLong()

            val sdf = SimpleDateFormat("dd/MM/yyyy")

            val dateInString = sdf.format(Date())

            val formatter = SimpleDateFormat("dd/MM/yyyy")

            val date = formatter.parse(dateInString)

            val cal = Calendar.getInstance()

            cal . setTime (date)

            cal . add (Calendar.DATE, -1)

            val perviousday=cal.time

            Log.e("previous","day $perviousday")

            try {

                val callDetails = splootDB!!.petMasterDao()

                val isempty=callDetails.check_date_filter_prc(date, user!!,perId)

                if(isempty){

                    Log.e("check","date in there")

                    val isview=callDetails.get_date_filter_prc(date, user!!,perId)

                    gridView?.post(Runnable {

                        gridView?.visibility=View.VISIBLE

                        gridView?.layoutManager = GridLayoutManager(viewsdata?.context,3)
                        val adapter = ImageAdapter(isview)
                        gridView?.adapter = adapter

                    })

                    today?.post(Runnable {
                        today?.visibility = View.GONE

                    })

                    Log.e("viewdata","$isview")
                }
                else{

                    gridView?.post(Runnable {
                        gridView?.visibility = View.GONE

                    })

                    today?.post(Runnable {
                        today?.visibility = View.VISIBLE

                    })
                }



                val check_pervious=callDetails.check_previous_filter_prc(perviousday, user!!,perId)

                if(check_pervious){

                    Log.e("pervious","data found")
                    val data=callDetails.get_previous_filter_pric(perviousday, user!!,perId)

                    gridView3?.post(Runnable {

                        gridView3?.visibility=View.VISIBLE

                        gridView3?.layoutManager = GridLayoutManager(viewsdata?.context,3)

                        val adapter = ImageAdapter(data)

                        gridView3!!.adapter = adapter

                            yesterday?.visibility=View.GONE
                    })

                    previous?.post(Runnable {
                        previous?.visibility = View.GONE

                    })

                }
                else{

                    gridView3?.post(Runnable {
                        gridView3?.visibility = View.GONE

                    })

                    previous?.post(Runnable {
                        previous?.visibility = View.VISIBLE

                    })
                    Log.e("pervious","nodata")
                }

                val isempty_previous=callDetails.check_date_filter_prc(perviousday, user!!,perId)

                if(isempty_previous){

                    Log.e("check","date in there")

                    val isview=callDetails.get_date_filter_prc(perviousday, user!!,perId)

                    gridView2?.post(Runnable {

                        gridView3?.visibility=View.VISIBLE

                        gridView2?.layoutManager = GridLayoutManager(viewsdata?.context,3)

                        val adapter = ImageAdapter(isview)

                        gridView2!!.adapter = adapter

                    })

                    yesterday?.post(Runnable {
                        yesterday?.visibility = View.GONE

                    })

                    Log.e("viewdata","$isview")
                }

                else{

                    gridView2?.post(Runnable {
                        gridView2?.visibility = View.GONE
                    })

                    yesterday?.post(Runnable {
                        yesterday?.visibility = View.VISIBLE

                    })
                    Log.e("check","Image no data")
                }
            } catch (e: Exception) {
                val s = e.message;
                Log.e("Error photo",s)
            }
        }
    }



}

class ImageAdapter(var userList: List<photoagalley>) : RecyclerView.Adapter<ImageAdapter.ViewHolder>() {

    var viewesdata:View?=null

    private var splootDB: SplootAppDB? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val v = LayoutInflater.from(parent.context).inflate(R.layout.galleryview, parent, false)

        viewesdata=v

        return ViewHolder(v)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bindItems(userList[position],viewesdata)

    }
    override fun getItemCount(): Int {

        return userList.size

    }
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(user: photoagalley, viewesdata: View?) {

            val path = itemView.findViewById(R.id.listimage) as ImageView

            var photoUri1: Uri = Uri.fromFile(File(user.photopath))

            Glide.with(viewesdata!!.context).load(photoUri1).into(path!!)


        }
    }


}
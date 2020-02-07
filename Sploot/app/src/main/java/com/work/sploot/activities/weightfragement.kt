package com.work.sploot.activities

import android.content.Context
import android.content.DialogInterface
import android.os.AsyncTask
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.work.sploot.Entity.weightdata
import com.work.sploot.R
import com.work.sploot.SplootAppDB
import com.work.sploot.data.stringPref
import kotlinx.android.synthetic.main.weight.view.*
import java.text.SimpleDateFormat
import java.util.*

class weightfragement: Fragment() {

    private var splootDB: SplootAppDB? = null

    private var viewref:View?=null

    companion object {

        var date_select:String?=null

        fun newInstance(dateSelect: String?): weightfragement {

            date_select=dateSelect

            return weightfragement()

        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val views=inflater.inflate(R.layout.weight, container, false)

        viewref=views

        splootDB = SplootAppDB.getInstance(views.context)

        val weigth_text=views.findViewById<EditText>(R.id.weightdata)

        weigth_text.text.clear()




        //pervious day edit

        var ischeck: Boolean=true

        var select_date by stringPref("select_date", null)

        val sdf = SimpleDateFormat("dd/MM/yyyy")

        val dateInString = sdf.format(Date())

        val formatter = SimpleDateFormat("dd/MM/yyyy")

        val current = formatter.parse(dateInString)

        val select = formatter.parse(date_select)

        weigth_text.setText("")

            AsyncTask.execute {

                var userId by stringPref("userId", null)

                var petid by stringPref("petid", null)

                var select_date by stringPref("select_date", null)

                val formatter = SimpleDateFormat("dd/MM/yyyy")

                Log.e("dayef", "not f  $select_date")

                var output = formatter.parse(date_select)

                try {

                    val callDetails = splootDB!!.petMasterDao()

                    val result = callDetails.weightTblempty(petid!!, userId!!)

                    if (result) {

                        val get = callDetails.getlastweight(petid!!, userId!!, output)

                        var temp_date=get[0].date

                        var weight:String?=null

                        for(i in 0..get.size-1){

                            if(temp_date!! <= get[i].date) {

                                Log.e("qwe","${get[i].date}")

                                temp_date=get[i].date

                                weight= get[i].weight + " kg"


                            }

                        }

                     //   viewref!!.pastweight.text = get.weight + " kg"

                        Log.e("qwer","$weight")

                        viewref!!.pastweight.text=weight

                        Log.e("weightkg", "data $get")
                    } else {
                        Log.e("Table", "table empty")
                    }

                } catch (e: Exception) {
                    val s = e.message
                    Log.e("Error", s)
                }
            }

        if(current==select) {

            AsyncTask.execute {

                var userId by stringPref("userId", null)

                var petid by stringPref("petid", null)

                var select_date by stringPref("select_date", null)

                val formatter = SimpleDateFormat("dd/MM/yyyy")

                val output = formatter.parse(date_select)

                try {

                    val callDetails = splootDB!!.petMasterDao()

                    val check = callDetails.checkweightat_day(petid!!, userId!!, output)

                    if (check) {

                        val get = callDetails.getweightat_day(petid!!, userId!!, output)

                        weigth_text.setText(get.weight)

                    } else {

                        viewref!!.weightdata.text.clear()


                    }

                } catch (e: Exception) {
                    val s = e.message
                    Log.e("Error", s)
                }
            }
        }



        var iswork=true

        views.weightdata.addTextChangedListener(object : TextWatcher {

            override fun onTextChanged(cs: CharSequence, arg1: Int, arg2: Int, arg3: Int) {

                if(views.weightdata.text.toString().length==3)
                {
                    if(views.weightdata.text.toString().contains("."))
                    {
                        iswork=false
                    }else
                    {
                        views.weightdata.text.insert(2,".")

                    }
                }


            }
            override fun beforeTextChanged(arg0: CharSequence, arg1: Int, arg2: Int, arg3: Int) {


                if(views.weightdata.text.toString().contains("."))
                {
                    if(views.weightdata.text.toString().length==3)
                    {
                        iswork=false

                    }
                }else
                {
                    if(views.weightdata.text.toString().length<2)
                    {
                        iswork=true
                    }
                }

            }
            override fun afterTextChanged(arg0: Editable) {
                // Toast.makeText(getApplicationContext(), "after text change", Toast.LENGTH_LONG)
                //    .show()
                if (iswork){

                    if(views.weightdata.text.toString().length==2)
                    {
                        views.weightdata.append(".")
                        //iswork=false
                    }

                }

                else{


                }


            }
        })

        if(ischeck){

            views.weightsave.setOnClickListener {

                Log.e("weight update","============================>")

                val inputMethodManager = views.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

                inputMethodManager.hideSoftInputFromWindow(activity?.currentFocus?.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)

                var weight_data=views.weightdata.text.toString().trim()

                val dataweight=weight_data.length

                if(dataweight==3){

                    weight_data += "00"

                }

                if(!weight_data.isNullOrEmpty()){

                    var select_date by stringPref("select_date", null)

                    val formatter = SimpleDateFormat("dd/MM/yyyy")

                    val output = formatter.parse(date_select)

                    Insert(output,weight_data)


                }else
                {
                    views.weightdata.error="Can't be empty"
                }
            }


        }
        return views

    }



    private fun Insert(date: Date?, weight:String) {

        AsyncTask.execute {

            var userId by stringPref("userId", null)

            var petid by stringPref("petid", null)

            var user= userId?.toInt()

            var petId= petid?.toLong()

            var select_date by stringPref("select_date", null)

            val formatter = SimpleDateFormat("dd/MM/yyyy")

            val output = formatter.parse(date_select)

            val call = splootDB!!.petMasterDao()

            try {

                Log.e("Insert page","userId=$userId , petid=$petId ,weight=$weight")

                val check=call.check_weight(petid!!,userId!!,date!!)

                if(check){

                    val data_=call.get_weight(petid!!,userId!!,date!!)

                    var ree = weightdata(
                        petId = petid,
                        weightId = data_.weightId,
                        userId= userId,
                        date = output,
                        weight = weight
                    )

                    val get = call.update_weight(ree)

                    val alldata=call.weightdata(petid!!,userId!!)

                    val weightdata=call.weightdataall()

                    Log.e("tabledata",""+alldata)

                    Log.e("weight_table","table data= $weightdata")

                    activity!!.runOnUiThread {

                        viewref?.weightsave!!.setImageResource(R.drawable.ic_done_submit)

                           try {

                                val mContext = activity

                                val fm = mContext?.supportFragmentManager

                                val transaction = fm?.beginTransaction()

                                transaction?.replace(
                                    R.id.frag_for_view, view_pager_fragement.newInstance(
                                        date_select!!,
                                        1
                                    )
                                )

                                transaction?.commit()
                            } catch (e: Exception) {


                            }


                        Toast.makeText(this.context,"Weight entered successfully(${weight} Kg)",Toast.LENGTH_LONG).show()

                    }

                }
                else{

                    var ree = weightdata(
                        petId = petid,
                        userId= userId,
                        date = output,
                        weight = weight
                    )

                    val get = call.insertweight(ree)

                    val alldata=call.weightdata(petid!!,userId!!)

                    val weightdata=call.weightdataall()

                    Log.e("tabledata",""+alldata)

                    Log.e("weight_table","table data= $weightdata")

                    activity!!.runOnUiThread {

                        viewref?.weightsave!!.setImageResource(R.drawable.ic_done_submit)

                        try{

                        val mContext = activity

                        val fm = mContext?.supportFragmentManager

                        val transaction = fm?.beginTransaction()

                        transaction?.replace(R.id.frag_for_view, view_pager_fragement.newInstance(
                            date_select!!,
                            1
                        ))

                        transaction?.commit()
                        }
                        catch (e:Exception){


                        }

                        Toast.makeText(this.context,"Weight entered successfully(${weight} Kg)",Toast.LENGTH_LONG).show()

                    }

                }



         /*       viewref!!.weightsave!!.post(Runnable {



                })*/

            } catch (e: Exception) {
                val s = e.message
                Log.e("Error",s)
            }
        }
    }



    private fun showDialog(
        output: Date?,
        weightData: String,
        views: View
    ) {

        var dialog: AlertDialog

        val builder = AlertDialog.Builder(views.context)

        builder.setTitle("Save Weight")

        builder.setMessage("Are you sure you want to Save?")

        val dialogClickListener = DialogInterface.OnClickListener{ _, which ->
            when(which){
                DialogInterface.BUTTON_POSITIVE ->
                {

                    Insert(output,weightData)


                }
                DialogInterface.BUTTON_NEGATIVE ->
                {

                }

            }

        }


        builder.setPositiveButton("YES",dialogClickListener)

        builder.setNegativeButton("NO",dialogClickListener)

        dialog = builder.create()

        dialog.show()


    }
}
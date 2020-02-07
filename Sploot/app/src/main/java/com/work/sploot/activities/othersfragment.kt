package com.work.sploot.activities

import android.annotation.TargetApi
import android.content.Context
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.work.sploot.Entity.activitynotes
import com.work.sploot.Entity.other
import com.work.sploot.R
import com.work.sploot.SplootAppDB
import com.work.sploot.data.stringPref
import kotlinx.android.synthetic.main.others.view.*
import kotlinx.android.synthetic.main.weight.view.*
import java.text.SimpleDateFormat

class othersfragment: Fragment()
{
    private var splootDB: SplootAppDB? = null

    companion object {

        fun newInstance(): othersfragment
        {

            return othersfragment()

        }

    }

    @TargetApi(Build.VERSION_CODES.O)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        val views = inflater.inflate(R.layout.others, container, false)

        splootDB = SplootAppDB.getInstance(views.context)

        val edit_text=views.findViewById<EditText>(R.id.othersdata)

        val imgr: InputMethodManager = activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        imgr.showSoftInput(edit_text, InputMethodManager.SHOW_IMPLICIT)

        edit_text.requestFocus()

        views.check_it.setOnClickListener {

            val imgr: InputMethodManager = activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

            imgr.showSoftInput(edit_text, InputMethodManager.SHOW_IMPLICIT)

            edit_text.requestFocus()
        }

        views.otherssave.setOnClickListener {

            var data= views.othersdata.text.toString().trim()

            when{

                views.othersdata.text.toString().trim().isNullOrEmpty()->Toast.makeText(views.context,"Please add note",Toast.LENGTH_LONG).show()

                else->{

                    process(data)

                    Toast.makeText(views.context,"Note Saved Successfully",Toast.LENGTH_LONG).show()

                }

            }

        }
        return views
    }

    private fun process(data: String) {

        AsyncTask.execute {

            var userId by stringPref("userId", null)

            var petid by stringPref("petid", null)

            var select_date by stringPref("select_date", null)

            val formatter = SimpleDateFormat("dd/MM/yyyy")

            val output = formatter.parse(select_date)

            try {
                //val callDetails = splootDB!!.petMasterDao().getAll()

                var data_inset=other(
                    petId = petid,
                    userId = userId,
                    date = output,
                    other = data
                )

                val result = splootDB!!.petMasterDao().insert_other(data_inset)

                var view_all=splootDB!!.petMasterDao().getother()


                    val mContext = activity
                    val manager = mContext?.supportFragmentManager
                    val transaction = manager?.beginTransaction()

                    transaction?.replace(R.id.medicine_frag, Medicine())

                    transaction?.commit()


                Log.e("data",".......................$view_all")

            } catch (e: Exception) {
                val s = e.message
                Log.e("Error", s)
            }
        }
    }
}
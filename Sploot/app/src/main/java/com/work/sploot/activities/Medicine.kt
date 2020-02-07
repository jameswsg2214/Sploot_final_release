package com.work.sploot.activities

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.fragment.app.Fragment
import com.work.sploot.R
import com.work.sploot.SplootAppDB
import kotlinx.android.synthetic.main.medicine.view.*

class Medicine : Fragment() {
    private var splootDB: SplootAppDB? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        val views = inflater.inflate(R.layout.medicine, container, false)

        var vaccinationbtn=views.findViewById<Button>(R.id.vaccinationadd)

        var dewormingbtn=views.findViewById<Button>(R.id.dewormingadd)

        var othersbtn=views.findViewById<Button>(R.id.othersadd)

        var notebtn=views.findViewById<Button>(R.id.noteadd)

        vaccinationbtn.setOnClickListener {


                try {


                    val mContext = activity

                    val fm = mContext?.supportFragmentManager

                    val transaction = fm?.beginTransaction()

                    transaction?.replace(
                        R.id.medicine_frag,
                        vaccination_update_fragment.newInstance(1)
                    )

                    transaction?.addToBackStack("null")

                    transaction?.commit()
                } catch (e: Exception) {


                }

        }
        dewormingbtn.setOnClickListener {

            try {

                    val mContext = activity

                    val fm = mContext?.supportFragmentManager

                    val transaction = fm?.beginTransaction()

                    transaction?.replace(R.id.medicine_frag, deworming_fragment())

                    transaction?.addToBackStack("null")

                    transaction?.commit()
                } catch (e: Exception) {


                }

        }
        othersbtn.setOnClickListener {

                try {
                    val mContext = activity

                    val fm = mContext?.supportFragmentManager

                    val transaction = fm?.beginTransaction()

                    transaction?.replace(
                        R.id.medicine_frag,
                        vaccination_update_fragment.newInstance(2)
                    )

                    transaction?.addToBackStack("null")

                    transaction?.commit()
                } catch (e: Exception) {


                }


        }

        notebtn.setOnClickListener {

                try {

                    val mContext = activity

                    val fm = mContext?.supportFragmentManager

                    val transaction = fm?.beginTransaction()

                    transaction?.replace(R.id.medicine_frag, othersfragment.newInstance())

                    transaction?.addToBackStack("null")

                    transaction?.commit()

                } catch (e: Exception) {


                }

        }
        return views
    }




}
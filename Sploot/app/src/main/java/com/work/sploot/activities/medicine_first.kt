package com.work.sploot.activities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.work.sploot.R

class medicine_first:Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        val views = inflater.inflate(R.layout.medicine_first_view, container, false)


            try {

                val mContext = activity

                val fm = mContext?.supportFragmentManager

                val transaction = fm?.beginTransaction()

                transaction?.replace(R.id.medicine_frag, Medicine())

                transaction?.commit()
            } catch (e: Exception) {

            }

        return views
    }
}

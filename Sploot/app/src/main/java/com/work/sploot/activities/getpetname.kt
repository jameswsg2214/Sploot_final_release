package com.work.sploot.activities

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.work.sploot.R

class getpetname : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        val views = inflater.inflate(R.layout.getpetname, container, false)

        val name =views.findViewById<EditText>(R.id.petname)

        if(name.text.toString()!=null)
        {
            Log.e("Petname","name "+name.text.toString())
        }

        return views
    }
}
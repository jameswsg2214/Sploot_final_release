package com.work.sploot.activities

import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.work.sploot.Entity.petMasterEntity
import com.work.sploot.Entity.taskdata

import androidx.annotation.UiThread
import com.work.sploot.R
import com.work.sploot.SplootAppDB
import com.work.sploot.data.stringPref
import com.work.sploot.data.taskviewdata

class TasklayoutActivity:Fragment() {

    private var splootDB: SplootAppDB? = null

    companion object {
        fun newInstance(): TasklayoutActivity {
            return TasklayoutActivity()
        }
    }
    val task = ArrayList<taskviewdata>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val views=inflater.inflate(R.layout.tasklayout, container, false)
        splootDB = SplootAppDB.getInstance(views.context)



            try {
                val mContext = activity
                val fm = mContext?.supportFragmentManager
                val transaction = fm?.beginTransaction()
                transaction?.replace(
                    R.id.task_frame,
                    taskfragmentActivity.newInstance(views, mContext)
                )
                transaction?.commit()

            } catch (e: Exception) {


            }

        return views
    }


}
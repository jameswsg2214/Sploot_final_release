package com.work.sploot.services

import android.widget.Toast
import android.content.Intent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent.getIntent
import com.work.sploot.activities.AlarmActivity

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(k1: Context, k2: Intent) {

     //   Toast.makeText(k1, "Alarm received at time", Toast.LENGTH_LONG).show()

        val intents= k2.getStringExtra("name")


        val intent=Intent(k1,AlarmActivity::class.java)

        intent.putExtra("name",intents)

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        k1.startActivity(intent)

      /*
      val intent=Intent(k1, AlarmActivity::class.java)

        k1.startActivity(intent)
        */

        /*val mIntent = Intent(k1, Myservice::class.java)
        //mIntent.putExtra("maxCountValue", 1000)
        val myservice=Myservice()

        myservice.enqueueWork(k1,mIntent)*/



    }

}
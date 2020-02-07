package com.work.sploot.services

import android.app.Dialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.Window
import android.view.WindowManager
import android.widget.NumberPicker
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import com.work.sploot.R
import com.work.sploot.activities.AlarmActivity
import com.work.sploot.activities.LoginActivity
import com.work.sploot.activities.firstpage
import kotlinx.android.synthetic.main.dosage.*

class Alertreciver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        // This method is called when the BroadcastReceiver is receiving an Intent broadcast.

        Toast.makeText(context,"data1",Toast.LENGTH_SHORT).show()

        val intent=Intent(context,AlarmActivity::class.java)

        context.startActivity(intent)
    }

}

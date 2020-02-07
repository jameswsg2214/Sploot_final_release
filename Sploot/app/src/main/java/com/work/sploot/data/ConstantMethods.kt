package com.work.sploot.data

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.ConnectivityManager
import android.os.AsyncTask
import android.util.Log
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.work.sploot.R
import com.work.sploot.SplootAppDB
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.dosage.*
import kotlinx.android.synthetic.main.dosage.calendercancel
import kotlinx.android.synthetic.main.gettime.*
import java.text.SimpleDateFormat
import java.util.regex.Matcher
import java.util.regex.Pattern
import kotlin.concurrent.timerTask
import android.os.SystemClock
import android.content.Context.ALARM_SERVICE
import androidx.core.content.ContextCompat.getSystemService
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent

import android.app.PendingIntent.getActivity


@Suppress("DEPRECATION")
class ConstantMethods {


    private var splootDB: SplootAppDB? = null
    fun checkNetwork(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = cm.activeNetworkInfo
        return netInfo != null && netInfo.isConnectedOrConnecting
    }
    fun setupPermissions(context: Context) {
        val MY_PERMISSIONS_REQUEST_READ_CONTACTS=101
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED &&ContextCompat.checkSelfPermission(context,
                Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED ) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    context as Activity,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
            } else {
                ActivityCompat.requestPermissions(context, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), MY_PERMISSIONS_REQUEST_READ_CONTACTS)
                ActivityCompat.requestPermissions(context, arrayOf(Manifest.permission.CAMERA), MY_PERMISSIONS_REQUEST_READ_CONTACTS)

            }
        } else {
            // Permission has already been granted
        }
    }



    fun setProgressDialog(context: Context): AlertDialog {

        val llPadding = 30
        val ll = LinearLayout(context)
        ll.orientation = LinearLayout.HORIZONTAL
        ll.setPadding(llPadding, llPadding, llPadding, llPadding)
        ll.gravity = Gravity.LEFT
        var llParam = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        llParam.gravity = Gravity.CENTER
        ll.layoutParams = llParam

        val progressBar = ProgressBar(context)
        progressBar.isIndeterminate = true
        progressBar.setPadding(llPadding, 0, llPadding, 0)
        progressBar.layoutParams = llParam

        llParam = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        llParam.gravity = Gravity.CENTER
        val tvText = TextView(context)
        tvText.text = "   Loading ..."
        tvText.setTextColor(Color.parseColor("#000000"))
        tvText.textSize = 20f
        tvText.layoutParams = llParam

        ll.addView(progressBar)
        ll.addView(tvText)

        val builder = AlertDialog.Builder(context)
        builder.setCancelable(true)
        builder.setView(ll)

        val dialog = builder.create()
        dialog.show()
        val window = dialog.getWindow()
        if (window != null) {
            val layoutParams = WindowManager.LayoutParams()
            layoutParams.copyFrom(dialog.getWindow()!!.getAttributes())
            layoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT
            layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT
            dialog.getWindow()!!.setAttributes(layoutParams)
        }

        return dialog
    }
    fun dialogforstartdaypicker(commandAdapter: Context, datas: String): String? {
        val dialog = Dialog(commandAdapter)
        Log.e("diloge","------------------------------------------>")
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dosage)
        var data="dabsbdjshjhsa"
        val name =dialog.findViewById<NumberPicker>(R.id.numberpicker)
        val header=dialog.findViewById<TextView>(R.id.headertextfordata)
        header.text = datas
        name.minValue=0
        name.maxValue=10
        dialog.calendercancel.setOnClickListener {
            dialog.dismiss()
        }

        name.setOnValueChangedListener { numberPicker, i, i2 ->
            Log.e("Picker","$i2")
        }
        dialog.show()
        return null
    }
    fun firstintakepicker(commandAdapter: Context, datas: String): String? {
        val dialog = Dialog(commandAdapter)
        Log.e("diloge","------------------------------------------>")
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.gettime)
        val header=dialog.findViewById<TextView>(R.id.headertextfortime)
        header.text = datas

        var day="am"
        var hours=0

        dialog.calendercancel.setOnClickListener {
            dialog.dismiss()
        }
        dialog.calenderok.setOnClickListener {
            dialog.dismiss()
        }
        dialog.gettime.setOnTimeChangedListener { timePicker, hour, minute ->

            if(hour>12){
                day="pm"
                hours =hour- 12
            } else{
                day="am"
            }
            Log.e("time", "Hour: "+ hours + " Minute : "+ minute+"day " +day)
        }
        dialog.show()
        return null
    }

    fun dialoghourspicker(commandAdapter: Context, datas: String): String? {
        val dialog = Dialog(commandAdapter)
        Log.e("diloge","------------------------------------------>")
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dosage)
        var data="dabsbdjshjhsa"
        val name =dialog.findViewById<NumberPicker>(R.id.numberpicker)
        val header=dialog.findViewById<TextView>(R.id.headertextfordata)
        header.text = datas

        val values = arrayOf("1 hours", "2 hours", "3 hours", "4 hours", "5 hours","6 hours","7 hours","8 hours","9 hours","10 hours","11 hours","12 hours")

        name.minValue = 0
        name.maxValue = values.size - 1
        name.displayedValues = values
        name.wrapSelectorWheel = true

        dialog.calendercancel.setOnClickListener {
            dialog.dismiss()
        }

        name.setOnValueChangedListener { numberPicker, i, i2 ->
            Log.e("Picker","hours"+(i2+1))
        }
        dialog.show()
        return null
    }
    fun dialogtabletspicker(commandAdapter: Context, datas: String): String? {
        val dialog = Dialog(commandAdapter)
        Log.e("diloge","------------------------------------------>")
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dosage)
        var data="dabsbdjshjhsa"
        val name =dialog.findViewById<NumberPicker>(R.id.numberpicker)
        val header=dialog.findViewById<TextView>(R.id.headertextfordata)
        header.text = datas

        val values = arrayOf("1 Tablet", "2 Tablet (s)", "3 Tablet (s)", "4 Tablet (s)", "5 Tablet (s)","6 Tablet (s)","7 Tablet (s)","8 Tablet (s)","9 Tablet (s)","10 Tablet (s)")
        name.minValue = 0
        name.maxValue = values.size - 1
        name.displayedValues = values
        name.wrapSelectorWheel = true

        dialog.calendercancel.setOnClickListener {
            dialog.dismiss()
        }

        name.setOnValueChangedListener { _, _, i2 ->
            Log.e("Picker","hours"+(i2+1))
        }
        dialog.show()
        return null
    }
    fun dialogdaysspicker(commandAdapter: Context, datas: String): String? {
        val dialog = Dialog(commandAdapter)
        Log.e("diloge","------------------------------------------>")
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dosage)
        var data="dabsbdjshjhsa"
        val name =dialog.findViewById<NumberPicker>(R.id.numberpicker)
        val header=dialog.findViewById<TextView>(R.id.headertextfordata)
        header.text = datas

        val values = arrayOf("1 Days", "2 Days", "3 Days", "4 Days", "5 Days","6 Days","7 Days","8 Days","9 Days","10 Days","11 Days","12 Days","13 days","14 Days","15 Days")
        name.minValue = 0
        name.maxValue = values.size - 1
        name.displayedValues = values
        name.wrapSelectorWheel = true

        dialog.calendercancel.setOnClickListener {
            dialog.dismiss()
        }

        name.setOnValueChangedListener { _, _, i2 ->
            Log.e("Picker","hours"+(i2+1))
        }
        dialog.show()
        return null
    }

    fun emailvalidation(trim: String): Boolean {
        val pattern: Pattern
        val matcher: Matcher
        var EMAIL_PATTERN:String  = "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"+"((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"+"[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."+ "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"              + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"            + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$"
        pattern = Pattern.compile(EMAIL_PATTERN)
        matcher = pattern.matcher(trim)
        val result=matcher.matches()
        Log.e("patten","Boll $result")
        return result

    }

    fun alarmcall(context: Context){

        splootDB = SplootAppDB.getInstance(context)

        AsyncTask.execute {
            var userId by stringPref("userId", null)
            var petid by stringPref("petid", null)

            var calenderdate by stringPref("calenderdate", null)

            Log.e("sharedate",calenderdate)

            val formatter = SimpleDateFormat("dd/MM/yyyy")
            val start = formatter.parse(calenderdate)
            val end = formatter.parse(calenderdate)
            var user= userId?.toInt()
            var petId= petid?.toLong()
            try {
                val callDetails = splootDB!!.petMasterDao()
/*
                var check=callDetails.checkalram(userId!!)

                if(check){

                    var data=callDetails.getalram(userId!!)
                    for(i in 0 ..data.size) {


                        val intent = Intent(getActivity(), MainActivity::class.java)
                        intent.action = Intent.ACTION_MAIN
                        intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT

                        val pendingIntent = PendingIntent.getActivity(
                            getActivity(), 0,
                            intent, 0
                        )


                        val alarmType = AlarmManager.ELAPSED_REALTIME

                        val FIFTEEN_SEC_MILLIS = 15000


                        val alarmManager =
                            getActivity().getSystemService(getActivity().ALARM_SERVICE) as AlarmManager



                        alarmManager.set(alarmType, SystemClock.elapsedRealtime(), pendingIntent)

                    }
                }*/
            }
            catch(e: Exception) {

                val s = e.message

                Log.e("Error list", s)
            }
            }
    }

}
package com.work.sploot.activities

import android.annotation.SuppressLint
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.work.sploot.Entity.petMasterEntity
import com.work.sploot.R
import com.work.sploot.SplootAppDB
import com.work.sploot.data.stringPref
import kotlinx.android.synthetic.main.activity_pet_detailsview.*
import kotlinx.android.synthetic.main.getpetname.*

class selectsex  : Fragment() {
    private var splootDB: SplootAppDB? = null
    var iscolor=true

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    @SuppressLint("ResourceAsColor")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?{

        val views = inflater.inflate(R.layout.sexselect, container, false)
        splootDB = SplootAppDB.getInstance(views.context)
        val malebtn=views.findViewById<LinearLayout>(R.id.malelayout)
        val maletxt=views.findViewById<TextView>(R.id.maletext)
        val femaletxt=views.findViewById<TextView>(R.id.femaletext)
        val femalebtn=views.findViewById<LinearLayout>(R.id.femalelayout)
        malebtn.setOnClickListener {
            process("Male")
            Log.e("click","dog")

            malebtn.setBackgroundResource(R.drawable.ic_edittext)
            femalebtn.setBackgroundResource(R.drawable.ic_login_edittext)

            maletxt.setTextColor(ContextCompat.getColor(views.context, R.color.white))
            femaletxt.setTextColor(ContextCompat.getColor(views.context, R.color.splootcolour))
        }
        femalebtn.setOnClickListener {
            var text=petname?.text.toString()
            Log.e("petname1","$text")
            process("Female")
            Log.e("click","cat")
            femalebtn.setBackgroundResource(R.drawable.ic_edittext)
            malebtn.setBackgroundResource(R.drawable.ic_login_edittext)
            femaletxt.setTextColor(ContextCompat.getColor(views.context, R.color.white))
            maletxt.setTextColor(ContextCompat.getColor(views.context, R.color.splootcolour))
        }
        return views
    }
    private fun process(cat:String) {
        Log.e("function called...","working")
        AsyncTask.execute {
            var userId by stringPref("userId", null)
            var petid by stringPref("petid", null)
            var user= userId?.toInt()
            var petId= petid?.toLong()
            try {
                //val callDetails = splootDB!!.petMasterDao().getAll()
                val viewdata = splootDB!!.petMasterDao().getSelectdata(petid!!,userId!!)
                var pet = petMasterEntity(
                    petId = petId,
                    userId= user,
                    petName = viewdata.petName,
                    age = viewdata.age,
                    sex = cat,
                    petCategoryId = viewdata.petCategoryId,
                    breedId= viewdata.breedId,
                    active = 0

                )
                val callDetails = splootDB!!.petMasterDao().update(pet)
                val get = splootDB!!.petMasterDao().getSelect(petid!!)

                Log.e("tabledata",""+get)

            } catch (e: Exception) {
                val s = e.message
                Log.e("Error",s)
            }
        }
    }
}
package com.work.sploot.activities

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.work.sploot.Entity.petMasterEntity
import com.work.sploot.R
import com.work.sploot.SplootAppDB
import com.work.sploot.data.stringPref
import kotlinx.android.synthetic.main.petselect.*

class selectpet : Fragment() {
    private var splootDB: SplootAppDB? = null
    var iscolor=true

    @SuppressLint("ResourceAsColor")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?{
        val views = inflater.inflate(R.layout.petselect, container, false)


        val inputMethodManager = views.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(activity?.currentFocus?.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)

        splootDB = SplootAppDB.getInstance(views.context)
        val dogbtn=views.findViewById<RelativeLayout>(R.id.dogbubble)
        val dogtext=views.findViewById<TextView>(R.id.dogtext)
        val cattext=views.findViewById<TextView>(R.id.cattext)
        val catbtn=views.findViewById<RelativeLayout>(R.id.catbubble)
        dogbtn.setOnClickListener {
            process("Dog")
            Log.e("click","dog")
            dogtext.setTextColor(ContextCompat.getColor(views.context, R.color.black))
            cattext.setTextColor(ContextCompat.getColor(views.context, R.color.white))
        }
        catbtn.setOnClickListener {
            process("Cat")
            Log.e("click","cat")
            dogtext.setTextColor(ContextCompat.getColor(views.context, R.color.white))
            cattext.setTextColor(ContextCompat.getColor(views.context, R.color.black))

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
                    sex = viewdata.sex,
                    petCategoryId = cat,
                    breedId= viewdata.breedId,
                    active = 0
                )
                val callDetails = splootDB!!.petMasterDao().update(pet)
                val get = splootDB!!.petMasterDao().getSelect(petid!!)

                Log.e("tabledata",""+get)

            } catch (e: Exception) {
                val s = e.message
              //  Log.e("Error",s)
            }
        }
    }
}
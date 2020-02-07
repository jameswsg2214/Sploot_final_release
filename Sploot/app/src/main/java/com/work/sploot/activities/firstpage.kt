package com.work.sploot.activities

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.facebook.login.LoginManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.work.sploot.Entity.petMasterEntity
import com.work.sploot.R
import com.work.sploot.SplootAppDB
import com.work.sploot.data.ConstantMethods
import com.work.sploot.data.stringPref

import kotlinx.android.synthetic.main.activity_firstpage.*
import kotlinx.android.synthetic.main.contect_us.*
import kotlinx.android.synthetic.main.pet_list_recycler.*
import kotlinx.android.synthetic.main.pet_list_view.view.*
import java.io.File
import java.util.*

class firstpage : AppCompatActivity() {
    private lateinit var viewPager: FrameLayout
    private lateinit var bottomNavigationView: BottomNavigationView
    private  lateinit var drawer_layout:DrawerLayout
    private var splootDB: SplootAppDB? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_firstpage)

        drawer_layout=findViewById(R.id.draw_layout)

        viewPager = findViewById(R.id.view_pager)

        bottomNavigationView = findViewById(R.id.bottom_navigation_view)


        bottomNavigationView.selectedItemId=R.id.bottom_navigationmenu

        splootDB = SplootAppDB.getInstance(this)


        val nav_viewnav_view_p=findViewById<NavigationView>(R.id.nav_view)

        var pet_Photo=findViewById<ImageView>(R.id.profile_pic_data)



            try {


                val manager = supportFragmentManager

                var transaction = manager.beginTransaction()

                transaction.replace(R.id.view_pager, Petprofile.newInstance(manager))

                transaction.commit()


            } catch (e: Exception) {

            }


        var petimage by stringPref("petimage", null)

        AsyncTask.execute {

            var petid by stringPref("petid", null)

            var userId by stringPref("userId", null)

            try {
                val callDetails = splootDB!!.petMasterDao()

                val check=callDetails.checkAll(userId!!)

                if(check) {

                    val data = callDetails.view_one(userId!!)

                    Log.e("data_logon","$data")

                    petid = data.petId.toString()

                    petimage = data.photo

                    this.runOnUiThread {


                        Glide.with(this).load(petimage).into(profile_pic_data)


                    }
                }

                else{

                    Log.e("data_logon","no")

                    var user= userId?.toInt()

                    var pet = petMasterEntity(

                        userId= user,
                        active = 0
                    )
                    var repocecreate=callDetails.insertAll(pet)

                    Log.e("rsponceof create","pet id"+repocecreate)

                    val viewdata = splootDB!!.petMasterDao().getparticulerpet()

                    Log.e("INseted", "worked   ${viewdata.petId}")

                    petid= viewdata.petId.toString()


                    this@firstpage.finish()

                    startActivity( Intent(this@firstpage, Petregister::class.java))

                }


            } catch (e: java.lang.Exception) {
                val s = e.message
                Log.e("Error",s)
            }
        }


        profile_pic_data.setOnClickListener {

            dialog_for_petselect(this,pet_Photo)

        }
        nav_viewnav_view_p.setNavigationItemSelectedListener { item->


            when {

                item.itemId== R.id.gallery -> {

                    Log.e("click","---->home")

                    if (drawer_layout.isDrawerOpen(Gravity.RIGHT)) {

                        drawer_layout.closeDrawer(Gravity.RIGHT)

                    } else {

                        drawer_layout.openDrawer(Gravity.RIGHT)

                    }


                        try {


                            val manager = supportFragmentManager
                            val transaction = manager.beginTransaction()
                            transaction.replace(R.id.view_pager, galley_fragemnt.newInstance())
                            transaction.addToBackStack(null)
                            transaction.commit()


                        } catch (e: Exception) {

                        }

                    true
                }
                item.itemId==R.id.tellafriend -> {

                    Log.e("click","---->3")

                    if (drawer_layout.isDrawerOpen(Gravity.RIGHT)) {

                        drawer_layout.closeDrawer(Gravity.RIGHT)

                        val share = Intent(Intent.ACTION_SEND)

                        share.setType("text/plain")

                        share.putExtra(Intent.EXTRA_TEXT, "Check out Sploot App, Get it for free at http://www.facebook.com/wesploot")

                        startActivity(Intent.createChooser(share, "Share link"))

                    } else {

                        drawer_layout.openDrawer(Gravity.RIGHT)

                    }

                    true
                }
                item.itemId==R.id.about_us -> {


                    var alertDialog = ConstantMethods().setProgressDialog(this)


                    alertDialog.show()

                    Log.e("click","---->3")



                    if (drawer_layout.isDrawerOpen(Gravity.RIGHT)) {

                        drawer_layout.closeDrawer(Gravity.RIGHT)

                        val dialog = Dialog(this)

                        Log.e("diloge","------------------------------------------>")

                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

                        dialog.setCancelable(false)

                        dialog.setCanceledOnTouchOutside(true)

                        dialog.onBackPressed()

                        dialog.setContentView(R.layout.contect_us)

                        this.runOnUiThread {

                            dialog.show()

                            alertDialog.dismiss()

                            // dialog_contect(this)

                        }

                        dialog.facebook_redirect.setOnClickListener {


                            startActivity(
                                Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse("http://www.facebook.com/wesploot")
                                )
                            )
                        }

                        dialog.insta_redirect.setOnClickListener {


                            startActivity(
                                Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse("https://www.instagram.com/wesploot/?hl=en")
                                )
                            )
                        }

                        dialog.email_send.setOnClickListener {

                            var intent =  Intent (Intent.ACTION_VIEW , Uri.parse("mailto:" + "boop@sploot.tech"))

                            startActivity(intent)

                        }

                    } else {
                        drawer_layout.openDrawer(Gravity.RIGHT)
                    }


                    true
                }
                item.itemId==R.id.profile -> {
                    Log.e("click","---->3")
                    if (drawer_layout.isDrawerOpen(Gravity.RIGHT)) {
                        drawer_layout.closeDrawer(Gravity.RIGHT)
                    } else {
                        drawer_layout.openDrawer(Gravity.RIGHT)
                    }


                        try {

                            val manager = supportFragmentManager

                            val transaction = manager.beginTransaction()

                            transaction.replace(R.id.view_pager, profile_fragment.newInstance())

                            transaction.addToBackStack(null)

                            transaction.commit()

                        } catch (e: Exception) {

                        }

                    true
                }
                item.itemId==R.id.logout_all -> {

                    Log.e("click","---->3")

                    if (drawer_layout.isDrawerOpen(Gravity.RIGHT)) {

                        drawer_layout.closeDrawer(Gravity.RIGHT)

                    } else {

                        drawer_layout.openDrawer(Gravity.RIGHT)
                    }

                    var userId by stringPref("userId", null)

                    userId = null

                    LoginManager.getInstance().logOut()

                    val intent=Intent(this, MainActivity::class.java)

                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    intent.putExtra("EXIT", true)
                    startActivity(intent)
                    finishAffinity()
                    true
                }


                item.itemId== R.id.pet_profile -> {

                    Log.e("click","---->home")

                    if (drawer_layout.isDrawerOpen(Gravity.RIGHT)) {

                        drawer_layout.closeDrawer(Gravity.RIGHT)

                        val intent =Intent(this@firstpage, firstpage::class.java)

                        startActivity(intent)


                        finishAffinity()

                    } else {

                        drawer_layout.openDrawer(Gravity.RIGHT)

                    }

                    true
                }
                else -> {
                    if (drawer_layout.isDrawerOpen(Gravity.RIGHT)) {
                        drawer_layout.closeDrawer(Gravity.RIGHT)
                    } else {
                        drawer_layout.openDrawer(Gravity.RIGHT)
                    }
                    true
                }
            }

        }

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when {
                item.itemId== R.id.bottom_navigation_home -> {


                    var alertDialog = ConstantMethods().setProgressDialog(this)
                    alertDialog.show()




                    val dateInString = Date()

                    Log.e("current_date","foemara $dateInString")


                    var cal =Calendar.getInstance()

                    cal.set(dateInString.year,dateInString.month,dateInString.date)

                    val i1=cal.get(Calendar.WEEK_OF_YEAR)

                    Log.e("validate","data $i1")

                        try {

                            val manager = supportFragmentManager

                            val transaction = manager.beginTransaction()

                            transaction.replace(R.id.view_pager, cms_first_frame())

                            transaction.commit()

                            alertDialog.dismiss()


                        } catch (e: Exception) {

                        }




                    true






                }
                item.itemId==R.id.bottom_navigation_task -> {

                    var alertDialog = ConstantMethods().setProgressDialog(this)
                    alertDialog.show()


                    var petid by stringPref("petid", null)

                    if(petid!=null) {

                            try {

                                val manager = supportFragmentManager

                                val transaction = manager.beginTransaction()

                                transaction.replace(
                                    R.id.view_pager,
                                    TasklayoutActivity.newInstance()
                                )

                                transaction.commit()


                                alertDialog.dismiss()


                            } catch (e: Exception) {


                            }



                    }
                    else{


                        this.runOnUiThread {

                            alertDialog.dismiss()


                        }

                        Toast.makeText(this,"Please register pet first",Toast.LENGTH_LONG).show()
                    }
                    true
                }
                item.itemId==R.id.bottom_navigation_dayupdate -> {


                    var alertDialog = ConstantMethods().setProgressDialog(this)
                    alertDialog.show()




                    var petid by stringPref("petid", null)

                    if(petid!=null) {


                            try {

                                val manager = supportFragmentManager

                                val transaction = manager.beginTransaction()

                                transaction.replace(R.id.view_pager, dailyupdate.newInstance())

                                transaction.commit()

                                alertDialog.dismiss()


                            } catch (e: Exception) {


                            }


                    }
                    else{

                        Toast.makeText(this,"Please register pet first",Toast.LENGTH_LONG).show()




                        this.runOnUiThread {

                            alertDialog.dismiss()


                        }

                    }
                    true
                }
                item.itemId==R.id.bottom_navigation_calender -> {


                    var alertDialog = ConstantMethods().setProgressDialog(this)
                    alertDialog.show()




                    var petid by stringPref("petid", null)

                    if(petid!=null) {


                            try {

                                val manager = supportFragmentManager

                                val transaction = manager.beginTransaction()

                                transaction.replace(R.id.view_pager, calanderLayout.newInstance())

                                transaction.commit()



                                alertDialog.dismiss()


                            } catch (e: Exception) {

                            }

                    }
                    else{
                        Toast.makeText(this,"Please register pet first",Toast.LENGTH_LONG).show()

                        this.runOnUiThread {

                            alertDialog.dismiss()


                        }

                    }
                    true

                }
                else -> {
                    if (drawer_layout.isDrawerOpen(Gravity.RIGHT)) {
                        drawer_layout.closeDrawer(Gravity.RIGHT)
                    } else {
                        drawer_layout.openDrawer(Gravity.RIGHT)
                    }
                    true
                }
            }
        }
    }

    override fun onBackPressed() {

        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {

            drawer_layout.closeDrawer(GravityCompat.START)

        } else {

            super.onBackPressed()

        }
    }


    override fun onDestroy() {
        super.onDestroy()


    }

    fun dialog_for_petselect(
        commandAdapter: Context,
        pet_Photo: ImageView

    ) {
        val dialog = Dialog(commandAdapter)
        Log.e("diloge","------------------------------------------>")
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        dialog.setCancelable(false)

        dialog.setCanceledOnTouchOutside(true)

        dialog.onBackPressed()

        dialog.setContentView(R.layout.pet_list_recycler)

        dialog.add_pet.setOnClickListener {
            process()
            startActivity(Intent(dialog.context, Petregister::class.java))
            dialog.dismiss()
        }
        proceesdata(dialog,commandAdapter,pet_Photo)
        dialog.show()

    }

    fun dialog_contect(
        commandAdapter: Context

    ) {

        val dialog = Dialog(commandAdapter)

        Log.e("diloge","------------------------------------------>")

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        dialog.setCancelable(false)

        dialog.setCanceledOnTouchOutside(true)

        dialog.onBackPressed()

        dialog.setContentView(R.layout.contect_us)

        dialog.facebook_redirect.setOnClickListener {


            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("http://www.facebook.com/wesploot")
                )
            )
        }

        dialog.insta_redirect.setOnClickListener {


            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://www.instagram.com/wesploot/?hl=en")
                )
            )
        }

        dialog.email_send.setOnClickListener {

            var intent =  Intent (Intent.ACTION_VIEW , Uri.parse("mailto:" + "boop@sploot.tech"))

            startActivity(intent)

        }

        dialog.show()

    }



    private fun process()
    {
        Log.e("function called...","working")

        AsyncTask.execute {
            var userId by stringPref("userId", null)
            var petid by stringPref("petid", null)
            //  petid= 1.toString()
            var user= userId?.toInt()
            try {
                //val callDetails = splootDB!!.petMasterDao().getAll()
                val callDetails = splootDB!!.petMasterDao()
                var pet = petMasterEntity(
                    userId= user,
                    active = 0
                )
                var repocecreate=callDetails.insertAll(pet)
                Log.e("rsponceof create","pet id"+repocecreate)
                val viewdata = splootDB!!.petMasterDao().getparticulerpet()
                Log.e("INseted", "worked   ${viewdata.petId}")
                petid= viewdata.petId.toString()

            } catch (e: Exception) {
                val s = e.message
                Log.e("Error",s)
            }
        }
    }

    private fun proceesdata(dialog: Dialog, context: Context, petPhoto: ImageView)
    {
        AsyncTask.execute {

            var userId by stringPref("userId", null)

            var user= userId?.toInt()

            try {

                val callDetails = splootDB!!.petMasterDao()

                Log.e("userId","useridzxcvbnm,. $user")

                val data = splootDB!!.petMasterDao().getAll()

                Log.e("all data","$data")

                val findtable = callDetails.findpet(userId!!)

                val recyclerView= dialog!!.findViewById<RecyclerView>(R.id.pet_list)

                if(findtable){

                    Log.e("responce"," data correct")

                    val findtablesss = callDetails.getpetid(userId!!)

                    Log.e("data","Pet data.............. $findtablesss")

                        recyclerView.layoutManager = LinearLayoutManager(this)

                    val adapter = petAdapter(findtablesss,dialog,context,petPhoto)

                    recyclerView.adapter = adapter
                    //})
                }
                else{


                    val findtablesss = callDetails.getpetid(userId!!)

                    Log.e("data","Pet data.............. $findtablesss")

                    Log.e("Pet","Pet not found")

                }
            } catch (e: Exception) {
                val s = e.message
                Log.e("Error",s)
            }
        }
    }
    companion object {
        const val NUM_PAGES = 4
    }
}

class petAdapter(var userList: List<petMasterEntity>,var dialog: Dialog,var context: Context,var petPhoto: ImageView) : RecyclerView.Adapter<petAdapter.ViewHolder>()
{

    var viewesdata: View?=null

    public val contct=context

    private var splootDB: SplootAppDB? = null

      lateinit var bottomNavigationView:BottomNavigationView

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    {

        val v = LayoutInflater.from(parent.context).inflate(R.layout.pet_list_view, parent, false)

        viewesdata=v

        return ViewHolder(v)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bindItems(userList[position],context)

        splootDB = SplootAppDB.getInstance(viewesdata!!.context)

        holder.itemView.pet_field.setOnClickListener {

            dialog.dismiss()

            var petid by stringPref("petid", null)

            var petimage by stringPref("petimage", null)

            petid= userList[position].petId.toString()

            petimage=userList[position].photo.toString()

            Log.e("petimgaeurl",petimage)

            var photoUri: Uri = Uri.fromFile(File(petimage))

            Glide.with(context).load(photoUri).into(petPhoto)


            val intent =Intent(holder.itemView.getContext(), firstpage::class.java)

            holder.itemView.getContext().startActivity(intent)


            Log.e("data","${userList[position].petName}")
        }
    }
    override fun getItemCount(): Int {

        return userList.size

    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(user: petMasterEntity, context: Context) {

            val name = itemView.findViewById(R.id.list_pet_name) as TextView

            val date  = itemView.findViewById(R.id.pet_field) as CardView

            val image=itemView.findViewById<ImageView>(R.id.list_pet_image)

            name.text = user.petName

            var photoUri: Uri = Uri.fromFile(File(user.photo))

            Glide.with(context).load(photoUri).into(image)
        }
    }

}

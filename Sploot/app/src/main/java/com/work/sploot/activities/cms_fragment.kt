package com.work.sploot.activities

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.work.sploot.R
import com.work.sploot.api.ApiProduction
import com.work.sploot.api.request.Cmsdata
import com.work.sploot.api.service.CommonServices
import com.work.sploot.data.ConstantMethods
import com.work.sploot.rx.RxAPICallHelper
import com.work.sploot.rx.RxAPICallback
import io.reactivex.Observable
import android.graphics.BitmapFactory
import android.os.AsyncTask

import android.util.Base64
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import com.work.sploot.SplootAppDB
import com.work.sploot.data.stringPref
import kotlinx.android.synthetic.main.cmsrecycler.view.*


class cms_fragment:Fragment() {

    private var splootDB: SplootAppDB? = null

    companion object {

        var managers: FragmentActivity?=null

        fun newInstance(manager: FragmentActivity): cms_fragment
        {
            managers=manager

            return cms_fragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        val views=inflater.inflate(R.layout.cmslayout, container, false)

        val recyclerView=views.findViewById<RecyclerView>(R.id.cms_recycler)

        recyclerView.layoutManager = GridLayoutManager(views.context,1)

        splootDB = SplootAppDB.getInstance(views.context)

        var type:String?="Pet"

        AsyncTask.execute {

            var userId by stringPref("userId", null)

            try {

                val callDetails = splootDB!!.petMasterDao()

                val viewdata = callDetails.getAll(userId!!)

                Log.e("all_data", "$viewdata")

                for (i in 0..viewdata.size - 1) {

                    if (viewdata[i].petCategoryId == "Dog") {

                        type += " Dog"

                        Log.e("pet_in", "dog")

                    } else if (viewdata[i].petCategoryId == "Cat") {


                        type += " Cat"

                        Log.e("pet_in", "cat")

                    }

                }

            } catch (e: Exception) {
                val s: String = e.message.toString()

                Log.e("Viewer_error", s)
            }


            Log.e("Type", "$type")
        }


            if (ConstantMethods().checkNetwork(this.context!!)) {

                var alertDialog = ConstantMethods().setProgressDialog(views.context)

                alertDialog.show()

                var commService: CommonServices = ApiProduction(views!!.context).provideService(CommonServices::class.java)

                var apiCall: Observable<Cmsdata> = commService.getcms()

                RxAPICallHelper().call(apiCall, object : RxAPICallback<Cmsdata> {

                    override fun onSuccess(Response: Cmsdata)
                    {
                        var datalist = ArrayList<Cmsdata.cmsviewdata>()

                        if (Response.status!!) {

                            //  recyclerView.layoutManager = LinearLayoutManager(views?.context)

                            for (i in 0 until Response.cms_data!!.size) {

                                Log.e("i value", "$i")

                                if (type!!.contains(Response.cms_data!![i].category!!, ignoreCase = true))
                                {

                                    datalist.add(Response.cms_data!![i])

                                }

                            }
                            val adapter = CustomAdapter(datalist)

                            recyclerView.adapter = adapter

                            alertDialog.dismiss()

                        } else {

                            alertDialog.dismiss()
                        }
                    }

                    override fun onFailed(throwable: Throwable) {

                        Log.e("addjourney_dategrid", " clicked Throwable:$throwable")

                        alertDialog.dismiss()


                    }
                })


            } else {

                Toast.makeText(
                    this.context,
                    getString(R.string.network_connection_error),
                    Toast.LENGTH_SHORT
                ).show()
            }

        return views
    }


    class CustomAdapter(val userList: ArrayList<Cmsdata.cmsviewdata>) : RecyclerView.Adapter<CustomAdapter.ViewHolder>()
    {

        var view_data:View?=null

        //this method is returning the view for each item in the list

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomAdapter.ViewHolder
        {

            val v = LayoutInflater.from(parent.context).inflate(R.layout.cmsrecycler, parent, false)

            view_data=v

            return ViewHolder(v)
        }

        //this method is binding the data on the list
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {

            var type:String?="Pet"

            holder.bindItems(userList[position],type)


            Log.e("Type","$type")

            holder.itemView.card_click.setOnClickListener {

                Log.e("Click","Fragment Click")

//                this.runOnUiThread(java.lang.Runnable {

                    try {

                        val fm = managers!!.supportFragmentManager

                        val transaction = fm?.beginTransaction()

                        transaction?.replace(
                            R.id.cms_frame,
                            cms_page_viewer_frag.newInstance(userList[position]!!, managers!!)
                        )

                        transaction?.addToBackStack(null)

                        transaction?.commit()
                    } catch (e: Exception) {

                    }
//                })

            }
        }

        //this method is giving the size of the list

        override fun getItemCount(): Int {

            return userList.size

        }

        //the class is hodling the list view

        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

            fun bindItems(user: Cmsdata.cmsviewdata, type: String?)
            {

                val nametxt = itemView.findViewById(R.id.headertxt) as TextView

                val sub_heading=itemView.findViewById(R.id.sub_txt) as TextView

                val author=itemView.findViewById(R.id.cms_author) as TextView

                val cms_image_heading=itemView.findViewById<ImageView>(R.id.card_image_view)

                val image_data=user.titleImage

                    Log.e("Typeview","$type")

                    val base64Image = image_data!!.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1]

                    val decodedString = Base64.decode(base64Image, Base64.DEFAULT)

                    val decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)

                    cms_image_heading.setImageBitmap(decodedByte)

                    nametxt.text=""+user.heading

                    sub_heading.text=user.subheading

                    author.text="Posted date "+ user.schedule+"\nPosted by ${user.authordetails}"


            }
        }
    }

}

package com.work.sploot.activities

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.work.sploot.R
import com.work.sploot.api.request.Cmsdata
import kotlinx.android.synthetic.main.cms_page_viewer.view.*

class cms_page_viewer_frag:Fragment() {

    companion object {

        var  viewed_data:Cmsdata.cmsviewdata?=null

        var managers: FragmentActivity?=null

        fun newInstance(
            user: Cmsdata.cmsviewdata,
            manager: FragmentActivity
        ): cms_page_viewer_frag {

            managers=manager

            viewed_data= user

            return cms_page_viewer_frag()

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val views=inflater.inflate(R.layout.cms_page_viewer, container, false)

        val web_Date=views.findViewById<WebView>(R.id.html_text_view)

        val heading=views.findViewById<TextView>(R.id.header_view_txt)
        val sub=views.findViewById<TextView>(R.id.sub_view_txt)
        val auther=views.findViewById<TextView>(R.id.cms_view_author)


        heading.text=""+viewed_data!!.heading

        sub.text= viewed_data!!.subheading

        auther.text="Posted date "+viewed_data!!.schedule+"\nPosted by ${viewed_data!!.authordetails}"

        web_Date.getSettings().setLoadsImagesAutomatically(true)

        web_Date.getSettings().setJavaScriptEnabled(true)

        web_Date.loadData(viewed_data!!.content,"text/html","UTF-8")


        views.cms_fragment_close.setOnClickListener {

            Log.e("cms","Close")

                try {

                    val fm = managers?.supportFragmentManager

                    val transaction = fm?.beginTransaction()

                    transaction?.replace(R.id.cms_frame, cms_fragment.newInstance(managers!!))

                    transaction?.commit()


                } catch (e: Exception) {


                }
        }



        return views
    }
}
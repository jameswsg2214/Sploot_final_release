package com.work.sploot.activities

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.work.sploot.R
import com.work.sploot.data.stringPref
import kotlinx.android.synthetic.main.activity_tast.*
import java.lang.Exception

class view_pager_fragement:Fragment() {



    companion object {

        var date_select:String?=null

        var poistion:Int?=null

        fun newInstance(Date: String, i: Int): view_pager_fragement {

            date_select=Date

            poistion=i

            return view_pager_fragement()
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val views=inflater.inflate(R.layout.view_pager_frag, container, false)

        val tabLayout = views.findViewById<TabLayout>(R.id.view_tabLayout)

        var viewPager = views.findViewById<ViewPager>(R.id.view_dayupdateframe)

        val mContext = activity

        val fm = mContext?.supportFragmentManager


            try {


                val transaction = fm?.beginTransaction()

                transaction?.replace(
                    R.id.view_dayupdateframe,
                    weightfragement.newInstance(date_select)
                )

                transaction?.commit()
            } catch (e: Exception) {


            }


        tabLayout!!.addTab(tabLayout!!.newTab().setText("Weight"))

        tabLayout!!.addTab(tabLayout!!.newTab().setText("Photos"))

        tabLayout!!.addTab(tabLayout!!.newTab().setText("Medicine"))

        tabLayout!!.tabGravity = TabLayout.GRAVITY_FILL

        var select_date by stringPref("select_date", null)

        select_date= date_select


        var adapter = MyAdapter_frag(views.context, fm!!, tabLayout!!.tabCount)

        viewPager!!.adapter = adapter

        viewPager.currentItem=poistion!!


        viewPager!!.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))

        tabLayout!!.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {

            override fun onTabSelected(tab: TabLayout.Tab) {

                val inputMethodManager = views.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

                inputMethodManager.hideSoftInputFromWindow(activity?.currentFocus?.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)

                viewPager!!.currentItem = tab.position

            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })
        return views
    }


    class MyAdapter_frag(private val myContext: Context, fm: FragmentManager, internal var totalTabs: Int) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment? {
            when (position) {
                0 -> {

                    return weightfragement.newInstance(date_select)
                }
                1 -> {
                    return photo_update_fragement.newInstance(1, date_select)
                }
                2 -> {
                    return medicine_first()
                }

                else -> return null
            }
        }

        override fun getCount(): Int {
            return totalTabs
        }

    }
}
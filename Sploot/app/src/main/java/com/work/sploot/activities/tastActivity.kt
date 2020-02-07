package com.work.sploot.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.work.sploot.R

class tastActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tast)


        val tabLayout = findViewById<TabLayout>(R.id.tab)

        val viewPager = findViewById<ViewPager>(R.id.dayframe)

        tabLayout!!.addTab(tabLayout!!.newTab().setText("weight"))

        tabLayout!!.addTab(tabLayout!!.newTab().setText("photos"))

        tabLayout!!.addTab(tabLayout!!.newTab().setText("Medicine"))

        tabLayout!!.addTab(tabLayout!!.newTab().setText("Food"))

        tabLayout!!.tabGravity = TabLayout.GRAVITY_FILL


        viewPager!!.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))

        tabLayout!!.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager!!.currentItem = tab.position
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {

            }
            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })

      //  return root
    }

}

//class MyAdapter(private val myContext: Context, fm: FragmentManager, internal var totalTabs: Int) : FragmentPagerAdapter(fm) {
//
//    override fun getItem(position: Int): Fragment? {
//        when (position) {
//            0 -> {
//                //  val homeFragment: HomeFragment = HomeFragment()
//                return weightfragement()
//            }
//            1 -> {
//                return Medicine()
//            }
//            2 -> {
//                // val movieFragment = MovieFragment()
//                return Medicine()
//            }
//            3 -> {
//                // val movieFragment = MovieFragment()
//                return Medicine()
//            }
//
//            else -> return null
//        }
//    }
//
//    // this counts total number of tabs
//    override fun getCount(): Int {
//        return totalTabs
//    }
//
//}

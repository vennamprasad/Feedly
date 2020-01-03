package com.appyhigh.feedly.ui.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.appyhigh.feedly.R
import com.appyhigh.feedly.ui.menu.*


class CategoryAdapter(context: Context, fm: FragmentManager) :
    FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    var mContext: Context? = context;
    //fragments
    override fun getItem(position: Int): Fragment {
        val fragment: Fragment
        when (position) {
            0 -> fragment = Top_HeadlinesFragment()
            1 -> fragment = WorldFragment()
            2 -> fragment = TechnologyFragment()
            3 -> fragment = BusinessFragment()
            4 -> fragment = ScienceFragment()
            5 -> fragment = SportsFragment()
            6 -> fragment = EntertainmentFragment()
            else -> {
                fragment = Top_HeadlinesFragment()
            }
        }
        return fragment
    }

    override fun getCount(): Int {
        return 7
    }

    //title
    override fun getPageTitle(position: Int): CharSequence? {
        var pageTitle: String? = null
        when (position) {
            0 -> pageTitle = mContext?.getString(R.string.category_headlines)
            1 -> pageTitle = mContext?.getString(R.string.category_world)
            2 -> pageTitle = mContext?.getString(R.string.category_technology)
            3 -> pageTitle = mContext?.getString(R.string.category_business)
            4 -> pageTitle = mContext?.getString(R.string.category_science)
            5 -> pageTitle = mContext?.getString(R.string.category_sports)
            6 -> pageTitle = mContext?.getString(R.string.category_entertainment)
            else -> {
            }
        }
        return pageTitle

    }
}

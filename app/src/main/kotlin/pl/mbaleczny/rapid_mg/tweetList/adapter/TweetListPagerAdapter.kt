package pl.mbaleczny.rapid_mg.tweetList.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

/**
 * Created by mariusz on 07.02.17.
 */
class TweetListPagerAdapter(supportFragmentManager: FragmentManager)
    : FragmentPagerAdapter(supportFragmentManager) {

    val fragments: MutableList<Fragment> = mutableListOf()

    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }

    override fun getCount(): Int {
        return fragments.size
    }

    fun addFragment(pos: Int, f: Fragment) {
        fragments.add(pos, f)
        notifyDataSetChanged()
    }

}
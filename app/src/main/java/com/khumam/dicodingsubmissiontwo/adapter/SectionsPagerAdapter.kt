package com.khumam.dicodingsubmissiontwo.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.khumam.dicodingsubmissiontwo.fragment.FollowerFragment
import com.khumam.dicodingsubmissiontwo.fragment.FollowingFragment
import com.khumam.dicodingsubmissiontwo.fragment.UserinfoFragment

class SectionsPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = UserinfoFragment()
            1 -> fragment = FollowerFragment()
            2 -> fragment = FollowingFragment()
        }
        return fragment as Fragment
    }

}
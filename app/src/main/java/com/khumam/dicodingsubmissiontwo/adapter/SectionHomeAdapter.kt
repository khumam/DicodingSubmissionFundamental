package com.khumam.dicodingsubmissiontwo.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.khumam.dicodingsubmissiontwo.fragment.FavoritFragment
import com.khumam.dicodingsubmissiontwo.fragment.HomeFragment

class SectionHomeAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = HomeFragment()
            1 -> fragment = FavoritFragment()
        }
        return fragment as Fragment
    }
}
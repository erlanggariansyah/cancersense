package com.dicoding.asclepius.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.dicoding.asclepius.fragment.HistoryFragment
import com.dicoding.asclepius.fragment.NewsFragment

class SectionsPagerAdapter(activity: AppCompatActivity): FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = NewsFragment()
            1 -> fragment = HistoryFragment()
        }

        return fragment as Fragment
    }
}
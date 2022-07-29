package com.example.ghostdiary.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.ghostdiary.fragment.login.FindEmailFragment
import com.example.ghostdiary.fragment.login.FindPasswordFragment

class ViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return 2
    }
    


    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> return FindEmailFragment()
            1 -> return FindPasswordFragment()

        }
        return FindEmailFragment()
    }
}
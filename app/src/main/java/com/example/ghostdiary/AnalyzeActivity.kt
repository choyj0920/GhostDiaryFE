package com.example.ghostdiary

import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.adapter.FragmentViewHolder
import androidx.viewpager2.widget.ViewPager2
import com.example.ghostdiary.databinding.ActivityAnalyzeBinding
import com.example.ghostdiary.fragment.analyze.AnalysisFragment
import com.example.ghostdiary.fragment.analyze.SleepFragment
import com.example.ghostdiary.fragment.calendar.CalendarFragment
import com.example.ghostdiary.fragment.calendar.RecordFragment
import com.example.ghostdiary.fragment.main.*
import java.util.*


class AnalyzeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAnalyzeBinding
    private lateinit var analysisFragment: AnalysisFragment
    private lateinit var sleepFragment: SleepFragment

    companion object{

    }

    private lateinit var viewPager: ViewPager2

    lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding=ActivityAnalyzeBinding.inflate(layoutInflater)
        viewModel= MainActivity.mainactivity.viewModel

        viewPager = binding.pager

        analysisFragment= AnalysisFragment(viewModel)
        sleepFragment= SleepFragment(viewModel.getsleepdataArray())

        init_view()

        setContentView(binding.root)
        Util.setGlobalFont(binding.root)


    }
    fun Update_pager(){
        val pagerAdapter = CustomPagerAdapter(this)
        viewPager.adapter = pagerAdapter
        viewPager.apply {  }



    }

    fun init_view(){
        Update_pager()

    }






    override fun onStart() {
        super.onStart()

    }

    override fun onDestroy() {
        super.onDestroy()
    }


    override fun onBackPressed() {


        if (viewPager.currentItem == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed()
            return

        } else {
            // Otherwise, select the previous step.
            viewPager.currentItem = viewPager.currentItem - 1
            return


        }




    }
    fun showmessage(str: String) {
        val toast:Toast  = Toast.makeText(this, str, Toast.LENGTH_SHORT);
        toast.show();

        var handler = Handler();
        handler.postDelayed( Runnable() {
            run {
                toast.cancel()
            }
        }, 1000);
    }




    private inner class CustomPagerAdapter(var fa: FragmentActivity) : FragmentStateAdapter(fa) {
        override fun getItemCount(): Int {
            return 2
        }

        override fun onBindViewHolder(
            holder: FragmentViewHolder,
            position: Int,
            payloads: MutableList<Any>
        ) {
            val fragment  =  fa.supportFragmentManager.findFragmentByTag("f$position")
            fragment?.let{
                if( it is CalendarFragment){
                }else if(it is RecordFragment){
                    Log.d("TAG","확인 vie호출")
                }
            }

            super.onBindViewHolder(holder, position, payloads)
        }

        override fun createFragment(position: Int): Fragment {
            when (position) {
                0 -> {
                    return analysisFragment
                }
                else ->{
                    return sleepFragment
                }

            }
        }

    }

}
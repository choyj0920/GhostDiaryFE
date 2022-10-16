package com.ghostdiary.ghostdiary

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.adapter.FragmentViewHolder
import androidx.viewpager2.widget.ViewPager2
import com.ghostdiary.ghostdiary.databinding.ActivityAnalyzeBinding
import com.ghostdiary.ghostdiary.fragment.analyze.AnalysisDetailFragment
import com.ghostdiary.ghostdiary.fragment.analyze.AnalysisFragment
import com.ghostdiary.ghostdiary.fragment.analyze.SleepFragment
import com.ghostdiary.ghostdiary.utilpackage.Util
import com.google.android.material.tabs.TabLayoutMediator


class AnalyzeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAnalyzeBinding
    private lateinit var analysisFragment: AnalysisFragment
    private lateinit var aDetailFragment: AnalysisDetailFragment
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
        aDetailFragment= AnalysisDetailFragment(viewModel)
        sleepFragment= SleepFragment(viewModel.getsleepdataArray())

        init_view()

        setContentView(binding.root)
        Util.setGlobalFont(binding.root)


    }
    fun Update_pager(){
        val pagerAdapter = CustomPagerAdapter(this)
        viewPager.adapter = pagerAdapter
        viewPager.apply {  }

        var pageCallBack = object: ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                // 특정 페이지가 선택될 때 마다 여기가 호출됩니다.
                // position 에는 현재 페이지 index - 첫페이지면 0
                when(position){
                    0-> {
                        binding.tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#F08080"))
                        binding.imageView.setImageResource(R.drawable.banner_analyze1)
                    }
                    1-> {
                        binding.tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#FAD98C"))
                        binding.imageView.setImageResource(R.drawable.banner_analyze2)

                    }
                    2->{
                        binding.tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#6DDBD9"))
                        binding.imageView.setImageResource(R.drawable.banner_analyze_sleep)


                    }
                }
            }
        }
        viewPager.registerOnPageChangeCallback(pageCallBack)


    }

    fun init_view(){
        Update_pager()

        val tabtitle=resources.getStringArray(R.array.analyze_tabtitle)


        TabLayoutMediator(binding.tabLayout, binding.pager) {tab, position ->
            when(position) {
                0 -> tab.text = tabtitle[0]
                1 -> tab.text = tabtitle[1]
                2 -> tab.text = tabtitle[2]
            }
        }.attach()

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
            return 3
        }

        override fun onBindViewHolder(
            holder: FragmentViewHolder,
            position: Int,
            payloads: MutableList<Any>
        ) {
            val fragment  =  fa.supportFragmentManager.findFragmentByTag("f$position")
            fragment?.let{

            }

            super.onBindViewHolder(holder, position, payloads)
        }

        override fun createFragment(position: Int): Fragment {
            when (position) {
                0 -> {
                    return analysisFragment
                }1->{
                    return aDetailFragment
                }
                else ->{
                    return sleepFragment
                }

            }
        }

    }

}
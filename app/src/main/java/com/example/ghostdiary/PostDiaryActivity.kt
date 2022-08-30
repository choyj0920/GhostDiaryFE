package com.example.ghostdiary


import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.ghostdiary.databinding.ActivityPostDiaryBinding
import com.example.ghostdiary.dataclass.Day_Diary
import com.example.ghostdiary.dataclass.emotionclass
import com.example.ghostdiary.fragment.postdiary.EditDiaryFragment
import com.example.ghostdiary.fragment.postdiary.SelectEmotionFragment
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class PostDiaryActivity( ) : AppCompatActivity () {

    lateinit var emotions:Array<String>
    lateinit var emotionselect :ArrayList<ArrayList<emotionclass>>
    lateinit var curDiary:Day_Diary
    lateinit var rv_linearlayoutmanger : LinearLayoutManager
    lateinit var pageCallBack: ViewPager2.OnPageChangeCallback
    lateinit var selectEmotionFragment: SelectEmotionFragment
    lateinit var editDiaryFragment: EditDiaryFragment
    private lateinit var viewPager: ViewPager2
    lateinit var viewModel :MainViewModel
    lateinit var date: Date
    var firsttext:Boolean=false
    private lateinit var binding:ActivityPostDiaryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel=MainActivity.mainactivity.viewModel
        firsttext=intent.getBooleanExtra("firsttext",false)
        date=Date(intent.getLongExtra("date",0.toLong()))
        if(date.time==0.toLong())
            finish()

        binding=ActivityPostDiaryBinding.inflate(layoutInflater)


        viewPager=binding.viewpager


        var formatDate = SimpleDateFormat("yyyy-MM-dd")
        var strdate = formatDate.format(date)
        if(viewModel.getEmotionArray(this).contains(strdate)){
            curDiary=viewModel.getEmotionArray(null)[strdate]!!.clone()
        }else{
            curDiary= Day_Diary(date)
        }

        selectEmotionFragment= SelectEmotionFragment(this,curDiary.date)
        editDiaryFragment= EditDiaryFragment(this,curDiary.date)

        emotionselect=curDiary.getEmotionarr()

        initdata()
        initpager()
        setContentView(binding.root)
        Util.setGlobalFont(binding!!.root)


    }
    fun addDiary(){
        try{
          editDiaryFragment.savediary()
        }catch(e: Exception){

        }
        viewModel.addDiary(curDiary)

        finish()
    }

    fun initpager(){
        val pagerAdapter = CustomPagerAdapter(this)
        viewPager.adapter = pagerAdapter
        viewPager.apply {  }

        viewPager.currentItem=if(firsttext) 1 else 0
        if(firsttext)
            binding!!.ivNext.visibility=View.GONE

        pageCallBack = object: ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                // 특정 페이지가 선택될 때 마다 여기가 호출됩니다.
                // position 에는 현재 페이지 index - 첫페이지면 0
                when(position){
                    0 -> {

                        binding.ivNext.visibility= View.VISIBLE
                        binding.ivNext.setOnClickListener {
                            viewPager.currentItem = viewPager.currentItem + 1
                        }
                        try{
                            editDiaryFragment.savediary()

                        }catch(e: Exception){

                        }

                    }
                    1->{
                        binding.ivNext.visibility= View.GONE

                        selectEmotionFragment.getcurDiary()
                        editDiaryFragment.updateGhostview()

                    }
                }
            }
        }
        viewPager.registerOnPageChangeCallback(pageCallBack)
    }
    fun switchmode_emotion(isedit:Boolean){
        if(isedit){
            binding.ivNext.visibility=View.GONE
            viewPager.isUserInputEnabled=false
        }else{
            binding.ivNext.visibility=View.VISIBLE
            viewPager.isUserInputEnabled=true


        }

    }

    fun initdata(){
        emotions= Day_Diary.emotionname
    }

    override fun onBackPressed() {

        if (viewPager.currentItem == 0 || firsttext) {

            super.onBackPressed()
        } else {
            // Otherwise, select the previous step.
            viewPager.currentItem = viewPager.currentItem - 1
            return

        }



    }

    private inner class CustomPagerAdapter(var fa: FragmentActivity) : FragmentStateAdapter(fa) {
        override fun getItemCount(): Int {
            return 2
        }
        override fun createFragment(position: Int): Fragment {
            when (position) {
                0 -> {
                    return selectEmotionFragment
                }
                else ->{
                    return editDiaryFragment
                }

            }
        }

    }



}
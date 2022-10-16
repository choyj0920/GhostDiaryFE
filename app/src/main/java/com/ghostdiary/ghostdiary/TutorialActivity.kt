package com.ghostdiary.ghostdiary

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.ghostdiary.ghostdiary.databinding.ActivityTutorialBinding
import com.ghostdiary.ghostdiary.fragment.TutorialFragment
import com.ghostdiary.ghostdiary.utilpackage.Util


class TutorialActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTutorialBinding

    private lateinit var prefs : SharedPreferences

    companion object{

    }

    private lateinit var viewPager: ViewPager2

    lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        prefs = this.getSharedPreferences("Prefs", Context.MODE_PRIVATE)

        binding=ActivityTutorialBinding.inflate(layoutInflater)

        viewPager=binding.myIntroViewPager


        val pagerAdapter = ScreenSlidePagerAdapter(this)
        viewPager.adapter = pagerAdapter



        binding.previousBtn.setOnClickListener {

            val editor : SharedPreferences.Editor = prefs.edit() // 데이터 기록을 위한 editor

            editor.putBoolean("firststart",false).apply()
            editor.commit()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()

        }

        binding.dotsIndicator.attachTo(viewPager)

        var pageCallBack = object: ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                // 특정 페이지가 선택될 때 마다 여기가 호출됩니다.
                // position 에는 현재 페이지 index - 첫페이지면 0
                when(position){
                    2-> {
                        try {
                            binding.nextBtn.text=resources.getString(R.string.start)
                            binding.nextBtn.setOnClickListener {
                                binding.previousBtn.callOnClick()

                            }

                        }catch(e: Exception){
                        }

                    }
                    else-> {
                        try {
                            binding.nextBtn.text=resources.getString(R.string.next)
                            binding.nextBtn.setOnClickListener {
                                viewPager.currentItem = viewPager.currentItem + 1


                            }

                        }catch(e: Exception){

                        }

                    }
                }
            }
        }
        viewPager.registerOnPageChangeCallback(pageCallBack)




        setContentView(binding.root)
        Util.setGlobalFont(binding.root)


    }





    override fun onStart() {
        super.onStart()

    }

    override fun onDestroy() {
        super.onDestroy()
    }


    override fun onBackPressed() {

        super.onBackPressed()

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


    private inner class ScreenSlidePagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
        override fun getItemCount(): Int = 3

        override fun createFragment(position: Int): Fragment {
            return when(position) {
                0 -> TutorialFragment(R.drawable.tutorial_00)
                1 -> TutorialFragment(R.drawable.tutorial_01)
                else -> TutorialFragment(R.drawable.tutorial_02)
            }
        }
    }





}
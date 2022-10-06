package com.example.ghostdiary

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.ghostdiary.databinding.ActivityMemoBinding
import com.example.ghostdiary.databinding.ActivityTutorialBinding
import com.example.ghostdiary.fragment.TutorialFragment
import com.example.ghostdiary.fragment.analyze.SleepFragment
import com.example.ghostdiary.fragment.memo.EditMemoFragment
import com.example.ghostdiary.fragment.memo.MemoSelectFragment
import com.example.ghostdiary.utilpackage.Util


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




        binding.dotsIndicator.attachTo(viewPager)

        binding.previousBtn.setOnClickListener {

            val editor : SharedPreferences.Editor = prefs.edit() // 데이터 기록을 위한 editor

            editor.putBoolean("firststart",false).apply()
            editor.commit()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()

        }

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
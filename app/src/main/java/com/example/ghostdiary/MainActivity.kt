package com.example.ghostdiary

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProvider
import com.example.ghostdiary.databinding.ActivityMainBinding
import com.example.ghostdiary.fragment.main.CalendarFragment
import com.example.ghostdiary.fragment.main.DefaultFragment
import com.example.ghostdiary.fragment.main.RecordFragment
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var defaultFragment: DefaultFragment
    private lateinit var calendarFragment: CalendarFragment
    private lateinit var recordFragment: RecordFragment


    lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding=ActivityMainBinding.inflate(layoutInflater)
        viewModel= ViewModelProvider(this).get(MainViewModel::class.java)
        defaultFragment= DefaultFragment()
        calendarFragment= CalendarFragment()
        recordFragment= RecordFragment()

        var today= LocalDateTime.now()
        var to = today.format(DateTimeFormatter.ofPattern("yyyy.MM.dd"))
        
        binding!!.tvDate.text=to

        binding!!.btnSideomenu.setOnClickListener {
            binding!!.drawerlayout.openDrawer(GravityCompat.START)
        }

        supportFragmentManager.beginTransaction().replace(binding.container.id,defaultFragment).commit()

        binding.navigationbar.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.invisible -> {
                    var today= LocalDateTime.now()
                    var to = today.format(DateTimeFormatter.ofPattern("yyyy.MM.dd"))
                    binding!!.tvDate.text=to
                    binding!!.tvDate.gravity=Gravity.LEFT

                    supportFragmentManager.beginTransaction().replace(binding.container.id, defaultFragment).commit()
                }
                R.id.calendar ->  {
                    binding!!.tvDate.text="캘린더"
                    binding!!.tvDate.gravity=Gravity.CENTER
                    supportFragmentManager.beginTransaction().replace(binding.container.id,calendarFragment).commit()
                }

                R.id.record-> {
                    binding!!.tvDate.text="기록"
                    binding!!.tvDate.gravity=Gravity.CENTER
                    supportFragmentManager.beginTransaction().replace(binding.container.id,recordFragment).commit()
                }
                R.id.memo->{
                    binding!!.tvDate.text="메모"
                    binding!!.tvDate.gravity=Gravity.CENTER
                }
                R.id.clinic->{
                    binding!!.tvDate.text="클리닉"
                    binding!!.tvDate.gravity=Gravity.CENTER
                }

            }
            true
        }




        setContentView(binding.root)

    }

    override fun onBackPressed() {

        if (supportFragmentManager.fragments.get(0) is CalendarFragment && calendarFragment.isshow){
            calendarFragment.down_post()
        }
        else if(supportFragmentManager.fragments.get(0) is DefaultFragment) {
            finishAffinity() //해당 앱의 루트 액티비티를 종료시킨다.

            System.runFinalization() //현재 작업중인 쓰레드가 다 종료되면, 종료 시키라는 명령어이다.
            System.exit(0)


        }else{
            binding.navigationbar.selectedItemId=R.id.invisible
            supportFragmentManager.beginTransaction().replace(binding.container.id,defaultFragment).commit()

        }

    }

}
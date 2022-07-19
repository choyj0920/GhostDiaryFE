package com.example.ghostdiary

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.ghostdiary.databinding.ActivityMainBinding
import com.example.ghostdiary.fragment.main.CalendarFragment
import com.example.ghostdiary.fragment.main.DefaultFragment
import com.example.ghostdiary.fragment.main.RecordFragment

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

        supportFragmentManager.beginTransaction().replace(binding.container.id,defaultFragment).commit()

        binding.navigationbar.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.invisible -> supportFragmentManager.beginTransaction().replace(binding.container.id, defaultFragment).commit()
                R.id.calendar ->  supportFragmentManager.beginTransaction().replace(binding.container.id,calendarFragment).commit()

                R.id.record-> supportFragmentManager.beginTransaction().replace(binding.container.id,recordFragment).commit()

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
            supportFragmentManager.beginTransaction().replace(binding.container.id,defaultFragment).commit()

        }

    }

}
package com.example.ghostdiary

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.ghostdiary.databinding.ActivityMainBinding
import com.example.ghostdiary.fragment.CalendarFragment
import com.example.ghostdiary.fragment.DefaultFragment
import com.example.ghostdiary.fragment.RecordFragment
import com.google.android.material.navigation.NavigationBarView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var defaultFragment: DefaultFragment
    private lateinit var calendarFragment: CalendarFragment
    private lateinit var recordFragment: RecordFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding=ActivityMainBinding.inflate(layoutInflater)

        defaultFragment= DefaultFragment()
        calendarFragment= CalendarFragment()
        recordFragment=RecordFragment()

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

}
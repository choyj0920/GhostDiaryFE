package com.example.ghostdiary

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.ghostdiary.databinding.ActivityPostDiaryBinding

class PostDiaryActivity : AppCompatActivity() {

    private lateinit var binding:ActivityPostDiaryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityPostDiaryBinding.inflate(layoutInflater)
        setContentView(binding.root)



    }
}
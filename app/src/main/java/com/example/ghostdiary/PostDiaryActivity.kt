package com.example.ghostdiary

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ghostdiary.adapter.AdapterDay
import com.example.ghostdiary.adapter.AdapterPostdiary
import com.example.ghostdiary.databinding.ActivityPostDiaryBinding

class PostDiaryActivity : AppCompatActivity() {


    lateinit var emotions:Array<String>
    lateinit var emotionselect :HashMap<String,Int>
    lateinit var adapterPostdiary:AdapterPostdiary

    private lateinit var binding:ActivityPostDiaryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityPostDiaryBinding.inflate(layoutInflater)

        emotionselect= hashMapOf()

        setContentView(binding.root)

        initdata()

    }
    fun initdata(){
        emotions= arrayOf("하루의 감정","누구와","무엇을","어디에서","분위기","기분","날씨")
        for( i in emotions){
            emotionselect.put(i,-1)
        }

        binding.ivCancelbtn.setOnClickListener{
            finish()
        }

        update()

    }

    fun update(){

        val emotionListAdapter = AdapterPostdiary(this,emotions,emotionselect)

        adapterPostdiary=emotionListAdapter

        binding.rvPostdiary.apply {
            layoutManager=LinearLayoutManager(this.context)
            adapter=adapterPostdiary
        }
    }
}
package com.example.ghostdiary

import android.content.ContentValues.TAG
import android.content.Intent
import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.widget.ImageView
import androidx.core.view.isInvisible
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ghostdiary.adapter.AdapterDay
import com.example.ghostdiary.adapter.AdapterPostdiary
import com.example.ghostdiary.databinding.ActivityPostDiaryBinding
import com.example.ghostdiary.dataclass.Day_Diary
import com.example.ghostdiary.fragment.CalendarFragment
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class PostDiaryActivity : AppCompatActivity() {


    lateinit var emotions:Array<String>
    lateinit var emotionselect :HashMap<String,Int>
    lateinit var adapterPostdiary:AdapterPostdiary
    lateinit var date:Date
    lateinit var emotionImageviews:Array<ImageView>

    private lateinit var binding:ActivityPostDiaryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityPostDiaryBinding.inflate(layoutInflater)

        emotionselect= hashMapOf()

        setContentView(binding.root)

        date= Date()
        date.time=intent.getLongExtra("Date",-1)

        emotionImageviews= arrayOf(binding.ivEmotion1,binding.ivEmotion2,binding.ivEmotion3,binding.ivEmotion4)



        initdata()

    }
    fun initdata(){
        emotions= arrayOf("하루의 감정","누구와","무엇을","어디에서","분위기","날씨")
        for( i in emotions){
            emotionselect.put(i,-1)
        }

        binding.ivCancelbtn.setOnClickListener{
            finish()
        }
        binding.ivNextbtn.setOnClickListener {

            to_posttext()

        }
        binding.ivTextCancelbtn.setOnClickListener{
            to_postemotion()
        }
        binding.ivTextSideoption.setOnClickListener{
            var array:MutableList<Int> = mutableListOf()
            for(i in emotions){
                array.add(emotionselect[i]!!)
            }
            Log.d(TAG, emotionselect.toString())
            Log.d(TAG, array.toString())

            var text=binding.evTextContent.text.toString()

            CalendarFragment.curCalendar!!.addDiary(Day_Diary(date,array[0],array[1],array[2],array[3],array[4],array[5],text=text))

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

    fun to_postemotion(){
        binding.layoutText.isInvisible=true
        binding.layoutEmotion.isInvisible=false

        binding.evTextContent.setInputType(EditorInfo.TYPE_NULL)

    }

    fun to_posttext(){
        binding.layoutText.isInvisible=false
        binding.layoutEmotion.isInvisible=true

        var transFormat = SimpleDateFormat("yyyy.MM.dd.")
        var to = transFormat.format(date)
        binding.tvTextDate.text=to

        binding.ivEmotionToday.setImageResource(selectimage(emotionselect[emotions[0]]!!))
        var i =1
        for(imageview in emotionImageviews){
            while ( i< emotions.size && emotionselect[emotions[i]] == -1){
                i+=1
            }
            if (i< emotions.size){
                imageview.setImageResource(selectimage(emotionselect[emotions[i]]!!))
                i+=1
                continue
            }

            imageview.setImageResource(R.drawable.ic_blankghost)
        }
        binding.evTextContent.setInputType(EditorInfo.TYPE_CLASS_TEXT)
        binding.evTextContent.isCursorVisible=true
    }

    fun selectimage(index:Int): Int {
        when(index){
            0 -> return R.drawable.ghost_verygood
            1 -> return R.drawable.ghost_good
            2 -> return R.drawable.ghost_normal
            3 -> return R.drawable.ghost_bad
            4 -> return R.drawable.ghost_verybad

        }
        return R.drawable.ic_blankghost
    }

}
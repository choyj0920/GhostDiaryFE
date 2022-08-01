package com.example.ghostdiary.fragment.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ghostdiary.R
import com.example.ghostdiary.adapter.AdapterPostdiary
import com.example.ghostdiary.databinding.ActivitySelectEmotionBinding
import com.example.ghostdiary.dataclass.Day_Diary
import java.util.*
import kotlin.collections.HashMap

class SelectEmotionFragment(var date: Date) : Fragment() {


    lateinit var emotions:Array<String>
    lateinit var emotionselect :HashMap<String,Int>
    lateinit var selecttexts:HashMap<String,Array<String>>
    lateinit var adapterPostdiary:AdapterPostdiary
    lateinit var emotionImageviews:Array<ImageView>
    var sleepstart:Int =-1
    var sleepend:Int =-1

    private var binding:ActivitySelectEmotionBinding?=null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding=ActivitySelectEmotionBinding.inflate(inflater,container,false)

        emotionselect= hashMapOf()




        initdata()

        return binding!!.root

    }
    fun initdata(){
        emotions= Day_Diary.emotionname
        selecttexts= Day_Diary.selecttexts

        for( i in emotions){
            emotionselect.put(i,-1)
        }




        update()

    }

    fun update(){

        val emotionListAdapter = AdapterPostdiary(this,emotions,emotionselect,selecttexts)

        adapterPostdiary=emotionListAdapter

        binding!!.rvPostdiary.apply {
            layoutManager=LinearLayoutManager(this.context)
            adapter=adapterPostdiary
        }
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

    fun postemotion(calendarFragment: CalendarFragment) {
        var array:MutableList<Int> = mutableListOf()
        for(i in emotions){
            array.add(emotionselect[i]!!)
        }
        var startsleep:Date? =null
        var endsleep:Date? =null
        if(sleepend != -1 && sleepstart != -1){
            var yesterday = Calendar.getInstance()
            yesterday.time = date
            yesterday.add(Calendar.DATE, -1)
            yesterday.set(Calendar.HOUR, 18)
            yesterday.set(Calendar.MINUTE,0)
            yesterday.set(Calendar.SECOND,0)
            var temp = Calendar.getInstance()
            temp.time=yesterday.time
            temp.add(Calendar.HOUR,sleepstart)
            startsleep=temp.time
            temp.time=yesterday.time
            temp.add(Calendar.HOUR,sleepend)
            endsleep=temp.time

            Log.d("TAG",startsleep.toString()+endsleep.toString())
        }


        calendarFragment.addDiary(Day_Diary(date,array[0],array[1],array[2],array[3],array[4],text="", sleepend = endsleep, sleepstart = startsleep))



    }

}
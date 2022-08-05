package com.example.ghostdiary.fragment.main


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
import com.example.ghostdiary.databinding.FragmentSelectEmotionBinding
import com.example.ghostdiary.dataclass.Day_Diary
import com.example.ghostdiary.dataclass.emotionclass
import java.util.*
import kotlin.collections.HashMap

class SelectEmotionFragment(var parent:Fragment,var date: Date) : Fragment() {


    lateinit var emotions:Array<String>
    lateinit var emotionselect :MutableList<MutableList<emotionclass>>
    lateinit var selecttexts:HashMap<String,Array<String>>
    lateinit var adapterPostdiary:AdapterPostdiary
    lateinit var emotionImageviews:Array<ImageView>
    lateinit var curDiary:Day_Diary

    var sleepstart:Int =-1
    var sleepend:Int =-1

    private var binding:FragmentSelectEmotionBinding ?=null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding=FragmentSelectEmotionBinding.inflate(inflater,container,false)


        emotionselect= mutableListOf()
        for( i in Day_Diary.emotionname){
            if (Day_Diary.emotionarr.contains(i)) {

                var temp = mutableListOf<emotionclass>().apply { addAll(Day_Diary.emotionarr[i]!!) }
                emotionselect.add(temp)
            }

        }


        initdata()

        return binding!!.root

    }
    fun initdata(){
        emotions= Day_Diary.emotionname

        update()
    }

    fun update(){

        val emotionListAdapter = AdapterPostdiary(this,emotions,emotionselect)

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
        var array:MutableList<emotionclass?> = mutableListOf()
        for(i in emotionselect){
            var temp:emotionclass?=null
            for(j in i){
                if(j.isactive){
                    temp=j.clone()
                    break
                }
            }
            array.add(temp)
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

        curDiary= Day_Diary(date,array[0]!!,array[1],array[2],array[3],array[4],text="", sleepend = endsleep, sleepstart = startsleep)


        calendarFragment.addDiary(curDiary)

    }

    fun getcurDiary():Day_Diary{
        var array:MutableList<emotionclass?> = mutableListOf()
        for(i in emotionselect){
            var temp:emotionclass?=null
            for(j in i){
                if(j.isactive){
                    temp=j.clone()
                    break
                }
            }
            array.add(temp)
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

        curDiary= Day_Diary(date,array[0]!!,array[1],array[2],array[3],array[4],text="", sleepend = endsleep, sleepstart = startsleep)


        return curDiary
    }



}
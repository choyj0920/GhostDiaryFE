package com.example.ghostdiary.fragment.main


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ghostdiary.MainViewModel
import com.example.ghostdiary.R
import com.example.ghostdiary.Util
import com.example.ghostdiary.adapter.AdapterPostdiary
import com.example.ghostdiary.databinding.ActivitySelectEmotionBinding
import com.example.ghostdiary.dataclass.Day_Diary
import com.example.ghostdiary.dataclass.emotionclass
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class SelectEmotionFragment(val viewModel :MainViewModel,var date: Date,var editmode:Boolean=false) : AppCompatActivity () {

    lateinit var emotions:Array<String>
    lateinit var emotionselect :ArrayList<ArrayList<emotionclass>>
    lateinit var adapterPostdiary:AdapterPostdiary
    lateinit var curDiary:Day_Diary
    lateinit var rv_linearlayoutmanger : LinearLayoutManager

    var sleepstart:Int =-1
    var sleepend:Int =-1

    private lateinit var binding:ActivitySelectEmotionBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding=ActivitySelectEmotionBinding.inflate(layoutInflater)
        rv_linearlayoutmanger=LinearLayoutManager(this)
        AdapterPostdiary.init_value()


        var formatDate = SimpleDateFormat("yyyy-MM-dd")
        var strdate = formatDate.format(date)
        if(viewModel.getEmotionArray(this).contains(strdate)){
            curDiary=viewModel.getEmotionArray(null)[strdate]!!

            if(curDiary.sleepstart != null && curDiary.sleepend !=null){
                sleepstart=curDiary.sleepstart!!
                sleepend= curDiary.sleepend!!


                if(sleepstart>120 || sleepstart<0||sleepend>120 ||sleepend<0) {
                    sleepstart = -1
                    sleepend = -1
                }

            }


        }else{
            curDiary= Day_Diary(date)
        }

        emotionselect=curDiary.getEmotionarr()


        initdata()
        setContentView(binding.root)
        Util.setGlobalFont(binding!!.root)


    }
    fun switcheditmode(isedit:Boolean){
        var rv_state = binding!!.rvPostdiary.layoutManager?.onSaveInstanceState()
        editmode=isedit
        update()
        binding!!.rvPostdiary.layoutManager?.onRestoreInstanceState(rv_state)


    }

    fun getIgnoreTime(time:Long):Long{
        return Calendar.getInstance().apply {
            timeInMillis=time

            set(Calendar.SECOND,0)
            set(Calendar.MILLISECOND,0)

        }.timeInMillis
    }



    fun initdata(){
        emotions= Day_Diary.emotionname

        update()
    }

    fun update(){

        val emotionListAdapter = AdapterPostdiary(this,sleepstart,sleepend,emotions,emotionselect)

        adapterPostdiary=emotionListAdapter

        binding!!.rvPostdiary.apply {
            layoutManager=LinearLayoutManager(this.context)
            adapter=adapterPostdiary
        }
    }


    fun selectimage(index:Int): Int {
        when(index){
            0 -> return R.drawable.ghost_00_verygood
            1 -> return R.drawable.ghost_01_good
            2 -> return R.drawable.ghost_02_normal
            3 -> return R.drawable.ghost_03_bad
            4 -> return R.drawable.ghost_04_verybad

        }
        return R.drawable.ic_blankghost
    }

    fun postemotion(uptofragment: Fragment) {
        var todayemotion:emotionclass=emotionclass("오류임",0,false)
        for(i in emotionselect[0]){
            if(i.isactive) {
                todayemotion = i
                break
            }
        }


        curDiary= Day_Diary(date, todayemotion,emotionselect[1],emotionselect[2],emotionselect[3], emotionselect[4],this.sleepstart,this.sleepend,curDiary.text, curDiary.image)

        if(uptofragment is CalendarFragment){
//            uptofragment.addDiary(curDiary)

        }else if(uptofragment is RecordFragment){
            uptofragment.addDiary(curDiary)

        }

    }

    fun getcurDiary():Day_Diary{
        var todayemotion:emotionclass=emotionclass("오류임",0,false)
        for(i in emotionselect[0]){
            if(i.isactive) {
                todayemotion = i
                break
            }
        }


        curDiary= Day_Diary(date, todayemotion,emotionselect[1],emotionselect[2],emotionselect[3], emotionselect[4],this.sleepstart,this.sleepend,curDiary.text, curDiary.image)


        return curDiary
    }




}
package com.example.ghostdiary.fragment.postdiary


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ghostdiary.PostDiaryActivity
import com.example.ghostdiary.R
import com.example.ghostdiary.utilpackage.Util
import com.example.ghostdiary.adapter.AdapterPostdiary
import com.example.ghostdiary.databinding.FragmentSelectEmotionBinding
import com.example.ghostdiary.dataclass.Day_Diary
import com.example.ghostdiary.dataclass.emotionclass
import java.util.*
import kotlin.collections.ArrayList

class SelectEmotionFragment(val parent: PostDiaryActivity, var date: Date) : Fragment() {

    lateinit var emotions:Array<String>
    lateinit var emotionselect :ArrayList<ArrayList<emotionclass>>
    lateinit var adapterPostdiary:AdapterPostdiary
    lateinit var curDiary:Day_Diary
    var editmode:Boolean=false
    lateinit var rv_linearlayoutmanger : LinearLayoutManager

    var sleepstart:Int =-1
    var sleepend:Int =-1

    private var binding: FragmentSelectEmotionBinding?=null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        binding=FragmentSelectEmotionBinding.inflate(inflater,container,false)
        rv_linearlayoutmanger=LinearLayoutManager(this.context)


        curDiary=parent.curDiary

        if(curDiary.sleepstart != null && curDiary.sleepend !=null){
            sleepstart=curDiary.sleepstart!!
            sleepend= curDiary.sleepend!!


            if(sleepstart>120 || sleepstart<0||sleepend>120 ||sleepend<0) {
                sleepstart = -1
                sleepend = -1
            }
        }


        emotionselect=curDiary.getEmotionarr()

        binding!!.tvEditmode.setOnClickListener{
            switcheditmode(!editmode)
        }

        binding!!.btnPostcheck.setOnClickListener {
            getcurDiary()
            parent.addDiary()
        }


        initdata()
        switcheditmode(editmode)

        Util.setGlobalFont(binding!!.root)
        return binding!!.root

    }


    fun switcheditmode(isedit:Boolean){
        if(isedit){
            binding!!.tvEditmode.text="완료"
            binding!!.btnPostcheck.visibility=View.GONE
        }else{
            binding!!.tvEditmode.text="수정"
            binding!!.btnPostcheck.visibility=View.VISIBLE

        }
        parent.switchmode_emotion(isedit)


        var rv_state = binding!!.rvPostdiary.layoutManager?.onSaveInstanceState()
        editmode=isedit
        update()
        binding!!.rvPostdiary.layoutManager?.onRestoreInstanceState(rv_state)


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AdapterPostdiary.init_value()

    }

    fun initdata(){
        emotions= Day_Diary.emotionname

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

    fun postemotion() {
        getcurDiary()


    }

    fun getcurDiary():Day_Diary{
        var todayemotion:emotionclass=emotionclass("오류임",0,false)
        for(i in emotionselect[0]){
            if(i.isactive) {
                todayemotion = i
                break
            }
        }
        curDiary.apply {
            this.today_emotion=todayemotion
            whom = emotionselect[1]
            doing = emotionselect[2]
            where=emotionselect[3]
            weather=emotionselect[4]
            sleepstart=this@SelectEmotionFragment.sleepstart
            sleepend=this@SelectEmotionFragment.sleepend

        }

        return curDiary
    }




}
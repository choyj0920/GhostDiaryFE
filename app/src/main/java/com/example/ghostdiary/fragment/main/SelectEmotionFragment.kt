package com.example.ghostdiary.fragment.main


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ghostdiary.MainViewModel
import com.example.ghostdiary.R
import com.example.ghostdiary.adapter.AdapterPostdiary
import com.example.ghostdiary.databinding.FragmentSelectEmotionBinding
import com.example.ghostdiary.dataclass.Day_Diary
import com.example.ghostdiary.dataclass.emotionclass
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.math.round

class SelectEmotionFragment(var parent:Fragment,var date: Date,var editmode:Boolean=false) : Fragment() {

    private val viewModel: MainViewModel by activityViewModels()

    lateinit var emotions:Array<String>
    lateinit var emotionselect :ArrayList<ArrayList<emotionclass>>
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

        var formatDate = SimpleDateFormat("yyyy-MM-dd")
        var strdate = formatDate.format(date)

        if (viewModel.getEmotionArray(requireContext()).contains(strdate)){
            curDiary= viewModel.getEmotionArray(null)[strdate]!!

            if(curDiary.sleepstart != null && curDiary.sleepend !=null){
                var yesterday = Calendar.getInstance()
                var tempstart=Calendar.getInstance()
                tempstart.time=curDiary.sleepstart
                var tempend=Calendar.getInstance()
                tempend.time=curDiary.sleepend
                yesterday.time = date
                yesterday.add(Calendar.DATE, -1)
                yesterday.set(Calendar.HOUR_OF_DAY, 22)
                yesterday.set(Calendar.MINUTE, 0)

                sleepstart= ((getIgnoreTime(curDiary.sleepstart!!.time) - getIgnoreTime(yesterday.time.time))/(1000 * 60 * 10)).toInt()

                sleepend=  ((getIgnoreTime(curDiary.sleepend!!.time) - getIgnoreTime(yesterday.time.time))/(1000 * 60 * 10)).toInt()

                if(sleepstart>72 || sleepstart<0||sleepend>72 ||sleepend<0) {
                    sleepstart = -1
                    sleepend = -1
                }

            }


        }else{
            curDiary= Day_Diary(date)
        }

        emotionselect=curDiary.getEmotionarr()


        initdata()

        return binding!!.root

    }
    fun switcheditmode(isedit:Boolean){
        editmode=isedit
        update()
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
            0 -> return R.drawable.ghost_verygood
            1 -> return R.drawable.ghost_good
            2 -> return R.drawable.ghost_normal
            3 -> return R.drawable.ghost_bad
            4 -> return R.drawable.ghost_verybad

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
        var startsleep:Date? =null
        var endsleep:Date? =null
        if(sleepend != -1 && sleepstart != -1){
            var yesterday = Calendar.getInstance()
            yesterday.time = date
            yesterday.add(Calendar.DAY_OF_MONTH, -1)
            yesterday.set(Calendar.HOUR_OF_DAY, 22)
            yesterday.set(Calendar.MINUTE,0)
            yesterday.set(Calendar.SECOND,0)
            var temp = Calendar.getInstance()
            temp.time=yesterday.time
            temp.add(Calendar.MINUTE,sleepstart*10)
            startsleep=temp.time
            temp.time=yesterday.time
            temp.add(Calendar.MINUTE,sleepend*10)
            endsleep=temp.time

            Log.d("TAG",startsleep.toString()+endsleep.toString())
        }

        curDiary= Day_Diary(date, todayemotion,emotionselect[1],emotionselect[2],emotionselect[3], emotionselect[4],startsleep,endsleep,curDiary.text, curDiary.image)

        if(uptofragment is CalendarFragment){
            uptofragment.addDiary(curDiary)

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
        var startsleep:Date? =null
        var endsleep:Date? =null
        if(sleepend != -1 && sleepstart != -1){
            var yesterday = Calendar.getInstance()
            yesterday.time = date
            yesterday.add(Calendar.DATE, -1)
            yesterday.set(Calendar.HOUR_OF_DAY, 22)
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

        curDiary= Day_Diary(date, todayemotion,emotionselect[1],emotionselect[2],emotionselect[3], emotionselect[4],startsleep,endsleep,curDiary.text, curDiary.image)


        return curDiary
    }




}
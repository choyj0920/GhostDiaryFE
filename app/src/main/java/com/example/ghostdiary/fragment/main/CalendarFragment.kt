package com.example.ghostdiary.fragment.main

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView

import android.widget.TextView
import androidx.core.view.isInvisible
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ghostdiary.MainActivity
import com.example.ghostdiary.MainViewModel
import com.example.ghostdiary.PostDiaryActivity
import com.example.ghostdiary.R
import com.example.ghostdiary.adapter.AdapterDay
import com.example.ghostdiary.databinding.FragmentCalendarBinding
import com.example.ghostdiary.dataclass.Day_Diary
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class CalendarFragment : Fragment() {
    private lateinit var calendar :Calendar
    companion object {
        var curCalendar: CalendarFragment?=null
    }
    private val viewModel: MainViewModel by activityViewModels()
    var pageIndex = 0
    lateinit var currentDate: Date
    lateinit var dayList:MutableList<Date>
    lateinit var mContext: Context
    lateinit var calendar_year_month_text: TextView
    lateinit var calendar_view: RecyclerView
    lateinit var calendarAdapter: AdapterDay
    lateinit var emotionImageviews:Array<ImageView>
    private var binding: FragmentCalendarBinding? =null
    var isshow = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        calendar = viewModel.calendar
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        Log.d("TAG","current Date: ${calendar.get(Calendar.YEAR)}/${calendar.get(Calendar.MONTH)}/${calendar.get(Calendar.DAY_OF_MONTH)}")

//        calendar.add(Calendar.MONTH, position - center)

        binding=FragmentCalendarBinding.inflate(inflater,container,false)

        initView()
        curCalendar =this

        create_days()

        return binding!!.root
    }

    fun addMonth(add:Int){

        calendar.add(Calendar.MONTH,add)

        create_days()

    }



    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MainActivity) {
            mContext = context
        }
    }



    fun create_days(){
        dayList = MutableList(6 * 7) { Date() }
        var _calendar=Calendar.getInstance()
        _calendar.set(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DATE))
        for(i in 0..5) {
            for(k in 0..6) {
                _calendar.add(Calendar.DAY_OF_MONTH, (1-_calendar.get(Calendar.DAY_OF_WEEK)) + k)
                dayList[i * 7 + k] = _calendar.time
            }
            _calendar.add(Calendar.WEEK_OF_MONTH, 1)
            Log.d("TAG","${Calendar.WEEK_OF_MONTH} , ${1}")

        }
        Log.d("TAG","${dayList}")



        updatecalendar()

    }
    fun addDiary(newDiary:Day_Diary){
        var day=newDiary.date
        var transFormat = SimpleDateFormat("yyyyMMdd")
        var to = transFormat.format(day)
        viewModel.getEmotionArray().put(to, newDiary)
        Log.d("TAG","addDiary ${newDiary}")
        updatecalendar()

    }
    fun updatecalendar(){
        var transFormat = SimpleDateFormat("yyyy/MM")
        var to = transFormat.format(calendar.time)
        calendar_year_month_text.setText(to)
        val tempMonth = calendar.get(Calendar.MONTH)

        val dayListManager = GridLayoutManager(context, 7)
        val dayListAdapter = AdapterDay(this,tempMonth, dayList, viewModel.getEmotionArray())

        calendarAdapter=dayListAdapter


        calendar_view.apply {
            layoutManager=dayListManager
            adapter=dayListAdapter
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // TODO: Use the ViewModel
        binding!!.ivTodayemotionhint.isInvisible =viewModel.ishintinvisible
    }

    override fun onDestroyView() {
        binding=null
        super.onDestroyView()
    }

    fun initView() {
        binding!!.ivTodayemotionhint.setOnClickListener{
            viewModel.ishintinvisible= true
            binding!!.ivTodayemotionhint.isInvisible=viewModel.ishintinvisible
        }
        emotionImageviews= arrayOf(binding!!.ivEmotionToday,binding!!.ivEmotion1,binding!!.ivEmotion2,binding!!.ivEmotion3,binding!!.ivEmotion4)
        pageIndex -= (Int.MAX_VALUE / 2)
        //Log.e(TAG, "Calender Index: $pageIndex")
        calendar_year_month_text = binding!!.tvDate
        calendar_view = binding!!.calendar
        // 날짜 적용
        val formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd")
        val datetime =LocalDateTime.now().format(formatter)

        calendar_year_month_text.setText(datetime)

        binding!!.btnLastmonth.setOnClickListener {
            addMonth(-1)
        }
        binding!!.btnNextmonth.setOnClickListener {
            addMonth(1)
        }



    }



    fun start_post(day:Date){
        var intent = Intent(getActivity(),PostDiaryActivity::class.java)
        intent.putExtra("Date",day.time)
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        startActivity(intent)
    }
    fun show_post(day:Date){
        if(isshow)
            return

        isshow=true
        var transFormat = SimpleDateFormat("yyyyMMdd")
        var to = transFormat.format(day)

        var diary= viewModel.getEmotionArray()[to]
        var emotions= diary!!.getEmotionarr()
        Log.d("TAG","addDiary ${diary.getEmotionarr_name()} ${emotions}")

        binding!!.ivEmotionToday.setImageResource(selectimage(emotions[0]))
        var i =1
        for(imageview in emotionImageviews){
            if(emotions.size<=i)
                break
            imageview.setImageResource(selectimage(emotions[i]))
            i+=1
        }


        binding!!.tvPopupDate.text=SimpleDateFormat("MMMM yyyy dd").format(day)

        val animup = AnimationUtils.loadAnimation(
            context,R.anim.popup_ani);
        binding!!.popupLayout.startAnimation(animup)

        binding!!.popupLayout.setOnClickListener{
            if (isshow)
                down_post()
        }
        binding!!.tvPopupText.text=diary.text

        binding!!.popupLayout.visibility=View.VISIBLE

    }

    fun down_post(){
        if(!isshow)
            return
        isshow=false
        binding!!.popupLayout.visibility=View.GONE

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
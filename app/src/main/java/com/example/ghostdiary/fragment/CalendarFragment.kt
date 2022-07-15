package com.example.ghostdiary.fragment

import android.content.ContentValues.TAG
import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.isInvisible
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ghostdiary.MainActivity
import com.example.ghostdiary.R
import com.example.ghostdiary.adapter.AdapterDay
import com.example.ghostdiary.databinding.FragmentCalendarBinding
import com.example.ghostdiary.dataclass.Day_Diary
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.HashMap

class CalendarFragment : Fragment() {
    private var calendar = Calendar.getInstance()

    companion object {
        fun newInstance() = CalendarFragment()
    }
    var pageIndex = 0
    lateinit var currentDate: Date
    lateinit var mContext: Context

    lateinit var calendar_year_month_text: TextView
    lateinit var calendar_view: RecyclerView
    lateinit var calendarAdapter: AdapterDay
    var calendar_emotionArray: HashMap<String,Day_Diary>  = HashMap<String, Day_Diary>()

    private lateinit var viewModel: CalendarViewModel
    private var binding: FragmentCalendarBinding? =null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        calendar.time = Date()
        calendar.set(Calendar.DAY_OF_MONTH, 1)
//        calendar.add(Calendar.MONTH, position - center)

        binding=FragmentCalendarBinding.inflate(inflater,container,false)
        initView()


        create_days()

        return binding!!.root
    }



    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MainActivity) {
            mContext = context
        }
    }


    fun create_days(){
        var dayList: MutableList<Date> = MutableList(6 * 7) { Date() }
        for(i in 0..5) {
            for(k in 0..6) {
                calendar.add(Calendar.DAY_OF_MONTH, (1-calendar.get(Calendar.DAY_OF_WEEK)) + k)
                dayList[i * 7 + k] = calendar.time
            }
            calendar.add(Calendar.WEEK_OF_MONTH, 1)
            Log.d("TAG","${Calendar.WEEK_OF_MONTH} , ${1}")

        }
        Log.d("TAG","${dayList}")



        this.calendar_emotionArray = HashMap<String, Day_Diary>()
        var instant = LocalDate.of(2022,7,10).atStartOfDay(ZoneId.systemDefault()).toInstant()
        var date = Date.from(instant);


        calendar_emotionArray.put("20220710" ,Day_Diary(date,2))
        date=Date.from(LocalDate.of(2022,7,14).atStartOfDay(ZoneId.systemDefault()).toInstant())
        calendar_emotionArray.put("20220714" , Day_Diary(date,1))
        date=Date.from(LocalDate.of(2022,7,15).atStartOfDay(ZoneId.systemDefault()).toInstant())
        calendar_emotionArray.put("20220715" ,Day_Diary(date,0, text = "배고프다."))
        date=Date.from(LocalDate.of(2022,7,9).atStartOfDay(ZoneId.systemDefault()).toInstant())
        calendar_emotionArray.put("20220709" ,Day_Diary(date,3))

        val tempMonth = calendar.get(Calendar.MONTH)
        Log.d(TAG,"${tempMonth} why!@!@!@#@@!")


        val dayListManager = GridLayoutManager(context, 7)
        val dayListAdapter = AdapterDay(tempMonth, dayList,calendar_emotionArray)

        calendarAdapter=dayListAdapter



        calendar_view.apply {
            layoutManager=dayListManager
            adapter=dayListAdapter
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(CalendarViewModel::class.java)
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

        pageIndex -= (Int.MAX_VALUE / 2)
        Log.e(TAG, "Calender Index: $pageIndex")
        calendar_year_month_text = binding!!.tvDate
        calendar_view = binding!!.calendar
        // 날짜 적용
        val formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd")
        val datetime =LocalDateTime.now().format(formatter)

        calendar_year_month_text.setText(datetime)
    }

}
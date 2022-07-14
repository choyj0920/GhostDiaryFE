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
import androidx.recyclerview.widget.RecyclerView
import com.example.ghostdiary.MainActivity
import com.example.ghostdiary.R
import com.example.ghostdiary.adapter.CalendarAdapter
import com.example.ghostdiary.databinding.FragmentCalendarBinding
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class CalendarFragment : Fragment() {

    companion object {
        fun newInstance() = CalendarFragment()
    }
    var pageIndex = 0
    lateinit var currentDate: Date
    lateinit var mContext: Context

    lateinit var calendar_year_month_text: TextView
    lateinit var calendar_view: RecyclerView
    lateinit var calendarAdapter: CalendarAdapter

    private lateinit var viewModel: CalendarViewModel
    private var binding: FragmentCalendarBinding? =null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        binding=FragmentCalendarBinding.inflate(inflater,container,false)
        initView()
        return binding!!.root
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MainActivity) {
            mContext = context
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(CalendarViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onDestroyView() {
        binding=null
        super.onDestroyView()
    }

    fun initView() {
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
package com.example.ghostdiary.fragment.main

import android.app.AlertDialog

import android.content.ContentValues.TAG
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.core.content.ContextCompat

import androidx.core.view.isInvisible
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ghostdiary.MainActivity
import com.example.ghostdiary.MainViewModel
import com.example.ghostdiary.fragment.main.SelectEmotionFragment
import com.example.ghostdiary.R
import com.example.ghostdiary.adapter.AdapterDay
import com.example.ghostdiary.adapter.EmotionSpinnerAdapter
import com.example.ghostdiary.databinding.DialogTodayemotionBinding
import com.example.ghostdiary.databinding.FragmentCalendarBinding
import com.example.ghostdiary.databinding.FragmentMonthpickerBinding
import com.example.ghostdiary.dataclass.Day_Diary
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

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
    private var binding: FragmentCalendarBinding? =null
    var emotionpostion:Int=-1
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

    fun setMonth(year:Int,month:Int){
        calendar.set(year,month-1,1)
        create_days()
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MainActivity) {
            mContext = context
        }
    }

    fun selecttoday_emotion(){


    }
    fun init_spinner(){
        val array= arrayListOf<Int>(-1,0,1,2,3,4)
        val adapter=EmotionSpinnerAdapter(requireContext(),array)

        binding!!.spinnerEmotion.adapter=adapter

        binding!!.spinnerEmotion.onItemSelectedListener= object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                emotionpostion=array.get(position)

                updatecalendar()

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

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
        var transFormat = SimpleDateFormat("yyyy-MM-dd")
        var to = transFormat.format(day)
        viewModel.getEmotionArray().put(to, newDiary)
        viewModel.getdb(null).insertDiary(newDiary)
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

    }

    override fun onDestroyView() {
        binding=null
        super.onDestroyView()
    }

    fun initmonthpicker(){
        //  날짜 dialog
        binding!!.tvDate.setOnClickListener {

            val edialog : LayoutInflater = LayoutInflater.from(context)
            val dialogbinding:FragmentMonthpickerBinding=FragmentMonthpickerBinding.inflate(edialog)
            val mView : View = dialogbinding.root

            val year : NumberPicker = dialogbinding.yearpickerDatepicker
            val month : NumberPicker = dialogbinding.monthpickerDatepicker

            val dialog = AlertDialog.Builder(context).apply {
                setPositiveButton("확인",
                    DialogInterface.OnClickListener { dialog, i ->

                        setMonth(year.value, month.value)
                        dialog.dismiss()
                        dialog.cancel()
                    })
                setNegativeButton("취소",
                    DialogInterface.OnClickListener { dialog, i ->
                        dialog.dismiss()
                        dialog.cancel()

                    })}.create()


            //  순환 안되게 막기
            year.wrapSelectorWheel = false
            month.wrapSelectorWheel = false

            //  editText 설정 해제
            year.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
            month.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS

            //  최소값 설정
            year.minValue = 2001
            month.minValue = 1

            //  최대값 설정
            year.maxValue = 2080
            month.maxValue = 12

            // 값 설정
            year.value=calendar.get(Calendar.YEAR)
            month.value=calendar.get(Calendar.MONTH)+1

            dialog.setView(mView)
            dialog.create()
            dialog.show()


        }
    }

    fun initView() {

//        emotionImageviews= arrayOf(binding!!.ivEmotionToday,binding!!.ivEmotion1,binding!!.ivEmotion2,binding!!.ivEmotion3,binding!!.ivEmotion4)
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
        initmonthpicker()

        init_spinner()


    }


    fun start_post(day:Date){
        var intent = Intent(getActivity(), SelectEmotionFragment::class.java)
        intent.putExtra("Date",day.time)
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        startActivity(intent)
    }
    fun show_post(day:Date){

    }

    fun down_post(){


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
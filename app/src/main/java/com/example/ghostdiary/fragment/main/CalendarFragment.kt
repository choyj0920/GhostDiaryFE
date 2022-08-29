package com.example.ghostdiary.fragment.main

import android.app.AlertDialog

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
import android.widget.*
import androidx.annotation.NonNull

import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.ghostdiary.MainActivity
import com.example.ghostdiary.MainViewModel
import com.example.ghostdiary.R
import com.example.ghostdiary.Util
import com.example.ghostdiary.adapter.AdapterDay
import com.example.ghostdiary.adapter.AdapterMonth
import com.example.ghostdiary.adapter.EmotionSpinnerAdapter
import com.example.ghostdiary.databinding.FragmentCalendarBinding
import com.example.ghostdiary.databinding.FragmentMonthpickerBinding
import com.example.ghostdiary.databinding.ViewGhostspinnerBinding
import com.example.ghostdiary.dataclass.Day_Diary
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class CalendarFragment : Fragment() {
    companion object {
        var curCalendar: CalendarFragment?=null
    }
    val viewModel: MainViewModel by activityViewModels()
    var pageIndex = 0
    lateinit var mContext: Context
    private var binding: FragmentCalendarBinding? =null
    var emotionpostion:Int=-1
    val snap = PagerSnapHelper()
    var lastcalendar:Calendar=Calendar.getInstance()
    var curpo:Int=0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        binding=FragmentCalendarBinding.inflate(inflater,container,false)

        initView()
        curCalendar =this

        Util.setGlobalFont(binding!!.root)

        snap.attachToRecyclerView(binding!!.rvMonth)
        return binding!!.root
    }

    override fun onStart() {
        MainActivity.mainactivity.switchHidetopmenu(false)

        super.onStart()
    }




    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MainActivity) {
            mContext = context
        }
    }

    fun init_rv(){


        val onScrollListener = object:RecyclerView.OnScrollListener() {
            override fun onScrolled(@NonNull recyclerView:RecyclerView, dx:Int, dy:Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (recyclerView.layoutManager != null) {
                    val view = snap.findSnapView(recyclerView.layoutManager)!!
                    val position = recyclerView.layoutManager!!.getPosition(view)
                    if (curpo != position) {
                        curpo = position
                        var center=Int.MAX_VALUE / 2
                        var newcal=Calendar.getInstance()
                        newcal.time=lastcalendar.time
                        newcal.add(Calendar.MONTH, curpo - center)
                        viewModel.calendar =newcal
                    }
                }

            }
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

            }
        }
        binding!!.rvMonth.let {
            it.clearOnScrollListeners()
            it.addOnScrollListener(onScrollListener)
        }
        lastcalendar.time=viewModel.calendar.time

        val monthListManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        val monthListAdapter = AdapterMonth(this,viewModel)

        binding!!.rvMonth.apply {
            layoutManager = monthListManager
            adapter = monthListAdapter
            scrollToPosition(Int.MAX_VALUE/2)
        }



    }

    fun init_spinner(){
        val array= arrayListOf<Int>(-1,0,1,2,3,4)
        val adapter=EmotionSpinnerAdapter(MainActivity.mainactivity,array)

        binding!!.spinnerEmotion.adapter=adapter


        binding!!.spinnerEmotion.onItemSelectedListener= object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                emotionpostion=array.get(position)

                init_rv()

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

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
        /*
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


        }*/
    }

    fun initView() {

        initmonthpicker()

        init_spinner()
        init_rv()


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
            0 -> return R.drawable.ghost_00_verygood
            1 -> return R.drawable.ghost_01_good
            2 -> return R.drawable.ghost_02_normal
            3 -> return R.drawable.ghost_03_bad
            4 -> return R.drawable.ghost_04_verybad

        }
        return R.drawable.ic_blankghost
    }

}
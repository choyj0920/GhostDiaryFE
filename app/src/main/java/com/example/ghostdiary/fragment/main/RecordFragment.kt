package com.example.ghostdiary.fragment.main

import android.app.AlertDialog
import android.content.DialogInterface
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ghostdiary.MainViewModel
import com.example.ghostdiary.R
import com.example.ghostdiary.adapter.AdapterDiary
import com.example.ghostdiary.adapter.AdapterEmotionjustview
import com.example.ghostdiary.databinding.FragmentCalendarBinding
import com.example.ghostdiary.databinding.FragmentMonthpickerBinding
import com.example.ghostdiary.databinding.FragmentRecordBinding
import com.example.ghostdiary.dataclass.Day_Diary
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class RecordFragment : Fragment() {
    private lateinit var calendar :Calendar
    companion object {
        fun newInstance() = RecordFragment()
    }
    private val viewModel: MainViewModel by activityViewModels()
    private var binding: FragmentRecordBinding?=null
    private lateinit var monthDiary: ArrayList<Day_Diary>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        calendar=Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        binding= FragmentRecordBinding.inflate(inflater,container,false)

        init()

        return binding!!.root
    }

    fun init(){

        var transFormat = SimpleDateFormat("yyyy/MM")
        var to = transFormat.format(Calendar.getInstance().time)
        binding!!.tvDate.setText(to)

        binding!!.btnLastmonth.setOnClickListener {
            addMonth(-1)
        }
        binding!!.btnNextmonth.setOnClickListener {
            addMonth(1)
        }
        initmonthpicker()

        update()


    }

    fun addMonth(add:Int){

        calendar.add(Calendar.MONTH,add)
        update()
    }

    fun setMonth(year:Int,month:Int){
        calendar.set(year,month-1,1)
        update()
    }

    fun initmonthpicker(){
        //  날짜 dialog
        binding!!.tvDate.setOnClickListener {

            val edialog : LayoutInflater = LayoutInflater.from(context)
            val dialogbinding: FragmentMonthpickerBinding = FragmentMonthpickerBinding.inflate(edialog)
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
    fun update(){

        var transFormat1 = SimpleDateFormat("yyyy/MM")
        var to1 = transFormat1.format(calendar.time)
        binding!!.tvDate.setText(to1)

        monthDiary= arrayListOf()
        var transFormat = SimpleDateFormat("yyyy-MM")
        var to = transFormat.format(calendar.time)
        for ((key,value) in viewModel.getEmotionArray()){
            if(key.slice(IntRange(0,6)).equals(to)){

                monthDiary.add(value)
            }
        }

        var _adapter = AdapterDiary(this,monthDiary)

        binding!!.rvRecord.apply {
            layoutManager= LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL,false)
            adapter=_adapter
        }

    }



    override fun onDestroyView() {
        binding=null
        super.onDestroyView()
    }

}
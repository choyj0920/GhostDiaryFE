package com.example.ghostdiary.fragment.calendar

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.NonNull

import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.ghostdiary.*
import com.example.ghostdiary.adapter.AdapterMonth
import com.example.ghostdiary.adapter.EmotionSpinnerAdapter
import com.example.ghostdiary.databinding.FragmentCalendarBinding
import com.example.ghostdiary.databinding.FragmentMonthpickerBinding
import com.example.ghostdiary.fragment.postdiary.SelectEmotionFragment
import java.util.*

class CalendarFragment : Fragment() {
    companion object {
        var curCalendar: CalendarFragment?=null
    }
    val viewModel: MainViewModel by activityViewModels()
    var pageIndex = 0
    var curCal=Calendar.getInstance()
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
        init_rv()
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
                        curCal.time=newcal.time
                        if(curpo-center >10 ||curpo-center<-10){
                            initmonthpicker()
                            return
                        }

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
        curCal.time=lastcalendar.time

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
    fun update_rv(){
        viewModel.calendar.set(Calendar.YEAR,curCal.get(Calendar.YEAR))
        viewModel.calendar.set(Calendar.MONTH,curCal.get(Calendar.MONTH))
        init_rv()
    }

    fun initmonthpicker(){
        //  날짜 dialog

        val edialog : LayoutInflater = LayoutInflater.from(context)
        val dialogbinding: FragmentMonthpickerBinding =FragmentMonthpickerBinding.inflate(edialog)
        val mView : View = dialogbinding.root

        val year : NumberPicker = dialogbinding.yearpickerDatepicker
        val month : NumberPicker = dialogbinding.monthpickerDatepicker

        val dialog = AlertDialog.Builder(context).apply {
            setPositiveButton("확인",
                DialogInterface.OnClickListener { dialog, i ->

                    dialog.dismiss()
                    dialog.cancel()
                    viewModel.calendar.set(Calendar.YEAR,year.value)
                    viewModel.calendar.set(Calendar.MONTH,month.value-1)
                    init_rv()
                })
            setNegativeButton("취소",
                DialogInterface.OnClickListener { dialog, i ->
                    dialog.dismiss()
                    dialog.cancel()
                    viewModel.calendar.set(Calendar.YEAR,curCal.get(Calendar.YEAR))
                    viewModel.calendar.set(Calendar.MONTH,curCal.get(Calendar.MONTH))
                    init_rv()

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
        year.value= curCal.get(Calendar.YEAR)
        month.value=curCal.get(Calendar.MONTH)+1

        dialog.setView(mView)
        dialog.create()
        dialog.show()


    }

    fun initView() {


        init_spinner()
        init_rv()
        binding!!.ivPost.setOnClickListener{
            val intent = Intent(requireContext(), PostDiaryActivity::class.java)
            intent.putExtra("date",Calendar.getInstance().time.time)
            intent.putExtra("firsttext",false)
            startActivity(intent)
        }
        binding!!.ivAnalyze.setOnClickListener {
            val intent = Intent(requireContext(), AnalyzeActivity::class.java)
            startActivity(intent)
        }


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
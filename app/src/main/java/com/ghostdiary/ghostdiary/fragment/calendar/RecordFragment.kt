package com.ghostdiary.ghostdiary.fragment.calendar

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.TypedValue
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.AdapterView
import android.widget.PopupWindow
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.ghostdiary.ghostdiary.*
import com.ghostdiary.ghostdiary.adapter.AdapterDiary
import com.ghostdiary.ghostdiary.adapter.EmotionSpinnerAdapter
import com.ghostdiary.ghostdiary.databinding.DialogSearchBinding
import com.ghostdiary.ghostdiary.databinding.FragmentRecordBinding
import com.ghostdiary.ghostdiary.databinding.MenuSideoptionBinding
import com.ghostdiary.ghostdiary.dataclass.Day_Diary
import com.ghostdiary.ghostdiary.utilpackage.Util
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
    private lateinit var filltermonthDiary: ArrayList<Day_Diary>
    var emotionpostion:Int=-1
    var searchstring:String=""
    private var isrevse=true
    var curCal=Calendar.getInstance()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding= FragmentRecordBinding.inflate(inflater,container,false)
        calendar=viewModel.calendar
        curCal.time=calendar.time


        init()
        Util.setGlobalFont(binding!!.root)

        return binding!!.root
    }

    override fun onStart() {
        super.onStart()
        calendar=viewModel.calendar
        curCal.time=calendar.time


        update()
        Util.setGlobalFont(binding!!.root)

    }fun updatefont(){


        Util.setGlobalFont(binding!!.root)
        initmonthpicker()
        init_spinner()

        update()

    }


    @SuppressLint("ClickableViewAccessibility")
    fun init(){

        var transFormat = SimpleDateFormat("yyyy.MM.")
        var to = transFormat.format(calendar.time)
        binding!!.tvDate.setText(to)

        var toutchlistener = View.OnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    v.setBackgroundResource(R.drawable.circle_backgroud_select)
                }
                MotionEvent.ACTION_UP -> {
                    v.setBackgroundColor(Color.parseColor("#00000000"))
                    if (v.id == binding!!.ivSwap.id) {
                        isrevse = !isrevse
                        update()
                    }
                }
            }
            false
        }
        binding!!.ivSwap.setOnTouchListener(toutchlistener)

        initmonthpicker()
        init_spinner()
        init_searchbar()

        update()


    }

    fun init_searchbar(){
        val popupInflater =
            requireActivity().applicationContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupBind = DialogSearchBinding.inflate(popupInflater)


        val popupWindow = PopupWindow(
            popupBind.root,dpToPx(requireContext(),150f).toInt() ,dpToPx(requireContext(),36f).toInt()  , true
        ).apply { contentView.setOnClickListener { dismiss() }
            popupBind.ivErase.setOnClickListener {
                popupBind.inputSearch.setText("")
                dismiss()

            }
            popupBind.inputSearch.setText(searchstring)
            popupBind.inputSearch.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun afterTextChanged(p0: Editable?) {


                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                    searchstring=popupBind.inputSearch.text.toString()
                    filter_rv()

                }
            })

        }
        Util.setGlobalFont(popupBind.root)
        // make sure you use number than wrap_content or match_parent,
        // because for me it is not showing anything if I set it to wrap_content from ConstraintLayout.LayoutParams.


        binding!!.ivSearch.setOnClickListener{
            var loc:IntArray= intArrayOf(0,0)
            binding!!.ivSearch.getLocationOnScreen(loc)
            popupWindow.showAtLocation(binding!!.ivSearch, Gravity.NO_GRAVITY, loc[0]-popupWindow.width+binding!!.ivSearch.width, loc[1]);
        }
    }

    fun addMonth(add:Int){

        calendar.add(Calendar.MONTH,add)
        update()
    }

    fun setMonth(year:Int,month:Int){
        calendar.set(year,month,1)


        update()
    }

    fun initmonthpicker(){
        //  날짜 dialog
        binding!!.tvDate.setOnClickListener {

            val dialog = MonthSelectDialog(this,calendar)
            dialog.isCancelable = true
            dialog.show(requireActivity().supportFragmentManager, "ConfirmDialog")


        }
    }
    fun addDiary(newDiary:Day_Diary){
        viewModel.addDiary(newDiary)
        update()
    }


    fun update(){

        calendar=viewModel.calendar
        curCal.time=calendar.time


        var transFormat1 = SimpleDateFormat("yyyy.MM.")
        var to1 = transFormat1.format(calendar.time)
        binding!!.tvDate.setText(to1)

        monthDiary= arrayListOf()
        var transFormat = SimpleDateFormat("yyyy-MM")
        var to = transFormat.format(calendar.time)
        for ((key,value) in viewModel.getEmotionArray()){
            if(key.slice(IntRange(0,6)).equals(to)){
                if(emotionpostion==-1 || emotionpostion==value.today_emotion.ghostimage) {
                    monthDiary.add(value)
                }
            }
        }
        monthDiary.sortBy { dayDiary -> dayDiary.date}
        if(isrevse)
            monthDiary.reverse()

        filter_rv()

    }
    fun filter_rv(){
        filltermonthDiary= arrayListOf()
        if(searchstring==""){
            filltermonthDiary.addAll(monthDiary)
        }else{
            for(diary in monthDiary){
                if(diary.ishavetext(searchstring)){
                    filltermonthDiary.add(diary)
                }

            }
        }

        filltermonthDiary.sortBy { dayDiary -> dayDiary.date}
        if(isrevse)
            filltermonthDiary.reverse()

        var _adapter = AdapterDiary(this,filltermonthDiary)

        binding!!.rvRecord.apply {
            layoutManager= LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL,false)
            adapter=_adapter
        }


    }


    fun init_spinner() {
        val array = arrayListOf<Int>(-1, 0, 1, 2, 3, 4)
        val adapter = EmotionSpinnerAdapter(requireContext(), array)

        binding!!.spinnerEmotion.adapter = adapter

        binding!!.spinnerEmotion.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    emotionpostion = array.get(position)
                    update()

                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                }

            }
    }


    override fun onDestroyView() {
        binding=null
        super.onDestroyView()
    }

    fun deleteDiary(date: Date) {
        viewModel.deleteDiary(date)
        update()

    }
    fun dpToPx(context: Context, dp: Float): Float {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.resources.displayMetrics)
    }
}
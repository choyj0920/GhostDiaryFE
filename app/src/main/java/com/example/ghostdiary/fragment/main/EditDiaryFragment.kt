package com.example.ghostdiary.fragment.main


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.ghostdiary.MainActivity
import com.example.ghostdiary.MainViewModel
import com.example.ghostdiary.R
import com.example.ghostdiary.adapter.AdapterPostdiary
import com.example.ghostdiary.databinding.FragmentEditDiaryBinding
import com.example.ghostdiary.dataclass.Day_Diary
import com.example.ghostdiary.dataclass.emotionclass
import java.text.SimpleDateFormat
import java.util.*

class EditDiaryFragment(var parent:Fragment,var date: Date,var diary: Day_Diary?=null) : Fragment() {

        private val viewModel: MainViewModel by activityViewModels()
    lateinit var emotions:Array<String>
    lateinit var emotionselect :MutableList<MutableList<emotionclass>>
    lateinit var selecttexts:HashMap<String,Array<String>>
    lateinit var adapterPostdiary:AdapterPostdiary
    lateinit var emotionImageviews:Array<ImageView>
    var sleepstart:Int =-1
    var sleepend:Int =-1
    lateinit var curDiary:Day_Diary

    private var binding:FragmentEditDiaryBinding?=null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding=FragmentEditDiaryBinding.inflate(inflater,container,false)

        init()

        return binding!!.root

    }
    fun init(){
        binding!!.inputText.requestFocus()
        if(diary==null){
            curDiary= Day_Diary(date,3)

        }else{
            curDiary=diary!!
        }

        var transFormat = SimpleDateFormat("yyyy/MM/dd")
        var to = transFormat.format(date)
        binding!!.tvDate.text=to

        binding!!.btnCancel.setOnClickListener {
            if(diary==null){
                MainActivity.mainactivity.change_record()

            }else{
                // -----

            }
        }

        binding!!.btnPost.setOnClickListener{

            curDiary.text=binding!!.inputText.text.toString()


            if(parent is CalendarFragment){
                var calendarparent=   parent as CalendarFragment
                calendarparent.addDiary(curDiary)
                MainActivity.mainactivity.change_calendar()

            }else if(parent is RecordFragment){
                var recordparent=parent as RecordFragment
                // recordparent.update()
            }

        }

        binding!!.btnImage.setOnClickListener {

        }

        binding!!.btnClock.setOnClickListener {
            timestamp()

        }



    }

    fun timestamp(){
        var editText=  binding!!.inputText
        var transFormat = SimpleDateFormat("hh:mm")
        var calendar= Calendar.getInstance()
        var text = transFormat.format(calendar.time)


        val fullTxt: String = editText.getText().toString()

        // 커서의 현재 위치
        // 커서의 현재 위치
        val pos: Int = editText.getSelectionStart()

        // 에디트에 텍스트가 하나도 없는 경우

        // 에디트에 텍스트가 하나도 없는 경우
        if (fullTxt.isEmpty()) {

            editText.setText(text)

            // 커서가 눈에 보이게 한다.
            editText.requestFocus()
        } else editText.setText(
            fullTxt.substring(
                0,
                pos
            ) + text.toString() + fullTxt.substring(pos)
        )

        // 편집이 편하도록 커서를 삽입한 텍스트 끝에 위치시킨다.


        // 편집이 편하도록 커서를 삽입한 텍스트 끝에 위치시킨다.
        editText.setSelection(pos + text.length)
    }


    override fun onDestroyView() {
        binding=null
        super.onDestroyView()
    }

    fun update(){



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

    fun postDiary(calendarFragment: CalendarFragment) {


        calendarFragment.addDiary(curDiary)



    }


}
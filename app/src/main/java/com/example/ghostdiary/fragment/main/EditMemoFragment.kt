package com.example.ghostdiary.fragment.main


import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ghostdiary.MainActivity
import com.example.ghostdiary.MainViewModel
import com.example.ghostdiary.R
import com.example.ghostdiary.Util
import com.example.ghostdiary.adapter.AdapterEmotionjustview
import com.example.ghostdiary.databinding.FragmentEditDiaryBinding
import com.example.ghostdiary.databinding.FragmentEditMemoBinding
import com.example.ghostdiary.dataclass.Day_Diary
import com.example.ghostdiary.dataclass.Memo
import com.example.ghostdiary.dataclass.emotionclass
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class EditMemoFragment(var parent:MemoSelectFragment,var folder_id:Int,var memo: Memo?) : Fragment() {

    private val viewModel: MainViewModel by activityViewModels()

    lateinit var curMemo: Memo

    private var binding:FragmentEditMemoBinding?=null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding=FragmentEditMemoBinding.inflate(inflater,container,false)

        init()
        Util.setGlobalFont(binding!!.root)

        return binding!!.root

    }
    fun init(){
        if(memo==null){
            curMemo=Memo(folder_id,"","", memoid = -1)
        }else{
            curMemo=memo!!

        }
        binding!!.inputTitle.setText(curMemo.title)
        binding!!.inputText.setText(curMemo.text)

        binding!!.btnClock.setOnClickListener {
            timestamp()
        }
        binding!!.btnPost.setOnClickListener {
            addMemo()
        }
        binding!!.btnCancel.setOnClickListener {
            MainActivity.mainactivity.reversechange(this)
        }



    }



    fun addMemo(){
        curMemo.title=binding!!.inputTitle.text.toString()
        curMemo.text=binding!!.inputText.text.toString()
        if(curMemo.title==""){
            var transFormat = SimpleDateFormat("yy/MM/dd hh:mm")
            var calendar= Calendar.getInstance()
            var text = transFormat.format(calendar.time)
            curMemo.title=text
        }

        if(curMemo.text==""){
            MainActivity.mainactivity.showmessage("메모를 입력해주세요.")
            return
        }

        viewModel.editmemo(curMemo)

        MainActivity.mainactivity.reversechange(this)
        try {
            if(memo==null){
                parent.update(true)
            }else{
                parent.update()
            }

        }catch (e:Exception){

        }



    }


    override fun onDestroyView() {
        binding=null
        super.onDestroyView()
    }



    fun timestamp(){
        var editText:EditText?=null
        if(binding!!.inputText.isFocused){
            editText=binding!!.inputText
        }else if(binding!!.inputTitle.isFocused){
            editText=binding!!.inputTitle
        }else{
            return
        }

        var transFormat = SimpleDateFormat(" hh:mm ")
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


}
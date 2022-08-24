package com.example.ghostdiary.fragment.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.ghostdiary.MainActivity
import com.example.ghostdiary.MainViewModel
import com.example.ghostdiary.R
import com.example.ghostdiary.Util
import com.example.ghostdiary.databinding.FragmentClinicBinding
import com.example.ghostdiary.databinding.FragmentMemoBinding
import com.example.ghostdiary.databinding.FragmentRecordBinding


class ClinicFragment : Fragment() {

    private val viewModel: MainViewModel by activityViewModels()
    private var binding: FragmentClinicBinding?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentClinicBinding.inflate(inflater,container,false)

        init()
        Util.setGlobalFont(binding!!.root)

        return binding!!.root
    }

    private fun init() {

        binding!!.btnAnalysis.setOnClickListener{
            viewModel.getdiaryAnalysisMap(requireContext())
            if(viewModel.diaryAnalysisMap!!.size <3){
                MainActivity.mainactivity.showmessage("분석에 필요한 감정수가 너무 적습니다!!\n다이어리를 더 써주세요")
                return@setOnClickListener
            }else if(viewModel.diaryAnalysisMap!!.size < 6 || viewModel.getEmotionArray().size <= 10){
                MainActivity.mainactivity.showmessage("분석에 필요한 감정수가 적습니다!!\n정확하지 않을 수 있습니다.")

            }
            MainActivity.mainactivity.containerChange(AnalysisFragment())
        }
        binding!!.btnSleep.setOnClickListener{
            var array =viewModel.getsleepdataArray()
            if(array.size==0){
                MainActivity.mainactivity.showmessage("입력된 수면 데이터가 하나도 없습니다.\n 다이어리에 수면시간을 넣어주세요")
                return@setOnClickListener
            }else if (array.size<15){
                MainActivity.mainactivity.showmessage("입력된 수면 데이터가 너무 적습니다.(${array.size})\n다이어리에 수면시간을 더 넣어주세요")

            }

            MainActivity.mainactivity.containerChange(SleepFragment(array))

        }

    }

    override fun onDestroyView() {
        binding=null
        super.onDestroyView()
    }

}
package com.example.ghostdiary.fragment.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.ghostdiary.*
import com.example.ghostdiary.databinding.FragmentAnalysisBinding
import com.example.ghostdiary.databinding.FragmentClinicBinding
import com.example.ghostdiary.databinding.FragmentMemoBinding
import com.example.ghostdiary.databinding.FragmentRecordBinding
import com.example.ghostdiary.dataclass.Day_Diary
import com.example.ghostdiary.dataclass.emotion_analysis


class AnalysisFragment : Fragment() {

    private val viewModel: MainViewModel by activityViewModels()
    private var binding: FragmentAnalysisBinding?=null

    lateinit var analysisMap :HashMap<String,emotion_analysis>
    lateinit var analysisList:List<emotion_analysis>
    var allscore:Int=-1
    var selectemotion :emotion_analysis?=null
    lateinit var allpercen:ArrayList<Int>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentAnalysisBinding.inflate(inflater,container,false)

        init()
        Util.setGlobalFont(binding!!.root)

        return binding!!.root
    }

    private fun init() {

        allscore=emotion_analysis.getallscore()
        allpercen=emotion_analysis.getallpercentage()
        analysisMap=viewModel.diaryAnalysisMap!!
        analysisList= analysisMap.values.toList()



        analysisList=analysisList.sortedBy { -it.mypercentage!!.slice(0..1).sum() }

        binding!!.tvVeryhappy.text=analysisList[0].text

        binding!!.ivGood1.setImageResource(Day_Diary.int_to_image.get(analysisList[0].ghostnum))
        binding!!.tvGood1.text=analysisList[0].text
        binding!!.tvGoodValue1.text="+${analysisList[0].mypercentage!!.slice(0..1).sum()}%"
        binding!!.sliderGood1.value= analysisList[0].mypercentage!!.slice(0..1).sum().toFloat()

        binding!!.ivGood2.setImageResource(Day_Diary.int_to_image[analysisList[1].ghostnum])
        binding!!.tvGood2.text=analysisList[1].text
        binding!!.tvGoodValue2.text="+${analysisList[1].mypercentage!!.slice(0..1).sum()}%"
        binding!!.sliderGood2.value= analysisList[1].mypercentage!!.slice(0..1).sum().toFloat()

        binding!!.ivGood3.setImageResource(Day_Diary.int_to_image[analysisList[2].ghostnum])
        binding!!.tvGood3.text=analysisList[2].text
        binding!!.tvGoodValue3.text="+${analysisList[2].mypercentage!!.slice(0..1).sum()}%"
        binding!!.sliderGood3.value= analysisList[2].mypercentage!!.slice(0..1).sum().toFloat()


        analysisList= analysisList.sortedBy { -it.mypercentage!!.slice(3..4).sum() }

        binding!!.tvVerybad.text=analysisList[0].text
        binding!!.tvVerybad2.text=analysisList[0].text

        binding!!.ivBad1.setImageResource(Day_Diary.int_to_image[analysisList[0].ghostnum])
        binding!!.tvBad1.text=analysisList[0].text
        binding!!.tvBadValue1.text="+${analysisList[0].mypercentage!!.slice(3..4).sum()}%"
        binding!!.sliderBad1.value= analysisList[0].mypercentage!!.slice(3..4).sum().toFloat()

        binding!!.ivBad2.setImageResource(Day_Diary.int_to_image[analysisList[1].ghostnum])
        binding!!.tvBad2.text=analysisList[1].text
        binding!!.tvBadValue2.text="+${analysisList[1].mypercentage!!.slice(3..4).sum()}%"
        binding!!.sliderBad2.value= analysisList[1].mypercentage!!.slice(3..4).sum().toFloat()

        binding!!.ivBad3.setImageResource(Day_Diary.int_to_image[analysisList[2].ghostnum])
        binding!!.tvBad3.text=analysisList[2].text
        binding!!.tvBadValue3.text="+${analysisList[2].mypercentage!!.slice(3..4).sum()}%"
        binding!!.sliderBad3.value= analysisList[2].mypercentage!!.slice(3..4).sum().toFloat()


        binding!!.tvAvgScore.text=allscore.toString()

        binding!!.tvVergoodPer.text="${allpercen[0]} %"
        binding!!.tvGoodPer.text="${allpercen[1]} %"
        binding!!.tvNormalPer.text="${allpercen[2]} %"
        binding!!.tvBadPer.text="${allpercen[3]} %"
        binding!!.tvVerbadPer.text="${allpercen[4]} %"

        binding!!.layoutSelect.visibility=View.GONE

        binding!!.tvSelectAnalyze.setOnClickListener {
            val dialog = AnalyzeSelectDialog(this )
            dialog.isCancelable = true
            dialog.show(requireActivity().supportFragmentManager, "ConfirmDialog")
        }

        analysisList=analysisList.sortedBy { -it.mypercentage!!.slice(0..1).sum() }




    }

    fun selectemotionUpdate(){
        if(selectemotion==null){
            binding!!.layoutSelect.visibility=View.GONE
            binding!!.tvSelectAnalyze.text="감정 선택"
            binding!!.tvSelectAnalyze.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0)
        }
        binding!!.layoutSelect.visibility=View.VISIBLE

        binding!!.tvSelectAnalyze.text=" ${selectemotion!!.text}"
        binding!!.tvSelectAnalyze.setCompoundDrawablesWithIntrinsicBounds( Day_Diary.int_to_image[selectemotion!!.ghostnum],0,0,0)


        var selectscore=selectemotion!!.getmyscore()
        var selectpercen=selectemotion!!.getmypercentage()


        binding!!.tvSelectScore.text=selectscore.toString()

        binding!!.tvSelectVergoodPer.text="${selectpercen[0]} %"
        binding!!.tvSelectGoodPer.text="${selectpercen[1]} %"
        binding!!.tvSelectNormalPer.text="${selectpercen[2]} %"
        binding!!.tvSelectBadPer.text="${selectpercen[3]} %"
        binding!!.tvSelectVerbadPer.text="${selectpercen[4]} %"

    }

    override fun onDestroyView() {
        binding=null
        super.onDestroyView()
    }

}
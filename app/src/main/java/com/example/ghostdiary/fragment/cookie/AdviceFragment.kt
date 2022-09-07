package com.example.ghostdiary.fragment.cookie

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import com.example.ghostdiary.CookiesActivity
import com.example.ghostdiary.MainViewModel
import com.example.ghostdiary.databinding.FragmentAdviceBinding
import com.example.ghostdiary.utilpackage.Util
import com.example.ghostdiary.databinding.FragmentClinicBinding


class AdviceFragment : Fragment() {

    private var binding: FragmentAdviceBinding?=null
    private lateinit var buttonarray:Array<TextView>

    companion object{
        var adviceMap: MutableMap<String,ArrayList<String>> = mutableMapOf()
        var advicecategory:Array<String> = arrayOf("분노","우울","무기력","슬픔","자존감 저하","피곤함")
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentAdviceBinding.inflate(inflater,container,false)

        init()
        Util.setGlobalFont(binding!!.root)

        return binding!!.root
    }

    private fun init() {

        buttonarray= arrayOf( binding!!.tvRage,binding!!.tvDepressed,binding!!.tvLethargy,binding!!.tvSad,binding!!.tvLowself,binding!!.tvTired)

        for(i in 0 .. buttonarray.size-1){
            buttonarray[i].setOnClickListener {
                if(!( advicecategory[i] in adviceMap)){
                    var temp= arrayListOf<String>()
                    for(j in 0..10){
                        temp.add("${advicecategory[i]}조언 : num${j}")

                    }
                    adviceMap.put(advicecategory[i],temp)
                }
                if(activity is CookiesActivity){
                    (activity as CookiesActivity).containerChange(SelectCookieFragment(false,
                        adviceMap[advicecategory[i]]!!
                    ))
                }
            }
        }
    }

    override fun onDestroyView() {
        binding=null
        super.onDestroyView()
    }

}
package com.ghostdiary.ghostdiary.fragment.cookie

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.ghostdiary.ghostdiary.CookiesActivity
import com.ghostdiary.ghostdiary.R
import com.ghostdiary.ghostdiary.databinding.FragmentAdviceBinding
import com.ghostdiary.ghostdiary.utilpackage.Util


class AdviceFragment : Fragment() {

    private var binding: FragmentAdviceBinding?=null
    private lateinit var buttonarray:Array<TextView>

    companion object{
        var adviceMap: MutableMap<String,Array<String>> = mutableMapOf()
        var advicecategory:Array<String> = arrayOf("불안","피로","우울","스트레스","좌절","자존감")
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

        buttonarray= arrayOf( binding!!.tvUnrest,binding!!.tvTired,binding!!.tvDepressed,binding!!.tvStress,binding!!.tvOtl,binding!!.tvLowself)



        for(i in 0 .. buttonarray.size-1){
            buttonarray[i].setOnClickListener {
                if(!( advicecategory[i] in adviceMap)){
                    for(i in advicecategory){
                        when(i){
                            "불안"-> adviceMap.put(i, resources.getStringArray(R.array.COMMONADVICE) +resources.getStringArray( R.array.불안))
                            "피로"-> adviceMap.put(i,resources.getStringArray( R.array.피로))
                            "우울"-> adviceMap.put(i,resources.getStringArray(R.array.COMMONADVICE) + resources.getStringArray( R.array.우울))
                            "스트레스"-> adviceMap.put(i,resources.getStringArray(R.array.COMMONADVICE) + resources.getStringArray( R.array.스트레스))
                            "좌절"-> adviceMap.put(i,resources.getStringArray( R.array.좌절))
                            "자존감"-> adviceMap.put(i,resources.getStringArray( R.array.자존감))
                        }
                    }
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
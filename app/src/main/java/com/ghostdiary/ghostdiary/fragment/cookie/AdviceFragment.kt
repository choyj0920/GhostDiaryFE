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
        lateinit var advicecategory:Array<String>
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

        advicecategory=resources.getStringArray(R.array.advice_category)

        for(i in 0 .. buttonarray.size-1){
            buttonarray[i].setOnClickListener {
                if(!( advicecategory[i] in adviceMap)){
                    for(i in advicecategory){
                        when(i){
                            advicecategory[0]-> adviceMap.put(i, resources.getStringArray(R.array.COMMONADVICE) +resources.getStringArray( R.array.불안))
                            advicecategory[1]-> adviceMap.put(i,resources.getStringArray( R.array.피로))
                            advicecategory[2]-> adviceMap.put(i,resources.getStringArray(R.array.COMMONADVICE) + resources.getStringArray( R.array.우울))
                            advicecategory[3]-> adviceMap.put(i,resources.getStringArray(R.array.COMMONADVICE) + resources.getStringArray( R.array.스트레스))
                            advicecategory[4]-> adviceMap.put(i,resources.getStringArray( R.array.좌절))
                            advicecategory[5]-> adviceMap.put(i,resources.getStringArray( R.array.자존감))
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
package com.example.ghostdiary.fragment.cookie

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.activityViewModels
import com.example.ghostdiary.MainViewModel
import com.example.ghostdiary.R
import com.example.ghostdiary.utilpackage.Util
import com.example.ghostdiary.databinding.FragmentClinicBinding
import com.example.ghostdiary.databinding.FragmentCookieViewBinding


class CookieViewFragment(var text:String) : Fragment() {

    private var binding: FragmentCookieViewBinding?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentCookieViewBinding.inflate(inflater,container,false)

        init()
        Util.setGlobalFont(binding!!.root)

        return binding!!.root
    }

    private fun init() {
        binding!!.tvText.text=text
        binding!!.tvCancel.setOnClickListener {
            requireActivity().finish()
        }

        binding!!.ivCookies.startAnimation(AnimationUtils.loadAnimation(context, R.anim.scaleup))
        binding!!.tvText.startAnimation(AnimationUtils.loadAnimation(context, R.anim.vertical_enter))


    }

    override fun onDestroyView() {
        binding=null
        super.onDestroyView()
    }

}
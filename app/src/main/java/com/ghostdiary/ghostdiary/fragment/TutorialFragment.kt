package com.ghostdiary.ghostdiary.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ghostdiary.ghostdiary.databinding.FragmentTutorialBinding
import com.ghostdiary.ghostdiary.utilpackage.Util

class TutorialFragment(val image : Int) : Fragment() {
    lateinit var binding: FragmentTutorialBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentTutorialBinding.inflate(inflater,container,false)


        binding.imgSlideImage.setImageResource(image)

        Util.setGlobalFont(binding!!.root)

        return binding!!.root
    }
}
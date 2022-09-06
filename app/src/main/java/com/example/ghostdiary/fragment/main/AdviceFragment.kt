package com.example.ghostdiary.fragment.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.ghostdiary.MainViewModel
import com.example.ghostdiary.utilpackage.Util
import com.example.ghostdiary.databinding.FragmentClinicBinding


class AdviceFragment : Fragment() {

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

    }

    override fun onDestroyView() {
        binding=null
        super.onDestroyView()
    }

}
package com.example.ghostdiary.fragment.main

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.ghostdiary.Util
import com.example.ghostdiary.databinding.FragmentDefaultBinding
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class DefaultFragment : Fragment() {

    companion object {
        fun newInstance() = DefaultFragment()
    }
    private var binding: FragmentDefaultBinding? =null



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentDefaultBinding.inflate(inflater,container,false)

        Util.setGlobalFont(binding!!.root)

        return binding!!.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // TODO: Use the ViewModel
    }
    override fun onDestroyView() {
        binding=null
        super.onDestroyView()
    }
}
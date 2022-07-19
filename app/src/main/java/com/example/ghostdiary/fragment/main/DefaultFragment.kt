package com.example.ghostdiary.fragment.main

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.ghostdiary.databinding.FragmentDefaultBinding
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class DefaultFragment : Fragment() {

    companion object {
        fun newInstance() = DefaultFragment()
    }
    private var binding: FragmentDefaultBinding? =null

    private lateinit var viewModel: DefaultViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentDefaultBinding.inflate(inflater,container,false)

        var today=LocalDateTime.now()
        var to = today.format(DateTimeFormatter.ofPattern("yyyy.MM.dd"))


        binding!!.tvDate.text=to

        return binding!!.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(DefaultViewModel::class.java)
        // TODO: Use the ViewModel
    }
    override fun onDestroyView() {
        binding=null
        super.onDestroyView()
    }
}
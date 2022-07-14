package com.example.ghostdiary.fragment

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.ghostdiary.databinding.FragmentDefaultBinding

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
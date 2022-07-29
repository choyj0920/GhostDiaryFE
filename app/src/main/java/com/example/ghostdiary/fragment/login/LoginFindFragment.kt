package com.example.ghostdiary.fragment.login

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.ghostdiary.LoginActivity
import com.example.ghostdiary.LoginViewModel
import com.example.ghostdiary.adapter.ViewPagerAdapter
import com.example.ghostdiary.databinding.FragmentLoginFindBinding
import com.example.ghostdiary.databinding.FragmentLoginMainBinding
import com.example.ghostdiary.fragment.main.RecordFragment
import com.google.android.material.tabs.TabLayoutMediator

class LoginFindFragment : Fragment() {
    companion object {
        fun newInstance() = RecordFragment()
    }
    private val tabTitleArray = arrayOf(
        "이메일 찾기",
        "비밀번호 찾기"
    )

    private lateinit var binding:FragmentLoginFindBinding

    private lateinit var viewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding= FragmentLoginFindBinding.inflate(inflater,container,false)


        init()

        return binding.root
    }

    fun init(){
        viewModel=LoginActivity.loginActivity.viewModel

        binding.close.setOnClickListener {
            LoginActivity.loginActivity.change_login_email()

        }

        val viewPager = binding.viewPager
        val tabLayout = binding.tabLayout

        viewPager.adapter = ViewPagerAdapter(childFragmentManager,LoginActivity.loginActivity.lifecycle)

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = tabTitleArray[position]
        }.attach()

    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // TODO: Use the ViewModel
    }
}
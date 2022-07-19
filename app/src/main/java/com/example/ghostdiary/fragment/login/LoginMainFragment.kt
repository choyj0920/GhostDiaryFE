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
import com.example.ghostdiary.databinding.FragmentLoginMainBinding
import com.example.ghostdiary.fragment.main.RecordFragment

class LoginMainFragment : Fragment() {
    companion object {
        fun newInstance() = RecordFragment()
    }
    private lateinit var binding:FragmentLoginMainBinding

    private lateinit var viewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding= FragmentLoginMainBinding.inflate(inflater,container,false)


        init()

        return binding.root
    }

    fun init(){
        viewModel=LoginActivity.loginActivity.viewModel
        binding.close.setOnClickListener {
            requireActivity().finishAffinity() //해당 앱의 루트 액티비티를 종료시킨다.

            System.runFinalization() //현재 작업중인 쓰레드가 다 종료되면, 종료 시키라는 명령어이다.

            System.exit(0)
        }

        binding.tvLoginKakao.setOnClickListener{

        }

        binding.tvLoginGoogle.setOnClickListener{
        }
        binding.tvLoginEmail.setOnClickListener{
            LoginActivity.loginActivity.change_login_email()

        }
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // TODO: Use the ViewModel
    }
}
package com.example.ghostdiary.fragment.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.KeyEvent.KEYCODE_ENTER
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.ghostdiary.*
import com.example.ghostdiary.databinding.FragmentLoginEmailBinding
import com.example.ghostdiary.fragment.main.RecordFragment

class LoginEmailFragment : Fragment() {
    companion object {
        fun newInstance() = RecordFragment()
    }
    private lateinit var binding:FragmentLoginEmailBinding

    private lateinit var viewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding= FragmentLoginEmailBinding.inflate(inflater,container,false)
        init()
        return binding.root
    }

    fun init(){
        viewModel=LoginActivity.loginActivity.viewModel

        // 로그인 버튼
        binding.btnLoginEmail.setOnClickListener {
            val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(binding.root.windowToken, 0)

            var email :String=binding.inputLoginEmail.text.toString()
            var password :String=binding.inputLoginPassword.text.toString()

            if(! viewModel.get_UserList().contains(email)){
                Toast.makeText(activity,"해당 이메일 없음",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if(viewModel.get_UserList()!!.get(email)!!.password==password){
                var intent = Intent(getActivity(), MainActivity::class.java)
                intent.putExtra("uid",email)
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                startActivity(intent)

                return@setOnClickListener
            }
            else{
                binding.tvWrongpassword.visibility=View.VISIBLE
                binding.inputLoginPassword.setBackgroundResource(R.drawable.red_border)
            }
        }

        binding.tvRegister.setOnClickListener {
            LoginActivity.loginActivity.change_login_register()
        }

        // 종료버튼 --> login main
        binding.close.setOnClickListener{
            LoginActivity.loginActivity.change_login_main()
        }
        //  엔터 키 리스너
        binding.inputLoginEmail.setOnKeyListener { v, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KEYCODE_ENTER) {
                // 엔터 눌렀을때 행동
//                binding.inputLoginPassword.requestFocus()
                true
            }
            false
        }
        binding.inputLoginPassword.setOnKeyListener { v, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KEYCODE_ENTER) {
                // 엔터 눌렀을때 행동
                val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(binding.inputLoginPassword.windowToken, 0)
                true
            }
            false
        }




    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // TODO: Use the ViewModel
    }
}
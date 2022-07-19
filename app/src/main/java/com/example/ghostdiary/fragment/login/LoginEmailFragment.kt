package com.example.ghostdiary.fragment.login

import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.*
import android.view.KeyEvent.KEYCODE_ENTER
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.getSystemService
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.ghostdiary.*
import com.example.ghostdiary.databinding.FragmentCalendarBinding
import com.example.ghostdiary.databinding.FragmentLoginEmailBinding
import com.example.ghostdiary.databinding.FragmentLoginMainBinding
import com.example.ghostdiary.fragment.RecordFragment
import com.example.ghostdiary.fragment.RecordViewModel

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
        binding.btnLoginEmail.setOnClickListener {

            binding.inputLoginEmail.text
            binding.inputLoginPassword.text


            if(true){
                var intent = Intent(getActivity(), MainActivity::class.java)
//                intent.putExtra(".time)
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                startActivity(intent)
            }

        }
        binding.close.setOnClickListener{
            LoginActivity.loginActivity.change_login_main()
        }
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
        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        // TODO: Use the ViewModel
    }
}
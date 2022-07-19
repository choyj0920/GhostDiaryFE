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
import com.example.ghostdiary.databinding.FragmentLoginRegistBinding
import com.example.ghostdiary.fragment.RecordFragment
import com.example.ghostdiary.fragment.RecordViewModel

class LoginRegistFragment : Fragment() {
    companion object {
        fun newInstance() = RecordFragment()
    }
    private lateinit var binding:FragmentLoginRegistBinding

    private lateinit var viewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding= FragmentLoginRegistBinding.inflate(inflater,container,false)
        init()
        return binding.root
    }

    fun init(){


    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        // TODO: Use the ViewModel
    }
}
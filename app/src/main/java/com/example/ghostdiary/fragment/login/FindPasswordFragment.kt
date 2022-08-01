package com.example.ghostdiary.fragment.login

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.ghostdiary.LoginActivity
import com.example.ghostdiary.LoginViewModel
import com.example.ghostdiary.R
import com.example.ghostdiary.databinding.FragmentFindPasswordlBinding
import com.example.ghostdiary.fragment.main.RecordFragment
import java.util.*


class FindPasswordFragment : Fragment() {
    companion object {
        fun newInstance() = RecordFragment()
    }
    private var t_timer = Timer()
    var int_count=0
    var istoolate:Boolean=false


    private lateinit var binding:FragmentFindPasswordlBinding
    private lateinit var viewModel: LoginViewModel
    var iscurrentcodemode=false
    var emailpattern=android.util.Patterns.EMAIL_ADDRESS


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding= FragmentFindPasswordlBinding.inflate(inflater,container,false)


        init()

        return binding.root
    }

    fun init(){
        viewModel=LoginActivity.loginActivity.viewModel

        binding.layoutCodenumber.visibility=View.GONE

        binding.btnFindepasswordNext.setOnClickListener {
            val email=binding.inputFindpasswordEmail.text.toString()
            emailpattern.matcher(email).matches()
            if (! iscurrentcodemode){ // 아직 코드발송 x
                if(!emailpattern.matcher(email).matches()){
                    //이메일 형식오류
                    show_dialog("이메일 형식이 아닙니다.")
                }
                else if(false){ // 없는 이메일


                }else{  // 메일 발송
                    iscurrentcodemode=true
                    istoolate=false
                    show_dialog("인증번호를 발송했습니다.\n3분 이내에 입력하세요")
                    /*
                    
                    -------------------------
                    메일 발송 추가해야함
                    
                    */
                    binding.layoutCodenumber.visibility=View.VISIBLE


                    int_count = 180

                    t_timer = kotlin.concurrent.timer(period = 1000) { //반복주기는 peroid 프로퍼티로 설정, 단위는 1000분의 1초 (period = 1000, 1초)
                        int_count --
                        val sec = int_count% 60
                        val min = int_count / 60

                        // UI조작을 위한 메서드
                        activity?.runOnUiThread {
                            binding.tvTimer.text="%02d:%02d".format(min,sec)
                        }
                    }
                }
            }  
            else{  //코드 발송후

            }

        }


        binding.inputFindpasswordEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                iscurrentcodemode=false
                binding.layoutCodenumber.visibility=View.GONE

            }
        })


    }

    fun show_dialog(text:String){
        val builder= AlertDialog.Builder(context,R.style.AlertDialogTheme)
        builder.setMessage(text)
        builder.setPositiveButton("확인",
            DialogInterface.OnClickListener { dialog, i ->

                dialog.dismiss()
                dialog.cancel()
            })
        val dialog= builder.create()
        dialog.setOnShowListener{
            DialogInterface.OnShowListener(){
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(requireContext(),R.color.pink))
            }
        }
        dialog.show()
    }

}
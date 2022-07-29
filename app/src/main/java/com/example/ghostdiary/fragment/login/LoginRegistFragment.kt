package com.example.ghostdiary.fragment.login

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.ghostdiary.*
import com.example.ghostdiary.databinding.FragmentLoginRegistBinding
import com.example.ghostdiary.fragment.main.RecordFragment
import java.text.SimpleDateFormat
import java.util.*

class LoginRegistFragment : Fragment() {
    companion object {
        fun newInstance() = RecordFragment()
    }
    private lateinit var binding:FragmentLoginRegistBinding

    private lateinit var viewModel: LoginViewModel
    var isemailduplicate=true
    var ispasswordcorrect=false
    var ispasswordcheckcorrect=false
    var ischeckbirthday=false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding= FragmentLoginRegistBinding.inflate(inflater,container,false)
        init()
        return binding.root
    }

    fun init(){
        binding.close.setOnClickListener {
            LoginActivity.loginActivity.change_login_email()
        }

        viewModel=LoginActivity.loginActivity.viewModel

        var emailpattern=android.util.Patterns.EMAIL_ADDRESS

        binding.inputRegistEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }override fun afterTextChanged(p0: Editable?) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // text가 바뀔 때마다 호출된다.
                // 우린 이 함수를 사용한다.
                isemailduplicate=true
            }


        })
        // 비밀번호 체크부
        check_password("")
        binding.inputRegistPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun afterTextChanged(p0: Editable?) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // text가 바뀔 때마다 호출된다.
                // 우린 이 함수를 사용한다.
                check_password(binding.inputRegistPassword.text.toString())

                if(binding.inputRegistPassword2.text.toString()== binding.inputRegistPassword.text.toString()){
                    binding.tvCheckpassword3.text=" ✓ 동일한 비밀번호를 입력하세요"
                    binding.tvCheckpassword3.setTextColor(ContextCompat.getColor(context!!,R.color.green))
                    ispasswordcheckcorrect=true
                }else{

                    binding.tvCheckpassword3.text=" × 동일한 비밀번호를 입력하세요"
                    binding.tvCheckpassword3.setTextColor(ContextCompat.getColor(context!!,R.color.red2))
                    ispasswordcheckcorrect=false
                }

            }
        })
        // 비밀번호 확인 체크부
        binding.inputRegistPassword2.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }override fun afterTextChanged(p0: Editable?) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // text가 바뀔 때마다 호출된다.
                // 우린 이 함수를 사용한다.
                if(binding.inputRegistPassword2.text.toString()== binding.inputRegistPassword.text.toString()){
                    binding.tvCheckpassword3.text=" ✓ 동일한 비밀번호를 입력하세요"
                    binding.tvCheckpassword3.setTextColor(ContextCompat.getColor(context!!,R.color.green))
                    ispasswordcheckcorrect=true
                }else{

                    binding.tvCheckpassword3.text=" × 동일한 비밀번호를 입력하세요"
                    binding.tvCheckpassword3.setTextColor(ContextCompat.getColor(context!!,R.color.red2))
                    ispasswordcheckcorrect=false
                }

            }
        })

        binding.tvRegistEmailDuplicate.setOnClickListener {
            val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(binding.root.windowToken, 0)
//            binding.layoutRegistPopup.visibility=View.VISIBLE
//            binding.layoutRegistMain.isClickable=false

            var email=binding.inputRegistEmail.text

            if(!emailpattern.matcher(email).matches()){
                show_dialog("이메일이 아닙니다.")
                isemailduplicate=true

            }else if(true) { // 사용가능 이메일
                show_dialog("사용 가능한 이메일 입니다.")
                isemailduplicate=false

            }else{ //이미 존재하는 이메일
                show_dialog("중복된 이메일이 있습니다.")
                isemailduplicate=true


            }

        }


        binding.inputBirthday.setOnClickListener {
            val datepickercalendar = Calendar.getInstance()
            val year = datepickercalendar.get(Calendar.YEAR)
            val month = datepickercalendar.get(Calendar.MONTH)
            val day = datepickercalendar.get(Calendar.DAY_OF_MONTH)

            val dpd = DatePickerDialog(
                requireContext(),
                R.style.MySpinnerDatePickerStyle,
                { _, year, monthOfYear, dayOfMonth ->
//                  월이 0부터 시작하여 1을 더해주어야함
                    val month = monthOfYear + 1
//                   선택한 날짜의 요일을 구하기 위한 calendar
                    val calendar = Calendar.getInstance()
//                    선택한 날짜 세팅
                    calendar.set(year, monthOfYear, dayOfMonth)
                    val date = calendar.time
                    val simpledateformat = SimpleDateFormat("yyyy / MM / dd", Locale.getDefault())
                    val to: String = simpledateformat.format(date)
                    ischeckbirthday=true

                    binding.inputBirthday.setText(to)


                },
                year,
                month,
                day
            )
//           최대 날짜를 현재 시각 이후로
            dpd.datePicker.maxDate = System.currentTimeMillis() - 1000;
            dpd.show()

        }
        
        binding.tvRegistRegist.setOnClickListener {
            val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(binding.root.windowToken, 0)
            var name =binding.inputRegistName.text.toString()
            if(name.length==0){
//                Toast.makeText(context,"이름을 입력해주세요", Toast.LENGTH_SHORT).show()
                show_dialog("이름을 입력해주세요")

                return@setOnClickListener
            }
            else if(isemailduplicate) {
//                Toast.makeText(context, "email 중복 확인해주세요.", Toast.LENGTH_SHORT).show()
                show_dialog("email 중복 확인해주세요.")
                return@setOnClickListener

            }else if(!ispasswordcorrect){
//                Toast.makeText(context, "형식에 맞는 password를 입력해주세요", Toast.LENGTH_SHORT).show()
                show_dialog("형식에 맞는 password를 입력해주세요")
                return@setOnClickListener
            }else if(!ispasswordcheckcorrect) {
//                Toast.makeText(context, "비밀번호 확인 체크", Toast.LENGTH_SHORT).show()
                    show_dialog("비밀번호 확인 체크")
                return@setOnClickListener


            }else if(!ischeckbirthday) {
//                Toast.makeText(context, "생일을 입력해주세요", Toast.LENGTH_SHORT).show()
                    show_dialog("생일을 입력해주세요")
                return@setOnClickListener

            }else{
                var email=binding.inputRegistEmail.text.toString()
                var password=binding.inputRegistPassword.text.toString()
                var birthday=binding.inputBirthday.text.toString()
                viewModel.get_UserList()!!.put(email,LoginViewModel.User(name,email,password,birthday))
                LoginActivity.loginActivity.change_login_email()

                LoginActivity.loginActivity.loginRegistFragment=LoginRegistFragment()
                Toast.makeText(context,"회원 가입 완료!",Toast.LENGTH_SHORT).show()

            }
            
        }

    }

    fun show_dialog(text:String){
        var builder=AlertDialog.Builder(context,R.style.AlertDialogTheme)
        builder.setMessage(text)
        builder.setPositiveButton("확인",
            DialogInterface.OnClickListener { dialog, i ->

            dialog.dismiss()
            dialog.cancel()
        })
        var dialog= builder.create()
        dialog.setOnShowListener{
            DialogInterface.OnShowListener(){
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(requireContext(),R.color.pink))
            }
        }
        dialog.show()
    }


    fun check_password(s:String){
        var check1:Boolean= s.length>=10 && s.length<=20
        if(check1){
            binding.tvCheckpassword1.text=" ✓ 10자 이상 입력"
            binding.tvCheckpassword1.setTextColor(ContextCompat.getColor(requireContext(),R.color.green))

        }else{
            binding.tvCheckpassword1.text=" • 10자 이상 입력"
            binding.tvCheckpassword1.setTextColor(ContextCompat.getColor(requireContext(),R.color.black2))
        }


        var check2:Boolean= s.matches("^(?=.*[a-zA-Z0-9])(?=.*[a-zA-Z!@#\$%^&*])(?=.*[0-9!@#\$%^&*]).{1,}\$".toRegex())
        if(check2){
            binding.tvCheckpassword2.text=" ✓ 영문/숫자/특수문자(공백제외)만 허용하며, 2개 이상 조합"
            binding.tvCheckpassword2.setTextColor(ContextCompat.getColor(requireContext(),R.color.green))

        }else{
            binding.tvCheckpassword2.text=" • 영문/숫자/특수문자(공백제외)만 허용하며, 2개 이상 조합"
            binding.tvCheckpassword2.setTextColor(ContextCompat.getColor(requireContext(),R.color.black2))
        }

        ispasswordcorrect=check1 && check2


    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // TODO: Use the ViewModel
    }
}
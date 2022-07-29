package com.example.ghostdiary.fragment.login

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.ContentValues.TAG
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.ghostdiary.LoginActivity
import com.example.ghostdiary.LoginViewModel
import com.example.ghostdiary.R
import com.example.ghostdiary.databinding.FragmentFindEmailBinding
import com.example.ghostdiary.databinding.FragmentLoginFindBinding
import com.example.ghostdiary.databinding.FragmentLoginMainBinding
import com.example.ghostdiary.fragment.main.RecordFragment
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class FindEmailFragment : Fragment() {
    companion object {
        fun newInstance() = RecordFragment()
    }

    private lateinit var binding:FragmentFindEmailBinding
    var ischeckbirthday=false
    private lateinit var viewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding= FragmentFindEmailBinding.inflate(inflater,container,false)


        init()

        return binding.root
    }

    fun init(){
        viewModel=LoginActivity.loginActivity.viewModel

        // 생일 입력
        binding.inputFindemailBirth.setOnClickListener {
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

                    binding.inputFindemailBirth.setText(to)
                },
                year,
                month,
                day
            )
//           최대 날짜를 현재 시각 이후로
            dpd.datePicker.maxDate = System.currentTimeMillis() - 1000;
            dpd.show()
        }


        // 찾기 버튼->
        binding.btnFindemailNext.setOnClickListener {
            var name=binding.inputFindemailName.text.toString()
            if (!ischeckbirthday){
                show_dialog("생일을 입력해주세요")
                return@setOnClickListener

            }else if (name.length==0){
                show_dialog("이름을 입력해주세요")
                return@setOnClickListener

            }

            var str_birthday = binding.inputFindemailBirth.text
            val formatter = DateTimeFormatter.ofPattern("yyyy / MM / dd",Locale.KOREA)
            var birthday = LocalDate.parse(str_birthday, formatter)

        }



    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // TODO: Use the ViewModel
    }

    fun show_dialog(text:String){
        var builder= AlertDialog.Builder(context,R.style.AlertDialogTheme)
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
}
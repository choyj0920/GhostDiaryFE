package com.ghostdiary.ghostdiary;

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle;
import android.view.*
import androidx.fragment.app.DialogFragment
import com.ghostdiary.ghostdiary.databinding.DialogAlarmSetterBinding

import com.ghostdiary.ghostdiary.utilpackage.Util
import java.util.*

class AlarmSetterDialog(var parent: MainActivity,var lasttime:Calendar): DialogFragment() {

    // 뷰 바인딩 정의
    private var _binding: DialogAlarmSetterBinding? = null
    private val binding get() = _binding!!



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogAlarmSetterBinding.inflate(inflater, container, false)
        val view = binding.root

        // 레이아웃 배경을 투명하게 해줌, 필수 아님
        dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        initview()


        Util.setGlobalFont(binding.root)

        return view
    }

    fun initview(){
        binding.timepicker.hour=lasttime.get(Calendar.HOUR_OF_DAY)
        binding.timepicker.minute=lasttime.get(Calendar.MINUTE)


        binding.tvCancel.setOnClickListener {
            dismiss()
        }
        binding.tvSave.setOnClickListener {

            parent.updatealarm(binding.timepicker.hour,binding.timepicker.minute)


//            var notificationHelper = NotificationHelper(requireContext())
//
//            val nb: NotificationCompat.Builder = notificationHelper.getChannelNotification("알람 설정", "${binding.timepicker.hour}:${binding.timepicker.minute}",null)
//
//            notificationHelper.getManager().notify(1, nb.build())
            dismiss()

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}

package com.ghostdiary.ghostdiary;

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import com.ghostdiary.ghostdiary.databinding.DialogAfterupdateBinding
import com.ghostdiary.ghostdiary.utilpackage.Util
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.review.ReviewManagerFactory
import com.google.android.play.core.review.model.ReviewErrorCode


class AfterupdateDialog(): DialogFragment() {

    // 뷰 바인딩 정의
    private var _binding: DialogAfterupdateBinding? = null
    private val binding get() = _binding!!



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogAfterupdateBinding.inflate(inflater, container, false)
        val view = binding.root

        dialog?.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
//        dialog?.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        dialog?.window?.setDimAmount(.2f);  // 뒷 배경 30% dim 처리

        initview()


        Util.setGlobalFont(binding.root)

        return view
    }

    fun initview(){

        binding.tvClose.setOnClickListener {
            dismiss()

        }
        binding.tvReview.setOnClickListener {

//            val manager = ReviewManagerFactory.create(requireContext())
//            val request = manager.requestReviewFlow()
//            request.addOnCompleteListener { task ->
//                if (task.isSuccessful) {
//                    // We got the ReviewInfo object
//                    val reviewInfo = task.result
//                    val flow = manager.launchReviewFlow(requireActivity(), reviewInfo!!)
//                    flow.addOnCompleteListener { _ ->
//
//                    }
//                } else {
//                    // There was some problem, log or handle the error code.
//                }
//            }


            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("market://details?id=${requireActivity().packageName}")
            requireContext().startActivity(intent)

        }




    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}

package com.example.ghostdiary.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ghostdiary.R
import com.example.ghostdiary.databinding.ItemEmotionGhostBinding
import com.example.ghostdiary.databinding.ItemSelectEmotionBinding
import com.example.ghostdiary.databinding.ItemSelectSleeptimeBinding
import com.example.ghostdiary.dataclass.Day_Diary
import com.example.ghostdiary.dataclass.emotionclass
import com.example.ghostdiary.fragment.main.EditDiaryFragment
import com.example.ghostdiary.fragment.main.SelectEmotionFragment
import com.google.android.material.slider.LabelFormatter
import com.skydoves.balloon.Balloon
import com.skydoves.balloon.BalloonSizeSpec
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*


class AdapterEmotionjustview(val parent: EditDiaryFragment, var emotions:MutableList<emotionclass?>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    class GhostView(binding: ItemEmotionGhostBinding) : RecyclerView.ViewHolder(binding.root) {
        var emotionname: TextView = binding.tvGhost
        var btn_close: ImageView = binding.btnGhostDelete
        var ghostimage:ImageView=binding.ivGhost
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view : View?

        view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_emotion_ghost, parent, false)
        return GhostView(ItemEmotionGhostBinding.bind(view))


    }
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        var holder=holder as GhostView
        holder.btn_close.visibility=View.GONE
        holder.emotionname.visibility=View.GONE

        if (emotions[position]==null){
            holder.ghostimage.visibility=View.GONE
            return
        }

        holder.ghostimage.setImageResource(Day_Diary.int_to_image.get(emotions[position]!!.ghostimage))
        holder.ghostimage.alpha=1f
        holder.ghostimage.setOnClickListener {

            val balloon = Balloon.Builder(parent.requireContext())
                .setWidth(BalloonSizeSpec.WRAP)
                .setHeight(BalloonSizeSpec.WRAP)
                .setText(emotions[position]!!.text)
                .setTextColorResource(R.color.white)
                .setTextSize(15f)
                .setArrowSize(10)
                .setArrowPosition(0.5f)
                .setPadding(12)
                .setCornerRadius(8f)
                .setBackgroundColorResource(R.color.pink)
                .setLifecycleOwner(parent)
                .build()
            balloon.showAlignBottom(holder.ghostimage)
            balloon.isShowing

        }
    }


    override fun getItemViewType(position: Int): Int {
        return 1
    }

    override fun getItemCount(): Int {
        return emotions.size
    }

}
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
import com.example.ghostdiary.fragment.main.SelectEmotionFragment
import com.google.android.material.slider.LabelFormatter
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*


class AdapterEmotion(val parent: AdapterPostdiary,var listPosion:Int,var emotions:MutableList<emotionclass>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {


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
        holder.emotionname.text =emotions[position].text
        holder.ghostimage.setImageResource(Day_Diary.int_to_Image(emotions[position].ghostimage))
        if(emotions[position].isactive){
            holder.ghostimage.alpha=1.0f
        }
        holder.ghostimage.setOnClickListener {
            if(!emotions[position].isactive){
                for(i in emotions){
                    i.isactive=false
                }
                emotions[position].isactive=true
                parent.update(listPosion)
            }

        }

        if(listPosion==0){
            //하루의 감정은 삭제못함
            holder.btn_close.visibility=View.INVISIBLE
        }else{
            holder.btn_close.setOnClickListener {
                emotions.removeAt(position)
                parent.update(listPosion)

            }
        }


    }


    override fun getItemViewType(position: Int): Int {
        return 1
    }

    override fun getItemCount(): Int {
        return emotions.size
    }

}
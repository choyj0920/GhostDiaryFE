package com.example.ghostdiary.adapter

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.ghostdiary.R
import com.example.ghostdiary.utilpackage.Util
import com.example.ghostdiary.databinding.ItemEmotionGhostjustviewBinding
import com.example.ghostdiary.dataclass.Day_Diary
import com.example.ghostdiary.dataclass.emotionclass
import com.example.ghostdiary.fragment.calendar.RecordFragment
import com.skydoves.balloon.Balloon
import com.skydoves.balloon.BalloonSizeSpec


class AdapterEmotionjustview(val parent: Fragment, var emotions:MutableList<emotionclass?>,var erasetoday:Boolean=false): RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    class GhostView(binding: ItemEmotionGhostjustviewBinding) : RecyclerView.ViewHolder(binding.root) {


        var ghostimage:ImageView=binding.ivGhost
        var layout=binding.layoutGhostView
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view : View?

        view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_emotion_ghostjustview, parent, false)
        Util.setGlobalFont(view!!)

        return GhostView(ItemEmotionGhostjustviewBinding.bind(view))

    }
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        var holder=holder as GhostView

        if(parent is RecordFragment){
            holder.layout.foregroundGravity=Gravity.RIGHT
        }

        if (emotions[position]==null){
            holder.ghostimage.visibility=View.GONE
            return
        }
        if(erasetoday && position==0){
            holder.ghostimage.visibility=View.GONE
            return
        }
        holder.ghostimage.visibility=View.VISIBLE

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
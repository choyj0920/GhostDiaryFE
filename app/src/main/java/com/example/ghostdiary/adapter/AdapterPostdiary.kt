package com.example.ghostdiary.adapter

import android.content.ContentValues.TAG
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isInvisible
import androidx.recyclerview.widget.RecyclerView
import com.example.ghostdiary.PostDiaryActivity
import com.example.ghostdiary.R
import com.example.ghostdiary.databinding.ItemPostDiaryBinding
import com.example.ghostdiary.dataclass.Day_Diary
import com.example.ghostdiary.fragment.CalendarFragment
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class AdapterPostdiary(val parent: PostDiaryActivity, val emotions: Array<String>, var selectemotion:HashMap<String,Int>): RecyclerView.Adapter<AdapterPostdiary.GhostView>() {


    class GhostView(binding: ItemPostDiaryBinding) : RecyclerView.ViewHolder(binding.root) {
        var tv_title: TextView = binding.tvTitle
        var arr_emotion:Array<ImageView> = arrayOf(binding.ivGhostVerygood,binding.ivGhostGood,binding.ivGhostNormal,binding.ivGhostBad,binding.ivGhostVerybad)

        var tv_plus:TextView = binding.tvPlus
        var iv_addghost:ImageView = binding.ivAddghost
        var isactive:Boolean=false
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GhostView {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.item_post_diary, parent, false)
        return GhostView(ItemPostDiaryBinding.bind(view))
    }
    override fun onBindViewHolder(holder: GhostView, position: Int) {
        holder.tv_title.text=emotions[position]


        holder.tv_plus.setOnClickListener{
            switch_Active(holder)
        }

        if(emotions[position]=="하루의 감정") {
            holder.tv_title.gravity = Gravity.CENTER
            switch_Active(holder)
        }

        for(i in 0..4){
            holder.arr_emotion[i].setOnClickListener{
                if(!holder.isactive || i == selectemotion[emotions[position]]) {
                    Toast.makeText(parent,"+ 눌러 활성화 해주세여",Toast.LENGTH_SHORT)
                    return@setOnClickListener
                }
                if(selectemotion[emotions[position]]!= -1)
                    holder.arr_emotion[selectemotion[emotions[position]]!!].alpha=0.4f
                selectemotion[emotions[position]]=i
                holder.arr_emotion[i].alpha=1f

            }
        }



    }
    fun switch_Active(holder:GhostView){
        if (holder.isactive){
            holder.isactive=false
            for(i in holder.arr_emotion){
                i.setAlpha(0.4f)
            }
            holder.tv_plus.isInvisible=false
            holder.iv_addghost.isInvisible=false
            selectemotion[holder.tv_title.text as String]=-1
        }else{
            holder.tv_plus.isInvisible=true
            holder.iv_addghost.isInvisible=true

            holder.isactive=true
            for(i in holder.arr_emotion){
                i.setAlpha(0.4f)
            }
            selectemotion[holder.tv_title.text as String]=2
            holder.arr_emotion[2].alpha=1f

        }

    }

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

    override fun getItemCount(): Int {
        return emotions.size
    }
}
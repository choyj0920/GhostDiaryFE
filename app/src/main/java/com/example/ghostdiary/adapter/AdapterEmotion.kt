package com.example.ghostdiary.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ghostdiary.R
import com.example.ghostdiary.databinding.ItemEmotionGhostBinding
import com.example.ghostdiary.dataclass.Day_Diary
import com.example.ghostdiary.dataclass.emotionclass


class AdapterEmotion(val parent: AdapterPostdiary,var listPosion:Int,var emotions:MutableList<emotionclass>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    class GhostView(binding: ItemEmotionGhostBinding) : RecyclerView.ViewHolder(binding.root) {
        var emotionname: TextView = binding.tvGhost
        var btn_close: ImageView = binding.btnGhostDelete
        var ghostimage:ImageView=binding.ivGhost
    }
    companion object{

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
        holder.ghostimage.setImageResource(Day_Diary.int_to_image.get(emotions[position].ghostimage))




        if(listPosion==0){ // 하루의감정
            //하루의 감정은 삭제못함
            holder.btn_close.visibility=View.INVISIBLE

            if(emotions[position].isactive){
                holder.ghostimage.alpha=1.0f
            }else{
                holder.ghostimage.alpha=0.4f
            }
            holder.ghostimage.setOnClickListener {
                if(!emotions[position].isactive){
                    for(i in emotions){
                        i.isactive=false
                    }
                    emotions[position].isactive=true
                    parent.update(listPosion, AdapterPostdiary.states[position])
                }
            }


        }else{ //다른곳 삭제도 가능 추가도가능
            if(emotions[position].isactive){
                holder.ghostimage.alpha=1.0f

            }else{
                holder.ghostimage.alpha=0.4f
            }

            if(parent.parent.editmode){
                holder_editmode(holder,position)
            }else{
                holder_select_mode(holder,position)

            }


        }


    }

    fun holder_editmode(holder:GhostView, position: Int){
        holder.btn_close.visibility=View.VISIBLE

        holder.btn_close.setOnClickListener {
            try {
                AdapterPostdiary.rv_emotionList[listPosion].let { AdapterPostdiary.states[listPosion]= it!!.layoutManager!!.onSaveInstanceState() }
            }catch (e:Exception){
                AdapterPostdiary.states[listPosion]=null
            }
            emotions.removeAt(position)
            parent.update(listPosion, null)


        }


    }
    fun holder_select_mode(holder:GhostView, position: Int){
        holder.btn_close.visibility=View.INVISIBLE
        holder.ghostimage.setOnClickListener {
            if(emotions[position].isactive){
                emotions[position].isactive = false
                holder.ghostimage.alpha = 0.4f

            }else {
                emotions[position].isactive = true
                holder.ghostimage.alpha = 1.0f
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
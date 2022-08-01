package com.example.ghostdiary.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ghostdiary.R
import com.example.ghostdiary.databinding.ItemSelectEmotionBinding
import com.example.ghostdiary.databinding.ItemSelectSleeptimeBinding
import com.example.ghostdiary.fragment.main.SelectEmotionFragment
import com.google.android.material.slider.LabelFormatter
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*


class AdapterPostdiary(val parent: SelectEmotionFragment, val emotions: Array<String>, var selectemotion:HashMap<String,Int>, val selecttexts:HashMap<String,Array<String>>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    class GhostView(binding: ItemSelectEmotionBinding) : RecyclerView.ViewHolder(binding.root) {
        var tv_title: TextView = binding.tvTitle
        var arr_emotion:Array<ImageView> = arrayOf(binding.ivGhostVerygood,binding.ivGhostGood,binding.ivGhostNormal,binding.ivGhostBad,binding.ivGhostVerybad)
        var arr_text:Array<TextView> = arrayOf(binding.tvGhostVerygood ,binding.tvGhostGood,binding.tvGhostNormal,binding.tvGhostBad,binding.tvGhostVerybad)
        var arr_layout:Array<View> = arrayOf(binding.layoutGhostVerygood,binding.tvGhostGood,binding.layoutGhostNoraml,binding.layoutGhostBad,binding.layoutGhostVerybad)
    }
    class SleepView(binding: ItemSelectSleeptimeBinding) :RecyclerView.ViewHolder(binding.root) {
        var tv_title: TextView = binding.tvTitle
        var slider=binding.slider

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view : View?
        return when(viewType) {
            1 -> {
                view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_select_emotion, parent, false)
                return GhostView(ItemSelectEmotionBinding.bind(view))
            }
            else -> {
                var view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_select_sleeptime, parent, false)
                return SleepView(ItemSelectSleeptimeBinding.bind(view))
            }
        }

    }
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(getItemViewType(position)==1){
            var holder=holder as GhostView

            holder.tv_title.text=emotions[position]
            var seltext =selecttexts[emotions[position]]
            var j =0

            if(emotions[position]=="하루의 감정") {
                switch_Active(holder)
            }

            for(i in 0..4){
                if (seltext==null || j >= seltext!!.size ){
                    holder.arr_layout[i].visibility=View.GONE
                    continue
                }
                holder.arr_text[i].text = seltext[j]
                holder.arr_emotion[i].setOnClickListener{
                    if(selectemotion[emotions[position]]!= -1)
                        holder.arr_emotion[selectemotion[emotions[position]]!!].alpha=0.4f
                    selectemotion[emotions[position]]=i
                    holder.arr_emotion[i].alpha=1f

                }
                j++
            }

        }else {
            var holder = holder as SleepView
            var yesterday = Calendar.getInstance()
            var curday = Calendar.getInstance()
            curday.time = parent.date
            yesterday.time = parent.date
            yesterday.add(Calendar.DATE, -1)
            yesterday.set(Calendar.HOUR, 18)

            var timeformat = SimpleDateFormat("dd일 HH시")


            holder.slider.values= listOf<Float>(0.0f,10.0f)
            holder.slider.setLabelFormatter(

                LabelFormatter { value ->
                    var temp = Calendar.getInstance()
                    temp.time=yesterday.time
                    temp.add(Calendar.HOUR, value.toInt())
                    var to = timeformat.format(temp.time)

                    to
                })
            holder.slider.addOnChangeListener { slider, value, fromUser ->
                val values = holder.slider.values
                parent.sleepend = values[1].toInt()
                parent.sleepstart = values[0].toInt()
            }
        }



    }

    fun switch_Active(holder:GhostView){

        for(i in holder.arr_emotion){
            i.setAlpha(0.4f)
        }
        selectemotion[holder.tv_title.text as String]=2
        holder.arr_emotion[2].alpha=1f

    }

    override fun getItemViewType(position: Int): Int {
        if(emotions[position]!="수면시간"){
            return 1

        }
        else{
            return 2
        }
    }

    override fun getItemCount(): Int {
        return emotions.size
    }

}
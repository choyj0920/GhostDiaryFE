package com.example.ghostdiary.adapter

import android.content.ContentValues.TAG
import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.isInvisible
import androidx.recyclerview.widget.RecyclerView
import com.example.ghostdiary.R
import com.example.ghostdiary.dataclass.Day_Diary
import java.text.SimpleDateFormat
import java.util.*

class AdapterDay(val tempMonth:Int, val dayList: MutableList<Date>,val emotionMap: HashMap<String, Day_Diary>): RecyclerView.Adapter<AdapterDay.DayView>() {
    val ROW = 6

    class DayView(view: View) : RecyclerView.ViewHolder(view) {
        var tv_date: TextView = view.findViewById(R.id.tv_date)
        var iv_date: ImageView=view.findViewById(R.id.iv_date)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayView {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.item_calendar_days, parent, false)
        return DayView(view)
    }
    override fun onBindViewHolder(holder: DayView, position: Int) {
        holder.iv_date.setOnClickListener {

        }
        holder.tv_date.text = dayList[position].date.toString()

        var transFormat = SimpleDateFormat("yyyyMMdd")
        var to = transFormat.format(dayList[position]);
        if(emotionMap.containsKey(to)){
            Log.d(TAG,"포함!! ${to}, ${emotionMap[to]}")
            holder.iv_date.setImageResource(
                when(emotionMap.get(to)!!.today_emotion){
                    0 -> R.drawable.ghost_verygood
                    1 -> R.drawable.ghost_good
                    2 -> R.drawable.ghost_normal
                    3 -> R.drawable.ghost_bad
                    4 -> R.drawable.ghost_verybad

                    else -> {R.drawable.ic_ghost}
                } as Int
            )
        }
        else if(tempMonth != dayList.get(position).month+1) {
            holder.iv_date.isInvisible=true
            holder.tv_date.isInvisible=true
        }
        else{ // 비워져 있는 칸
            
        }
    }

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

    override fun getItemCount(): Int {
        return ROW * 7
    }
}
package com.example.ghostdiary.adapter

import android.content.ContentValues.TAG
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isInvisible
import androidx.recyclerview.widget.RecyclerView
import com.example.ghostdiary.MainActivity
import com.example.ghostdiary.R
import com.example.ghostdiary.databinding.ItemCalendarDaysBinding
import com.example.ghostdiary.dataclass.Day_Diary
import com.example.ghostdiary.fragment.main.CalendarFragment
import java.text.SimpleDateFormat
import java.util.*

class AdapterDay(val parent_fragment: CalendarFragment, val tempMonth:Int, val dayList: MutableList<Date>, val emotionMap: HashMap<String, Day_Diary>): RecyclerView.Adapter<AdapterDay.DayView>() {
    val ROW = 6

    class DayView(val bindng: ItemCalendarDaysBinding) : RecyclerView.ViewHolder(bindng.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayView {

        var view = LayoutInflater.from(parent.context).inflate(R.layout.item_calendar_days, parent, false)

        return DayView(ItemCalendarDaysBinding.bind(view))
    }
    override fun onBindViewHolder(holder: DayView, position: Int) {
        holder.bindng.ivDate .setOnClickListener {

        }
        holder.bindng.tvDate.text = dayList[position].date.toString()

        var transFormat = SimpleDateFormat("yyyy-MM-dd")
        var to = transFormat.format(dayList[position]);
        if(tempMonth != dayList.get(position).month) {
            holder.bindng.ivDate.isInvisible=true
            holder.bindng.tvDate.isInvisible=true
        } else if(emotionMap.containsKey(to)){
            Log.d(TAG,"포함!! ${to}, ${emotionMap[to]}")
            holder.bindng.ivDate.setImageResource(
                when(emotionMap.get(to)!!.today_emotion.ghostimage){
                    0 -> R.drawable.ghost_00_verygood
                    1 -> R.drawable.ghost_01_good
                    2 -> R.drawable.ghost_02_normal
                    3 -> R.drawable.ghost_03_bad
                    4 -> R.drawable.ghost_04_verybad

                    else -> {R.drawable.ic_ghost}
                } as Int


            )
            holder.bindng.ivDate.setOnClickListener{
//                parent_fragment.show_post(emotionMap[to]!!.date)
                MainActivity.mainactivity.change_to_selectemotion(dayList[position])
            }
            if(parent_fragment.emotionpostion != -1 && parent_fragment.emotionpostion != emotionMap.get(to)!!.today_emotion.ghostimage){
                holder.bindng.ivDate.setImageResource(R.drawable.rectangle)
            }

        }
        else{ // 비워져 있는 칸
            holder.bindng.ivDate.setOnClickListener{
                MainActivity.mainactivity.change_to_selectemotion(dayList[position])
            }

        }
    }



    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

    override fun getItemCount(): Int {
        return ROW * 7
    }
}
package com.example.ghostdiary.adapter

import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isInvisible
import androidx.recyclerview.widget.RecyclerView
import com.example.ghostdiary.MainActivity
import com.example.ghostdiary.R
import com.example.ghostdiary.utilpackage.Util
import com.example.ghostdiary.databinding.ItemCalendarDaysBinding
import com.example.ghostdiary.dataclass.Day_Diary
import com.example.ghostdiary.fragment.calendar.CalendarFragment
import com.example.ghostdiary.PostDiaryActivity
import java.text.SimpleDateFormat
import java.util.*

class AdapterDay(val parent_fragment: CalendarFragment, val tempMonth:Int, val dayList: MutableList<Date>, val emotionMap: HashMap<String, Day_Diary>): RecyclerView.Adapter<AdapterDay.DayView>() {
    val ROW = 6

    class DayView(val bindng: ItemCalendarDaysBinding) : RecyclerView.ViewHolder(bindng.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayView {

        var view = LayoutInflater.from(parent.context).inflate(R.layout.item_calendar_days, parent, false)
        Util.setGlobalFont(view!!)

        return DayView(ItemCalendarDaysBinding.bind(view))
    }
    override fun onBindViewHolder(holder: DayView, position: Int) {
        holder.bindng.ivDate .setOnClickListener {

        }
        holder.bindng.tvDate.text = dayList[position].date.toString()

        var transFormat = SimpleDateFormat("yyyy-MM-dd")
        var to = transFormat.format(dayList[position]);

        when(MainActivity.curTheme){
            1->{
                holder.bindng.tvDate.setTextColor(Color.parseColor("#4A4A4A"))

            }
            2->{
                holder.bindng.tvDate.setTextColor(Color.parseColor("#FFFFFF"))

            }
            3->{
                holder.bindng.tvDate.setTextColor(Color.parseColor("#FFFFFF"))

            }
        }

        if(tempMonth != dayList.get(position).month) {
            holder.bindng.ivDate.isInvisible=true
            holder.bindng.tvDate.isInvisible=true
        } else if(emotionMap.containsKey(to)){
            Log.d(TAG,"포함!! ${to}, ${emotionMap[to]}")
            holder.bindng.ivDate.setImageResource(
                when(emotionMap.get(to)!!.today_emotion.ghostimage){

                    0 -> if(MainActivity.curTheme !=3) R.drawable.ghost_00_verygood else R.drawable.ghost_halloween_00
                    1 -> if(MainActivity.curTheme !=3) R.drawable.ghost_01_good else R.drawable.ghost_halloween_01
                    2 -> if(MainActivity.curTheme !=3) R.drawable.ghost_02_normal else R.drawable.ghost_halloween_02
                    3 -> if(MainActivity.curTheme !=3) R.drawable.ghost_03_bad else R.drawable.ghost_halloween_03
                    4 -> if(MainActivity.curTheme !=3) R.drawable.ghost_04_verybad else R.drawable.ghost_halloween_04

                    else -> {if(MainActivity.curTheme==1) R.drawable.ic_ghost_default else R.drawable.ic_ghost_default_dark}
                } as Int


            )
            holder.bindng.ivDate.setOnClickListener{
//                parent_fragment.show_post(emotionMap[to]!!.date)
//                MainActivity.mainactivity.change_to_selectemotion(dayList[position])
                val intent = Intent(parent_fragment.requireContext(), PostDiaryActivity::class.java)
                intent.putExtra("date",dayList[position].time)
                intent.putExtra("firsttext",true)
                parent_fragment.requireActivity().startActivity(intent)

            }
            if(parent_fragment.emotionpostion != -1 && parent_fragment.emotionpostion != emotionMap.get(to)!!.today_emotion.ghostimage){
                holder.bindng.ivDate.setImageResource(if(MainActivity.curTheme==1) R.drawable.ic_ghost_default else R.drawable.ic_ghost_default_dark)
            }

        }
        else{ // 비워져 있는 칸
            holder.bindng.ivDate.setOnClickListener{
                val intent = Intent(parent_fragment.requireContext(), PostDiaryActivity::class.java)
                intent.putExtra("date",dayList[position].time)
                intent.putExtra("firsttext",false)
                parent_fragment.requireActivity().startActivity(intent)

            }
            holder.bindng.ivDate.setImageResource(if(MainActivity.curTheme==1) R.drawable.ic_ghost_default else R.drawable.ic_ghost_default_dark)

        }
    }



    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

    override fun getItemCount(): Int {
        return ROW * 7
    }
}
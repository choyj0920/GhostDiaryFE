package com.ghostdiary.ghostdiary.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ghostdiary.ghostdiary.MainActivity
import com.ghostdiary.ghostdiary.MainViewModel
import com.ghostdiary.ghostdiary.R
import com.ghostdiary.ghostdiary.utilpackage.Util
import com.ghostdiary.ghostdiary.databinding.ItemMonthBinding
import com.ghostdiary.ghostdiary.fragment.calendar.CalendarFragment
import java.text.SimpleDateFormat
import java.util.*

class AdapterMonth(var parent: CalendarFragment, val viewmodel : MainViewModel): RecyclerView.Adapter<AdapterMonth.MonthView>() {
    val center = Int.MAX_VALUE / 2
    private var calendar =Calendar.getInstance()


    inner class MonthView(val binding: ItemMonthBinding): RecyclerView.ViewHolder(binding.root){
        var year:TextView=binding.tvYear
        var month:TextView=binding.tvMonth
        var llayout=binding.parentlayout
        var rvcalendar:RecyclerView=binding.rvCalendar
        var halloweenback=binding.backhalloween

        var daylayout=binding.`daynamelayout`


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MonthView {

        val view : View?

        view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_month, parent, false)

        Util.setGlobalFont(view!!)
        return MonthView(ItemMonthBinding.bind(view))
    }

    override fun onBindViewHolder(holder: MonthView, position: Int) {
        calendar.time = parent.lastcalendar.time
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        calendar.add(Calendar.MONTH, position - center)

        val year = SimpleDateFormat("yyyy").format(calendar.time)

        val month = SimpleDateFormat("MMM").format(calendar.time)

        holder.month.text=month
        holder.month.setOnClickListener{
            parent.showmonthpicker()
        }
        holder.year.setOnClickListener {
            parent.showmonthpicker()
        }
        when(MainActivity.curTheme){
            1->{
                holder.llayout.setBackgroundColor(Color.parseColor("#ffffff"))
                holder.year.setTextColor(Color.parseColor("#4A4A4A"))
                holder.month.setTextColor(Color.parseColor("#4A4A4A"))
                holder.halloweenback.visibility=View.GONE
                Util.setChildColor(holder.daylayout,"#4A4A4A")

            }
            2->{
                holder.llayout.setBackgroundColor(Color.parseColor("#000000"))
                holder.year.setTextColor(Color.parseColor("#FFFFFF"))
                holder.month.setTextColor(Color.parseColor("#FFFFFF"))
                Util.setChildColor(holder.daylayout,"#FFFFFF")
                holder.halloweenback.visibility=View.GONE



            }
            3->{
                
                holder.llayout.setBackgroundColor(Color.parseColor("#000000"))
                holder.halloweenback.visibility=View.VISIBLE
                holder.year.setTextColor(Color.parseColor("#FFFFFF"))
                holder.month.setTextColor(Color.parseColor("#FFFFFF"))
                Util.setChildColor(holder.daylayout,"#FFFFFF")


            }
        }


        holder.year.text=year

        val tempMonth = calendar.get(Calendar.MONTH)

        var dayList: MutableList<Date> = MutableList(6 * 7) { Date() }
        for(i in 0..5) {
            for(k in 0..6) {
                calendar.add(Calendar.DAY_OF_MONTH, (1-calendar.get(Calendar.DAY_OF_WEEK)) + k)
                dayList[i * 7 + k] = calendar.time
            }
            calendar.add(Calendar.WEEK_OF_MONTH, 1)
        }

        val dayListManager = GridLayoutManager(this.parent.requireContext(), 7)
        val dayListAdapter = AdapterDay(this.parent,tempMonth, dayList, viewmodel.getEmotionArray())


        holder.rvcalendar.apply {
            layoutManager=dayListManager
            adapter=dayListAdapter
        }
    }

    override fun getItemCount(): Int {
        return Int.MAX_VALUE
    }
}
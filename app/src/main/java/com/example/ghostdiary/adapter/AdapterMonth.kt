package com.example.ghostdiary.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ghostdiary.MainViewModel
import com.example.ghostdiary.R
import com.example.ghostdiary.utilpackage.Util
import com.example.ghostdiary.databinding.ItemMonthBinding
import com.example.ghostdiary.fragment.calendar.CalendarFragment
import java.text.SimpleDateFormat
import java.util.*

class AdapterMonth(var parent: CalendarFragment, val viewmodel : MainViewModel): RecyclerView.Adapter<AdapterMonth.MonthView>() {
    val center = Int.MAX_VALUE / 2
    private var calendar =Calendar.getInstance()


    inner class MonthView(val binding: ItemMonthBinding): RecyclerView.ViewHolder(binding.root){
        var year:TextView=binding.tvYear
        var month:TextView=binding.tvMonth
        var rvcalendar:RecyclerView=binding.rvCalendar


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
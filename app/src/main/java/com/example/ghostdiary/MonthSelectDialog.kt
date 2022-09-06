package com.example.ghostdiary;

import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle;
import android.os.Parcelable
import android.util.Log
import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.ghostdiary.databinding.DialogMonthSelectBinding
import com.example.ghostdiary.databinding.ItemSelectMonthBinding
import com.example.ghostdiary.dataclass.Day_Diary
import com.example.ghostdiary.fragment.calendar.CalendarFragment
import com.example.ghostdiary.fragment.calendar.RecordFragment
import com.example.ghostdiary.utilpackage.Util
import java.util.*

class MonthSelectDialog(var parent: Fragment?,var calendar: Calendar): DialogFragment() {

    // 뷰 바인딩 정의
    private var _binding: DialogMonthSelectBinding? = null
    private val binding get() = _binding!!
    var selectmonth=-1
    var selectyear=-1





    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogMonthSelectBinding.inflate(inflater, container, false)
        val view = binding.root

        // 레이아웃 배경을 투명하게 해줌, 필수 아님

        selectmonth=calendar.get(Calendar.MONTH)
        selectyear=calendar.get(Calendar.YEAR)


        initview()


        Util.setGlobalFont(binding.root)

        return view
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        if(parent is CalendarFragment){
            (parent as CalendarFragment).init_rv()
        }
    }

    fun initview(){
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding.tvYear.text="${selectyear}년"

        binding.ivLastyear.setOnClickListener {
            selectyear-=1
            binding.tvYear.text="${selectyear}년"
        }
        binding.ivNextyear.setOnClickListener {
            selectyear+=1
            binding.tvYear.text="${selectyear}년"
        }



        binding.tvCancel.setOnClickListener {
            if(parent is CalendarFragment){
                (parent as CalendarFragment).init_rv()

            }else{

            }

            dismiss()
        }
        binding.tvOk.setOnClickListener {
            if(parent is CalendarFragment){
                calendar.set(selectyear,selectmonth,1)
                (parent as CalendarFragment).init_rv()
                Log.d("TAG","INITRV 실행")


            }else if (parent is RecordFragment){
                (parent as RecordFragment).setMonth(selectyear,selectmonth)

            }
            dismiss()

        }
        update_rv()




    }
    fun update_rv(){
        var state: Parcelable?=null
        try {
            state=binding.rvMonth.layoutManager!!.onSaveInstanceState()

        }catch (e:Exception){
            state=null
        }

        val ghostarr= Day_Diary.addghost_arr()
        val ghostlistmanager = GridLayoutManager(context, 4,GridLayoutManager.VERTICAL,false)
        val ghostlistadpter = Monthadapter(this)


        binding.rvMonth.apply {
            layoutManager=ghostlistmanager
            adapter=ghostlistadpter
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    class Monthadapter(var parent:MonthSelectDialog): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        companion object{
            var monthname= arrayListOf<String>("JAN","FEB","MAR","APR","MAY","JUN","JUL","AUG","SEP","OCT","NOV","DEC")
        }


        class MonthView(binding: ItemSelectMonthBinding) : RecyclerView.ViewHolder(binding.root) {
            var layout=binding.layoutmonthview
            var monthname=binding.tvName
            var monthnum=binding.tvNum
        }



        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val view : View?

            view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_select_month, parent, false)

            Util.setGlobalFont(view!!)

            return MonthView(ItemSelectMonthBinding.bind(view))


        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

            var holder=holder as MonthView



            var isnum=position

            holder.monthname.text= monthname[isnum]
            holder.monthnum.text= (isnum+1).toString()

            if(isnum==parent.selectmonth){
                holder.layout.background=ContextCompat.getDrawable(MainActivity.mainactivity,R.drawable.circle_backgroud_black)
                holder.monthnum.setTextColor(ContextCompat.getColor(parent.requireContext(), R.color.white))
                holder.monthname.setTextColor(ContextCompat.getColor(parent.requireContext(), R.color.white))

                holder.layout.setOnClickListener {  }

            }else{
                holder.layout.background=ContextCompat.getDrawable(MainActivity.mainactivity,R.drawable.circle_backgroud_white)
                holder.monthnum.setTextColor(ContextCompat.getColor(parent.requireContext(), R.color.lightblack))
                holder.monthname.setTextColor(ContextCompat.getColor(parent.requireContext(), R.color.gray))

                holder.layout.setOnClickListener {
                    parent.selectmonth=isnum
                    parent.update_rv()

                }

            }


        }


        override fun getItemViewType(position: Int): Int {
            return 1
        }

        override fun getItemCount(): Int {
            return 12
        }

    }

}

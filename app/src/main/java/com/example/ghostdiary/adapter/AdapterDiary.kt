package com.example.ghostdiary.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.example.ghostdiary.MainActivity
import com.example.ghostdiary.R
import com.example.ghostdiary.databinding.ItemDiaryBinding

import com.example.ghostdiary.dataclass.Day_Diary

import com.example.ghostdiary.fragment.main.RecordFragment
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class AdapterDiary(val parent: RecordFragment, var diaryarr:MutableList<Day_Diary>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object{
        var daytostring:ArrayList<String> = arrayListOf("error","월","화","수","목","금","토","일")
    }

    class DiaryView(binding: ItemDiaryBinding) : RecyclerView.ViewHolder(binding.root) {
        var tv_date: TextView = binding.tvDate
        var tv_day: TextView = binding.tvDay
        var iv_ghost:ImageView=binding.ivGhost
        var iv_image:ImageView=binding.ivImage
        var sideoption:ImageView = binding.btnOption
        var tv_text:TextView= binding.tvText
        var layout=binding.layoutItemDiary

    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view : View?

        view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_diary, parent, false)
        return DiaryView(ItemDiaryBinding.bind(view))


    }
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        var holder=holder as DiaryView

        var diary=diaryarr[position]

        var transFormat = SimpleDateFormat("M월 d일")
        var str_date = transFormat.format(diary.date)
        var day= Calendar.getInstance()
        day.time=diary.date

        holder.tv_date.text=str_date
        holder.tv_day.text= "${daytostring[day.get(Calendar.DAY_OF_WEEK)]}요일"

        holder.iv_ghost.setImageResource(Day_Diary.int_to_Image(diary.today_emotion.ghostimage))

        // 이미지 뷰 등록
        if(diary.image!="" && diary.image != null){
            var transFormat = SimpleDateFormat("yyyy_MM_dd")
            var to = transFormat.format(diary.date)
            //내부저장소 캐시 경로를 받아옵니다.
            val storage: File = parent.requireActivity().cacheDir
            //저장할 파일 이름

            val fileName = diary.image
            //storage 에 파일 인스턴스를 생성합니다.
            val tempFile = File(storage, fileName)
            try {
                holder.iv_image.visibility=View.VISIBLE
                holder.iv_image.setImageURI(tempFile.toUri())
            }catch (e: Exception){
                holder.iv_image.visibility=View.GONE
            }
        }else{
            holder.iv_image.visibility=View.GONE
        }
         holder.sideoption.setOnClickListener {

             var popupMenu=PopupMenu(parent.context,holder.sideoption)
             parent.requireActivity().menuInflater.inflate(R.menu.diary_sidemenu,popupMenu.menu)

             popupMenu.setOnMenuItemClickListener { item  ->
                when(item?.itemId){
                    R.id.menu_edit->{
                        MainActivity.mainactivity.change_to_editDiary(diary.date)
                    }
                    R.id.menu_delete->{
                        parent.deleteDiary(diary.date)
                    }
                }
                 item !=null
             }
             popupMenu.show()
         }
         holder.tv_text.text=diary.text

    }

    override fun getItemViewType(position: Int): Int {
        return 1
    }

    override fun getItemCount(): Int {
        return diaryarr.size
    }

}
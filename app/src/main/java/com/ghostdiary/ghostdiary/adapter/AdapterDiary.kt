package com.ghostdiary.ghostdiary.adapter

import android.content.Intent
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ghostdiary.ghostdiary.PostDiaryActivity
import com.ghostdiary.ghostdiary.R
import com.ghostdiary.ghostdiary.utilpackage.Util
import com.ghostdiary.ghostdiary.databinding.ItemDiaryBinding

import com.ghostdiary.ghostdiary.dataclass.Day_Diary

import com.ghostdiary.ghostdiary.fragment.calendar.RecordFragment
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class AdapterDiary(val parent: RecordFragment, var diaryarr:MutableList<Day_Diary>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object{
        var daytostring: java.util.ArrayList<String> = arrayListOf("error","일","월","화","수","목","금","토")

    }

    class DiaryView(binding: ItemDiaryBinding) : RecyclerView.ViewHolder(binding.root) {
        var tv_date: TextView = binding.tvDate
        var tv_day: TextView = binding.tvDay
        var iv_ghost:ImageView=binding.ivGhost
        var iv_image:ImageView=binding.ivImage
        var rv_diary:RecyclerView = binding.rvDiary
        var tv_text:TextView= binding.tvText
        var layout=binding.layoutItemDiary

    }



    override fun onCreateViewHolder(vg: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view : View?

        view = LayoutInflater.from(vg.context)
            .inflate(R.layout.item_diary, vg, false)

        Util.setGlobalFont(view!!)

        return DiaryView(ItemDiaryBinding.bind(view))


    }
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        var holder=holder as DiaryView


        var diary=diaryarr[position]
        holder.layout.setOnClickListener {

            val intent = Intent(parent.requireContext(), PostDiaryActivity::class.java)
            intent.putExtra("date",diary.date.time)
            intent.putExtra("firsttext",true)
            parent.requireActivity().startActivity(intent)
        }


        var transFormat = SimpleDateFormat("M월 d일")
        var str_date = transFormat.format(diary.date)
        var day= Calendar.getInstance()
        day.time=diary.date

        holder.tv_date.text=str_date
        holder.tv_day.text= "${daytostring[day.get(Calendar.DAY_OF_WEEK)]}요일"

        holder.iv_ghost.setImageResource(Day_Diary.int_to_image[diary.today_emotion.ghostimage])

        var _adapter = AdapterEmotionjustview(this.parent, diary.getEmotionarrElement(),true)

        holder.rv_diary.apply {
            layoutManager= LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL,false)
            adapter=_adapter
        }

        // 이미지 뷰 등록
        if(diary.image!="" && diary.image != null){
            var transFormat = SimpleDateFormat("yyyy_MM_dd")
            var to = transFormat.format(diary.date)
            //내부저장소 캐시 경로를 받아옵니다.
            val storage: File = parent.requireActivity().cacheDir
            //저장할 파일 이름

            val fileName = diary.image
            //storage 에 파일 인스턴스를 생성합니다.
            val tempFile = File(storage, fileName).path
            val bitmap = BitmapFactory.decodeFile(tempFile)
            if (bitmap != null) {
                holder.iv_image.visibility=View.VISIBLE
                holder.iv_image.setImageURI(tempFile.toUri())
            }
            else {
                holder.iv_image.visibility=View.GONE
                diary.image=null
                Log.e("TAG","파일을 찾을 수 없어 수정되었습니다.")


            }


        }else{
            holder.iv_image.visibility=View.GONE
        }

         holder.tv_text.text=diary.text
        if(diary.text==""){
            holder.tv_text.visibility=View.GONE
        }else{
            holder.tv_text.visibility=View.VISIBLE

        }



    }

    override fun getItemViewType(position: Int): Int {
        return 1
    }

    override fun getItemCount(): Int {
        return diaryarr.size
    }

}
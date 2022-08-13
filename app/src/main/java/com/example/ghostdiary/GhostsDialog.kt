package com.example.ghostdiary;

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle;
import android.os.Parcelable
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ghostdiary.adapter.AdapterDay
import com.example.ghostdiary.adapter.AdapterPostdiary

import com.example.ghostdiary.databinding.DialogGhostsBinding;
import com.example.ghostdiary.databinding.ItemEmotionGhostBinding
import com.example.ghostdiary.dataclass.Day_Diary
import com.example.ghostdiary.dataclass.emotionclass
import com.example.ghostdiary.fragment.main.EditDiaryFragment
import com.skydoves.balloon.Balloon
import com.skydoves.balloon.BalloonSizeSpec

class GhostsDialog(var curpos:Int,var parent:AdapterPostdiary,var emotionlist: ArrayList<emotionclass>): DialogFragment() {

    // 뷰 바인딩 정의
    private var _binding: DialogGhostsBinding? = null
    private val binding get() = _binding!!

    private var selectghost:Int=0



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogGhostsBinding.inflate(inflater, container, false)
        val view = binding.root

        // 레이아웃 배경을 투명하게 해줌, 필수 아님

        initview()



        return view
    }

    fun initview(){

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding.tvOk.setOnClickListener {
            var ghostname=binding.inputGhostname.text.toString()
            if(ghostname==""){
                MainActivity.mainactivity.showmessage("유령의 이름을 입력해주세요.")
                return@setOnClickListener
            }
            emotionlist.add(emotionclass(ghostname,selectghost,false))
            dismiss()
            parent.update(curpos)

        }
        Update_rv()


    }
    fun Update_rv(){
        var state: Parcelable?=null
        try {
            state=binding.rvGhosts.layoutManager!!.onSaveInstanceState()

        }catch (e:Exception){
            state=null
        }

        val ghostarr= (0..(Day_Diary.int_to_image.size-1)).toList()
        val ghostlistmanager = GridLayoutManager(context, 4)
        val ghostlistadpter = ghostadaper(this,ghostarr)


        binding.rvGhosts.apply {
            layoutManager=ghostlistmanager
            adapter=ghostlistadpter
        }
        if(state !=null){
            binding.rvGhosts.layoutManager!!.onRestoreInstanceState(state)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    class ghostadaper(var parent:GhostsDialog ,var emotions:List<Int>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {


        class GhostView(binding: ItemEmotionGhostBinding) : RecyclerView.ViewHolder(binding.root) {
            var emotionname: TextView = binding.tvGhost
            var layout=binding.layoutGhostView
            var btn_close: ImageView = binding.btnGhostDelete
            var ghostimage: ImageView =binding.ivGhost
        }



        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val view : View?

            view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_emotion_ghost, parent, false)
            return GhostView(ItemEmotionGhostBinding.bind(view))


        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

            var holder=holder as GhostView

            holder.btn_close.visibility=View.GONE
            holder.emotionname.visibility=View.GONE

            holder.ghostimage.setImageResource(Day_Diary.int_to_image.get(emotions[position]))
            if(position==parent.selectghost){
                holder.layout.setBackgroundColor(ContextCompat.getColor(MainActivity.mainactivity,R.color.gray_overlay))
            }
            holder.ghostimage.setOnClickListener {
                if(position==parent.selectghost){

                }else{
                    parent.selectghost=position

                    parent.Update_rv()

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

}

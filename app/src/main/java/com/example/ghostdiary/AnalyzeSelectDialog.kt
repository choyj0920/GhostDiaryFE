package com.example.ghostdiary;

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle;
import android.os.Parcelable
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ghostdiary.databinding.DialogAnalyzeSelectBinding

import com.example.ghostdiary.databinding.ItemAnalyzeBinding
import com.example.ghostdiary.dataclass.Day_Diary
import com.example.ghostdiary.dataclass.emotion_analysis
import com.example.ghostdiary.fragment.analyze.AnalysisDetailFragment
import com.example.ghostdiary.utilpackage.Util

class AnalyzeSelectDialog(var _parent: AnalysisDetailFragment): DialogFragment() {

    // 뷰 바인딩 정의
    private var binding: DialogAnalyzeSelectBinding? = null


    private var selectghost:emotion_analysis?=null

    lateinit var emotionarray : ArrayList<emotion_analysis>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogAnalyzeSelectBinding.inflate(inflater, container, false)
        val view = binding!!.root

        // 레이아웃 배경을 투명하게 해줌, 필수 아님

        initview()


        Util.setGlobalFont(binding!!.root)

        return view
    }

    fun initview(){
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        updatelist()

        binding!!.tvOk.setOnClickListener {
            if(selectghost==null){
                MainActivity.mainactivity.showmessage("분석할 활동이 선택되지 않았습니다.")
                return@setOnClickListener
            }else{
                _parent.selectemotion=selectghost

                dismiss()
                _parent.selectemotionUpdate()

            }

        }

        binding!!.inputEmotionname.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                updatelist()

            }
        })

    }
    fun updatelist(){
        emotionarray  = arrayListOf()
        var searchkey=binding!!.inputEmotionname.text.toString()
        for(i in _parent.analysisList){
            if(i.text.contains(searchkey)){
                emotionarray.add(i)
            }
        }
        if(selectghost!=null && !emotionarray.contains(selectghost)){
//            selectghost=null
        }

        update_rv()
    }

    fun update_rv(){

        var state: Parcelable?=null
        try {
            state=binding!!.rvAnalyze.layoutManager!!.onSaveInstanceState()

        }catch (e:Exception){
            state=null
        }

        val ghostlistmanager = GridLayoutManager(context, 3,GridLayoutManager.HORIZONTAL,false)
        val ghostlistadpter = ghostadapter(this,emotionarray)


        binding!!.rvAnalyze.apply {
            layoutManager=ghostlistmanager
            adapter=ghostlistadpter
        }
        if(state !=null){
            binding!!.rvAnalyze.layoutManager!!.onRestoreInstanceState(state)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    class ghostadapter(var parent:AnalyzeSelectDialog, var emotions:ArrayList<emotion_analysis>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {


        class AnalyzeGhostView(binding: ItemAnalyzeBinding) : RecyclerView.ViewHolder(binding.root) {
            var layout=binding.layoutGhostView
            var ghostimage: ImageView =binding.ivGhost
            var ghostname:TextView= binding.tvName
        }



        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val view : View?

            view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_analyze, parent, false)

            Util.setGlobalFont(view!!)

            return AnalyzeGhostView(ItemAnalyzeBinding.bind(view))


        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

            var holder=holder as AnalyzeGhostView

            var emotion = emotions[position]
            holder.ghostimage.alpha=1f

            holder.ghostimage.setImageResource(Day_Diary.int_to_image.get(emotion.ghostnum))
            holder.ghostname.text=emotion.text

            if(emotion==parent.selectghost){
                holder.layout.background=ContextCompat.getDrawable(MainActivity.mainactivity,R.drawable.circle_backgroud)

                holder.layout.setOnClickListener {

                }
            }else{
                holder.layout.background=ContextCompat.getDrawable(MainActivity.mainactivity,R.drawable.circle_backgroud_white)

                holder.layout.setOnClickListener {
                    parent.selectghost=emotion
                    parent.update_rv()

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

package com.ghostdiary.ghostdiary.adapter

import android.content.res.ColorStateList
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ghostdiary.ghostdiary.GhostsSelectDialog
import com.ghostdiary.ghostdiary.R
import com.ghostdiary.ghostdiary.utilpackage.Util
import com.ghostdiary.ghostdiary.databinding.ItemSelectEmotionBinding
import com.ghostdiary.ghostdiary.databinding.ItemSelectSleeptimeBinding
import com.ghostdiary.ghostdiary.dataclass.Day_Diary
import com.ghostdiary.ghostdiary.dataclass.emotionclass
import com.ghostdiary.ghostdiary.fragment.postdiary.SelectEmotionFragment
import com.google.android.material.slider.LabelFormatter
import java.text.SimpleDateFormat
import java.util.*


class AdapterPostdiary(val parent: SelectEmotionFragment, var sleepstart:Int, var sleepend :Int, val emotions: Array<String>, var selecttexts:ArrayList<ArrayList<emotionclass>>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object{
        var rv_emotionList:Array<RecyclerView?> = arrayOfNulls<RecyclerView?>(Day_Diary.emotionarr.size)
        var states:Array<Parcelable?> = arrayOfNulls<Parcelable?>(Day_Diary.emotionarr.size)
        var emotionAdapterList:Array<AdapterEmotion?> = arrayOfNulls<AdapterEmotion?>(Day_Diary.emotionarr.size)

        fun init_value(){
            rv_emotionList= arrayOfNulls<RecyclerView?>(Day_Diary.emotionarr.size)
            states = arrayOfNulls<Parcelable?>(Day_Diary.emotionarr.size)
            emotionAdapterList = arrayOfNulls<AdapterEmotion?>(Day_Diary.emotionarr.size)
        }

        var arraytint:Array<String> = arrayOf("#FFDED7","#FDE9D1","#FFF4D7","#D7F5FF","#D7E2FF")
    }





    class EmotionView(binding: ItemSelectEmotionBinding) : RecyclerView.ViewHolder(binding.root) {
        var tv_title: TextView = binding.tvTitle
        var rv_emotion: RecyclerView= binding.rvEmotionlist
        var btn_plus:ImageView=binding.btnPlusbutton


    }
    class SleepView(binding: ItemSelectSleeptimeBinding) :RecyclerView.ViewHolder(binding.root) {
        var tv_title: TextView = binding.tvTitle
        var slider=binding.slider
        var scrollView=binding.viewScroll

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view : View?
        return when(viewType) {
            1 -> {
                view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_select_emotion, parent, false)
                Util.setGlobalFont(view!!)

                return EmotionView(ItemSelectEmotionBinding.bind(view))
            }
            else -> {
                var view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_select_sleeptime, parent, false)
                Util.setGlobalFont(view!!)

                return SleepView(ItemSelectSleeptimeBinding.bind(view))
            }
        }

    }
    fun update(position: Int, parcelable: Parcelable?=null,isadd:Boolean=false){

        var state = states[position]


        var emotionManager = LinearLayoutManager(parent.context,LinearLayoutManager.HORIZONTAL,false)
        emotionAdapterList[position]= AdapterEmotion(this,position,selecttexts[position])

        rv_emotionList[position].let {
            it?.apply {
                layoutManager = emotionManager
                adapter = emotionAdapterList[position]
            }
            state.let { rv_emotionList[position]!!.layoutManager?.onRestoreInstanceState(it) }
            if(isadd)
                rv_emotionList[position]!!.smoothScrollToPosition(selecttexts[position].size-1)

        }

    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(getItemViewType(position)==1){
            var holder=holder as EmotionView


            holder.tv_title.backgroundTintList=  ColorStateList.valueOf(ContextCompat.getColor(parent.requireContext(),
                when(emotions[position]){
                    Day_Diary.emotionname[0]->R.color.tint1
                    Day_Diary.emotionname[1]->R.color.tint2
                    Day_Diary.emotionname[2]->R.color.tint3
                    Day_Diary.emotionname[3]->R.color.tint4
                    Day_Diary.emotionname[4]->R.color.tint5

                    else->R.color.tint5
                } ))



            holder.tv_title.text=emotions[position]
            if(parent.editmode && position !=0 && emotions[position] !=Day_Diary.emotionname[4]){
                holder.btn_plus.visibility=View.VISIBLE
                holder.btn_plus.setOnClickListener {
                    val dialog = GhostsSelectDialog(position,this, selecttexts[position])
                    dialog.isCancelable = true
                    dialog.show(parent.requireActivity().supportFragmentManager, "ConfirmDialog")
                }

            }
            else{
                holder.btn_plus.visibility=View.GONE
            }
            try {
                rv_emotionList[position].let { states[position]= it!!.layoutManager!!.onSaveInstanceState() }
            }catch (e:Exception){
                states[position]=null
            }
            rv_emotionList[position]=holder.rv_emotion

            update(position,states[position])

        }
        else {
            var holder = holder as SleepView
            var yesterday = Calendar.getInstance()
            yesterday.time = parent.date
            yesterday.add(Calendar.DATE, -1)
            yesterday.set(Calendar.HOUR_OF_DAY, 18)
            yesterday.set(Calendar.MINUTE,0)

            var timeformat = SimpleDateFormat(parent.resources.getString(R.string.transformat_day_hour_min))


            holder.slider.values= listOf<Float>(24.0f,72.0f)
            if(sleepstart !=-1 && sleepend != -1){
                holder.slider.values= listOf<Float>(sleepstart.toFloat(),sleepend.toFloat())
            }
            holder.slider.setLabelFormatter(

                LabelFormatter { value ->
                    var temp = Calendar.getInstance()
                    temp.time=yesterday.time
                    temp.add(Calendar.MINUTE, value.toInt()*10)
                    var to = timeformat.format(temp.time)


                    to
                })
            holder.slider.addOnChangeListener { slider, value, fromUser ->

                holder.slider.alpha=1f

                val values = holder.slider.values
                parent.sleepend = values[1].toInt()
                parent.sleepstart = values[0].toInt()
            }





            holder.scrollView.post(Runnable { holder.scrollView.smoothScrollTo((holder.slider.values.average() *600 / 120).toInt(),0) })

            holder.slider.setOnTouchListener(OnTouchListener { v, event ->

                val action = event.action
                when (action) {
                    MotionEvent.ACTION_DOWN ->                 // Disallow ScrollView to intercept touch events.
                        v.parent.requestDisallowInterceptTouchEvent(true)
                    MotionEvent.ACTION_UP ->                 // Allow ScrollView to intercept touch events.
                        v.parent.requestDisallowInterceptTouchEvent(false)
                }
                // Handle Seekbar touch events.
                v.onTouchEvent(event)
                true
            })
            holder.scrollView.setOnTouchListener(OnTouchListener { v, event ->
                val action = event.action
                when (action) {
                    MotionEvent.ACTION_DOWN ->                 // Disallow ScrollView to intercept touch events.
                        v.parent.requestDisallowInterceptTouchEvent(true)
                    MotionEvent.ACTION_UP ->                 // Allow ScrollView to intercept touch events.
                        v.parent.requestDisallowInterceptTouchEvent(false)
                }
                // Handle Seekbar touch events.
                v.onTouchEvent(event)
                true
            })


        }



    }


    override fun getItemViewType(position: Int): Int {
        if(emotions[position]!=Day_Diary.emotionname[5]){
            return 1

        }
        else{
            return 2
        }
    }

    override fun getItemCount(): Int {
        return emotions.size
    }

}
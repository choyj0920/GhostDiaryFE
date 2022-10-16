package com.ghostdiary.ghostdiary;

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle;
import android.os.Parcelable
import android.view.*
import android.widget.RadioButton
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ghostdiary.ghostdiary.databinding.DialogFontSelectBinding
import com.ghostdiary.ghostdiary.databinding.ItemFontBinding

import com.ghostdiary.ghostdiary.utilpackage.Util

class FontSelectDialog(var parent: MainActivity): DialogFragment() {

    // 뷰 바인딩 정의
    private var _binding: DialogFontSelectBinding? = null
    private val binding get() = _binding!!

    private var selectfont:Int=-1



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogFontSelectBinding.inflate(inflater, container, false)
        val view = binding.root

        // 레이아웃 배경을 투명하게 해줌, 필수 아님
        dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        selectfont= Util.curfont

        initview()


        Util.setGlobalFont(binding.root)

        return view
    }

    fun initview(){

        binding.tvCancel.setOnClickListener {
            dismiss()
        }
        binding.tvSave.setOnClickListener {
            if(selectfont in 0..Util.fontarray.size-1){
                parent.savefontsetting(selectfont)
            }else{
                parent.showmessage("Font was not set due to an error.")
            }
            dismiss()



        }

        Update_rv()

        binding.rvFont.smoothScrollToPosition(selectfont)



    }
    fun Update_rv(){

        var state: Parcelable?=null
        try {
            state=binding.rvFont.layoutManager!!.onSaveInstanceState()

        }catch (e:Exception){
            state=null
        }

        var arr:List<Int> = (0..Util.fontarray.size-1).toList()

        val listmanager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
        val fontlistadpter = fontadapter(this,arr)


        binding.rvFont.apply {
            layoutManager=listmanager
            adapter=fontlistadpter
        }
        if(state !=null){
            binding.rvFont.layoutManager!!.onRestoreInstanceState(state)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    class fontadapter(var parent:FontSelectDialog, var fontlist:List<Int>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {


        class fontview(binding: ItemFontBinding) : RecyclerView.ViewHolder(binding.root) {
            var rb :RadioButton=binding.rbFont
            var fonttext:TextView=binding.tvFont
        }



        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val view : View?

            view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_font, parent, false)

            Util.setGlobalFont(view!!)

            return fontview(ItemFontBinding.bind(view))


        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

            var holder=holder as fontview

            holder.fonttext.text=MainActivity.fontname[fontlist[position]]
            holder.fonttext.setTypeface(Util.fontarray[fontlist[position]])


            if(fontlist[position]==parent.selectfont){
                holder.rb.isChecked=true

                holder.rb.setOnClickListener {  }
                holder.fonttext.setOnClickListener {  }


            }else{
                holder.rb.isChecked=false

                holder.rb.setOnClickListener {
                    parent.selectfont =fontlist[position]
                    parent.Update_rv()


                }
                holder.fonttext.setOnClickListener {
                    parent.selectfont =fontlist[position]
                    parent.Update_rv()

                }

            }


        }


        override fun getItemViewType(position: Int): Int {
            return 1
        }

        override fun getItemCount(): Int {
            return fontlist.size
        }

    }

}

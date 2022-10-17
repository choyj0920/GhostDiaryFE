package com.ghostdiary.ghostdiary.fragment.memo

import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ghostdiary.ghostdiary.*
import com.ghostdiary.ghostdiary.databinding.*
import com.ghostdiary.ghostdiary.dataclass.Memo
import com.ghostdiary.ghostdiary.dataclass.Memo_Folder
import com.ghostdiary.ghostdiary.utilpackage.Util
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class MemoSelectFragment(var parent:MemoActivity, var memoFolder: Memo_Folder) : Fragment() {

    private var binding: FragmentMemoSelectBinding?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentMemoSelectBinding.inflate(inflater,container,false)
        init()
        Util.setGlobalFont(binding!!.root)

        return binding!!.root
    }

    private fun init() {

        binding!!.tvName.text=memoFolder.folder_name

        binding!!.ivMemoplus.setOnClickListener {
            parent.containerChange(EditMemoFragment( parent,memoFolder.folder_id,null))
            binding!!.rvMemo.scrollToPosition(0)


        }

        update()

    }

    override fun onDestroyView() {
        binding=null
        super.onDestroyView()
    }


    fun update(isnew:Boolean=false) {
        memoFolder.arrMemo.sortBy { it.date }
        memoFolder.arrMemo.reverse()

        var state: Parcelable?=null
        try {
            state=binding!!.rvMemo.layoutManager!!.onSaveInstanceState()

        }catch (e:Exception){
            state=null
        }
        val memolistmanager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
        val memolistadapter = Memoadpater(memoFolder.arrMemo)

        binding!!.rvMemo.apply {
            layoutManager=memolistmanager
            adapter=memolistadapter
        }
        if(state !=null){
            binding!!.rvMemo.layoutManager!!.onRestoreInstanceState(state)
        }
        if(isnew){
            binding!!.rvMemo.smoothScrollToPosition(memoFolder.arrMemo.size-1)
        }
    }


    inner class Memoadpater(var memos:ArrayList<Memo>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {


        inner class Memoview(binding: ItemMemoBinding) : RecyclerView.ViewHolder(binding.root) {
            var memoTitle:TextView= binding.tvTitle
            var memodate:TextView= binding.tvDate
            var layoutmemo=binding.layoutmemo
        }



        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val view : View?

            view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_memo, parent, false)
            Util.setGlobalFont(view!!)

            return Memoview(ItemMemoBinding.bind(view))


        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

            var holder=holder as Memoview

            var memo =memos[position]

            holder.memoTitle.text=memo.title
            var formatdate = SimpleDateFormat("yy.MM.dd")
            var formatdatenonyear = SimpleDateFormat("MM.dd.")
            var curyear =Calendar.getInstance().time.year
            holder.memodate.text= if(curyear == memo.date.year) formatdatenonyear.format(memo.date) else formatdate.format(memo.date)

            holder.layoutmemo.setOnClickListener{
                parent.containerChange(EditMemoFragment(parent,memo.folder_id,memo))
            }




        }


        override fun getItemViewType(position: Int): Int {
            return 1
        }

        override fun getItemCount(): Int {
            return memos.size
        }

    }


}
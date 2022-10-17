package com.ghostdiary.ghostdiary;

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle;
import android.os.Parcelable
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.*
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.ghostdiary.ghostdiary.databinding.DialogMemoFolderAddBinding
import com.ghostdiary.ghostdiary.databinding.ItemGhostBinding
import com.ghostdiary.ghostdiary.dataclass.Day_Diary
import com.ghostdiary.ghostdiary.dataclass.Memo_Folder
import com.ghostdiary.ghostdiary.fragment.main.MemoFragment
import com.ghostdiary.ghostdiary.utilpackage.Util

class MemoFolderAddDialog(var memoparent:MemoFragment?=null,var memoFolderindex:Int=-1): DialogFragment() {

    // 뷰 바인딩 정의
    private var _binding: DialogMemoFolderAddBinding? = null
    private val binding get() = _binding!!

    private var selectghost:Int=-1



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogMemoFolderAddBinding.inflate(inflater, container, false)
        val view = binding.root

        // 레이아웃 배경을 투명하게 해줌, 필수 아님

        initview()


        Util.setGlobalFont(binding.root)

        return view
    }

    fun initview(){
        // 메모에서
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        if(memoFolderindex==-1){

            binding.tvTitle.text=resources.getString(R.string.folderaddtitle)

            binding.inputGhostname.hint=resources.getString(R.string.please_input_foldername1)

            binding.tvDelete.visibility=View.GONE

            binding.tvOk.setOnClickListener {
                if(selectghost==-1){
                    MainActivity.mainactivity.showmessage(resources.getString(R.string.please_select_foldericon))
                    return@setOnClickListener
                }
                var ghostname=binding.inputGhostname.text.toString()

                if(ghostname==""){
                    MainActivity.mainactivity.showmessage(resources.getString(R.string.please_input_foldername2))
                    return@setOnClickListener
                }
                var index=MainActivity.mainactivity.viewModel.getdb(null).insertMemo_folder(selectghost,ghostname)
                if(index==-1){
                    MainActivity.mainactivity.showmessage(resources.getString(R.string.failed_AddingFolder))
                }else{
                    memoparent!!.folderList.add(Memo_Folder(index,ghostname,selectghost))
                    memoparent!!.update()

                }
                dismiss()

            }

        }else{
            var memoFolder=memoparent!!.viewModel.getMemo_FolderArray()[memoFolderindex]

            binding.tvDelete.visibility=View.VISIBLE
            binding.tvTitle.text=resources.getString(R.string.folderedittitle)
            binding.inputGhostname.hint=memoFolder.folder_name
            binding.inputGhostname.setText(memoFolder.folder_name)
            selectghost=memoFolder.ghost_num

            binding.tvDelete.setOnClickListener{

                dismiss()
            }
            binding.tvOk.setOnClickListener {
                if(selectghost==-1){
                    MainActivity.mainactivity.showmessage(resources.getString(R.string.please_select_foldericon))
                    return@setOnClickListener
                }
                var ghostname=binding.inputGhostname.text.toString()

                if(ghostname==""){
                    MainActivity.mainactivity.showmessage(resources.getString(R.string.please_input_foldername2))
                    return@setOnClickListener
                }
                memoFolder.folder_name= binding.inputGhostname.text.toString()
                memoFolder.ghost_num=selectghost
                memoparent!!.viewModel.edit_memoFolder(memoFolder)
                memoparent!!.update()

                dismiss()
            }
            binding.tvDelete.setOnClickListener {
                memoparent!!.viewModel.delete_MemoFolder(fid = memoFolder.folder_id)
                memoparent!!.update()

                dismiss()
            }


        }
      




        binding.inputGhostname.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(10))
        binding.tvTextcount.text= "${binding.inputGhostname.text.toString().length}/10"

        binding.inputGhostname.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.tvTextcount.text= "${binding.inputGhostname.text.toString().length}/10"

            }
        })

        Update_rv()




    }
    fun Update_rv(){
        var state: Parcelable?=null
        try {
            state=binding.rvGhosts.layoutManager!!.onSaveInstanceState()

        }catch (e:Exception){
            state=null
        }

        val ghostarr= Day_Diary.addghost_arr()
        val ghostlistmanager = GridLayoutManager(context, 4,GridLayoutManager.HORIZONTAL,false)
        val ghostlistadpter = ghostadapter(this,ghostarr)


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

    class ghostadapter(var parent:MemoFolderAddDialog, var emotions:List<Int>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {


        class GhostView(binding: ItemGhostBinding) : RecyclerView.ViewHolder(binding.root) {
            var layout=binding.layoutGhostView
            var ghostimage: ImageView =binding.ivGhost
        }



        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val view : View?

            view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_ghost, parent, false)

            Util.setGlobalFont(view!!)

            return GhostView(ItemGhostBinding.bind(view))


        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

            var holder=holder as GhostView


            holder.ghostimage.alpha=1f

            var isnum=emotions[position]
            if(isnum==-1){
                holder.layout.visibility=View.INVISIBLE
                return

            }else{
                holder.layout.visibility=View.VISIBLE
            }

            holder.ghostimage.setImageResource(Day_Diary.int_to_image.get(emotions[position]))
            if(emotions[position]==parent.selectghost){
                holder.layout.background=ContextCompat.getDrawable(MainActivity.mainactivity,R.drawable.circle_backgroud)
            }else{
                holder.layout.background=ContextCompat.getDrawable(MainActivity.mainactivity,R.drawable.circle_backgroud_white)
            }
            holder.ghostimage.setOnClickListener {
                if(isnum==parent.selectghost){

                }else{
                    parent.selectghost=isnum

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

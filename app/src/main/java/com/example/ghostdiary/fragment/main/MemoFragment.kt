package com.example.ghostdiary.fragment.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ghostdiary.GhostsSelectDialog
import com.example.ghostdiary.MainViewModel
import com.example.ghostdiary.R
import com.example.ghostdiary.databinding.*
import com.example.ghostdiary.dataclass.Day_Diary
import com.example.ghostdiary.dataclass.Memo_Folder


class MemoFragment : Fragment() {

    private val viewModel: MainViewModel by activityViewModels()
    private var binding: FragmentMemoBinding?=null
    lateinit var folderList:ArrayList<Memo_Folder>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentMemoBinding.inflate(inflater,container,false)
        folderList=viewModel.getMemo_FolderArray()

        init()

        return binding!!.root
    }

    private fun init() {

        binding!!.ivFolderadd.setOnClickListener {
                val dialog = GhostsSelectDialog(-1,adpaterparent = null , emotionlist = null,this )
            dialog.isCancelable = true
            dialog.show(requireActivity().supportFragmentManager, "ConfirmDialog")
        }

        update()


    }

    override fun onDestroyView() {
        binding=null
        super.onDestroyView()
    }


    fun update() {
        val ghostlistmanager = GridLayoutManager(context, 3, GridLayoutManager.VERTICAL,false)
        val ghostlistadpter = Folderadpater(folderList)


        binding!!.rvFolder.apply {
            layoutManager=ghostlistmanager
            adapter=ghostlistadpter
        }
    }



    class Folderadpater(var folders:ArrayList<Memo_Folder>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {


        class GhostView(binding: ItemFolderBinding) : RecyclerView.ViewHolder(binding.root) {
            var foldername:TextView= binding.tvName
            var ghostimage: ImageView =binding.ivGhost
        }



        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val view : View?

            view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_folder, parent, false)
            return GhostView(ItemFolderBinding.bind(view))


        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

            var holder=holder as GhostView


            holder.ghostimage.alpha=1f
            holder.ghostimage.setImageResource(Day_Diary.int_to_image.get(folders[position].ghost_num))

            holder.foldername.text=folders[position].folder_name




        }

        override fun getItemViewType(position: Int): Int {
            return 1
        }

        override fun getItemCount(): Int {
            return folders.size
        }

    }


}
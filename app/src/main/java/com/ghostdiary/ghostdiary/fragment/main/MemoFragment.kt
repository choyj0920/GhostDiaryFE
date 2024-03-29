package com.ghostdiary.ghostdiary.fragment.main

import android.content.Intent
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
import com.ghostdiary.ghostdiary.*
import com.ghostdiary.ghostdiary.databinding.*
import com.ghostdiary.ghostdiary.dataclass.Day_Diary
import com.ghostdiary.ghostdiary.dataclass.Memo_Folder
import com.ghostdiary.ghostdiary.utilpackage.Util


class MemoFragment : Fragment() {

    val viewModel: MainViewModel by activityViewModels()
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


        init()
        Util.setGlobalFont(binding!!.root)

        return binding!!.root
    }

    override fun onStart() {
        super.onStart()
        init()
        Util.setGlobalFont(binding!!.root)
    }

    private fun init() {


        update()


    }

    override fun onDestroyView() {
        binding=null
        super.onDestroyView()
    }


    fun update() {
        folderList=viewModel.getMemo_FolderArray()

        val ghostlistmanager = GridLayoutManager(context, 3, GridLayoutManager.VERTICAL,false)
        val ghostlistadpter = Folderadpater(folderList)


        binding!!.rvFolder.apply {
            layoutManager=ghostlistmanager
            adapter=ghostlistadpter
        }
    }



    inner class Folderadpater(var folders:ArrayList<Memo_Folder>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {


        inner class GhostView(binding: ItemFolderBinding) : RecyclerView.ViewHolder(binding.root) {
            var foldername:TextView= binding.tvName
            var ghostimage: ImageView =binding.ivGhost
            var layout=binding.linearLayout
        }



        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val view : View?

            view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_folder, parent, false)
            Util.setGlobalFont(view!!)

            return GhostView(ItemFolderBinding.bind(view))


        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

            var holder=holder as GhostView


            holder.ghostimage.alpha=1f
            holder.ghostimage.setImageResource(Day_Diary.int_to_image.get(folders[position].ghost_num))

            holder.layout.setOnClickListener {
                val intent = Intent(requireContext(), MemoActivity::class.java)
                intent.putExtra("memofoderindex",position)
               requireActivity().startActivity(intent)

            }
            holder.layout.setOnLongClickListener{

                val dialog = MemoFolderAddDialog(this@MemoFragment,position )
                dialog.isCancelable = true
                dialog.show(activity!!.supportFragmentManager, "ConfirmDialog")

                true
            }

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
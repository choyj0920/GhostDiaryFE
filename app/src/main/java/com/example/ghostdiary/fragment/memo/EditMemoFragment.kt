package com.example.ghostdiary.fragment.memo


import android.content.Context
import android.os.Bundle
import android.util.TypedValue
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.PopupWindow
import androidx.fragment.app.Fragment
import com.example.ghostdiary.*
import com.example.ghostdiary.databinding.FragmentEditMemoBinding
import com.example.ghostdiary.databinding.MenuSideoptionBinding
import com.example.ghostdiary.dataclass.Memo
import com.example.ghostdiary.utilpackage.Util
import java.text.SimpleDateFormat
import java.util.*


class EditMemoFragment(var parent: MemoActivity, var folder_id:Int, var memo: Memo?) : Fragment() {

    lateinit var curMemo: Memo
    var iseditmode: Boolean=false
    private var binding:FragmentEditMemoBinding?=null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding=FragmentEditMemoBinding.inflate(inflater,container,false)





        init()
        Util.setGlobalFont(binding!!.root)

        return binding!!.root

    }
    fun init(){
        if(memo==null){
            curMemo=Memo(folder_id,"","",Calendar.getInstance().time, memoid = -1)
            iseditmode=true
        }else{
            curMemo=Memo(folder_id,memo!!.title,memo!!.text,memo!!.date, memo!!.memoid )
        }
        var formatdate = SimpleDateFormat("yyyy.MM.dd.")
        binding!!.tvDay.text=formatdate.format(curMemo.date)
        binding!!.tvTitle.text=curMemo.title
        binding!!.tvText.text=curMemo.text



        switcheditmode(iseditmode)

        initPopupMenu()


        binding!!.btnClock.setOnClickListener {
            timestamp()
        }
        binding!!.ivPostmemo.setOnClickListener {
            addMemo()

        }
        binding!!.ivEditClose.setOnClickListener {
            parent.reversechange(this)
        }
        binding!!.ivShowClose.setOnClickListener{
            parent.reversechange(this)

        }





    }

    private fun initPopupMenu() {

        val popupInflater =
            requireActivity().applicationContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupBind = MenuSideoptionBinding.inflate(popupInflater)


        val popupWindow = PopupWindow(
            popupBind.root,dpToPx(requireContext(),75f).toInt() ,dpToPx(requireContext(),80f).toInt()  , true
        ).apply { contentView.setOnClickListener { dismiss() }
            popupBind.btn1.setOnClickListener {
                switcheditmode(true)
                dismiss()

            }
            popupBind.btn2.setOnClickListener {
                parent.viewModel.delMemo(folder_id,curMemo.memoid)
                parent.reversechange(this@EditMemoFragment)
                dismiss()

            }

        }
        Util.setGlobalFont(popupBind.root)
        // make sure you use number than wrap_content or match_parent,
        // because for me it is not showing anything if I set it to wrap_content from ConstraintLayout.LayoutParams.


        binding!!.btnSidemenu.setOnClickListener{
            var loc:IntArray= intArrayOf(0,0)
            binding!!.btnSidemenu.getLocationOnScreen(loc)
            popupWindow.showAtLocation(binding!!.btnSidemenu, Gravity.NO_GRAVITY, loc[0]-popupWindow.width/2, loc[1]+binding!!.btnSidemenu.height);
        }



        /*

        // popoup menu 에 적용할 style
        val contextThemeWrapper =
            ContextThemeWrapper(requireContext(), R.style.PopupMenuStyle)
        // popup menu 가 보일 위치
        val popupBase = binding!!.btnSidemenu

        // popup menu 선언
        val popupMenu = PopupMenu(contextThemeWrapper, popupBase, Gravity.BOTTOM)
        popupMenu.menuInflater.inflate(R.menu.diary_sidemenu, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { m ->
            when (m.itemId) {
                R.id.menu_edit -> {
                }
                R.id.menu_delete -> {
                    parent.viewModel.delMemo(folder_id,curMemo.memoid)
                    parent.reversechange(this)

                }
            }
            false
        }

        // Icon 보여주기
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            popupMenu.setForceShowIcon(true)
        } else {
            try {
                val fields = popupMenu.javaClass.declaredFields
                for (field in fields) {
                    if ("mPopup" == field.name) {
                        field.isAccessible = true
                        val menuPopupHelper = field[popupMenu]
                        val classPopupHelper = Class.forName(menuPopupHelper.javaClass.name)
                        val setForceIcon: Method = classPopupHelper.getMethod(
                            "setForceShowIcon",
                            Boolean::class.javaPrimitiveType
                        )
                        setForceIcon.invoke(menuPopupHelper, true)
                        break
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        popupMenu.show()

  */


    }


    fun hideKeyboard() {
        val imm = parent.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        imm.hideSoftInputFromWindow(parent.currentFocus?.windowToken, 0)
        imm.hideSoftInputFromWindow(binding!!.inputTitle.windowToken,0)
        imm.hideSoftInputFromWindow(binding!!.inputText.windowToken,0)

    }
    fun switcheditmode(editmode:Boolean){
        if(editmode){
            iseditmode=true
            binding!!.editlayout.visibility=View.VISIBLE
            binding!!.showlayout.visibility=View.GONE

            binding!!.inputText.setText(curMemo.text.toString())
            binding!!.inputTitle.setText(curMemo.title.toString())

            binding!!.inputTitle.requestFocus()



        }else{
            iseditmode=false
            binding!!.editlayout.visibility=View.GONE
            binding!!.showlayout.visibility=View.VISIBLE

            var formatdate = SimpleDateFormat("yyyy.MM.dd")
            binding!!.tvDay.text=formatdate.format(curMemo.date)
            binding!!.tvTitle.text=curMemo.title
            binding!!.tvText.text=curMemo.text

            hideKeyboard()

        }

    }

    fun addMemo(){
        curMemo.title=binding!!.inputTitle.text.toString()
        curMemo.text=binding!!.inputText.text.toString()
        if(curMemo.title==""){
            parent.showmessage("제목을 입력해주세요.")
            return
        }

        if(curMemo.text==""){
            parent.showmessage("메모를 입력해주세요.")
            return
        }

        curMemo.date=Calendar.getInstance().time

        if(memo!=null){
            memo!!.text= curMemo.text
            memo!!.title=curMemo.title
            memo!!.date=curMemo.date

            parent.viewModel.editmemo(memo!!)

        }else{
            parent.viewModel.editmemo(curMemo)

        }

        switcheditmode(false)



        try {


        }catch (e:Exception){

        }



    }


    override fun onDestroyView() {
        binding=null
        super.onDestroyView()
    }

    fun dpToPx(context: Context, dp: Float): Float {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.resources.displayMetrics)
    }


    fun timestamp(){
        var editText:EditText?=null
        if(binding!!.inputTitle.isFocused){
            editText=binding!!.inputTitle
        }else if(binding!!.inputText.isFocused){
            editText=binding!!.inputText
        }else{
            return
        }

        var transFormat = SimpleDateFormat(" hh:mm ")
        var calendar= Calendar.getInstance()
        var text = transFormat.format(calendar.time)


        val fullTxt: String = editText.getText().toString()

        // 커서의 현재 위치
        // 커서의 현재 위치
        val pos: Int = editText.getSelectionStart()

        // 에디트에 텍스트가 하나도 없는 경우

        // 에디트에 텍스트가 하나도 없는 경우
        if (fullTxt.isEmpty()) {

            editText.setText(text)

            // 커서가 눈에 보이게 한다.
            editText.requestFocus()
        } else editText.setText(
            fullTxt.substring(
                0,
                pos
            ) + text.toString() + fullTxt.substring(pos)
        )
        // 편집이 편하도록 커서를 삽입한 텍스트 끝에 위치시킨다.

        // 편집이 편하도록 커서를 삽입한 텍스트 끝에 위치시킨다.
        editText.setSelection(pos + text.length)
    }


}
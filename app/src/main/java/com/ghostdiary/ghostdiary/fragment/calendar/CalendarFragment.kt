package com.ghostdiary.ghostdiary.fragment.calendar

import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.NonNull
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.ghostdiary.ghostdiary.*
import com.ghostdiary.ghostdiary.adapter.AdapterMonth
import com.ghostdiary.ghostdiary.adapter.EmotionSpinnerAdapter
import com.ghostdiary.ghostdiary.databinding.FragmentCalendarBinding
import com.ghostdiary.ghostdiary.fragment.postdiary.SelectEmotionFragment
import com.ghostdiary.ghostdiary.utilpackage.Util

import java.io.File
import java.util.*


class CalendarFragment : Fragment() {
    companion object {
        var curCalendar: CalendarFragment?=null
    }
    val viewModel: MainViewModel by activityViewModels()
    var pageIndex = 0
    var curCal=Calendar.getInstance()
    lateinit var mContext: Context
    private var binding: FragmentCalendarBinding? =null
    var emotionpostion:Int=-1
    val snap = PagerSnapHelper()
    var lastcalendar:Calendar=Calendar.getInstance()
    var curpo:Int=0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        binding=FragmentCalendarBinding.inflate(inflater,container,false)

        initView()
        curCalendar =this

        Util.setGlobalFont(binding!!.root)

        snap.attachToRecyclerView(binding!!.rvMonth)
        return binding!!.root
    }

    override fun onStart() {
        super.onStart()
        init_rv()
    }




    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MainActivity) {
            mContext = context
        }
    }

    fun init_rv() {

        when (MainActivity.curTheme) {
            1 -> {
                binding!!.rvMonth?.setBackgroundColor(Color.parseColor("#FFFFFF"))

            }
            2 -> {
                binding!!.rvMonth?.setBackgroundColor(Color.parseColor("#000000"))


            }
            3 -> {
                binding!!.rvMonth?.setBackgroundColor(Color.parseColor("#000000"))

            }
        }



        val onScrollListener = object:RecyclerView.OnScrollListener() {
            override fun onScrolled(@NonNull recyclerView:RecyclerView, dx:Int, dy:Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (recyclerView.layoutManager != null) {
                    val view = snap.findSnapView(recyclerView.layoutManager)!!
                    val position = recyclerView.layoutManager!!.getPosition(view)
                    if (curpo != position) {
                        curpo = position
                        var center=Int.MAX_VALUE / 2
                        var newcal=Calendar.getInstance()
                        newcal.time=lastcalendar.time
                        newcal.add(Calendar.MONTH, curpo - center)
                        viewModel.calendar =newcal
                        curCal.time=newcal.time
                        if(curpo-center >10 ||curpo-center<-10){
                            showmonthpicker()
                            return
                        }

                    }
                }
            }
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

            }
        }
        binding!!.rvMonth.let {
            it.clearOnScrollListeners()
            it.addOnScrollListener(onScrollListener)
        }
        lastcalendar.time=viewModel.calendar.time
        curCal.time=lastcalendar.time

        val monthListManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        val monthListAdapter = AdapterMonth(this,viewModel)

        binding!!.rvMonth.apply {
            layoutManager = monthListManager
            adapter = monthListAdapter
            scrollToPosition(Int.MAX_VALUE/2)
        }



    }

    fun ScreenShotActivity() {

        try {
            val view = (snap.findSnapView(binding!!.rvMonth.layoutManager)!! as ViewGroup)

            val viewsize= (snap.findSnapView(binding!!.rvMonth.layoutManager)!! as ViewGroup).getChildAt(1)


            //Imageview share = findViewById(R.id.share)
            //Imageview share = findViewById(R.id.share)
            view.layout(0, 0, viewsize.getMeasuredWidth(), viewsize.getMeasuredHeight())
            view.buildDrawingCache()
            val bitmap = Bitmap.createBitmap(
                viewsize.getMeasuredWidth(), viewsize.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(bitmap)
            view.draw(canvas)
            val fileName = getString(R.string.app_name) + System.currentTimeMillis() + ".jpeg"

            //외부 저장소에 이미지 파일 저장하기
            val file: File? = Util.saveImageIntoFileFromUri(
                requireContext(),
                bitmap,
                fileName,
                Util.getExternalFilePath(requireContext())
            )

            //다른 앱에 이미지 공유하기
            val shareIntent = Intent()

            val photoURI = FileProvider.getUriForFile(
                requireContext(),
                requireContext().getPackageName(),
                file!!
            )

            shareIntent.action = Intent.ACTION_SEND

            shareIntent.putExtra(Intent.EXTRA_STREAM, photoURI)
            shareIntent.type = "image/jpg"
            shareIntent.clipData= ClipData.newRawUri("abc",photoURI)
            shareIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);


            startActivity(Intent.createChooser(shareIntent, "공유"))


        }catch(e: Exception){

            MainActivity.mainactivity.showmessage("Error... Please try again in a momentarily")
        }



    }

    fun init_spinner(){
        val array= arrayListOf<Int>(-1,0,1,2,3,4)
        val adapter=EmotionSpinnerAdapter(MainActivity.mainactivity,array)


        binding!!.spinnerEmotion.adapter=adapter



        binding!!.spinnerEmotion.onItemSelectedListener= object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                emotionpostion=array.get(position)

                init_rv()

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

        }

    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // TODO: Use the ViewModel

    }

    override fun onDestroyView() {
        binding=null
        super.onDestroyView()
    }
    fun update_rv(){
        viewModel.calendar.set(Calendar.YEAR,curCal.get(Calendar.YEAR))
        viewModel.calendar.set(Calendar.MONTH,curCal.get(Calendar.MONTH))
        init_rv()
    }

    fun showmonthpicker(){
        //  날짜 dialog
        viewModel.calendar.set(Calendar.YEAR,curCal.get(Calendar.YEAR))
        viewModel.calendar.set(Calendar.MONTH,curCal.get(Calendar.MONTH))
//        init_rv()


        val dialog = MonthSelectDialog(this,viewModel.calendar)
        dialog.isCancelable = true

        dialog.show(requireActivity().supportFragmentManager, "ConfirmDialog")

    }

    fun initView() {


        init_spinner()
        init_rv()
        binding!!.ivPost.setOnClickListener{
            val intent = Intent(requireContext(), PostDiaryActivity::class.java)
            intent.putExtra("date",Calendar.getInstance().time.time)
            intent.putExtra("firsttext",false)
            startActivity(intent)
        }
        binding!!.ivAnalyze.setOnClickListener {
            val intent = Intent(requireContext(), AnalyzeActivity::class.java)
            startActivity(intent)
        }


    }


    fun start_post(day:Date){
        var intent = Intent(getActivity(), SelectEmotionFragment::class.java)
        intent.putExtra("Date",day.time)
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        startActivity(intent)
    }
    fun show_post(day:Date){

    }

    fun down_post(){

    }
    fun selectimage(index:Int): Int {
        when(index){
            0 -> return R.drawable.ghost_00_verygood
            1 -> return R.drawable.ghost_01_good
            2 -> return R.drawable.ghost_02_normal
            3 -> return R.drawable.ghost_03_bad
            4 -> return R.drawable.ghost_04_verybad

        }
        return R.drawable.ic_blankghost
    }

}
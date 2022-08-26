package com.example.ghostdiary.fragment.main


import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ghostdiary.MainActivity
import com.example.ghostdiary.MainViewModel
import com.example.ghostdiary.R
import com.example.ghostdiary.Util
import com.example.ghostdiary.adapter.AdapterEmotionjustview
import com.example.ghostdiary.databinding.FragmentEditDiaryBinding
import com.example.ghostdiary.dataclass.Day_Diary
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class EditDiaryFragment(var parent:Fragment,var date: Date,var diary: Day_Diary?=null,var isEditmode:Boolean = true) : Fragment() {

    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var activityResultLauncher:ActivityResultLauncher<Intent>

    var sleepstart:Int =-1
    var sleepend:Int =-1
    lateinit var curDiary:Day_Diary
    lateinit var fileName:String
    lateinit var curbitmap:Bitmap

    private var binding:FragmentEditDiaryBinding?=null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding=FragmentEditDiaryBinding.inflate(inflater,container,false)

        init()
        Util.setGlobalFont(binding!!.root)

        return binding!!.root

    }
    fun init(){
        
        binding!!.inputText.requestFocus()
        switcheditmode(isEditmode)
        var formatDate = SimpleDateFormat("yyyy-MM-dd")
        var strdate = formatDate.format(date)
        // curDiary 초기화

        if(diary!=null){
            curDiary=diary!!
            date=curDiary.date

        }else if (viewModel.getEmotionArray(requireContext()).contains(strdate)) {
            curDiary = viewModel.getEmotionArray(null)[strdate]!!
        }else{
            curDiary= Day_Diary(date)

        }
        binding!!.inputText.setText(curDiary.text)

        var transFormat = SimpleDateFormat("yyyy/MM/dd")
        var to = transFormat.format(date)
        binding!!.tvDate.text=to

        // 취소 버튼
        binding!!.btnCancel.setOnClickListener {
            if(parent is RecordFragment){
                MainActivity.mainactivity.change_record()

            }else if (parent is CalendarFragment){
                MainActivity.mainactivity.change_record()

            }else if(parent is SelectEmotionFragment){
                curDiary.image=null
                MainActivity.mainactivity.change_to_selectemotion(date,curDiary)

            }
        }

        binding!!.btnPost.setOnClickListener{

            curDiary.text=binding!!.inputText.text.toString()

            if(binding!!.layoutImage.visibility==View.VISIBLE){
                saveBitmapToJpeg(curbitmap)



            }


            if(parent is CalendarFragment || parent is SelectEmotionFragment){
                addDiary(curDiary)
                MainActivity.mainactivity.change_calendar()

            }
            else if(parent is RecordFragment){
                addDiary(curDiary)
                MainActivity.mainactivity.change_record()


            }

        }

        //타임스탬프 기능
        binding!!.btnClock.setOnClickListener {
            timestamp()

        }

        // 이미지 가져오기
        binding!!.btnImage.setOnClickListener {
            startDefaultGalleryApp()
        }
        // 이미지 가져오기 -액티비티 정의부
        activityResultLauncher= registerForActivityResult(ActivityResultContracts.StartActivityForResult())
        {//Result 매개변수 콜백 메서드
            //ActivityResultLauncher<T>에서 T를 intent로 설정했으므로
            //intent자료형을 Result 매개변수(콜백)를 통해 받아온다
            //엑티비티에서 데이터를 갖고왔을 때만 실행
            if (it.resultCode == RESULT_OK) {
                //SubActivity에서 갖고온 Intent(It)
                val extras = it.data!!.extras
                val uri=it.data!!.data
                try{
                    uri?.let {
                        var bitmap:Bitmap? = null
                        if(Build.VERSION.SDK_INT < 28) {
                            bitmap = MediaStore.Images.Media.getBitmap(
                                requireActivity().contentResolver,
                                uri
                            )
                            binding!!.ivImage?.setImageBitmap(bitmap)
                        } else {
                            val source = ImageDecoder.createSource(requireActivity().contentResolver, uri)
                            bitmap = ImageDecoder.decodeBitmap(source)
                            binding!!.ivImage?.setImageBitmap(bitmap)
                        }

                        if (bitmap != null) {
                            curbitmap=bitmap
                        }

                    }

                    binding!!.layoutImage.visibility=View.VISIBLE



                    binding!!.btnDelimage.setOnClickListener {
                        binding!!.layoutImage.visibility=View.GONE

                    }

                }catch (e:Exception){
                    curDiary.image=null
//                        binding!!.layoutImage.visibility=View.GONE
                    Log.e("TAG","${e.toString()}")

                }
            }
        }

        // 이미지 뷰 등록
        if(curDiary.image!="" && curDiary.image != null){

            var transFormat = SimpleDateFormat("yyyy_MM_dd")
            var to = transFormat.format(date)
            //내부저장소 캐시 경로를 받아옵니다.
            val storage: File = requireActivity().cacheDir
            //저장할 파일 이름

            val fileName = curDiary.image
            //storage 에 파일 인스턴스를 생성합니다.
            val tempFile = File(storage, fileName).path
            val bitmap = BitmapFactory.decodeFile(tempFile)
            if (bitmap != null) {
                binding!!.ivImage.setImageBitmap(bitmap)

                binding!!.layoutImage.visibility=View.VISIBLE

                binding!!.btnDelimage.setOnClickListener {
                    curDiary.image=null
                    binding!!.layoutImage.visibility=View.GONE

                }
            }
            else {
                binding!!.layoutImage.visibility=View.GONE
            }
        }

        updateGhostview()

    }

    fun switcheditmode(isedit:Boolean){
        if(isedit){
            isEditmode=true
            binding!!.inputText.focusable=View.FOCUSABLE
            binding!!.constraintLayout4.visibility=View.VISIBLE
            binding!!.btnPost.visibility=View.VISIBLE
            binding!!.btnDelimage.visibility=View.VISIBLE
            binding!!.inputText.hint="글 작성란"




        }else{
            isEditmode=true
            binding!!.inputText.focusable=View.NOT_FOCUSABLE
            binding!!.constraintLayout4.visibility=View.GONE
            binding!!.btnPost.visibility=View.GONE
            binding!!.btnDelimage.visibility=View.GONE
            binding!!.inputText.hint=""

        }
    }

    fun addDiary(newDiary:Day_Diary){
        viewModel.addDiary(newDiary)
        update()

    }

    fun updateGhostview(){

        var _adapter = AdapterEmotionjustview(this,curDiary.getEmotionarrElement())

        binding!!.rvEmotionlist.apply {
            layoutManager= LinearLayoutManager(this.context,LinearLayoutManager.HORIZONTAL,false)
            adapter=_adapter
        }

    }



    fun timestamp(){
        var editText=  binding!!.inputText
        var transFormat = SimpleDateFormat("hh:mm")
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


    override fun onDestroyView() {
        binding=null
        super.onDestroyView()
    }

    fun update(){



    }

    val REQUEST_GET_IMAGE = 105
    private fun startDefaultGalleryApp() {
        Log.d("TAG","갤러리 열기 함수 실행")

        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.setType("image/*")

        activityResultLauncher.launch(intent)

    }


    private fun saveBitmapToJpeg(bitmap: Bitmap) {
        var transFormat = SimpleDateFormat("yyyy_MM_dd")
        var to = transFormat.format(date)
        //내부저장소 캐시 경로를 받아옵니다.
        val storage: File = requireActivity().cacheDir

        //저장할 파일 이름

        fileName = "$to.jpg"

        //storage 에 파일 인스턴스를 생성합니다.
        val tempFile = File(storage, fileName)
        try {
            // 자동으로 빈 파일을 생성합니다.
            tempFile.createNewFile()

            // 파일을 쓸 수 있는 스트림을 준비합니다.
            val out = FileOutputStream(tempFile)

            // compress 함수를 사용해 스트림에 비트맵을 저장합니다.
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
            // 스트림 사용후 닫아줍니다.

            out.close()

            curDiary.image=fileName


        } catch (e: FileNotFoundException) {
            Log.e("MyTag", "FileNotFoundException : " )
        } catch (e: IOException) {
            Log.e("MyTag", "IOException : ")
        }
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

    fun postDiary(calendarFragment: CalendarFragment) {

        calendarFragment.addDiary(curDiary)



    }


}
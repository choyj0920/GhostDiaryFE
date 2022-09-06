package com.example.ghostdiary.fragment.analyze

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.ghostdiary.*
import com.example.ghostdiary.databinding.FragmentAnalysisDetailBinding
import com.example.ghostdiary.dataclass.Day_Diary
import com.example.ghostdiary.dataclass.emotion_analysis
import com.example.ghostdiary.utilpackage.MyYAxisLeftRenderer
import com.example.ghostdiary.utilpackage.Util
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.min


class AnalysisDetailFragment(private val viewModel:MainViewModel) : Fragment() {

    private var binding: FragmentAnalysisDetailBinding?=null

    lateinit var analysisMap :HashMap<String,emotion_analysis>
    lateinit var analysisList:List<emotion_analysis>
    var allscore:Int=-1
    var selectemotion :emotion_analysis?=null

    lateinit var chart: LineChart
    private var chartData = ArrayList<Entry>()  // 데이터 배열
    lateinit private var lineDataSet :LineDataSet  // 데이터 배열 → 데이터 셋
    private var lineData: LineData = LineData()
    var xlabelindex:ArrayList<String> = arrayListOf()


    lateinit var allpercen:ArrayList<Int>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentAnalysisDetailBinding.inflate(inflater,container,false)

        init()
        Util.setGlobalFont(binding!!.root)

        return binding!!.root
    }

    private fun init() {
        analysisMap=viewModel.getdiaryAnalysisMap()
        chart=binding!!.chart
        analysisList= analysisMap.values.toList()
        if(analysisList.size<5 ){
            binding!!.scrollView.visibility=View.GONE
            binding!!.tvWronganalyze.visibility=View.VISIBLE

            return
        }




        binding!!.ivSelectAnalyze.setOnClickListener {
            val dialog = AnalyzeSelectDialog(this )
            dialog.isCancelable = true
            dialog.show(requireActivity().supportFragmentManager, "ConfirmDialog")
        }

        analysisList=analysisList.sortedBy { -it.mypercentage!!.slice(0..1).sum() }

        selectemotionUpdate()


    }

    fun selectemotionUpdate(){
        if(selectemotion==null){
            binding!!.laytoutDetail.visibility=View.GONE
            binding!!.layoutScore.visibility=View.GONE

            binding!!.tvNotselect.visibility=View.VISIBLE
            return

        }
        binding!!.laytoutDetail.visibility=View.VISIBLE
        binding!!.layoutScore.visibility=View.VISIBLE
        binding!!.tvNotselect.visibility=View.GONE
        binding!!.ivSelectAnalyze.setImageResource(Day_Diary.int_to_image[selectemotion!!.ghostnum])



        var selectscore=selectemotion!!.getmyscore()
        var selectpercen=selectemotion!!.getmypercentage()


        binding!!.tvScore.text=selectscore.toString()

        binding!!.tvVerygoodPer.text="${selectpercen[0]} %"
        binding!!.tvGoodPer.text="${selectpercen[1]} %"
        binding!!.tvNormalPer.text="${selectpercen[2]} %"
        binding!!.tvBadPer.text="${selectpercen[3]} %"
        binding!!.tvVerybadPer.text="${selectpercen[4]} %"

        initChartData()

        initChart()

    }


    private fun initChartData() {
        // 더미데이터
        var detailmap =viewModel.getdb(requireContext()).select_diaryanalysis_detail(selectemotion!!.text).data
        var array =detailmap.keys.toList().sorted()



        xlabelindex = arrayListOf()
        xlabelindex.add("")
        var index=0
        var formatdate = SimpleDateFormat("yy.MM.dd")
        var formatdatenonyear = SimpleDateFormat("MM.dd.")

        var curyear=Calendar.getInstance().time.year
        chartData= arrayListOf()

        for(i in array){
            index+=1


            chartData.add(Entry(index.toFloat(), (4- detailmap[i]!!).toFloat()) )
            xlabelindex.add(if(i.year==curyear) formatdatenonyear.format(i) else formatdate.format(i))
        }
        xlabelindex.add("")


        lineDataSet = LineDataSet(chartData, "set1")

        lineDataSet.apply {
            color=resources.getColor(R.color.pink)
            lineWidth=2f
            setDrawCircles(false)
            setDrawValues(false)
            fillColor=resources.getColor(R.color.pink)

        }








    }
    // 차트 초기화 메서드
    private fun initChart() {
        chart.run {
            setDrawGridBackground(false)
            setBackgroundColor(resources.getColor(R.color.backf5))
            legend.isEnabled = false
            isHighlightPerTapEnabled=false


            legend.setDrawInside(true)
        }
        chart.setHighlightPerDragEnabled(false);


        chart.requestDisallowInterceptTouchEvent(true)


        val xAxis = chart.xAxis

        xAxis.setDrawGridLines(false) // disable x axis grid lines

        xAxis.axisMaximum = chartData.size.toFloat()+0.1f
        xAxis.axisMinimum = min(0.9f,xAxis.axisMaximum-5f)

        xAxis.labelCount = 5
//        xAxis.valueFormatter = TimeAxisValueFormat()
        xAxis.valueFormatter = IndexAxisValueFormatter(xlabelindex)
        xAxis.textSize=8f
        xAxis.granularity=1f
        xAxis.position= XAxis.XAxisPosition.BOTTOM_INSIDE

        chart.setVisibleXRangeMinimum(5f)
//        chart.setVisibleXRangeMinimum(6f)

        chart.moveViewToX(10f)




        xAxis.textColor = Color.BLACK
        xAxis.position = XAxis.XAxisPosition.BOTTOM  // x축 라벨 위치
        xAxis.setDrawLabels(true)  // Grid-line 표시
        xAxis.setDrawAxisLine(true)  // Axis-Line 표시

        // 왼쪽 y축 값
        val yLAxis = chart.axisLeft
        yLAxis.axisMaximum = 4f   // y축 최대값(고정)
        yLAxis.axisMinimum = 0f  // y축 최소값(고정)
        yLAxis.setDrawGridLines(true)
        yLAxis.setDrawAxisLine(false)




        // 왼쪽 y축 도메인 변경

        yLAxis.granularity = 1f

        // 오른쪽 y축 값
        val yRAxis = chart.axisRight
        yRAxis.setDrawLabels(false)
        yRAxis.setDrawAxisLine(false)
        yRAxis.setDrawGridLines(false)
        yRAxis.isEnabled=false


        // 마커 설정
//        val marker = LineMarkerView(requireContext(), R.layout.graph_marker)
//        marker.chartView = chart
//        chart.marker = marker
        chart.setVisibleYRange(5f,5f, YAxis.AxisDependency.LEFT)

        chart!!.description.isEnabled = false  // 설명

        lineData=LineData()
        chart.data=lineData
        lineData.addDataSet(lineDataSet)


        var imagelist:ArrayList<Bitmap> = arrayListOf()

        imagelist.add(drawableToBitmap(resources.getDrawable(R.drawable.ic_graph_04))!!)
        imagelist.add(drawableToBitmap(resources.getDrawable(R.drawable.ic_graph_03))!!)
        imagelist.add(drawableToBitmap(resources.getDrawable(R.drawable.ic_graph_02))!!)
        imagelist.add(drawableToBitmap(resources.getDrawable(R.drawable.ic_graph_01))!!)
        imagelist.add(drawableToBitmap(resources.getDrawable(R.drawable.ic_graph_00))!!)

        Log.d("TAG","여기에서는 ? ${imagelist} ${imagelist[2]}")

        chart.rendererLeftYAxis =
            MyYAxisLeftRenderer(
                chart.viewPortHandler,
                chart.axisLeft, chart.getTransformer(YAxis.AxisDependency.LEFT), imagelist
            )

        chart!!.invalidate()  // 다시 그리기


    }
    fun drawableToBitmap(drawable: Drawable): Bitmap? {
        if (drawable is BitmapDrawable) {
            return drawable.bitmap
        }
        val bitmap =
            Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight())
        drawable.draw(canvas)
        return bitmap
    }

    override fun onDestroyView() {
        binding=null
        super.onDestroyView()
    }


}

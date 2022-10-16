package com.ghostdiary.ghostdiary.fragment.analyze

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
import com.ghostdiary.ghostdiary.*
import com.ghostdiary.ghostdiary.databinding.FragmentAnalysisBinding
import com.ghostdiary.ghostdiary.dataclass.Day_Diary
import com.ghostdiary.ghostdiary.dataclass.emotion_analysis
import com.ghostdiary.ghostdiary.utilpackage.MyYAxisLeftRenderer
import com.ghostdiary.ghostdiary.utilpackage.Util
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


class AnalysisFragment(private val viewModel:MainViewModel) : Fragment() {

    private var binding: FragmentAnalysisBinding?=null

    lateinit var analysisMap :HashMap<String,emotion_analysis>
    lateinit var analysisList:List<emotion_analysis>
    var allscore:Int=-1

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
        binding= FragmentAnalysisBinding.inflate(inflater,container,false)

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
        allscore=emotion_analysis.getallscore()
        allpercen=emotion_analysis.getallpercentage()


        initChartData()

        initChart()




        analysisList=analysisList.sortedBy { -it.mypercentage!!.slice(0..1).sum() }



        if(analysisList[0].mypercentage!!.slice(0..1).sum()>0){
            binding!!.tvVeryhappy.text=analysisList[0].text

            binding!!.ivGood1.setImageResource(Day_Diary.int_to_image.get(analysisList[0].ghostnum))
            binding!!.tvGood1.text=analysisList[0].text
            binding!!.tvGoodValue1.text="+${analysisList[0].mypercentage!!.slice(0..1).sum()}%"
            binding!!.sliderGood1.value= analysisList[0].mypercentage!!.slice(0..1).sum().toFloat()
        }else{
            binding!!.tvVeryhappy.text= "--"

            binding!!.ivGood1.visibility=View.INVISIBLE
        }

        if(analysisList[1].mypercentage!!.slice(0..1).sum()>0){
            binding!!.ivGood2.setImageResource(Day_Diary.int_to_image[analysisList[1].ghostnum])
            binding!!.tvGood2.text=analysisList[1].text
            binding!!.tvGoodValue2.text="+${analysisList[1].mypercentage!!.slice(0..1).sum()}%"
            binding!!.sliderGood2.value= analysisList[1].mypercentage!!.slice(0..1).sum().toFloat()
        }else{
            binding!!.ivGood2.visibility=View.INVISIBLE
        }

        if(analysisList[2].mypercentage!!.slice(0..1).sum()>0){
            binding!!.ivGood3.setImageResource(Day_Diary.int_to_image[analysisList[2].ghostnum])
            binding!!.tvGood3.text=analysisList[2].text
            binding!!.tvGoodValue3.text="+${analysisList[2].mypercentage!!.slice(0..1).sum()}%"
            binding!!.sliderGood3.value= analysisList[2].mypercentage!!.slice(0..1).sum().toFloat()


        }else{
            binding!!.ivGood3.visibility=View.INVISIBLE
        }



        analysisList= analysisList.sortedBy { -it.mypercentage!!.slice(3..4).sum() }

        if(analysisList[0].mypercentage!!.slice(3..4).sum()>0){
            binding!!.tvVerybad.text=analysisList[0].text
            binding!!.tvVerybad2.text=analysisList[0].text

            binding!!.ivBad1.setImageResource(Day_Diary.int_to_image[analysisList[0].ghostnum])
            binding!!.tvBad1.text=analysisList[0].text
            binding!!.tvBadValue1.text="+${analysisList[0].mypercentage!!.slice(3..4).sum()}%"
            binding!!.sliderBad1.value= analysisList[0].mypercentage!!.slice(3..4).sum().toFloat()
        }else{
            binding!!.tvVerybad.text="--"
            binding!!.tvVerybad2.text="--"
            binding!!.ivBad1.visibility=View.INVISIBLE
        }

        if(analysisList[1].mypercentage!!.slice(3..4).sum()>0){
            binding!!.ivBad2.setImageResource(Day_Diary.int_to_image[analysisList[1].ghostnum])
            binding!!.tvBad2.text=analysisList[1].text
            binding!!.tvBadValue2.text="+${analysisList[1].mypercentage!!.slice(3..4).sum()}%"
            binding!!.sliderBad2.value= analysisList[1].mypercentage!!.slice(3..4).sum().toFloat()
        }else{
            binding!!.ivBad2.visibility=View.INVISIBLE

        }
        if(analysisList[2].mypercentage!!.slice(3..4).sum()>0){
            binding!!.ivBad3.setImageResource(Day_Diary.int_to_image[analysisList[2].ghostnum])
            binding!!.tvBad3.text=analysisList[2].text
            binding!!.tvBadValue3.text="+${analysisList[2].mypercentage!!.slice(3..4).sum()}%"
            binding!!.sliderBad3.value= analysisList[2].mypercentage!!.slice(3..4).sum().toFloat()
        }else{
            binding!!.ivBad3.visibility=View.INVISIBLE

        }



        binding!!.tvAvgScore.text=allscore.toString()

        binding!!.tvVergoodPer.text="${allpercen[0]} %"
        binding!!.tvGoodPer.text="${allpercen[1]} %"
        binding!!.tvNormalPer.text="${allpercen[2]} %"
        binding!!.tvBadPer.text="${allpercen[3]} %"
        binding!!.tvVerbadPer.text="${allpercen[4]} %"


        analysisList=analysisList.sortedBy { -it.mypercentage!!.slice(0..1).sum() }




    }



    private fun initChartData() {
        // 더미데이터
        var array =viewModel.getEmotionArray().values.toList()
        array =array.sortedBy { it.date }


        xlabelindex = arrayListOf()
        xlabelindex.add("")
        var index=0
        var formatdate = SimpleDateFormat("yy.MM.dd")
        var formatdatenonyear = SimpleDateFormat("MM.dd.")

        var curyear=Calendar.getInstance().time.year

        for(i in array){
            index+=1

            chartData.add(Entry(index.toFloat(), (4-i.today_emotion.ghostimage).toFloat()) )
            xlabelindex.add(if(i.date.year==curyear) formatdatenonyear.format(i.date) else formatdate.format(i.date))

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

        chart.data=lineData
        lineData.addDataSet(lineDataSet)


        var imagelist:ArrayList<Bitmap> = arrayListOf()

        imagelist.add(drawableToBitmap(resources.getDrawable(R.drawable.ic_graph_04))!!)
        imagelist.add(drawableToBitmap(resources.getDrawable(R.drawable.ic_graph_03))!!)
        imagelist.add(drawableToBitmap(resources.getDrawable(R.drawable.ic_graph_02))!!)
        imagelist.add(drawableToBitmap(resources.getDrawable(R.drawable.ic_graph_01))!!)
        imagelist.add(drawableToBitmap(resources.getDrawable(R.drawable.ic_graph_00))!!)


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

package com.example.ghostdiary.fragment.main

import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.alpha
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.ghostdiary.MainViewModel
import com.example.ghostdiary.R
import com.example.ghostdiary.Util
import com.example.ghostdiary.databinding.FragmentSleepBinding
import com.example.ghostdiary.dataclass.Sleep_data
import com.github.mikephil.charting.charts.CandleStickChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.round


class SleepFragment(var sleepArray:ArrayList<Sleep_data>) : Fragment() {

    private val viewModel: MainViewModel by activityViewModels()
    private var binding: FragmentSleepBinding?=null

    private var chartData = ArrayList<CandleEntry>()  // 데이터 배열
    private var lineDataSet = ArrayList<ILineDataSet>()  // 데이터 배열 → 데이터 셋
    private var candleData: CandleData = CandleData()
    lateinit var chart: CandleStickChart
    companion object{
        fun convertDays(date: Date): Float {
            val basedata = "2022-02-10"

            val format = SimpleDateFormat("yyyy-mm-dd")
            val parsedata = format.parse(basedata)

            Log.d("TAG","${date}--> ${(date.time- parsedata.time)/(1000*3600*24)}")
            return round((date.time- parsedata.time)/(1000*3600*24f))

        }fun reverseDate(day:Long): Date? {
            val basedata = "2022-02-10"

            val format = SimpleDateFormat("yyyy-mm-dd")

            val parsedata = format.parse(basedata)

            parsedata.time=parsedata.time+(day*(1000*3600*24))
            return parsedata

        }

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentSleepBinding.inflate(inflater,container,false)

        init()
        Util.setGlobalFont(binding!!.root)

        return binding!!.root
    }

    private fun init() {

        var sleepstart =Date (TimeUnit.MINUTES.toMillis(round(60 * 9 +Sleep_data.avgsleepstart!!*10).toLong()))
        var sleepend =Date (TimeUnit.MINUTES.toMillis(60 * 9+ round(Sleep_data.avgsleepend!!*10).toLong()))
        var sleeptime =Date (TimeUnit.MINUTES.toMillis(60*(-9)+ round(Sleep_data.avgsleeptime!!*10).toLong()))

        var formatMinutes = SimpleDateFormat("HH:mm")

        binding!!.tvSleepstarttime.text=formatMinutes.format(sleepstart)
        binding!!.tvSleependtime.text=formatMinutes.format(sleepend)
        binding!!.tvSleeptime.text=formatMinutes.format(sleeptime)



        chart=binding!!.chart

        initChartData()

        initChart()

        initadvice_sleep()


    }

    private fun initadvice_sleep(){

        var sleepmiddle=(Sleep_data.avgsleepstart!! + Sleep_data.avgsleepend!!)/2
        var Stime=Sleep_data.avgsleeptime!!/6
        if(Stime <=7.5f){
            Stime=3.5f
        }else{
            Stime=4.0f

        }
        sleepmiddle /= 6


        var startsleep= ((18+round(sleepmiddle-Stime))%24).toInt().toString()
        var endsleep= ((18+round(sleepmiddle+Stime))%24).toInt().toString()
        if(startsleep =="0")
            startsleep="24"
        if(endsleep=="0")
            endsleep="24"


        val content  = "${startsleep}시에 자서  ${endsleep} 시에 일어나는\n규칙적인 생활습관을 길러보면 어떨까요?"
        val spannableString = SpannableString(content) //객체 생성


        val Sstart: Int = content.indexOf(startsleep)
        val Send = Sstart + startsleep.length
        val Estart: Int = content.indexOf(endsleep)
        val Eend = Estart + endsleep.length

        spannableString.setSpan(ForegroundColorSpan(Color.parseColor("#F08080")), Sstart, Send, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(RelativeSizeSpan(1.2f), Sstart, Send, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(ForegroundColorSpan(Color.parseColor("#F08080")), Estart, Eend, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(RelativeSizeSpan(1.2f), Estart, Eend, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);

        binding!!.tvAdviceSleep.text=spannableString



    }

    // 차트 데이터 초기화 메서드
    private fun initChartData() {
        // 더미데이터
        sleepArray.sortBy { it.date }


        for(i in sleepArray){

            var start=i.sleepstart/6.0f
            var end=i.sleepend/6.0f
            var middle=round((start+end)/2)
            Log.d("TAG","에베베베$start,$end")
            chartData.add(CandleEntry(convertDays(i.date).toFloat(), end,start,start,start,resources.getDrawable(R.drawable.circle_justside)))

//            if((i.sleeptime/6)<=8 && (i.sleeptime/6)>=7){
//                chartData.add(CandleEntry(convertDays(i.date).toFloat(), end,start,end,start,resources.getDrawable(R.drawable.circle_justside)))
//
//            }else{
//                chartData.add(CandleEntry(convertDays(i.date).toFloat(), start,end,start,end,resources.getDrawable(R.drawable.circle_justside)))
//
//            }
        }


        var set = CandleDataSet(chartData, "set1")
        set.setDrawIcons(true)

        set.setShadowColor(getResources().getColor(R.color.pink));
        set.setShadowWidth(5f)

        set.color=resources.getColor(R.color.black)


        set.decreasingColor=resources.getColor(R.color.badsleep)
        set.setDecreasingPaintStyle(Paint.Style.FILL);
        set.increasingColor=resources.getColor(R.color.white)
        set.setIncreasingPaintStyle(Paint.Style.FILL);
        set.neutralColor=resources.getColor(R.color.white)

        candleData = CandleData(set)


        set.setDrawValues(false)
        set.formLineWidth=0f
//        set.mode = LineDataSet.Mode.STEPPED

    }
    // 차트 초기화 메서드
    private fun initChart() {
        chart.run {
            setDrawGridBackground(false)
            setBackgroundColor(Color.WHITE)
            legend.isEnabled = false
            isHighlightPerTapEnabled=false
            legend.setDrawInside(true)
        }
        chart.setHighlightPerDragEnabled(false);


        chart.requestDisallowInterceptTouchEvent(true)


        val xAxis = chart.xAxis

        xAxis.setDrawGridLines(false) // disable x axis grid lines

        xAxis.axisMaximum = chartData.get(chartData.size-1).x +1
        xAxis.axisMinimum = chartData.get(0).x - 1
        xAxis.labelCount = 5
        xAxis.valueFormatter = TimeAxisValueFormat()
        xAxis.textSize=12f
        xAxis.granularity=1f
        xAxis.position=XAxis.XAxisPosition.BOTTOM_INSIDE


        chart.setVisibleXRangeMinimum(10f)




        xAxis.textColor = Color.BLACK
        xAxis.position = XAxis.XAxisPosition.BOTTOM  // x축 라벨 위치
        xAxis.setDrawLabels(true)  // Grid-line 표시
        xAxis.setDrawAxisLine(true)  // Axis-Line 표시

        // 왼쪽 y축 값
        val yLAxis = chart.axisLeft
        yLAxis.axisMaximum = 20f   // y축 최대값(고정)
        yLAxis.axisMinimum = 0f  // y축 최소값(고정)
        yLAxis.setDrawGridLines(true)
        yLAxis.setDrawAxisLine(true)

        // 왼쪽 y축 도메인 변경
        val yAxisVals = ArrayList<String>()
        for (i in 0..20){
            yAxisVals.add(((i+18)%24).toString())
        }
        yLAxis.valueFormatter = IndexAxisValueFormatter(yAxisVals)
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

        chart.setVisibleYRange(20f,20f,YAxis.AxisDependency.LEFT)

        chart!!.description.isEnabled = false  // 설명
        chart.data=candleData

        chart.moveViewToX(chartData[chartData.size-1].x)



        chart!!.invalidate()  // 다시 그리기


    }

    class TimeAxisValueFormat : IndexAxisValueFormatter() {

        override fun getFormattedValue(value: Float): String {

            // Float(min) -> Date



            var timeMimutes = reverseDate(value.toLong())
            var formatMinutes = SimpleDateFormat("yy/MM/dd")

            return formatMinutes.format(timeMimutes)
        }
    }

    override fun onDestroyView() {
        binding=null
        super.onDestroyView()
    }

}
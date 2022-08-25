package com.example.ghostdiary.fragment.main

import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.ghostdiary.MainViewModel
import com.example.ghostdiary.Util
import com.example.ghostdiary.databinding.FragmentSleepBinding
import com.example.ghostdiary.dataclass.Sleep_data
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.round


class SleepFragment(var sleepArray:ArrayList<Sleep_data>) : Fragment() {

    private val viewModel: MainViewModel by activityViewModels()
    private var binding: FragmentSleepBinding?=null

    private var chartData = ArrayList<Entry>()  // 데이터 배열
    private var lineDataSet = ArrayList<ILineDataSet>()  // 데이터 배열 → 데이터 셋
    private var lineData: LineData = LineData()
    lateinit var chart: LineChart

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

        var sleepstart =Date (TimeUnit.MINUTES.toMillis(round(60 * 13 +Sleep_data.avgsleepstart!!*10).toLong()))
        var sleepend =Date (TimeUnit.MINUTES.toMillis(60 * 13+ round(Sleep_data.avgsleepend!!*10).toLong()))
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


        var startsleep= ((22+round(sleepmiddle-Stime))%24).toInt().toString()
        var endsleep= ((22+round(sleepmiddle+Stime))%24).toInt().toString()
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
            chartData.add(Entry(i.date.time.toFloat(), (i.sleeptime/6).toFloat()))
        }


        var set = LineDataSet(chartData, "set1")
        set.setColor(Color.parseColor("#F08080"))
        set.setCircleColor(Color.parseColor("#F08080"));

        lineDataSet.add(set)
        lineData = LineData(lineDataSet)

        set.lineWidth = 2F
        set.setDrawValues(false)
        set.highLightColor = Color.TRANSPARENT
//        set.mode = LineDataSet.Mode.STEPPED

    }
    // 차트 초기화 메서드
    private fun initChart() {
        chart.run {
            setDrawGridBackground(false)
            setBackgroundColor(Color.WHITE)
            legend.isEnabled = false
        }

        val xAxis = chart.xAxis
        xAxis.setDrawLabels(true)  // Label 표시 여부

        xAxis.axisMaximum = (sleepArray[sleepArray.size-1].date.time + 1000*3600*24*1f).toFloat()
        xAxis.axisMinimum = (sleepArray[0].date.time- 1000*3600*24*1f).toFloat()
        xAxis.labelCount = 4
        xAxis.valueFormatter = TimeAxisValueFormat()

        chart.setVisibleXRangeMinimum(1000*3600*24*6f)




        xAxis.textColor = Color.BLACK
        xAxis.position = XAxis.XAxisPosition.BOTTOM  // x축 라벨 위치
        xAxis.setDrawLabels(true)  // Grid-line 표시
        xAxis.setDrawAxisLine(true)  // Axis-Line 표시

        // 왼쪽 y축 값
        val yLAxis = chart.axisLeft
        yLAxis.axisMaximum = 13f   // y축 최대값(고정)
        yLAxis.axisMinimum = 0f  // y축 최소값(고정)


        // 왼쪽 y축 도메인 변경
        val yAxisVals = ArrayList<String>()
        for (i in 0..12){
            yAxisVals.add(i.toString())
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

        chart.setVisibleYRange(13f,13f,YAxis.AxisDependency.LEFT)

        chart!!.description.isEnabled = false  // 설명
        chart!!.data = lineData  // 데이터 설정

        chart.moveViewToX(chartData[chartData.size-1].x)



        chart!!.invalidate()  // 다시 그리기
    }

    class TimeAxisValueFormat : IndexAxisValueFormatter() {

        override fun getFormattedValue(value: Float): String {

            // Float(min) -> Date
            var timeMimutes = Date(value.toLong())
            var formatMinutes = SimpleDateFormat("yy/MM/dd")

            return formatMinutes.format(timeMimutes)
        }
    }

    override fun onDestroyView() {
        binding=null
        super.onDestroyView()
    }

}
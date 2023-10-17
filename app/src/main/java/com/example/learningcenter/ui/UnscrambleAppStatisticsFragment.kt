package com.example.learningcenter.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.coroutineScope
import com.example.learningcenter.BaseApplication
import com.example.learningcenter.R
import com.example.learningcenter.data.WordDao
import com.example.learningcenter.databinding.FragmentAddWordBinding
import com.example.learningcenter.databinding.FragmentUnscrambleAppStatisticsBinding
import com.example.learningcenter.model.Word
import com.example.learningcenter.ui.viewmodel.WordViewModel
import com.example.learningcenter.ui.viewmodel.WordViewModelFactory
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class UnscrambleAppStatisticsFragment : Fragment() {

    lateinit var arrayList: ArrayList<BarEntry>

    private var _binding: FragmentUnscrambleAppStatisticsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: WordViewModel by activityViewModels {
        WordViewModelFactory(
            (activity?.application as BaseApplication).database.wordDao()
        )
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentUnscrambleAppStatisticsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // on below line we are initializing
        // our variable with their ids.
        var barchart = binding.barchart
        lifecycle.coroutineScope.launch {
            viewModel.getTitles().collect() {

                arrayList = ArrayList<BarEntry>()

                val arrayListx = ArrayList<String>()
                for (items in it.indices) {
                    arrayList.add(BarEntry(items.toFloat(), it[items].wordFound.toFloat()))
                    arrayListx.add(it[items].wordTitle.toString())
                    val barDataSet = BarDataSet(arrayList, "")
                    val data = BarData(barDataSet)
                    barchart.data = data
                    barchart.invalidate()
                    barchart.animateXY(3000, 3000)
                    val xAxis: XAxis = barchart.xAxis

                    xAxis.valueFormatter = IndexAxisValueFormatter(arrayListx)
                    xAxis.setDrawLabels(true)
                    xAxis.labelRotationAngle = 90f
                    xAxis.textSize = 12f
                    xAxis.setDrawAxisLine(false)
                    xAxis.setDrawGridLines(true)
                    data.setValueTextSize(12f)
                    barchart.setFitBars(true)
                    barchart.isDoubleTapToZoomEnabled = false
                    barchart.isDrawValueAboveBarEnabled
                    barchart.legend.isEnabled = false
                    barchart.description.isEnabled = false
                    barchart.setVisibleXRangeMaximum(8f)
                    barchart.setScaleEnabled(false)
                    data.barWidth = 0.9f
                    barchart.xAxis.position = XAxis.XAxisPosition.BOTTOM
                }
            }
        }

    }
}

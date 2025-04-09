package com.example.androidassessmenttest.ui.main

import MainViewModel
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.androidassessmenttest.R
import com.example.androidassessmenttest.ui.adapter.CarouselAdapter
import com.example.androidassessmenttest.ui.adapter.LabelAdapter
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()

    private lateinit var labelAdapter: LabelAdapter

    private val carouselData = listOf(
        R.drawable.apple, R.drawable.banana, R.drawable.cherry
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val carouselViewPager = findViewById<ViewPager2>(R.id.carouselViewPager)
        val carouselIndicator = findViewById<TabLayout>(R.id.carouselIndicator)
        val listRecyclerView = findViewById<RecyclerView>(R.id.listRecyclerView)
        val searchBar = findViewById<EditText>(R.id.searchBar)
        val fab = findViewById<FloatingActionButton>(R.id.fab)

        // Setup Carousel
        val carouselAdapter = CarouselAdapter(carouselData)
        carouselViewPager.adapter = carouselAdapter
        TabLayoutMediator(carouselIndicator, carouselViewPager) { _, _ -> }.attach()

        // Setup RecyclerView
        labelAdapter = LabelAdapter(emptyList())
        listRecyclerView.layoutManager = LinearLayoutManager(this)
        listRecyclerView.adapter = labelAdapter

        // Observe Data
        viewModel.currentList.observe(this, Observer { newList ->
            labelAdapter.update(newList)
        })

        // Carousel page changes -> update list
        carouselViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                viewModel.updateListByCarouselPosition(position)
                searchBar.text.clear()
            }
        })

        // Search functionality
        searchBar.addTextChangedListener {
            viewModel.filterList(it.toString())
        }

        fab.setOnClickListener {
            showStatsBottomSheet()
        }
    }

    private fun showStatsBottomSheet() {
        val dialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_stats, null)

        val labelList = labelAdapter.labels
        val statsTextView = view.findViewById<TextView>(R.id.statsText)
        val topChars = viewModel.getTop3Characters(labelList)

        statsTextView.text = buildString {
            append("List 1 (${labelList.size} items)\n")
            topChars.forEach { (char, count) ->
                append("$char = $count\n")
            }
        }

        dialog.setContentView(view)
        dialog.show()
    }

}

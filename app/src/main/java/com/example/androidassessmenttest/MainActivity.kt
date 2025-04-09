package com.example.androidassessmenttest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.androidassessmenttest.Adapter.CarouselAdapter
import com.example.androidassessmenttest.Adapter.LabelAdapter
import com.example.androidassessmenttest.dataClass.ListItem
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {

    private lateinit var carouselAdapter: CarouselAdapter
    private lateinit var labelAdapter: LabelAdapter

    private val carouselData = listOf(
        R.drawable.apple, R.drawable.banana, R.drawable.cherry
    )

    private val listDataMap = listOf(
        listOf(
            ListItem("Apple", "Subtitle of Apple", R.drawable.apple),
            ListItem("Avocado", "Subtitle of Avocado", R.drawable.avocado),
            ListItem("Apricot", "Subtitle of Apricot", R.drawable.apricot)
        ),
        listOf(
            ListItem("Banana", "Subtitle of Banana", R.drawable.banana),
            ListItem("Blueberry", "Subtitle of Blueberry", R.drawable.blueberry),
            ListItem("Blackberry", "Subtitle of Blackberry", R.drawable.blackberry)
        ),
        listOf(
            ListItem("Cherry", "Subtitle of Cherry", R.drawable.cherry),
            ListItem("Coconut", "Subtitle of Coconut", R.drawable.coconut),
            ListItem("Cranberry", "Subtitle of Cranberry", R.drawable.cranberry)
        )
    )

    private var currentList = listDataMap[0]

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val carouselViewPager = findViewById<ViewPager2>(R.id.carouselViewPager)
        val carouselIndicator = findViewById<TabLayout>(R.id.carouselIndicator)
        val listRecyclerView = findViewById<RecyclerView>(R.id.listRecyclerView)
        val searchBar = findViewById<EditText>(R.id.searchBar)
        val fab = findViewById<FloatingActionButton>(R.id.fab)

        // Setup Carousel
        carouselAdapter = CarouselAdapter(carouselData)
        carouselViewPager.adapter = carouselAdapter

        TabLayoutMediator(carouselIndicator, carouselViewPager) { _, _ -> }.attach()

        // Setup List
        labelAdapter = LabelAdapter(currentList)
        listRecyclerView.layoutManager = LinearLayoutManager(this)
        listRecyclerView.adapter = labelAdapter

        // Listen for Carousel Scroll to change List
        carouselViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                currentList = listDataMap[position % listDataMap.size]
                labelAdapter.update(currentList)
                searchBar.text.clear()
            }
        })

        // Search Filtering
        searchBar.addTextChangedListener {
            val query = it.toString()
            if (query.isEmpty()) {
                labelAdapter.update(currentList)
            } else {
                val filtered = currentList.filter { it.title.contains(query, ignoreCase = true) }
                labelAdapter.update(filtered)
            }
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
        val topChars = getTop3Characters(labelList)

        statsTextView.text = buildString {
            append("List 1 (${labelList.size} items)\n")
            topChars.forEach { (char, count) ->
                append("$char = $count\n")
            }
        }

        dialog.setContentView(view)
        dialog.show()
    }

    private fun getTop3Characters(list: List<ListItem>): List<Pair<Char, Int>> {
        val charCount = mutableMapOf<Char, Int>()
        list.forEach { (label, _) ->
            label.toLowerCase().filter { ch -> ch.isLetter() }
                .forEach { ch -> charCount[ch] = charCount.getOrDefault(ch, 0) + 1 }
        }
        return charCount.entries.sortedByDescending { it.value }.take(3).map { it.toPair() }
    }

}
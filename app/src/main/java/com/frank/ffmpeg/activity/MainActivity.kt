package com.frank.ffmpeg.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

import com.frank.ffmpeg.R
import com.frank.ffmpeg.adapter.WaterfallAdapter
import com.frank.ffmpeg.listener.OnItemClickListener

/**
 * The main entrance of all Activity
 * Created by frank on 2018/1/23.
 */
class MainActivity : BaseActivity() {

    override val layoutId: Int
        get() = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initView()
    }

    private fun initView() {
        val list = listOf(
                getString(R.string.video_live))

        val viewWaterfall: RecyclerView = findViewById(R.id.list_main_item)
        val layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        viewWaterfall.layoutManager = layoutManager

        val adapter = WaterfallAdapter(list)
        adapter.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(position: Int) {
                doClick(position)
            }
        })
        viewWaterfall.adapter = adapter
    }

    private fun doClick(pos: Int) {
        val intent = Intent()
        when (pos) {
            0 //realtime living with rtmp stream
            -> intent.setClass(this@MainActivity, LiveActivity::class.java)
            else -> {
            }
        }
        startActivity(intent)
    }

    override fun onViewClick(view: View) {

    }

    override fun onSelectedFile(filePath: String) {

    }

}

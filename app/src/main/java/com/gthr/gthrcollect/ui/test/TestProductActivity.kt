package com.gthr.gthrcollect.ui.test

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gthr.gthrcollect.R
import com.gthr.gthrcollect.ui.homebottomnav.search.adapter.TestProductAdapter
import com.gthr.gthrcollect.utils.GridSpacingItemDecoration
import com.gthr.gthrcollect.utils.customviews.CustomProductCell

class TestProductActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_product)

        val rv : RecyclerView = findViewById(R.id.rv)
        val toolbar_title : TextView = findViewById(R.id.toolbar_title)
        rv.apply {
            layoutManager = GridLayoutManager(this@TestProductActivity, 2)
        }

        when(intent.getIntExtra("type",-1)){
            1 -> {
                toolbar_title.text = "FOR_SALE"
                rv.adapter = TestProductAdapter(CustomProductCell.State.FOR_SALE)
            }
            2 -> {
                toolbar_title.text = "WANT"
                rv.adapter = TestProductAdapter(CustomProductCell.State.WANT)
            }
            3 -> {
                toolbar_title.text = "NORMAL"
                rv.adapter = TestProductAdapter(CustomProductCell.State.NORMAL)
            }
            4 -> {
                toolbar_title.text = "FAVORITE"
                rv.adapter = TestProductAdapter(CustomProductCell.State.FAVORITE)
            }
            5 -> {
                toolbar_title.text = "OFFER"
                rv.adapter = TestProductAdapter(CustomProductCell.State.OFFER)
            }
            6 -> {
                toolbar_title.text = "SOLD"
                rv.adapter = TestProductAdapter(CustomProductCell.State.SOLD)
            }
        }

    }
}
package com.gthr.gthrcollect.ui.test

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gthr.gthrcollect.R
import com.gthr.gthrcollect.ui.homebottomnav.search.SearchFragment
import com.gthr.gthrcollect.ui.homebottomnav.search.adapter.TestProductAdapter
import com.gthr.gthrcollect.utils.GridSpacingItemDecoration
import com.gthr.gthrcollect.utils.customviews.CustomProductCell

class TestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        val button1 : Button = findViewById(R.id.rv1)
        val button2 : Button = findViewById(R.id.rv2)
        val button3 : Button = findViewById(R.id.rv3)
        val button4 : Button = findViewById(R.id.rv4)
        val button5 : Button = findViewById(R.id.rv5)
        val button6 : Button = findViewById(R.id.rv6)

        button1.setOnClickListener {
            val intent = Intent(this,TestProductActivity::class.java)
            intent.putExtra("type",1)
            startActivity(intent)
        }

        button2.setOnClickListener {
            val intent = Intent(this,TestProductActivity::class.java)
            intent.putExtra("type",2)
            startActivity(intent)
        }

        button3.setOnClickListener {
            val intent = Intent(this,TestProductActivity::class.java)
            intent.putExtra("type",3)
            startActivity(intent)
        }

        button4.setOnClickListener {
            val intent = Intent(this,TestProductActivity::class.java)
            intent.putExtra("type",4)
            startActivity(intent)
        }

        button5.setOnClickListener {
            val intent = Intent(this,TestProductActivity::class.java)
            intent.putExtra("type",5)
            startActivity(intent)
        }

        button6.setOnClickListener {
            val intent = Intent(this,TestProductActivity::class.java)
            intent.putExtra("type",6)
            startActivity(intent)
        }


    }


}
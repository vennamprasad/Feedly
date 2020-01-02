package com.appyhigh.feedly.ui.menu

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.appyhigh.feedly.R
import com.appyhigh.feedly.ui.adapter.CategoryAdapter
import kotlinx.android.synthetic.main.activity_menu.*


class MenuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        viewPager.adapter= CategoryAdapter(
            this,
            supportFragmentManager
        );
        tabs.setupWithViewPager(viewPager);
    }
}

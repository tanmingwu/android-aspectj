package com.example.singleapp.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.singleapp.R

class DetailActivity : AppCompatActivity() {

    companion object {
        const val TAG = "DetailActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
    }

    override fun onResume() {
        super.onResume()
        Log.e(TAG, "onResume: " + System.currentTimeMillis())
    }
}
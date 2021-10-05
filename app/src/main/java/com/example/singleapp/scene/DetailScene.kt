package com.example.singleapp.scene

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bytedance.scene.Scene
import com.example.singleapp.R

class DetailScene : Scene() {

    companion object {
        const val TAG = "DetailScene"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.detail_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val currentTimeMillis = System.currentTimeMillis()
        Log.e(TAG, "onViewCreated: " + (System.currentTimeMillis() - currentTimeMillis))
    }

    override fun onPause() {
        super.onPause()
        Log.e(TAG, "onPause: " + System.currentTimeMillis())
    }

    override fun onResume() {
        super.onResume()
        Log.e(TAG, "onResume: " + System.currentTimeMillis())
    }
}
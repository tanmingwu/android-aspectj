package com.example.singleapp.scene

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bytedance.scene.Scene
import com.example.singleapp.R

/**
 * use scene replace fragment
 */
class MainScene : Scene() {

    companion object {
        const val TAG = "MainActivity"
    }

    private lateinit var mIv: ImageView
    private lateinit var mIv2: ImageView
    private lateinit var mIv3: ImageView
    private lateinit var mIv4: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mIv = findViewById(R.id.tv_hello_world) ?: ImageView(requireApplicationContext())
        mIv2 = findViewById(R.id.tv_hello_world2) ?: ImageView(requireApplicationContext())
        mIv3 = findViewById(R.id.tv_hello_world3) ?: ImageView(requireApplicationContext())
        mIv4 = findViewById(R.id.tv_hello_world4) ?: ImageView(requireApplicationContext())
    }
}
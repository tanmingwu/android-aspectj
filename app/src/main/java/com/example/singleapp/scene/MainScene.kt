package com.example.singleapp.scene

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import coil.imageLoader
import coil.load
import coil.request.ImageRequest
import com.bytedance.scene.Scene
import com.example.singleapp.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * use scene replace fragment
 */
class MainScene : Scene() {

    companion object {
        const val TAG = "MainActivity"
        const val URL =
            "https://stg1-eco.cdn.lifeapp.pingan.com.cn/cmsinfo/upload/material_702_168/e3ut3f47-_-_-1625128353833.png?imageView2/0/w/1080/format/webp/ignore-error/1"
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
        mIv.load(URL)
//        sceneContext?.let { Glide.with(it).load(URL).into(mIv2) }
        sceneContext?.let {
            val request = ImageRequest.Builder(it).data(URL).build()
            GlobalScope.launch(Dispatchers.Main) {
                val drawable = it.imageLoader.execute(request).drawable
                mIv.setImageDrawable(drawable)
            }
        }
    }

//    private fun playWebpAnim() {
//        val imageView: SimpleDraweeView? = findViewById(R.id.my_image_view)
//        val uri = Uri.parse("asset://com.example.singleapp/img_0.webp")
//        val kevController = Fresco.newDraweeControllerBuilder()
//            .setUri(uri)
//            .setAutoPlayAnimations(true)
//            .build()
//        imageView?.controller = kevController
//    }
}
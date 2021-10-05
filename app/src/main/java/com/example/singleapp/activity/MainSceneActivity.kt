package com.example.singleapp.activity

import android.os.Bundle
import com.bytedance.scene.Scene
import com.bytedance.scene.ui.SceneActivity
import com.example.singleapp.scene.MainScene

class MainSceneActivity : SceneActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun supportRestore(): Boolean {
        return false
    }

    override fun getHomeSceneClass(): Class<out Scene> {
        return MainScene::class.java
    }
}
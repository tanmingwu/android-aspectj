package com.example.singleapp.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.singleapp.R
import com.example.singleapp.viewmodel.MainViewModel
import com.google.android.flexbox.FlexboxLayout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MainFragment : Fragment() {

    companion object {
        const val TAG = "MainFragment"
    }

    private lateinit var mTvHello: TextView
    private lateinit var mViewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        mViewModel.data.observe(viewLifecycleOwner, Observer {
            Log.e(TAG, "onViewCreated: $it")
        })
        mViewModel.getXxxData()
//        mTvHello = view.findViewById(R.id.tv_hello)
//        mTvHello.setOnClickListener {
//            Log.e(TAG, "onClick: " + System.currentTimeMillis())
//            val navController = Navigation.findNavController(mTvHello)
//            navController.navigate(R.id.action_mainFragment_to_detailFragment)
//        }
        lifecycleScope.launch(Dispatchers.Main) {
            val async = lifecycleScope.async(Dispatchers.IO) { getResult() }
            val result = async.await()
            Log.e(TAG, "onViewCreated: $result")
        }
        Log.e(TAG, "onViewCreated: hahahahah ")
    }

    override fun onResume() {
        super.onResume()
        Log.e(TAG, "onResume: " + System.currentTimeMillis())
    }

    override fun onPause() {
        super.onPause()
        Log.e(TAG, "onPause: " + System.currentTimeMillis())
    }

    private fun getResult(): Int {
        return 10
    }
}
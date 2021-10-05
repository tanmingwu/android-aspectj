package com.example.singleapp.activity

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.os.Process
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.PermissionChecker
import com.example.singleapp.R
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import pub.devrel.easypermissions.EasyPermissions

class MainActivity : AppCompatActivity() {

    companion object {
        const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val checkPermission = PermissionChecker.checkPermission(
            this, Manifest.permission.ACCESS_FINE_LOCATION,
            Process.myPid(), Process.myUid(), packageName
        )
        Log.e(TAG, "onCreate: checkPermission = $checkPermission")
        val hasPermissions =
            EasyPermissions.hasPermissions(this, Manifest.permission.ACCESS_FINE_LOCATION)
        Log.e(TAG, "onCreate: hasPermissions = $hasPermissions")
        val boolean =
            shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)
        Log.e(TAG, "onCreate: shouldShowRequestPermissionRationale = $boolean")
        if (hasPermissions) {
            requestLocation()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                100
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Log.e(TAG, "onRequestPermissionsResult: requestCode = $requestCode")
        val indexOf = permissions.indexOf(Manifest.permission.ACCESS_FINE_LOCATION)
        if (grantResults[indexOf] == 0) {
            requestLocation()
        }
    }

    private fun requestLocation() {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        locationManager.requestLocationUpdates(
            LocationManager.NETWORK_PROVIDER,
            1000,
            1.0f
        ) {
            Log.e(TAG, "requestLocation: $it")
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEventMainThread(obj: Any) {

    }

    fun onEventMainThread(str: String) {

    }
}
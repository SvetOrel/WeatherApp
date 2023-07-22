package com.example.weatherapp

import android.content.Context
import android.content.Intent
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if(!isLocationEnabled()){
            Toast.makeText(
                this,
                "Your location is turned off.Please turn it on.",
                Toast.LENGTH_SHORT
            ).show()
            //Allow us to turn on location
            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivity(intent)

        }else{
            Toast.makeText(
                this,
                "Your location provider is already turned on.",
                Toast.LENGTH_SHORT
            ).show()

            Dexter.withContext(this)
                .withPermissions(android.Manifest.permission.ACCESS_FINE_LOCATION,
                                 android.Manifest.permission.ACCESS_COARSE_LOCATION,)
                .withListener(object: MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                        report?.let {
                            if (report.areAllPermissionsGranted()) {
                                //TODO add requestLocationData()
                            }
                            if(report.isAnyPermissionPermanentlyDenied){
                                Toast.makeText(
                                    this@MainActivity,
                                    "You have denied location permission.Please enable them as it is mandatory for the app to work.",
                                    Toast.LENGTH_SHORT)
                            }
                        }
                    }
                    override fun onPermissionRationaleShouldBeShown(
                        permissions: MutableList<PermissionRequest>?,
                        token: PermissionToken?) {
                            //TODO - implement showRationalDialogForPermissions()
                        }
                    }).onSameThread()
                    .check()
        }
    }

    private fun isLocationEnabled(): Boolean{
        //This provides access to the system location services.
        val locationManager: LocationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }
}
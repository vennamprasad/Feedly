package com.appyhigh.feedly.ui.menu

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.appyhigh.feedly.ADD_ENABLE
import com.appyhigh.feedly.R
import com.appyhigh.feedly.adStatus
import com.appyhigh.feedly.ui.adapter.CategoryAdapter
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.onesignal.OneSignal
import kotlinx.android.synthetic.main.activity_menu.*


class MenuActivity : AppCompatActivity(), BackHandledFragment.BackHandlerInterface {
    private var selectedFragment: BackHandledFragment? = null
    private lateinit var remoteConfig: FirebaseRemoteConfig
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        viewPager.adapter = CategoryAdapter(
            this,
            supportFragmentManager
        )
        tabs.setupWithViewPager(viewPager)
        init_firebaseRemoteConfig()
        initOneSignal()
    }

    private fun initOneSignal() {
        try {
            OneSignal.idsAvailable { userId, registrationId ->
                var text = "OneSignal UserID:\n$userId\n\n"

                text += if (registrationId != null)
                    "Google Registration Id:\n$registrationId"
                else
                    "Google Registration Id:\nCould not subscribe for push"

                val textView: TextView = findViewById(R.id.debug_view)
                textView.text = text
                textView.visibility= View.GONE
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun init_firebaseRemoteConfig() {
        try {
            remoteConfig = FirebaseRemoteConfig.getInstance()
            val configSettings = FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(0)
                .build()
            remoteConfig.setConfigSettingsAsync(configSettings)
            remoteConfig.setDefaultsAsync(R.xml.remote_config_defaults)
            this.let {
                remoteConfig.fetchAndActivate().addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val updated = task.result
                        Log.d("Top_HeadlinesFragment", "Config params updated: $updated")
                        Toast.makeText(
                            applicationContext,
                            "Fetch and activate succeeded",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    } else {
                        Toast.makeText(
                            applicationContext, "Fetch failed",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    showRemoteConfig();
                }
            }
        } catch (e: Exception) {e.printStackTrace()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun showRemoteConfig() {
        try {
            adStatus = remoteConfig.getBoolean(ADD_ENABLE)
            Toast.makeText(
                applicationContext,
                "add_status(" + adStatus.toString() + ")",
                Toast.LENGTH_SHORT
            )
                .show()
        } catch (e: Exception) {e.printStackTrace()
        }
    }

    override fun setSelectedFragment(backHandledFragment: BackHandledFragment?) {
        this.selectedFragment = selectedFragment
    }

    override fun onBackPressed() {
        if(selectedFragment == null || !selectedFragment!!.onBackPressed()) {
            // Selected fragment did not consume the back press event.
            super.onBackPressed();
        }
    }
}

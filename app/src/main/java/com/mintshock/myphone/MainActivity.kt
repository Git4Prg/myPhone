package com.mintshock.myphone

import android.Manifest.permission.CALL_PHONE
import android.Manifest.permission.MANAGE_OWN_CALLS
import android.Manifest.permission.READ_CALL_LOG
import android.Manifest.permission.READ_CONTACTS
import android.Manifest.permission.WRITE_CALL_LOG
import android.app.role.RoleManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.telecom.TelecomManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mintshock.myphone.fragments.CallLogsFragment
import com.mintshock.myphone.fragments.ContactsFragment
import com.mintshock.myphone.fragments.DialerFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        requestDefaultDialer()
        requestPermissions()

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer, DialerFragment()).commit()

        bottomNavigationView.setOnItemSelectedListener { item ->
            val selectedFragment: androidx.fragment.app.Fragment = when (item.itemId) {
                R.id.nav_dialer -> DialerFragment()
                R.id.nav_contacts -> ContactsFragment()
                R.id.nav_logs -> CallLogsFragment()
                else -> DialerFragment()
            }

            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, selectedFragment)
                .commit()

            true
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val roleManager = getSystemService(Context.ROLE_SERVICE) as RoleManager
            if (!roleManager.isRoleHeld(RoleManager.ROLE_DIALER)) {
                val intent = roleManager.createRequestRoleIntent(RoleManager.ROLE_DIALER)
                defaultDialerLauncher.launch(intent)
            }
        }
    }

    private val defaultDialerLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                Toast.makeText(this, "App set as default dialer", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Failed to set default dialer", Toast.LENGTH_SHORT).show()
            }
        }

    private fun requestDefaultDialer() {
        val telecomManager = getSystemService(TELECOM_SERVICE) as TelecomManager
        val packageName = packageName

        if (telecomManager.defaultDialerPackage != packageName) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val roleManager = getSystemService(Context.ROLE_SERVICE) as RoleManager
                if (!roleManager.isRoleHeld(RoleManager.ROLE_DIALER)) {
                    val intent = roleManager.createRequestRoleIntent(RoleManager.ROLE_DIALER)
                    defaultDialerLauncher.launch(intent)
                    return
                }
            } else {
                val intent = Intent(TelecomManager.ACTION_CHANGE_DEFAULT_DIALER).apply {
                    putExtra(TelecomManager.EXTRA_CHANGE_DEFAULT_DIALER_PACKAGE_NAME, packageName)
                }
                startActivity(intent)
            }
        } else {
            Toast.makeText(this, "App is already the default dialer", Toast.LENGTH_SHORT).show()
        }
    }

    private fun requestPermissions() {
        val permissions = arrayOf(
            READ_CALL_LOG,
            WRITE_CALL_LOG,
            READ_CONTACTS,
            CALL_PHONE,
            MANAGE_OWN_CALLS
        )

        val permissionsNeeded = permissions.filter {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }

        if (permissionsNeeded.isNotEmpty()) {
            ActivityCompat.requestPermissions(this, permissionsNeeded.toTypedArray(), 101)
        }
    }
}

package com.udacity

import android.app.DownloadManager
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.udacity.databinding.ActivityMainBinding
import com.udacity.databinding.ContentMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var contentMainBinding: ContentMainBinding
    private lateinit var fileName: String
    private lateinit var url: String

    private var downloadID: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        contentMainBinding = binding.contentMain
        setSupportActionBar(binding.toolbar)

        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
        settingCustomBtn()
        settingUrlFile()


    }

    private fun settingCustomBtn() {
        contentMainBinding.customButton.setOnClickListener {
            if (::url.isInitialized) {
                contentMainBinding.customButton.buttonState = ButtonState.Loading
                download()
            } else
                Toast.makeText(
                    this@MainActivity,
                    getString(R.string.you_should_select_from_list),
                    Toast.LENGTH_SHORT
                ).show()

        }
    }

    private fun settingUrlFile() {
        binding.contentMain.radioGroup.setOnCheckedChangeListener { _, index ->
            when (index) {
                R.id.radioButton_main_glide -> {
                    url = GLIDE_URL
                    fileName = getString(R.string.glide)
                }
                R.id.radioButton_main_loadApp -> {
                    url = LOAD_APP_URL
                    fileName = getString(R.string.loadapp)
                }
                R.id.radioButton_main_retrofit -> {

                    url = RETROFIT_URL
                    fileName = getString(R.string.retrofit)
                }

            }

        }
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            if (id == -1L)
                return
            id?.let { it ->
                val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
                val query = DownloadManager.Query()
                query.setFilterById(it)
                val cursor = downloadManager.query(query)
                if (cursor.moveToFirst()) {
                    val index = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)
                    val downloadStatus =
                        if (DownloadManager.STATUS_SUCCESSFUL == cursor.getInt(index))
                            getString(R.string.successful)
                        else
                            getString(R.string.failed)

                    sendNotifications(downloadStatus)
                    contentMainBinding.customButton.buttonState = ButtonState.Completed

                }
            }

        }
    }

    private fun sendNotifications(status: String) {
        val manager = ContextCompat.getSystemService(
            applicationContext,
            NotificationManager::class.java
        ) as NotificationManager
        intent = Intent(applicationContext, DetailActivity::class.java)
        intent.putExtra(STATUS_KEY, status)
        intent.putExtra(FILE_NAME_KEY, fileName)
        manager.sendNotification(intent, this)
    }

    private fun download() {
        val request =
            DownloadManager.Request(Uri.parse(url))
                .setTitle(getString(R.string.app_name))
                .setDescription(String.format(getString(R.string.app_description), fileName))
                .setRequiresCharging(false)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)

        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        downloadID =
            downloadManager.enqueue(request)
    }

    companion object {
         const val STATUS_KEY = "status"
        const val FILE_NAME_KEY = "fileName"
        private const val LOAD_APP_URL =
            "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip"
        private const val GLIDE_URL = "https://github.com/bumptech/glide/archive/master.zip"
        private const val RETROFIT_URL = "https://github.com/square/retrofit/archive/master.zip"
    }

}

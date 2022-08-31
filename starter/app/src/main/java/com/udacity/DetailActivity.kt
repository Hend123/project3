package com.udacity

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.udacity.MainActivity.Companion.FILE_NAME_KEY
import com.udacity.MainActivity.Companion.STATUS_KEY
import com.udacity.databinding.ActivityDetailBinding
import com.udacity.databinding.ContentDetailBinding

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var bindingContent: ContentDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        bindingContent = binding.contentDetails

        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        bindingContent.textViewDetailFileName.text = intent.getStringExtra(FILE_NAME_KEY)
        bindingContent.textViewDetailStatus.text = intent.getStringExtra(STATUS_KEY)

        if (intent.getStringExtra(STATUS_KEY) == getString(R.string.failed))
            bindingContent.textViewDetailStatus.setTextColor(Color.RED)
        else
            bindingContent.textViewDetailStatus.setTextColor(Color.GREEN)

        bindingContent.button.setOnClickListener {
            finish()
        }
    }
}

package com.kkyoungs.elbum

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.kkyoungs.elbum.databinding.ActivityFrameBinding

class FrameActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFrameBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFrameBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}
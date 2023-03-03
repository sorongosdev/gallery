package com.sorongos.gallery

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sorongos.gallery.databinding.ActivityFrameBinding

class FrameActivity : AppCompatActivity() {
    private lateinit var binding : ActivityFrameBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFrameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //리스트를 인텐트를 통해 받음
        //받아온 데이터를 다시 리스트로 만듦
        val images = intent.getStringArrayExtra("images")
    }
}
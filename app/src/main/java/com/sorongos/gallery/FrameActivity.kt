package com.sorongos.gallery

import android.net.Uri
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
        val images = (intent.getStringArrayExtra("images") ?: emptyArray())
            //uri가 String으로 바뀌어있음, 다시 uri로 바꾸어야함
            .map { uriString -> FrameItem(Uri.parse(uriString))}

        val frameAdapter = FrameAdapter(images)

        binding.viewPager.adapter = frameAdapter
    }
}
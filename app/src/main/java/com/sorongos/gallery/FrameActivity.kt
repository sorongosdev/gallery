package com.sorongos.gallery

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayoutMediator
import com.sorongos.gallery.databinding.ActivityFrameBinding

class FrameActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFrameBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFrameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.apply {
            title = "나만의 앨범"
            setSupportActionBar(this)
        }
        //뒤로가기 버튼
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //리스트를 인텐트를 통해 받음
        //받아온 데이터를 다시 리스트로 만듦
        val images = (intent.getStringArrayExtra("images") ?: emptyArray())
            //uri가 String으로 바뀌어있음, 다시 uri로 바꾸어야함
            .map { uriString -> FrameItem(Uri.parse(uriString)) }

        val frameAdapter = FrameAdapter(images)
        binding.viewPager.adapter = frameAdapter

        /**탭으로 몇번째 사진인지 확인 가능*/
        TabLayoutMediator(
            binding.tabLayout,
            binding.viewPager,
        ) { tab, position ->
            binding.viewPager.currentItem = tab.position
        }.attach()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                Log.d("frame", "back button clicked")
                true
            }
            else -> {
                Log.d("frame", "else")
                super.onOptionsItemSelected(item)
            }
        }
    }
}
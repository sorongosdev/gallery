package com.sorongos.gallery

import android.Manifest
import android.content.ContentValues.TAG
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.sorongos.gallery.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loadImageButton.setOnClickListener {
//            checkPermission()
            Log.d(TAG,"clicked")
            ActivityCompat.requestPermissions(this,
            arrayOf(Manifest.permission.READ_MEDIA_IMAGES),
            REQUEST_READ_EXTERNAL_STORAGE)
        }
    }

    private fun checkPermission() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_MEDIA_IMAGES
            ) == PackageManager.PERMISSION_GRANTED -> {
                loadImage()
            }
            shouldShowRequestPermissionRationale(
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) -> {
                showPermissionInfoDialog()
            }
            else -> {
                requestReadExternalStorage()
            }
        }
    }
    private fun showPermissionInfoDialog() {
        AlertDialog.Builder(this).apply {
            setMessage("이미지를 가져오기 위해서, 외부 저장소 읽기 권한이 필요합니다.")
            setNegativeButton("취소", null)
            setPositiveButton("동의") { _, _ ->
                requestReadExternalStorage()
            }
        }.show()
    }
    private fun requestReadExternalStorage() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
            REQUEST_READ_EXTERNAL_STORAGE
        )
    }
    private fun loadImage() {
        Log.d(TAG,"loadImage")
    }

    /**request 코드*/
    companion object {
        const val REQUEST_READ_EXTERNAL_STORAGE = 100
    }
}
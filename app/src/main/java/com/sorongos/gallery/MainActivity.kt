package com.sorongos.gallery

import android.Manifest
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.GridLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.sorongos.gallery.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var imageAdapter: ImageAdapter

    /**사진을 찍어서, 비디오를 찍어서 클립을 가져올 수 있음
     * startactivity : 다른 앱에서 열기
     * 사진을 여러 장 받아옴
     * */
    private val imageLoadLauncher =
        registerForActivityResult(ActivityResultContracts.GetMultipleContents()) { uriList ->
            //urilist로 통해 이미지 업데이트
            updateImages(uriList)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.apply{
            title = "사진 가져오기"
            setSupportActionBar(this)
        }

        binding.loadImageButton.setOnClickListener {
            checkPermission()
        }

        binding.navigateFrameActivityButton.setOnClickListener {
            navigate2FrameActivity()
        }
        initRecyclerView()
    }

    /**메뉴를 만들어야함*/
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    /**메뉴 클릭시 동작*/
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when(item.itemId){
            R.id.action_add -> {
                checkPermission()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    private fun navigate2FrameActivity() {
        //FrameActvitt로 넘길 리스트를 뽑아줌
        val images =
            imageAdapter.currentList.filterIsInstance<ImageItems.Image>().map { it.uri.toString() }.toTypedArray()
        val intent = Intent(this, FrameActivity::class.java)
                //string array를 넣음
            .putExtra("images", images)
        startActivity(intent)
    }

    /**리사이클러뷰 초기화*/
    private fun initRecyclerView() {
        imageAdapter = ImageAdapter(object : ImageAdapter.ItemClickListener {
            override fun onLoadMoreClick() {
                checkPermission()
            }
        })
        binding.imageRecyclerView.apply {
            adapter = imageAdapter
            layoutManager = GridLayoutManager(context, 2)
        }
    }

    /**uri를 통해서 이미지를 보여줄 수 있다.*/
    private fun updateImages(uriList: List<Uri>) {
        Log.i("updateImages", "$uriList")
        //it -> uri
        val images = uriList.map { ImageItems.Image(it) }

        val updateImages = imageAdapter.currentList.toMutableList().apply { addAll(images) }

        //notifydatasetChanged랑 유사
        //리스트가 아예 갱신
        imageAdapter.submitList(updateImages)
    }

    /**권한을 물어보고, 버튼을 다시 눌러야하는 수고 없이 allow 누르면 바로 실행*/
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            REQUEST_READ_EXTERNAL_STORAGE -> { // 코드가 일치하고, 권한 허용이 되었다면 바로 실행
                // ?: 연산자 - null일 때 오른쪽에 있는 걸로 디폴트값을 정해줌
                val resultCode = grantResults.firstOrNull() ?: PackageManager.PERMISSION_GRANTED
                if (resultCode == PackageManager.PERMISSION_GRANTED) {
                    loadImage()
                }
            }
        }
    }

    private fun checkPermission() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_MEDIA_IMAGES
            ) == PackageManager.PERMISSION_GRANTED -> {
                //권한 모두 승인
                loadImage()
            }
            shouldShowRequestPermissionRationale(
                Manifest.permission.READ_MEDIA_IMAGES
            ) -> {
                //권한 승인 되기 전 권한에 대한 설명
                showPermissionInfoDialog()
            }
            else -> {
                //권한 승인 되기 전, 안드로이드 자체 권한 승인 팝업
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
            arrayOf(android.Manifest.permission.READ_MEDIA_IMAGES),
            REQUEST_READ_EXTERNAL_STORAGE
        )
    }

    /**모든 이미지 타입을 가져옴
     * saf 기능을 이용할 때 MIME 타입을 이용해 이미지 파일을 불러 오겠다*/
    private fun loadImage() {
        imageLoadLauncher.launch("image/*")
    }

    /**request 코드*/
    companion object {
        const val REQUEST_READ_EXTERNAL_STORAGE = 100
    }
}
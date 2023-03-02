package com.sorongos.gallery

import android.net.Uri
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView


/**리사이클러뷰 어댑터가 아닌 리스트 어댑터를 사용
 * 1. list adapter
 * 2. 다양한 타입의 아이템을 넣어본다
 * 리사이클러뷰와 어댑터의 차이
 * 변경에 대한 처리
 * */
class ImageAdapter : ListAdapter<ImageItems, RecyclerView.ViewHolder>(
    /**listadapter에 들어가야함
     * 데이터가 바뀌었을 때 알아서 콜함
     */
    object : DiffUtil.ItemCallback<ImageItems>() {
        override fun areItemsTheSame(oldItem: ImageItems, newItem: ImageItems): Boolean {
            return oldItem === newItem // 같은 객체를 참조하는가?
        }

        override fun areContentsTheSame(oldItem: ImageItems, newItem: ImageItems): Boolean {
            return oldItem == newItem // 같은 값인가?
        }
    }
) {

    override fun getItemCount(): Int {
        val originSize = currentList.size
        return if (originSize == 0) 0 else originSize.inc()
    }

    /**두 가지 타입이 생겨 체킹 부분 필요, 뷰타입 지정*/
    override fun getItemViewType(position: Int): Int {
        //마지막 포지션이면 푸터를 넣어줘야함
        return if(itemCount.dec() == position) ITEM_LOAD_MORE else ITEM_IMAGE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    companion object {
        const val ITEM_IMAGE = 0
        const val ITEM_LOAD_MORE = 1
    }
}

sealed class ImageItems {
    data class Image(
        val uri: Uri,
    ) : ImageItems()

    //바로 객체가 만들어짐
    object LoadMore : ImageItems()
}

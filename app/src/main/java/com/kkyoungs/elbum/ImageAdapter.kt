package com.kkyoungs.elbum

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kkyoungs.elbum.databinding.ItemImageBinding
import com.kkyoungs.elbum.databinding.ItemLoadMoreBinding

// ListAdpater은 차이점을 알아서 계산해줌
class ImageAdapter(private val itemClickListener : ItemClickListener)  : ListAdapter<ImageItems ,RecyclerView.ViewHolder>(
    // diffUtil :  oldItem, newItem의 두 데이터셋을 비교하여 값이 변경된 부분만을 RecyclerView에게 알려줄 수 있다.
    // 목록이 크면, 작업에 상당한 시간이 걸릴 수 있으므로 백그라운드 스레드에서 실행 하면 좋다.

    // areItemsTheSame : 비교대상인 두객체가 동일한지 확인
    // areContentsTheSame : 두 아이템이 동일한 데이터를 가지는지 확인
    object :DiffUtil.ItemCallback<ImageItems>(){
        override fun areItemsTheSame(oldItem: ImageItems, newItem: ImageItems): Boolean {
            // 똑같은 item을 참조하는지
            return oldItem === newItem
        }
        override fun areContentsTheSame(oldItem: ImageItems, newItem: ImageItems): Boolean {
            // 똑같은 데이터를 참조하는지
            return oldItem == newItem
        }
    }
){

    override fun getItemCount(): Int {
        val originSize = currentList.size
        return if (originSize == 0) 0 else originSize.inc()
    }

    override fun getItemViewType(position: Int): Int {
        return  if (itemCount.dec() == position) ITEM_LOAD_MORE else ITEM_IMAGE
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        return when(viewType){
            ITEM_IMAGE->{
                val binding = ItemImageBinding.inflate(inflater, parent, false)
                ImageViewHolder(binding)
            }
            else ->{
                val binding = ItemLoadMoreBinding.inflate(inflater, parent, false)
                LoadMoreViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is ImageViewHolder -> {
                holder.bind(currentList[position] as ImageItems.Image)
            }
            is LoadMoreViewHolder -> {
                holder.bind(itemClickListener)
            }

        }
    }

    interface ItemClickListener{
        fun onLoadMoreClick()
    }
    companion object{
        const val ITEM_IMAGE = 0
        const val ITEM_LOAD_MORE = 1
    }

}

// 같은 class를 사용할 때 sealed 사용하면 한번에 할 수 있 다

sealed class ImageItems{
    data class Image(
        val uri : Uri,
    ): ImageItems()
    object LoadMore : ImageItems()
}

class ImageViewHolder(private val binding : ItemImageBinding) :RecyclerView.ViewHolder(binding.root){
    fun bind(item :ImageItems.Image){
        binding.previewImageView.setImageURI(item.uri)
    }
}

class LoadMoreViewHolder(binding : ItemLoadMoreBinding) :RecyclerView.ViewHolder(binding.root){
    fun bind(itemClickLister : ImageAdapter.ItemClickListener){
        itemView.setOnClickListener { itemClickLister.onLoadMoreClick() }
    }
}

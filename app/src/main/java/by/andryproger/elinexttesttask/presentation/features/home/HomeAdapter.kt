package by.andryproger.elinexttesttask.presentation.features.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import by.andryproger.elinexttesttask.R
import by.andryproger.elinexttesttask.presentation.util.DiffUtilCallback
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.item_img.view.*

class MainAdapter : RecyclerView.Adapter<ImgViewHolder>() {

    private val items = mutableListOf<ImageState>()

    fun update(newItems: List<ImageState>) {
        val diffResult = DiffUtil.calculateDiff(DiffUtilCallback(items, newItems) { itemId })
        items.clear()
        items.addAll(newItems)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ImgViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_img, parent, false)

        return ImgViewHolder(view)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ImgViewHolder, position: Int) {
        holder.update(items[position])
    }
}

class ImgViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    fun update(state: ImageState) {
        Glide.with(itemView.context).load(state.link)
            .apply(RequestOptions.placeholderOf(R.drawable.place_holder))
            .into(itemView.imageView)
    }
}
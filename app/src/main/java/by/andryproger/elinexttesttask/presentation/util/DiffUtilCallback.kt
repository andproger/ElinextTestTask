package by.andryproger.elinexttesttask.presentation.util

import androidx.recyclerview.widget.DiffUtil

class DiffUtilCallback<T, ID>(
        private val oldItems: List<T>,
        private val newItems: List<T>,
        private val getId: T.() -> ID
) : DiffUtil.Callback() {

    override fun getNewListSize() = newItems.size

    override fun getOldListSize() = oldItems.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldItems[oldItemPosition].getId() == newItems[newItemPosition].getId()

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldItems[oldItemPosition] == newItems[newItemPosition]

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int) = Any()
}
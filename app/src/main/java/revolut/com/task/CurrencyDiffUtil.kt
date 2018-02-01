package revolut.com.task

import android.support.v7.util.DiffUtil
import com.revolut.data.Currency

/**
 *
 * Created on 01.02.2018.
 */
class CurrencyDiffUtil(val old: List<Currency>, val newValues: List<Currency>) : DiffUtil.Callback() {
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return old[oldItemPosition].key == newValues[newItemPosition].key
    }

    override fun getOldListSize(): Int {
        return old.size
    }

    override fun getNewListSize(): Int {
        return newValues.size
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return old[oldItemPosition].contentSame(newValues[newItemPosition])
    }
}
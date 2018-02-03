package revolut.com.task

import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import com.revolut.data.Currency
import java.text.NumberFormat
import java.text.ParseException

/**
 * Adapter for displaying list of the currencies.
 *
 * Created on 01.02.18.
 */
class CurrencyAdapter : RecyclerView.Adapter<CurrencyAdapter.CurrencyViewHolder>() {
    var currencies: MutableList<Currency> = mutableListOf()
    var enteredValue: Double? = 0.0
    var nf = NumberFormat.getInstance()
    var interactionListener: InteractionListener? = null
    private var baseCountView: EditText? = null

    private val listener = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (!s.isNullOrEmpty()) {
                try {
                    enteredValue = nf.parse(s.toString()).toDouble()
                    // update all items except the first one
                    notifyItemRangeChanged(1, itemCount - 1)
                } catch (nfe: ParseException) {
                    nfe.printStackTrace()
                }
            }
        }
    }

    private val clickListener = View.OnClickListener { v ->
        val tag = v!!.tag

        // request focus for item that is selected as base
        val futureBaseView = v.findViewById<EditText>(R.id.count)
        futureBaseView.requestFocus()
        futureBaseView.setSelection(futureBaseView.length())

        if (tag is Currency) {
            interactionListener?.itemClicked(tag)
            enteredValue = tag.value

            if (baseCountView != futureBaseView) {
                // update text watchers
                baseCountView?.removeTextChangedListener(listener)
                futureBaseView.addTextChangedListener(listener)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): CurrencyViewHolder {
        val inflater = LayoutInflater.from(parent?.context)
        val view = inflater.inflate(R.layout.layout_currency_item, parent, false)
        return CurrencyViewHolder(view, clickListener)
    }

    override fun onViewRecycled(holder: CurrencyViewHolder?) {
        with(holder!!) {
            // remove text watcher in case view is recycled
            if (adapterPosition == 0) {
                count.removeTextChangedListener(listener)
            }
        }
        super.onViewRecycled(holder)
    }

    override fun getItemId(position: Int): Long {
        return currencies[position].key.hashCode().toLong()
    }

    override fun onBindViewHolder(holder: CurrencyViewHolder?, position: Int) {
        val item = currencies[position]
        item.value = if (position == 0) enteredValue else item.multiplier * enteredValue!!
        with(holder!!) {
            name.text = item.key
            count.tag = item
            parent.tag = item
            count.post { count.setText(nf.format(item.value)) }
            if (position == 0) {
                count.addTextChangedListener(listener)
                baseCountView = count
            }
        }
    }

    override fun getItemCount(): Int {
        return currencies.size
    }

    inner class CurrencyViewHolder(view: View, listener: View.OnClickListener) : RecyclerView.ViewHolder(view) {
        val count: EditText = view.findViewById(R.id.count)
        val name: TextView = view.findViewById(R.id.name)
        val parent: ViewGroup = view.findViewById(R.id.parent_layout)

        init {
            parent.setOnClickListener(listener)
            count.setOnClickListener(listener)
        }

    }

    interface InteractionListener {
        fun itemClicked(item: Currency)
    }
}
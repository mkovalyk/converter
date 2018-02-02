package revolut.com.task

import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import com.revolut.data.Currency
import java.text.NumberFormat
import java.text.ParseException

/**
 * Created on 01.02.18.
 */
class CurrencyAdapter : RecyclerView.Adapter<CurrencyAdapter.CurrencyViewHolder>() {
    private val TAG = CurrencyAdapter::class.java.simpleName
    var currencies: MutableList<Currency> = mutableListOf()
    var enteredValue: Double? = 0.0

    val listener = object : LightWeightTextWatcher() {
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (!s.isNullOrEmpty()) {
                try {
                    enteredValue = nf.parse(s.toString()).toDouble()
                    Log.d(TAG, "onTextChanged: $enteredValue")
                    notifyItemRangeChanged(1, itemCount - 1)
                } catch (nfe: ParseException) {
                    nfe.printStackTrace()
                }
            }
        }
    }
    val clickListener = View.OnClickListener { v ->
        val tag = v!!.tag
        if (tag is Currency) {
            interactionListener?.itemClicked(tag)
            enteredValue = tag.value
            Log.d(TAG, "Click $enteredValue")
        }
    }
    var nf = NumberFormat.getInstance()

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): CurrencyViewHolder {
        val inflater = LayoutInflater.from(parent?.context)
        val view = inflater.inflate(R.layout.layout_currency_item, parent, false)
        return CurrencyViewHolder(view, clickListener)
    }

    override fun onViewRecycled(holder: CurrencyViewHolder?) {
        with(holder!!) {
            if (adapterPosition == 0) {
                count.removeTextChangedListener(listener)
            }
        }
        super.onViewRecycled(holder)

    }

    var interactionListener: InteractionListener? = null

    interface InteractionListener {
        fun itemClicked(item: Currency)
    }

    override fun getItemId(position: Int): Long {
        return currencies[position].key.hashCode().toLong()
    }

    override fun onBindViewHolder(holder: CurrencyViewHolder?, position: Int) {
        val item = currencies[position]
        item.value = if (position == 0) enteredValue else item.multiplier * enteredValue!!
        with(holder!!) {
//            Log.d(TAG, "Bind : item. $enteredValue + $item")
            name.text = item.key
            count.tag = item
            parent.tag = item
            count.post { count.setText(nf.format(item.value)) }
            if (position == 0) {
                count.addTextChangedListener(listener)
            }
        }
    }

    override fun getItemCount(): Int {
        return currencies.size
    }

    inner class CurrencyViewHolder : RecyclerView.ViewHolder {
        val count: EditText
        val name: TextView
        val parent: ViewGroup

        constructor(view: View, listener: View.OnClickListener) : super(view) {
            this.count = view.findViewById(R.id.count)
            this.name = view.findViewById(R.id.name)
            this.parent = view.findViewById(R.id.parent_layout)
            parent.setOnClickListener(listener)
            count.setOnClickListener(listener)
        }
    }

    inner abstract class LightWeightTextWatcher : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }
    }
}
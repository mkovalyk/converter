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

/**
 * Created on 01.02.18.
 */
class CurrencyAdapter : RecyclerView.Adapter<CurrencyAdapter.CurrencyViewHolder>() {
    private val TAG = CurrencyAdapter::class.java.simpleName
    var currencies: List<Currency> = ArrayList()
    var enteredValue: Double = 0.0
    val listener = object : LightWeightTextWatcher() {
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (!s.isNullOrEmpty()) {
                try {
                    val value = s.toString().toDouble()
                    enteredValue = value
                    notifyItemRangeChanged(1, itemCount - 1)
                } catch (nfe: NumberFormatException) {
                    nfe.printStackTrace()
                }
            }
        }
    }
    val clickListener = object : View.OnClickListener {
        override fun onClick(v: View?) {
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): CurrencyViewHolder {
        val inflater = LayoutInflater.from(parent?.context)
        val view = inflater.inflate(R.layout.layout_currency_item, parent, false)
        return CurrencyViewHolder(view)
    }

    override fun onViewRecycled(holder: CurrencyViewHolder?) {
        with(holder!!) {
            if (adapterPosition == 0) {
                count.removeTextChangedListener(listener)
            }
        }
        super.onViewRecycled(holder)

    }

    override fun onBindViewHolder(holder: CurrencyViewHolder?, position: Int) {
        val item = currencies[position]
        val value = if (position == 0) enteredValue else enteredValue * item.multiplier
        with(holder!!) {
            Log.d(TAG, "Bind at position $position. Value: $item. Count of currency: $enteredValue")
            name.text = item.key
            count.isEnabled = position == 0

            count.setText(String.format("%.3f", value))
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

        constructor(view: View) : super(view) {
            this.count = view.findViewById(R.id.count)
            this.name = view.findViewById(R.id.name)
        }
    }

    inner abstract class LightWeightTextWatcher : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }
    }
}
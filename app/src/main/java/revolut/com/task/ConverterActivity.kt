package revolut.com.task

import android.arch.lifecycle.ViewModelProviders
import android.arch.persistence.room.Room
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.support.v7.util.DiffUtil
import android.widget.Toast
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import com.revolut.data.*
import kotlinx.android.synthetic.main.activity_converter.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class ConverterActivity : AppCompatActivity() {
    private val model: CurrencyViewModel by lazy {
        ViewModelProviders.of(this).get(CurrencyViewModel::class.java)
    }
    private val currencyAdapter = CurrencyAdapter()
    private val handler: Handler = Handler()
    private val refresh = Runnable {
        model.getCurrencies(currentBaseCurrency, false)
    }
    val view = object : CurrencyView {
        override fun onError(error: Throwable) {
            Toast.makeText(this@ConverterActivity, error.localizedMessage, Toast.LENGTH_LONG).show()
        }

        override fun add(currency: Currency) {
            with(currencyAdapter)
            {
                val index = currencies.indexOf(currency)
                when (index) {
                    in 1 until currencies.size -> {
                        currencies[index] = currency
                        notifyItemChanged(index)
                    }
                    0 -> {
                    }// do nothing
                    else -> {
                        currencies.add(currency)
                        notifyItemInserted(currencies.size - 1)
                    }
                }
            }
            newList.add(currency)
        }

        override fun loadingStarted() {
        }

        override fun loadingCompleted() {
            handler.removeCallbacks(refresh)
            handler.postDelayed(refresh, C.REFRESH_DELAY)
        }
    }
    private var currentBaseCurrency: Currency = Currency("EUR", position = 0)
    var newList = mutableListOf<Currency>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_converter)

        // TODO extract into separate class. Dagger 2?
        val db = Room.databaseBuilder(applicationContext, RevolutDatabase::class.java, "db").build()
        val retrofit = Retrofit.Builder()
                .baseUrl("https://revolut.duckdns.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
        val service = retrofit.create(CurrencyWebService::class.java)
        val schedulersFactory = AppSchedulesFactory()

        model.presenter = CurrencyPresenter(db.currencyDao(), service, schedulersFactory, view)
        model.getCurrencies(currentBaseCurrency, true)
        recyclerView.setHasFixedSize(true)

        currencyAdapter.setHasStableIds(true)
        // restore state after screen rotation
        currencyAdapter.enteredValue = model.currencyCount
        currencyAdapter.interactionListener = object : CurrencyAdapter.InteractionListener {
            override fun itemClicked(item: Currency) {
                if (item == currentBaseCurrency) {
                    return
                }
                currentBaseCurrency = Currency(item.key)

                with(currencyAdapter) {
                    // shift selected currency to top of list. All other shift by one down
                    val newList = ArrayList(currencies)

                    val index = currencies.indexOf(item)
                    for (i in 0 until currencies.size) {
                        newList[i].multiplier /= item.multiplier
                        when (i) {
                        // increase position only for items above selected.
                            in 0 until index -> newList[i].position++
                            index -> {
                                newList[i].position = 0
                                newList[i].multiplier = 1.0
                            }
                        }
                    }
                    newList.sortBy { it.position }

                    // evaluate diffs for better performance
                    val diffResult = DiffUtil.calculateDiff(CurrencyDiffUtil(currencyAdapter.currencies, newList))
                    currencyAdapter.currencies = newList
                    diffResult.dispatchUpdatesTo(currencyAdapter)

                    // make sure user is on top of the list
                    recyclerView.scrollToPosition(0)
                }
            }
        }
        recyclerView.adapter = currencyAdapter
    }

    override fun onDestroy() {
        super.onDestroy()
        model.currencyCount = currencyAdapter.enteredValue
        handler.removeCallbacks(refresh)
    }
}

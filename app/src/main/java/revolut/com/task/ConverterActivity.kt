package revolut.com.task

import android.arch.lifecycle.ViewModelProviders
import android.arch.persistence.room.Room
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.support.v7.util.DiffUtil
import android.util.Log
import android.widget.Toast
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import com.revolut.data.*
import kotlinx.android.synthetic.main.activity_converter.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class ConverterActivity : AppCompatActivity() {
    val TAG = ConverterActivity::class.java.simpleName
    private val model: CurrencyViewModel by lazy {
        ViewModelProviders.of(this).get(CurrencyViewModel::class.java)
    }
    private val currencyAdapter = CurrencyAdapter()
    private val handler: Handler = Handler()
    private var currentBaseCurrency: Currency = Currency("EUR", position = 0)
    private val refresh = Runnable {
        model.getCurrencies(currentBaseCurrency, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_converter)
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
        currencyAdapter.enteredValue = model.currencyCount
        currencyAdapter.interactionListener = object : CurrencyAdapter.InteractionListener {
            override fun itemClicked(item: Currency) {
                currentBaseCurrency = Currency(item.key)

                with(currencyAdapter) {
                    val newList = ArrayList(currencies)

                    val index = currencies.indexOf(item)
                    for (i in 0 until currencies.size) {
                        newList[i].multiplier /= item.multiplier
                        when (i) {
                            in 0 until index -> newList[i].position++
                            index -> newList[i].position = 0
                        }
                    }
                    newList.sortBy { it.position }

                    val diffResult = DiffUtil.calculateDiff(CurrencyDiffUtil(currencyAdapter.currencies, newList))
                    currencyAdapter.currencies = newList
                    diffResult.dispatchUpdatesTo(currencyAdapter)
                    recyclerView.scrollToPosition(0)
                    Log.d(TAG, "Values:$currencies")
                }

//                model.getCurrencies(currentBaseCurrency, false)
//                recyclerView.scrollToPosition(0)
            }
        }
        recyclerView.adapter = currencyAdapter
    }

    var newList = mutableListOf<Currency>()

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
                if (index > 0) {
                } else {
                }
            }
            newList.add(currency)
        }

        override fun loadingStarted() {
//            newList = mutableListOf()
        }

        override fun loadingCompleted() {
//            val diffResult = DiffUtil.calculateDiff(CurrencyDiffUtil(currencyAdapter.currencies, newList))
//            currencyAdapter.currencies = newList
//            diffResult.dispatchUpdatesTo(currencyAdapter)

            handler.removeCallbacks(refresh)
            handler.postDelayed(refresh, C.REFRESH_DELAY)
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        model.currencyCount = currencyAdapter.enteredValue
        handler.removeCallbacks(refresh)
    }
}

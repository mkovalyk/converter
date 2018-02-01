package revolut.com.task

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.arch.persistence.room.Room
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.support.v7.util.DiffUtil
import android.util.Log
import com.revolut.data.Currency
import com.revolut.data.CurrencyRepository
import com.revolut.data.CurrencyWebService
import com.revolut.data.RevolutDatabase
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
    private var currentBaseCurrency: Currency = Currency("EUR")
    private val refresh = Runnable {
        model.getCurrencies(currentBaseCurrency)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_converter)
        val db = Room.databaseBuilder(applicationContext, RevolutDatabase::class.java, "db").build()
        val retrofit = Retrofit.Builder()
                .baseUrl("https://revolut.duckdns.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        val service = retrofit.create(CurrencyWebService::class.java)
        model.repository = CurrencyRepository(db.currencyDao(), service)
        model.getCurrencies(currentBaseCurrency)
        recyclerView.adapter = currencyAdapter
        model.liveData.observe(this, Observer { changed ->
            Log.d(TAG, "Value is changed:")
            val diffResult = DiffUtil.calculateDiff(CurrencyDiffUtil(currencyAdapter.currencies, changed!!))
            currencyAdapter.currencies = changed
            diffResult.dispatchUpdatesTo(currencyAdapter)
            
            handler.removeCallbacks(refresh)
            handler.postDelayed(refresh, C.REFRESH_DELAY)
        })
        button.setOnClickListener { model.getCurrencies(currentBaseCurrency) }
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(refresh)
    }
}

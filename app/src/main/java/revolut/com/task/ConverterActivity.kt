package revolut.com.task

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.revolut.data.CurrencyWebService
import kotlinx.android.synthetic.main.activity_converter.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class ConverterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_converter)
        button.setOnClickListener { request() }
    }

    fun request() {
        Thread(Runnable {
            val retrofit = Retrofit.Builder()
                    .baseUrl("https://api.fixer.io/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            val service = retrofit.create(CurrencyWebService::class.java)
            val call = service.latestCurrencies("EUR").execute()
            val result = call.body()
            Log.d("ConverterActivity", "Result: ${result?.rates?.size}")
        }).start()

    }
}

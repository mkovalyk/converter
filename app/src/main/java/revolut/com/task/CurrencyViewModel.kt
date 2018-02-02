package revolut.com.task

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.revolut.data.Currency
import com.revolut.data.CurrencyRepository
import kotlinx.coroutines.experimental.runBlocking

/**
 * Created on 27.01.2018.
 */
class CurrencyViewModel : ViewModel() {
    val liveData: MutableLiveData<List<Currency>> = MutableLiveData()
    var repository: CurrencyRepository? = null
    var currencyCount: Double? = 0.0

    fun getCurrencies(baseCurrency: Currency, loadFromLocal: Boolean) {
        runBlocking { repository?.getCurrencies(baseCurrency, liveData, loadFromLocal) }
    }
}
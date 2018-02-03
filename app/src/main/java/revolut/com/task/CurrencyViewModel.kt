package revolut.com.task

import android.arch.lifecycle.ViewModel
import com.revolut.data.Currency
import com.revolut.data.CurrencyPresenter

/**
 * ViewModel for Currency screen.
 *
 * Created on 27.01.2018.
 */
class CurrencyViewModel : ViewModel() {
    var presenter: CurrencyPresenter? = null
    var currencyCount: Double? = 0.0

    fun getCurrencies(baseCurrency: Currency, loadFromLocal: Boolean) {
        presenter?.getCurrencies(baseCurrency, loadFromLocal)
    }
}
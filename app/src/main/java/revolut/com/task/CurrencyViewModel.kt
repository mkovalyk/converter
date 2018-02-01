package revolut.com.task

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.revolut.data.Currency
import com.revolut.data.CurrencyRepository

/**
 * Created on 27.01.2018.
 */
class CurrencyViewModel(val repository: CurrencyRepository) : ViewModel() {
    val liveData: MutableLiveData<List<Currency>> = MutableLiveData()


    fun getCurrencies(baseCurrency:Currency)
    {
        liveData.value = repository.getCurrencies(baseCurrency)
    }
}
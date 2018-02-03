package revolut.com.task

import com.revolut.data.SchedulersFactory
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Android implementation of the schedulers factory.
 *
 * Created on 02.02.2018.
 */
class AppSchedulesFactory : SchedulersFactory {
    override fun background(): Scheduler = Schedulers.io()

    override fun main(): Scheduler = AndroidSchedulers.mainThread()
}
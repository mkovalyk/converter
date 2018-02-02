package revolut.com.task

import java.util.concurrent.TimeUnit

/**
 * General class for storing values common for whole app.
 *
 * Created on 01.02.2018.
 */
class C {
    companion object {
        val REFRESH_DELAY = TimeUnit.SECONDS.toMillis(3)
    }
}
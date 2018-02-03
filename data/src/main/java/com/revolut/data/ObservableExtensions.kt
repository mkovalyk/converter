package com.revolut.data

import io.reactivex.disposables.Disposable

/**
 * Extension functions for Rx classes.
 *
 * Created on 03.02.2018.
 */
fun Disposable?.safeDispose() {
    if (this == null) return
    if (!this.isDisposed) {
        this.dispose()
    }
}
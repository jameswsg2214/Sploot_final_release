package com.work.sploot.rx

/**
 * Created by  on 7/15/2017.
 */
interface RxAPICallback<P> {
    fun onSuccess(newsItems: P)

    fun onFailed(throwable: Throwable)
}
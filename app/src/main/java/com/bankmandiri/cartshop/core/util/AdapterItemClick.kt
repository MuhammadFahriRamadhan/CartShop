package com.bankmandiri.cartshop.core.util

abstract class AdapterItemClickListener<T> {
    abstract fun onTransactionItemClick(failure: T)
}
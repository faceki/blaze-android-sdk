package com.faceki.android.domain.provider

interface TokenProvider {
    fun getToken(): String?
    fun getTokenTimestamp(): Long
}

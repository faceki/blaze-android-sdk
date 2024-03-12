package com.faceki.android.domain.repository

import com.faceki.android.util.Resource

interface TokenRepository {
    suspend fun getBearerToken(clientId: String, clientSecret: String): Resource<String>
    fun getTokenTimestamp(): Long
}
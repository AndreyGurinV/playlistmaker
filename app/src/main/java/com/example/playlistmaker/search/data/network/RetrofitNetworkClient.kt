package com.example.playlistmaker.search.data.network

import com.example.playlistmaker.search.data.NetworkClient
import com.example.playlistmaker.search.data.dto.Response
import com.example.playlistmaker.search.data.dto.TracksSearchRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RetrofitNetworkClient(
    private val itunesAPI: ItunesAPI
): NetworkClient {

    override suspend fun doRequest(dto: Any): Response {
        if (dto is TracksSearchRequest) {
            return withContext(Dispatchers.IO){
                try {
                    val resp = itunesAPI.search(dto.expression)
                    resp.apply { resultCode = 200 }
                } catch (e: Throwable){
                    Response().apply { resultCode = 400 }
                }
            }
        } else {
            return Response().apply { resultCode = 400 }
        }
    }

}
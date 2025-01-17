package com.example.playlistmaker.search.data.network

import com.example.playlistmaker.search.data.NetworkClient
import com.example.playlistmaker.search.data.dto.Response
import com.example.playlistmaker.search.data.dto.TracksSearchRequest
import java.io.IOException

class RetrofitNetworkClient(
    private val itunesAPI: ItunesAPI
): NetworkClient {

    override fun doRequest(dto: Any): Response {
        if (dto is TracksSearchRequest) {
            try {
                val resp = itunesAPI.search(dto.expression).execute()
                val body = resp.body() ?: Response()

                return body.apply { resultCode = resp.code() }
            } catch (e: IOException) {
                return Response().apply { resultCode = 400 }
            }
        } else {
            return Response().apply { resultCode = 400 }
        }
    }

}
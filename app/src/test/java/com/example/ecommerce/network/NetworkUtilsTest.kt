package com.example.ecommerce.network

import okhttp3.mockwebserver.MockResponse
import java.io.InputStreamReader

object NetworkUtilsTest {

    fun createStringFromFile(fileName: String): String {
        val inputstream = javaClass.classLoader?.getResourceAsStream(fileName)
        val inputStreamReader = InputStreamReader(inputstream)
        return inputStreamReader.readText()
    }

    fun createMockResponse(fileName: String) = MockResponse()
        .setResponseCode(200)
        .setBody(createStringFromFile(fileName))

}
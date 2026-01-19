package com.example.practicaltest02v3

import android.util.Log

//import cz.msebera.android.httpclient.NameValuePair
//import cz.msebera.android.httpclient.client.HttpClient
//import cz.msebera.android.httpclient.client.ResponseHandler
//import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity
//import cz.msebera.android.httpclient.client.methods.HttpGet
//import cz.msebera.android.httpclient.client.methods.HttpPost
//import cz.msebera.android.httpclient.impl.client.BasicResponseHandler
//import cz.msebera.android.httpclient.impl.client.DefaultHttpClient
//import cz.msebera.android.httpclient.message.BasicNameValuePair
//import cz.msebera.android.httpclient.protocol.HTTP
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
//import org.jsoup.Jsoup
//import org.jsoup.nodes.Document
//import org.jsoup.nodes.Element
//import org.jsoup.select.Elements
import java.io.BufferedReader
import java.io.IOException
import java.io.PrintWriter
import java.net.Socket
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response

class CommunicationThread(private val serverThread: ServerThread, private val socket: Socket) :
    Thread() {
    override fun run() {
        if (socket == null) {
            Log.e(Constants.TAG, "[COMMUNICATION THREAD] Socket is null!")
            return
        }
        try {
            val bufferedReader: BufferedReader? = Utilities.getReader(socket)
            val printWriter: PrintWriter? = Utilities.getWriter(socket)
            if (bufferedReader == null || printWriter == null) {
                Log.e(
                    Constants.TAG,
                    "[COMMUNICATION THREAD] Buffered Reader / Print Writer are null!"
                )
                return
            }
            Log.i(
                Constants.TAG,
                "[COMMUNICATION THREAD] Waiting for parameters from client (city / information type!"
            )
            val word = bufferedReader.readLine()
            if (word == null || word.isEmpty()) {
                Log.e(
                    Constants.TAG,
                    "[COMMUNICATION THREAD] Error receiving parameters from client (city / information type!"
                )
                return
            }
            Log.i(Constants.TAG, "[COMMUNICATION THREAD] Word is $word")

            Log.i(
                Constants.TAG,
                "[COMMUNICATION THREAD] Getting the information from the webservice..."
            )

            val client = OkHttpClient()
            val request = Request.Builder()
                .url(Constants.WEB_SERVICE_ADDRESS + word)
                .get()
                .build()
            val response = client.newCall(request).execute()

            if (!response.isSuccessful) {
                Log.e(
                    Constants.TAG,
                    "[COMMUNICATION THREAD] HTTP error: ${response.code}"
                )
                return
            }

            val pageSourceCode = response.body?.string()

            if (pageSourceCode == null) {
                Log.e(
                    Constants.TAG,
                    "[COMMUNICATION THREAD] Empty response body!"
                )
                return
            }

            Log.i(Constants.TAG, "[COMMUNICATION THREAD] PageSourceCode: $pageSourceCode")

//            val content = JSONObject(pageSourceCode)
//
//            // main object
//            val main = content.getJSONObject("0")
//            Log.i(Constants.TAG, "[COMM_THREAD] main $main")
//            val meanings = main.getJSONObject("meanings")
//            Log.i(Constants.TAG, "[COMM_THREAD] meanings $meanings")
//            val sub_main = meanings.getJSONObject("0")
//            Log.i(Constants.TAG, "[COMM_THREAD] sub_main $sub_main")
//            val defs = sub_main.getJSONObject("definitions")
//            Log.i(Constants.TAG, "[COMM_THREAD] defs $defs")
//            val last_main = defs.getJSONObject("0")
//            Log.i(Constants.TAG, "[COMM_THREAD] last_main $last_main")
//            val definition = last_main.getString("definition").toString()
            val definition = pageSourceCode.split("\"definition\":\"", "\",\"synonyms\"")[1]

//            Log.i(Constants.TAG, "[COMMUNICATION THREAD] main = $main | meanings = $meanings | sub_main = $sub_main | defs = $defs | last_main = $last_main | definition = $definition")

            var result: String = definition

            Log.i(Constants.TAG, result)

            printWriter.println(result)
            printWriter.flush()
        } catch (ioException: IOException) {
            Log.e(
                Constants.TAG,
                "[COMMUNICATION THREAD] An IO exception has occurred: " + ioException.message
            )
            if (Constants.DEBUG) {
                ioException.printStackTrace()
            }
        } catch (jsonException: JSONException) {
            Log.e(
                Constants.TAG,
                "[COMMUNICATION THREAD] A JSON exception has occurred: " + jsonException.message
            )
            if (Constants.DEBUG) {
                jsonException.printStackTrace()
            }
        } finally {
            if (socket != null) {
                try {
                    socket.close()
                } catch (ioException: IOException) {
                    Log.e(
                        Constants.TAG,
                        "[COMMUNICATION THREAD] An IO finally exception has occurred: " + ioException.message
                    )
                    if (Constants.DEBUG) {
                        ioException.printStackTrace()
                    }
                }
            }
        }
    }
}
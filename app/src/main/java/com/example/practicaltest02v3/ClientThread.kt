package com.example.practicaltest02v3

import android.util.Log
import android.widget.TextView
import com.example.practicaltest02v3.Utilities.getReader
import com.example.practicaltest02v3.Utilities.getWriter
import java.io.IOException
import java.net.Socket


class ClientThread(private val address: String, private val port: Int, private val word: String,
                   val definitionTextView: TextView
) : Thread() {
    lateinit var socket: Socket

    @Override
    override fun run() {
        try {
            socket = Socket(address, port)
            if (socket == null) {
                Log.e(Constants.TAG, "[CLIENT THREAD] Could not create socket!")
                return
            }
            val bufferedReader = getReader(socket)
            val printWriter = getWriter(socket)
            if (bufferedReader == null || printWriter == null) {
                Log.e(Constants.TAG, "[CLIENT THREAD] Buffered Reader / Print Writer are null!")
                return
            }
            printWriter.println(word)
            printWriter.flush()
            var line: String? = bufferedReader.readLine()
            while (line != null) {
                val definition = line
                definitionTextView.post {
                    definitionTextView.text = definition
                }
                line = bufferedReader.readLine()
            }
        } catch (ioException: IOException) {
            Log.e(
                Constants.TAG,
                "[CLIENT THREAD] An exception has occurred: " + ioException.message
            )
            if (Constants.DEBUG) {
                ioException.printStackTrace()
            }
        } finally {
            if (socket != null) {
                try {
                    socket.close()
                } catch (ioException: IOException) {
                    Log.e(
                        Constants.TAG,
                        "[CLIENT THREAD] An exception has occurred: " + ioException.message
                    )
                    if (Constants.DEBUG) {
                        ioException.printStackTrace()
                    }
                }
            }
        }
    }
}
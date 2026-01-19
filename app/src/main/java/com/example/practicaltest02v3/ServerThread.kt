package com.example.practicaltest02v3

import android.util.Log
import cz.msebera.android.httpclient.client.ClientProtocolException
import java.io.IOException
import java.net.ServerSocket

class ServerThread(private val port: Int) : Thread() {
    var serverSocket: ServerSocket? = null

    fun startServer() {
        Log.i(Constants.TAG, "startServer() method was invoked")

        try {
            serverSocket = ServerSocket(port)
        } catch (ioException: IOException) {
            Log.e(Constants.TAG, "An exception has occurred: " + ioException.message)
            if (Constants.DEBUG) {
                ioException.printStackTrace()
            }
        }

        start()
    }

    fun stopServer() {
        interrupt()
        if (serverSocket != null) {
            try {
                serverSocket!!.close()
            } catch (ioException: IOException) {
                Log.e(
                    Constants.TAG,
                    "[SERVER THREAD] An exception has occurred: " + ioException.message
                )
                if (Constants.DEBUG) {
                    ioException.printStackTrace()
                }
            }
        }
    }

    override fun run() {
        try {
            while (!currentThread().isInterrupted) {
                Log.i(Constants.TAG, "[SERVER THREAD] Waiting for a client invocation...")
                val socket = serverSocket!!.accept()
                Log.i(
                    Constants.TAG,
                    "[SERVER THREAD] A connection request was received from " + socket.getInetAddress() + ":" + socket.getLocalPort()
                )
                val communicationThread = CommunicationThread(this, socket)
                communicationThread.start()
            }
        } catch (clientProtocolException: ClientProtocolException) {
            Log.e(
                Constants.TAG,
                "[SERVER THREAD] An exception has occurred: " + clientProtocolException.message
            )
            if (Constants.DEBUG) {
                clientProtocolException.printStackTrace()
            }
        } catch (ioException: IOException) {
            Log.e(
                Constants.TAG,
                "[SERVER THREAD] An exception has occurred: " + ioException.message
            )
            if (Constants.DEBUG) {
                ioException.printStackTrace()
            }
        }
    }
}
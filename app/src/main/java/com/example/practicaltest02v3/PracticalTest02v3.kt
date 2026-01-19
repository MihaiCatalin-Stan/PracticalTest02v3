package com.example.practicaltest02v3

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class PracticalTest02v3 : AppCompatActivity() {
    lateinit var serverThread: ServerThread
    lateinit var clientThread: ClientThread

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_practical_test02v3)

        val clientPortEditText = findViewById<EditText>(R.id.port_edit_2)
        val clientAddressEditText = findViewById<EditText>(R.id.addr_edit)
        val wordEditText = findViewById<EditText>(R.id.word_edit)
        val getDefinitionButton = findViewById<Button>(R.id.get_definition_btn)
        val definitionTextView = findViewById<TextView>(R.id.http_result)

        val serverPortEditText = findViewById<EditText>(R.id.port_edit_1)

        val connButton = findViewById<Button>(R.id.conn_btn)

        connButton.setOnClickListener {
            val port = serverPortEditText.text.toString().toIntOrNull()
            if (port == null) {
                Toast.makeText(
                    applicationContext,
                    "[MAIN ACTIVITY] Server port should be filled!",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            serverThread = ServerThread(port)
            serverThread.startServer()
        }

        getDefinitionButton.setOnClickListener {
            val clientAddress: String? = clientAddressEditText.getText().toString()
            val clientPort: String? = clientPortEditText.getText().toString()
            if (clientAddress == null || clientAddress.isEmpty()
                || clientPort == null || clientPort.isEmpty()
            ) {
                Toast.makeText(
                    getApplicationContext(),
                    "[MAIN ACTIVITY] Client connection parameters should be filled!",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            if (!serverThread.isAlive) {
                Toast.makeText(
                    applicationContext,
                    "[MAIN ACTIVITY] There is no server to connect to!",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            val word: String? = wordEditText.getText().toString()
            if (word == null || word.isEmpty()) {
                Toast.makeText(
                    applicationContext,
                    "[MAIN ACTIVITY] Parameters from client (city / information type) should be filled",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            definitionTextView.text = Constants.EMPTY_STRING

            clientThread = ClientThread(
                clientAddress, clientPort.toInt(), word, definitionTextView
            )
            clientThread.start()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}
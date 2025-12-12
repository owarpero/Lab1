package com.example.messengerapp

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels

class ProfileFragment : Fragment() {

    private val viewModel: ProfileViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("Lifecycle", "onCreate ProfileFragment")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val editName = view.findViewById<EditText>(R.id.edit_name)
        val editStatus = view.findViewById<EditText>(R.id.edit_status)

        viewModel.name.observe(viewLifecycleOwner) { name ->
            if (editName.text.toString() != name) {
                editName.setText(name)
            }
        }

        viewModel.status.observe(viewLifecycleOwner) { status ->
            if (editStatus.text.toString() != status) {
                editStatus.setText(status)
            }
        }

        editName.addTextChangedListener(simpleTextWatcher { text ->
            viewModel.updateName(text)
        })

        editStatus.addTextChangedListener(simpleTextWatcher { text ->
            viewModel.updateStatus(text)
        })
    }

    private fun simpleTextWatcher(onTextChanged: (String) -> Unit): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                onTextChanged(s?.toString() ?: "")
            }
            override fun afterTextChanged(s: Editable?) {}
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("Lifecycle", "onDestroy ProfileFragment")
    }
}

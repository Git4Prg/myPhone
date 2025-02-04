package com.mintshock.myphone.fragments

import android.content.ContentResolver
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.content.pm.PackageManager
import com.mintshock.myphone.ContactsAdapter
import com.mintshock.myphone.R
import com.mintshock.myphone.models.Contact

class ContactsFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ContactsAdapter
    private val contactsList = mutableListOf<Contact>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_contacts, container, false)

        recyclerView = view.findViewById(R.id.contactsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        adapter = ContactsAdapter(contactsList) { contact ->
            val phoneNumber = contact.phoneNumber.trim()

            // If number doesn't start with +91 or any country code, prepend +91
            val formattedNumber = if (!phoneNumber.startsWith("+")) {
                "+91$phoneNumber"
            } else {
                phoneNumber
            }

            if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.CALL_PHONE)
                == PackageManager.PERMISSION_GRANTED) {
                val intent = Intent(Intent.ACTION_CALL).apply {
                    data = Uri.parse("tel:$formattedNumber")
                }
                startActivity(intent)
            } else {
                ActivityCompat.requestPermissions(requireActivity(), arrayOf(android.Manifest.permission.CALL_PHONE), 102)
            }
        }

        recyclerView.adapter = adapter

        checkAndLoadContacts()

        return view
    }

    private fun checkAndLoadContacts() {
        if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.READ_CONTACTS)
            == PackageManager.PERMISSION_GRANTED) {
            loadContacts()
        } else {
            requestPermissions(arrayOf(android.Manifest.permission.READ_CONTACTS), 101)
        }
    }

    private fun loadContacts() {
        contactsList.clear()

        val contentResolver: ContentResolver = requireContext().contentResolver
        val cursor: Cursor? = contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            arrayOf(
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER
            ),
            null,
            null,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
        )

        cursor?.use {
            val nameIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
            val numberIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)

            while (it.moveToNext()) {
                val name = it.getString(nameIndex)
                val number = it.getString(numberIndex)
                contactsList.add(Contact(name, number))
                Log.d("ContactsFragment", "Loaded Contact: $name - $number")
            }
            adapter.notifyDataSetChanged()
        } ?: run {
            Toast.makeText(requireContext(), "No contacts found", Toast.LENGTH_SHORT).show()
        }
    }
}

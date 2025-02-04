package com.mintshock.myphone

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mintshock.myphone.models.Contact

class ContactsAdapter(private val contacts: List<Contact>, private val onContactClick: (Contact) -> Unit) : RecyclerView.Adapter<ContactsAdapter.ContactViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.contact_item, parent, false)
        return ContactViewHolder(view)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val contact = contacts[position]
        holder.bind(contact)
    }

    override fun getItemCount() = contacts.size

    inner class ContactViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val contactName: TextView = itemView.findViewById(R.id.contactName)
        private val contactPhone: TextView = itemView.findViewById(R.id.contactPhone)

        fun bind(contact: Contact) {
            contactName.text = contact.name
            contactPhone.text = contact.phoneNumber
            itemView.setOnClickListener {
                onContactClick(contact)
            }
        }
    }
}

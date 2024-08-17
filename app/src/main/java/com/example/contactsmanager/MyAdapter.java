package com.example.contactsmanager;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contactsmanager.databinding.ContactListItemBinding;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ContactViewHolder> {

    private ArrayList<Contacts> contacts;

    public MyAdapter(ArrayList<Contacts> contacts) {
        this.contacts = contacts;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Creating new View holders for items in recyclerView
        ContactListItemBinding contactListItemBinding =
                DataBindingUtil.inflate(
                        LayoutInflater.from(parent.getContext()),
                        R.layout.contact_list_item,
                        parent,
                        false
                );

        return new ContactViewHolder(contactListItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        // Called by recyclerView when it needs to display or update an item
        // at a specific position in the list or grid.

        Contacts currentContact  = contacts.get(position);

        holder.contactListItemBinding.setContact(currentContact);

    }

    @Override
    public int getItemCount() {
        // Determines the total number of items in the dataset that will
        // be displayed in the recyclerview

        if (contacts != null){
            return contacts.size();
        }else {
            return 0;
        }
    }


    public void setContacts(ArrayList<Contacts> contacts) {
        this.contacts = contacts;

        // Inform the associated RecyclerView that the underlying
        // dataset has changed, and the RecyclerView should refresh
        // its views to reflect these changes.
        notifyDataSetChanged();

    }

    class ContactViewHolder extends RecyclerView.ViewHolder{

        private ContactListItemBinding contactListItemBinding;

        public ContactViewHolder(@NonNull ContactListItemBinding contactListItemBinding) {
            super(contactListItemBinding.getRoot());
            this.contactListItemBinding = contactListItemBinding;
        }
    }
}

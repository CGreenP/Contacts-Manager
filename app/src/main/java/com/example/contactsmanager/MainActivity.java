package com.example.contactsmanager;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contactsmanager.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // Data Source
    private ContactDatabase contactDatabase;
    private ArrayList<Contacts> contactsArrayList = new ArrayList<>();

    // Adapter
    private MyAdapter myAdapter;

    // Binding
    private ActivityMainBinding mainBinding;
    private MainActivityClickHandlers handlers;

    // View model
    MyViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Data Binding
        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        handlers = new MainActivityClickHandlers(this);

        mainBinding.setClickHandler(handlers);

        // RecyclerView
        RecyclerView recyclerView = mainBinding.recyclerview;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        // Database:
        contactDatabase = ContactDatabase.getInstance(this);

        // View Model:
        viewModel = new ViewModelProvider(this)
                .get(MyViewModel.class);

        // Loading the Data from ROOM DB
        viewModel.getAllContacts().observe(this,
                new Observer<List<Contacts>>() {
                    @Override
                    public void onChanged(List<Contacts> contacts) {
                        contactsArrayList.clear();
                        for (Contacts c: contacts){
                            contactsArrayList.add(c);
                        }
                        myAdapter.notifyDataSetChanged();
                    }
                });

        // Adapter
        myAdapter = new MyAdapter(contactsArrayList);

        // Linking the RecyclerView with the Adapter
        recyclerView.setAdapter(myAdapter);

        // swipe to delete
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                // If you swipe the item to the left
                Contacts c = contactsArrayList.get(viewHolder.getAdapterPosition());

                viewModel.deleteContact(c);
            }
        }).attachToRecyclerView(recyclerView);
    }
}
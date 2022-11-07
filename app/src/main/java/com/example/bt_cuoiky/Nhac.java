package com.example.bt_cuoiky;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.FirebaseDatabase;

public class Nhac extends AppCompatActivity {
    RecyclerView recyclerView;
    SearchView searchView;
    FloatingActionButton floatingActionButton;
    NhacAdapter nhacAdapter;
    LinearLayout back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nhac);

        recyclerView = (RecyclerView) findViewById(R.id.rcv_covu);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        searchView = (SearchView) findViewById(R.id.search_view);
        back = (LinearLayout) findViewById(R.id.back);
        searchView.clearFocus();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        FirebaseRecyclerOptions<NhacModel> options =
                new FirebaseRecyclerOptions.Builder<NhacModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("nhac"), NhacModel.class)
                        .build();
        nhacAdapter = new NhacAdapter(options, this);
        recyclerView.setAdapter(nhacAdapter);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Nhac.this, GiaoDienChinh.class));
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                txtSearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                txtSearch(query);
                return false;
            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AddActivity.class));
            }
        });

    }

    protected void onStart() {
        super.onStart();
        nhacAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        nhacAdapter.stopListening();
    }

    private void txtSearch(String str){
        FirebaseRecyclerOptions<NhacModel> options =
                new FirebaseRecyclerOptions.Builder<NhacModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("nhac").orderByChild("ten").startAt(str).endAt(str+"~"), NhacModel.class)
                        .build();
        nhacAdapter = new NhacAdapter(options, this);
        nhacAdapter.startListening();
        recyclerView.setAdapter(nhacAdapter);
    }

}
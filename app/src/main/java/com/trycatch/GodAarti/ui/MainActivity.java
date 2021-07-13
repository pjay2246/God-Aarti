package com.trycatch.GodAarti.ui;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.trycatch.GodAarti.R;
import com.trycatch.GodAarti.adapter.RecyclerviewAdapter;
import com.trycatch.GodAarti.data.RecyclerDataModel;

import java.util.ArrayList;

import static com.trycatch.GodAarti.data.constant.desc;
import static com.trycatch.GodAarti.data.constant.imgss;
import static com.trycatch.GodAarti.data.constant.names;


public class MainActivity extends AppCompatActivity implements RecyclerviewAdapter.MyViewHolder.OnNoteListener {


    private static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    private static ArrayList<RecyclerDataModel> data;
    public static View.OnClickListener myOnClickListener;


    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.cablayout);

        View view = getSupportActionBar().getCustomView();
        ImageButton imageButton = (ImageButton) view.findViewById(R.id.action_bar_back);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });




        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setItemAnimator(new DefaultItemAnimator());

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.dividerline));
        recyclerView.addItemDecoration(dividerItemDecoration);

        data = new ArrayList<RecyclerDataModel>();
        for (int i = 0; i < names.length; i++) {
            data.add(new RecyclerDataModel(
                   names[i],
                    desc[i],
                    imgss[i]
            ));
        }

        adapter = new RecyclerviewAdapter(data,this::onNoteClick);
        recyclerView.setAdapter(adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {


            case R.id.share:
                ApplicationInfo api = getApplicationContext().getApplicationInfo();
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                String sharebody = " God Aarti";
                intent.putExtra(Intent.EXTRA_STREAM, sharebody);
                startActivity(Intent.createChooser(intent, "ShareVia"));
                return (true);
            case R.id.about:
                Intent intents = new Intent(MainActivity.this,PopUpActivity.class);
                startActivity(intents);
                return (true);
            case R.id.exit:
                onBackPressed();
                return (true);

        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onNoteClick(int position) {

                data.get(position);
                Intent intent = new Intent(getApplicationContext(),PlayerActivity.class);
                intent.putExtra("Key", position);
                startActivity(intent);
    }


    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {


                        finishAffinity();
                        System.exit(0);

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

    }

}
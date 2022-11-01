package com.mniprince.quiz;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

public class CategoryActivity extends AppCompatActivity {
    BroadcastReceiver br;
    RadioGroup radioGroup;
    RadioButton radioButton;
    Button start;
    Spinner spinner, catspinner;
    String[] limi = {"5", "10", "15", "20"};
    String[] category = {"Arts & Literature", "Film & TV", "Food & Drink", "General Knowledge", "Geography", "History", "Music", "Science", "Society & Culture", "Sport & Leisure"};
    String cat, limit = "", difficulty;
    private long backpresstime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkInternetConnection();


    }

    private void checkInternetConnection() {
        if (br == null) {

            br = new BroadcastReceiver() {

                @Override
                public void onReceive(Context context, Intent intent) {

                    Bundle extras = intent.getExtras();

                    NetworkInfo info = (NetworkInfo) extras
                            .getParcelable("networkInfo");
                    NetworkInfo.State state = info.getState();
                    if (state == NetworkInfo.State.CONNECTED) {
                        setContentView(R.layout.activity_category);


                        try {
                            initcat();

                        } catch (Exception e) {

                        }


                    } else {

                        setContentView(R.layout.internet_screen);
                        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
                        Toast.makeText(getApplicationContext(), "Internet connection is Off", Toast.LENGTH_LONG).show();
                    }

                }


            };

            final IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
            registerReceiver((BroadcastReceiver) br, intentFilter);
        }
    }

    private void initcat() {
        radioGroup = findViewById(R.id.radioGroup);
        start = findViewById(R.id.start);
        spinner = findViewById(R.id.spinner);
        catspinner = findViewById(R.id.catspinner);


        ArrayAdapter<String> catAdapter = new ArrayAdapter<String>(CategoryActivity.this, R.layout.spinner_item, category);
        catAdapter.setDropDownViewResource(R.layout.spinner_item);
        catspinner.setAdapter(catAdapter);
        catspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                cat = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ArrayAdapter<String> limitAdapter = new ArrayAdapter<String>(CategoryActivity.this, R.layout.spinner_item, limi);
        limitAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinner.setAdapter(limitAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                limit = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (difficulty != null) {
                    Intent intent = new Intent(CategoryActivity.this, MainActivity.class);
                    intent.putExtra("cat", cat);
                    intent.putExtra("limit", limit);
                    intent.putExtra("difficulty", difficulty);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(CategoryActivity.this, "Select Difficulty Level", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    public void checkButtion(View v) {
        int radioid = radioGroup.getCheckedRadioButtonId();
        radioButton = findViewById(radioid);
        difficulty = radioButton.getText().toString();
    }


    @Override
    public void onBackPressed() {
        if (backpresstime + 2000 > System.currentTimeMillis()) {

            final AlertDialog.Builder builder = new AlertDialog.Builder(CategoryActivity.this);
            builder.setMessage("Are you sure want to exit?");
            builder.setTitle("Confirmation Notice!");
            builder.setCancelable(true);
            builder.setIcon(R.mipmap.ic_launcher);
            builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    CategoryActivity.this.finish();
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addCategory(Intent.CATEGORY_HOME);
                    startActivity(intent);
                    finish();
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();

        } else {
            Toast.makeText(getApplicationContext(), "Press again to exit", Toast.LENGTH_SHORT).show();
        }
        backpresstime = System.currentTimeMillis();

    }
}
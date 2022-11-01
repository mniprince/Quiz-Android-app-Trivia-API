package com.mniprince.quiz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    BroadcastReceiver br;
    ArrayList<QuizModel> quizModels = new ArrayList<>();
    TextView questiontv, status, totaltv, scoretv;
    Button submit, restart;
    String cat, limit, difficulty;
    LinearLayout mainlayout, scorelayout;

    private static int[] idArray = {R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4};
    private Button[] button = new Button[idArray.length];

    int i;
    int isClick = 0;
    int score;
    int q = 0;

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
                        setContentView(R.layout.activity_main);


                        try {
                            init();

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

    public void init() {
        try {
            Intent intent = getIntent();
            cat = intent.getStringExtra("cat").toLowerCase(Locale.ROOT);
            limit = intent.getStringExtra("limit");
            difficulty = intent.getStringExtra("difficulty").toLowerCase(Locale.ROOT);
            getResponse(cat, limit, difficulty);
        } catch (Exception ignored) {
        }
        score = 0;
        questiontv = findViewById(R.id.questionTV);
        status = findViewById(R.id.status);
        submit = findViewById(R.id.submit);
        restart = findViewById(R.id.restart);
        totaltv = findViewById(R.id.totaltv);
        scoretv = findViewById(R.id.score);
        mainlayout = findViewById(R.id.mainlayout);
        scorelayout = findViewById(R.id.scorelayout);
    }


    private void getResponse(String key, String key1, String key2) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://the-trivia-api.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RequestInteface requestInteface = retrofit.create(RequestInteface.class);
        Call<List<QuizModel>> call = requestInteface.getJson(key, key1, key2);

        call.enqueue(new Callback<List<QuizModel>>() {
            private Context[] strArray;

            @Override
            public void onResponse(Call<List<QuizModel>> call, Response<List<QuizModel>> response) {

                if (response.body() != null) {
                    quizModels = new ArrayList<>(response.body());
                }


                try {

                    String[] strArray = new String[4];
                    strArray[0] = quizModels.get(q).getIncorrectAnswers().get(0);
                    strArray[1] = quizModels.get(q).getIncorrectAnswers().get(1);
                    strArray[2] = quizModels.get(q).getIncorrectAnswers().get(2);
                    strArray[3] = quizModels.get(q).getCorrectAnswer();
                    Collections.shuffle(Arrays.asList(strArray));

                    questiontv.setText(quizModels.get(q).getQuestion());
                    status.setText(q + 1 + "/" + quizModels.size());
                    for (i = 0; i < idArray.length; i++) {

                        button[i] = findViewById(idArray[i]);
                        button[i].setText(strArray[i]);
                        button[i].setBackground(getDrawable(R.drawable.bgbtn));

                        button[i].setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (isClick == 0) {
                                    isClick = 1;
                                    switch (view.getId()) {
                                        case R.id.btn1:
                                            button[0].setBackground(getDrawable(R.drawable.selectbtn));
                                            try {
                                                if (Objects.equals(strArray[0], quizModels.get(q).getCorrectAnswer())) {
                                                    score = score + 5;

                                                } else {
                                                    score = score;
                                                }
                                            } catch (Exception w) {

                                            }
                                            break;
                                        case R.id.btn2:
                                            button[1].setBackground(getDrawable(R.drawable.selectbtn));
                                            try {
                                                if (Objects.equals(strArray[1], quizModels.get(q).getCorrectAnswer())) {
                                                    score = score + 5;
                                                } else {
                                                    score = score;
                                                }
                                            } catch (Exception w) {

                                            }
                                            break;
                                        case R.id.btn3:
                                            button[2].setBackground(getDrawable(R.drawable.selectbtn));
                                            try {
                                                if (Objects.equals(strArray[2], quizModels.get(q).getCorrectAnswer())) {
                                                    score = score + 5;
                                                } else {
                                                    score = score;
                                                }
                                            } catch (Exception w) {

                                            }
                                            break;
                                        case R.id.btn4:
                                            button[3].setBackground(getDrawable(R.drawable.selectbtn));
                                            try {
                                                if (Objects.equals(strArray[3], quizModels.get(q).getCorrectAnswer())) {
                                                    score = score + 5;
                                                } else {
                                                    score = score;
                                                }
                                            } catch (Exception w) {

                                            }
                                            break;
                                    }
                                    if (q < quizModels.size() - 1) {
                                        q = q + 1;
                                        isClick = 0;
                                        getResponse(cat, limit, difficulty);
                                    } else {
                                        submit.setVisibility(View.VISIBLE);
                                        submitbtn();
                                    }


                                }
                            }
                        });

                    }
                    //Toast to get correct answer Test purpose
//                      Toast.makeText(MainActivity.this, quizModels.get(q).getCorrectAnswer(), Toast.LENGTH_SHORT).show();

                } catch (Exception ignored) {
                }


            }

            @Override
            public void onFailure(Call<List<QuizModel>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void submitbtn() {

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainlayout.setVisibility(View.GONE);
                scorelayout.setVisibility(View.VISIBLE);
                totaltv.setText(String.valueOf(q + 1));
                scoretv.setText(String.valueOf(score) + "/" + (q + 1) * 5);
                restart();
            }
        });
    }

    private void restart() {

        restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CategoryActivity.class);
                startActivity(intent);
                finish();

            }
        });
    }
}
package com.example.assignment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements SetListener {
    private ArrayList<MatchModel> listMatchModels = new ArrayList<>();
    private RecyclerView recyclerViewMatch;
    private RecyclerAdapter recyclerAdapter;
    private ProgressDialog progressDialog;
    private ProfileDataBase profileDataBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getMatchData();
    }

    public void getMatchData() {

        try {
            progressDialog = ProgressDialog.show(
                    MainActivity.this, "", "Please wait...");
            Service service = RetrofitInstance.getRetrofitInstance(this).create(Service.class);
            Call<JsonElement> call = service.getMatchData("20");
            call.enqueue(new Callback<JsonElement>() {
                @Override
                public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                    Log.d("Response", response.toString());
                    if (progressDialog.isShowing())
                        progressDialog.dismiss();
                    try {
                        JSONObject result = new JSONObject(response.body().toString());
                        JSONArray jsonArray = result.getJSONArray("results");
                        setMatchData(jsonArray);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailure(Call<JsonElement> call, Throwable error) {
                    Log.d("Response", error.toString());
                    if (progressDialog.isShowing())
                        progressDialog.dismiss();

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setMatchData(JSONArray jsonArray) {
        try {
            for (int i = 0; i < jsonArray.length(); i++) {

                String gender = jsonArray.getJSONObject(i).getString("gender");
                String name = jsonArray.getJSONObject(i).getJSONObject("name").getString("title") +
                        "." + jsonArray.getJSONObject(i).getJSONObject("name").getString("first") +
                        " " + jsonArray.getJSONObject(i).getJSONObject("name").getString("last");
                String image = jsonArray.getJSONObject(i).getJSONObject("picture").getString("large");
                String city = jsonArray.getJSONObject(i).getJSONObject("location").getString("city");
                String state = jsonArray.getJSONObject(i).getJSONObject("location").getString("state");
                String country = jsonArray.getJSONObject(i).getJSONObject("location").getString("country");
                String age = jsonArray.getJSONObject(i).getJSONObject("dob").getString("age");

                MatchModel matchModel = new MatchModel();
                matchModel.setGender(gender);
                matchModel.setName(name);
                matchModel.setImage(image);
                matchModel.setCity(city);
                matchModel.setState(state);
                matchModel.setCountry(country);
                matchModel.setAge(age);

                listMatchModels.add(matchModel);
            }
            setAdapter();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setAdapter() {
        recyclerViewMatch = findViewById(R.id.recyclerViewMatch);
        recyclerViewMatch.setLayoutManager(new GridLayoutManager(this, 2));

        recyclerAdapter = new RecyclerAdapter(listMatchModels, this, this);
        recyclerViewMatch.setAdapter(recyclerAdapter);
    }

    @Override
    public void onClick(MatchModel matchModel, String query) {
        profileDataBase = Room.databaseBuilder(this, ProfileDataBase.class, "ProfileDao").build();
        if (query.equals("insert")) {
            new Thread(() ->
                    profileDataBase.taskDao().insert(matchModel)
            ).start();
            Toast.makeText(this, "Member accepted", Toast.LENGTH_LONG).show();
        } else {
            new Thread(() ->
                    profileDataBase.taskDao().delete(matchModel)
            ).start();
            Toast.makeText(this, "Member Declined", Toast.LENGTH_LONG).show();
        }

    }
}
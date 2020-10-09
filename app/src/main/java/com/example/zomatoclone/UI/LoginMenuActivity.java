package com.example.zomatoclone.UI;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.zomatoclone.Adapters.DishAdapter;
import com.example.zomatoclone.Comman.AutoScrollRecyclerView;
import com.example.zomatoclone.Model.DishModel;
import com.example.zomatoclone.R;
import com.example.zomatoclone.SharedPrefrence.PreferenceManager;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LoginMenuActivity extends AppCompatActivity {

    private final String TAG = getClass().getSimpleName();
    private AutoScrollRecyclerView rvDishes;
    private List<DishModel> dishModelList;
    private CardView facebookLoginButton;
    private CallbackManager callbackManager;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(getResources().getColor(R.color.statusBarColor));

        // Init preference manager
        PreferenceManager.init(this);

        FacebookSdk.sdkInitialize(this.getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.e(TAG, "Login");
                        PreferenceManager.setIsLogin(true);
                        startActivity(new Intent(LoginMenuActivity.this,PermissionActivity.class));
                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(LoginMenuActivity.this, "Login Cancel", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Toast.makeText(LoginMenuActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
                    }
                });
        setContentView(R.layout.activity_login_menu);

        init();

        getDataInRecyclerView();


        facebookLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginManager.getInstance().logInWithReadPermissions(LoginMenuActivity.this, Arrays.asList("public_profile","email"));
            }
        });
    }

    private void getDataInRecyclerView() {
        dishModelList = new ArrayList<>();
        dishModelList.add(new DishModel(R.drawable.dish_1));
        dishModelList.add(new DishModel(R.drawable.dish_2));
        dishModelList.add(new DishModel(R.drawable.dish_3));
        dishModelList.add(new DishModel(R.drawable.dish_4));
        dishModelList.add(new DishModel(R.drawable.dish_5));
        dishModelList.add(new DishModel(R.drawable.dish_6));

        rvDishes.setHasFixedSize(true);
        rvDishes.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        rvDishes.setAdapter(new DishAdapter(this,dishModelList));
        rvDishes.startAutoScroll();
        rvDishes.setLoopEnabled(true);
        rvDishes.setCanTouch(false);

    }

    private void init() {
        rvDishes =  findViewById(R.id.rv_dishes);
        facebookLoginButton = findViewById(R.id.card_facebook);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
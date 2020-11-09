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
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LoginMenuActivity extends AppCompatActivity {

    private final String TAG = getClass().getSimpleName();
    private static final int RC_SIGN_IN = 2;
    private AutoScrollRecyclerView rvDishes;
    private List<DishModel> dishModelList;
    private CardView facebookLoginButton,googleSiginButton;
    private CallbackManager callbackManager;
    private GoogleSignInClient mGoogleSignInClient;
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
                        finish();
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

        // google signin
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        facebookLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginManager.getInstance().logInWithReadPermissions(LoginMenuActivity.this, Arrays.asList("public_profile","email"));
            }
        });

        googleSiginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.card_google:
                        signIn();
                        break;
                }
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
        googleSiginButton = findViewById(R.id.card_google);
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            if (account != null) {
                startActivity(new Intent(LoginMenuActivity.this, PermissionActivity.class));
                finish();
            }
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
       if (account != null){
           startActivity(new Intent(LoginMenuActivity.this,PermissionActivity.class));
           finish();
       }
    }
}
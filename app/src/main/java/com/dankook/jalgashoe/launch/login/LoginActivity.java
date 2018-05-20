package com.dankook.jalgashoe.launch.login;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.dankook.jalgashoe.BaseActivity;
import com.dankook.jalgashoe.R;
import com.dankook.jalgashoe.data.vo.LoginVO;
import com.dankook.jalgashoe.data.vo.UserVO;
import com.dankook.jalgashoe.databinding.ActivityLoginBinding;
import com.dankook.jalgashoe.launch.join.JoinActivity;
import com.dankook.jalgashoe.map.MapActivity;
import com.dankook.jalgashoe.util.HttpClient;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class LoginActivity extends BaseActivity<ActivityLoginBinding> {
    private EditText textUserId;
    private EditText textUserPassword;
    private Button buttonJoin;
    private Button buttonSubmit;
    private LoginVO loginVO = new LoginVO();
    private UserVO userVO = new UserVO();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();

        getPermission();
    }

    private void initView() {

        textUserId = findViewById(R.id.text_user_id);
        textUserPassword = findViewById(R.id.text_user_password);

        buttonJoin = findViewById(R.id.button_join);
        buttonJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), JoinActivity.class);
                startActivity(intent);
            }
        });
        buttonSubmit = findViewById(R.id.button_submit);
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getLoginProfile();
                processLogin();

                Intent intent = new Intent(getApplicationContext(), MapActivity.class);
                startActivity(intent);
                finish();

            }
        });
    }

    private void processLogin() {
        RequestParams params = new RequestParams();
        params.put("user_id", loginVO.getUserId());
        params.put("user_password", loginVO.getUserPassword());

        HttpClient.post("/user/login", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String responseString = new String(responseBody);

                try {
                    JSONObject result = new JSONObject(responseString);
                    Boolean isSuccess = result.getBoolean("result");

                    if (isSuccess == true) {
                        Log.i("login", "Login Success");
                        JSONObject user = result.getJSONObject("user");
                        userVO.setUserId(user.getString("user_id"));
                        userVO.setUserName(user.getString("user_name"));
                        userVO.setUserHeight(user.getString("user_height"));
                        userVO.setUserWeight(user.getString("user_weight"));

                        ////////////////////// 세션저장 //////////////////////
                    }
                }catch (Exception e){

                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    private void getLoginProfile() {
        loginVO.setUserId(textUserId.getText().toString());
        loginVO.setUserPassword(textUserPassword.getText().toString());
    }

    private void getPermission(){

        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
            }
        };

        TedPermission.with(this)
                .setPermissionListener(permissionListener)
                .setPermissions(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
                .check();

    }
}

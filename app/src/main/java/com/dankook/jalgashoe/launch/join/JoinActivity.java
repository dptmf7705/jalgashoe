package com.dankook.jalgashoe.launch.join;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.dankook.jalgashoe.BaseActivity;
import com.dankook.jalgashoe.R;
import com.dankook.jalgashoe.data.vo.UserVO;
import com.dankook.jalgashoe.databinding.ActivityJoinBinding;
import com.dankook.jalgashoe.util.HttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import cz.msebera.android.httpclient.Header;

public class JoinActivity extends BaseActivity<ActivityJoinBinding> {
    private EditText textUserId;
    private EditText textUserPassword;
    private EditText textUserName;
    private Spinner spinnerHeight;
    private Spinner spinnerWeight;
    private Button buttonSubmit;
    private Button buttonCancel;

    private UserVO userVO = new UserVO();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_join;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
    }

    private void initView() {

        textUserId = findViewById(R.id.user_id_text);
        textUserPassword = findViewById(R.id.user_password_text);
        textUserName = findViewById(R.id.user_name_text);

        spinnerHeight = findViewById(R.id.spinner_height);
        spinnerWeight = findViewById(R.id.spinner_weight);

        buttonSubmit = findViewById(R.id.button_submit);
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        buttonCancel = findViewById(R.id.button_cancel);
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getUserProfile();
                processJoin();
                finish();
            }
        });
    }

    private UserVO getUserProfile() {

        userVO.setUserId(textUserId.getText().toString());
        userVO.setUserPassword(textUserPassword.getText().toString());
        userVO.setUserName(textUserName.getText().toString());
        userVO.setUserHeight(spinnerHeight.getSelectedItem().toString());
        userVO.setUserWeight(spinnerWeight.getSelectedItem().toString());
        return userVO;
    }

    private void processJoin() {
        RequestParams params = new RequestParams();
        params.put("user_id", userVO.getUserId());
        params.put("user_password", userVO.getUserPassword());
        params.put("user_name", userVO.getUserName());
        params.put("user_height", userVO.getUserHeight());
        params.put("user_weight", userVO.getUserWeight());

        HttpClient.post("/user/join", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }
}

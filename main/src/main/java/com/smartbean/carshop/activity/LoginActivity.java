package com.smartbean.carshop.activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import com.rey.material.widget.Button;
import com.rey.material.widget.SnackBar;
import com.smartbean.carshop.activity.base.BaseActivity;
import com.smartbean.carshop.common.Constants;
import com.smartbean.carshop.service.UserService;
import com.ta.TASyncHttpClient;
import com.ta.annotation.TAInject;
import com.ta.annotation.TAInjectView;
import com.ta.common.TAStringUtils;
import com.ta.util.http.AsyncHttpClient;
import com.ta.util.http.AsyncHttpResponseHandler;

public class LoginActivity extends BaseActivity{

    @TAInjectView(id = R.id.login_button)
    private Button loginButton;

    @TAInjectView(id = R.id.login_name)
    private EditText loginNameText;

    @TAInjectView(id = R.id.password)
    private EditText passwordText;


    @TAInjectView(id = R.id.main_toolbar)
    private Toolbar toolbar;

    UserService userService;

    @TAInjectView(id = R.id.main_sn)
    SnackBar mSnackBar;

    @TAInject
    private TASyncHttpClient syncHttpClient;
    @TAInject
    private AsyncHttpClient asyncHttpClient;

    @Override
    protected void onPreOnCreate(Bundle savedInstanceState) {
        super.onPreOnCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        toolbar.setTitle(R.string.app_name);


        getTAApplication().registerActivity(R.string.orderActivity, OrderActivity.class);
        getTAApplication().registerActivity(R.string.customerActivity, CustomerActivity.class);
        getTAApplication().registerActivity(R.string.qrcodeActivity, QrcodeActivity.class);
        getTAApplication().registerActivity(R.string.asksActivity, AskActivity.class);
        getTAApplication().registerActivity(R.string.mainActivity, MainActivity.class);
        getTAApplication().registerActivity(R.string.loginActivity, LoginActivity.class);



    }

    @Override
    protected void onAfterSetContentView() {
        super.onAfterSetContentView();
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.login_button:
                        userService = new UserService(syncHttpClient);
                        String userName = loginNameText.getText().toString().trim();
                        String password = passwordText.getText().toString().trim();

                        if(TAStringUtils.isBlank(userName)){
                            mSnackBar.applyStyle(R.style.SnackBarMultiLine)
                                    .text("登录账号不能为空")
                                    .duration(Constants.DISPLAY_TIME)
                                    .show();
                        }else if(TAStringUtils.isBlank(password)){
                            mSnackBar.applyStyle(R.style.SnackBarMultiLine)
                                    .text("登录密码不能为空")
                                    .duration(Constants.DISPLAY_TIME)
                                    .show();
                        }else{
                            new Thread(loginThread).start();
                        }

                        break;
                }
            }
        };
        loginButton.setOnClickListener(onClickListener);
    }

    Runnable loginThread =  new Runnable(){
        @Override
        public void run() {
            String userName = loginNameText.getText().toString().trim();
            String password = passwordText.getText().toString().trim();
            boolean isSuccess = userService.login(userName, password);
            if(isSuccess){
                doActivity(R.string.mainActivity);
            }else{
                mSnackBar.applyStyle(R.style.SnackBarMultiLine)
                        .text("用户名或密码错误")
                        .duration(Constants.DISPLAY_TIME)
                        .show();
            }
        }
    };
}
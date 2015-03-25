package com.redoct.iclub.ui.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.oct.ga.comm.domain.account.AccountDetailInfo;
import com.redoct.iclub.BaseActivity;
import com.redoct.iclub.MainActivity;
import com.redoct.iclub.R;
import com.redoct.iclub.config.AppConfig;
import com.redoct.iclub.task.GetAccountTask;
import com.redoct.iclub.task.UserLoginTask;
import com.redoct.iclub.util.EncryptUtil;
import com.redoct.iclub.util.PersistentUtil;
import com.redoct.iclub.util.ToastUtil;
import com.redoct.iclub.util.UserInformationLocalManagerUtil;
import com.redoct.iclub.util.activity.BaseActivityUtil;
import com.redoct.iclub.widget.DeleteEditText;

public class LoginActivity extends BaseActivity implements OnClickListener {
	private TextView tvForgetPWD;

	private Button btnRegister;

	private Button btnLogin;

	private DeleteEditText etLoginName;

	private DeleteEditText etLoginPWD;

	private CheckBox cbLoginRememberpwd;
	private TextView tvLoginTitle, tvLoginForgetPassWord;
	private final String FILENAME = "iclub";
	private Dialog dia;
	private GetAccountTask getTask;
	public static AccountDetailInfo act;
	private String pwd;
	private String name;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub

		tvLoginForgetPassWord = (TextView) findViewById(R.id.tv_longin_forgetpass);
		tvLoginForgetPassWord.setOnClickListener(this);

		tvLoginTitle = (TextView) findViewById(R.id.mTitleView);
		tvLoginTitle.setText(R.string.login);
		tvForgetPWD = (TextView) findViewById(R.id.tv_longin_forgetpass);
		tvForgetPWD.setOnClickListener(this);

		btnRegister = (Button) findViewById(R.id.btn_register);
		btnRegister.setOnClickListener(this);

		btnLogin = (Button) findViewById(R.id.btn_login);
		btnLogin.setOnClickListener(this);

		// 登录账号
		etLoginName = (DeleteEditText) findViewById(R.id.et_login_name);
		name = PersistentUtil.getInstance().readString(this, "loginName", "");
		etLoginName.setText(name);

		// 密码
		etLoginPWD = (DeleteEditText) findViewById(R.id.et_login_pwd);
		// pwd = PersistentUtil.getInstance().readString(this,"passWord","");
		// etLoginPWD.setText(pwd);

		// 记住密码
		cbLoginRememberpwd = (CheckBox) findViewById(R.id.cb_login_rememberpwd);

		cbLoginRememberpwd.setChecked(true);
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.tv_longin_forgetpass: {

			break;
		}
		case R.id.btn_register: {
			BaseActivityUtil.startActivity(LoginActivity.this,
					RegisterActivity.class, true);
			break;
		}
		case R.id.btn_login: {

			login();

		}
		default: {
			break;
		}
		}

	}

	private void login() {
		// TODO Auto-generated method stub
		// 检查
		final Boolean isCheck = cbLoginRememberpwd.isChecked();
		PersistentUtil.getInstance().write(this, "isChcek", isCheck);

		name = etLoginName.getText().toString().trim();
		// if(TextUtils.isEmpty(pwd)){

		pwd = EncryptUtil.md5(etLoginPWD.getText().toString());
		// }

		if (TextUtils.isEmpty(name)) {
			ToastUtil
					.toastshort(this, getString(R.string.login_enter_username));
			return;
		}

		if (TextUtils.isEmpty(pwd)) {
			ToastUtil.toastshort(this, getString(R.string.login_enter_pwd));
			return;
		}
		UserLoginTask login = new UserLoginTask(name, pwd) {
			public void callback() {
				Log.i("zyf", "成功》》》》》》》》》》》》》》》》》》》》》》》》》》》》》》》》》》》》》");
				BaseActivityUtil.startActivity(LoginActivity.this,
						MainActivity.class, true);
				if (isCheck) {

					PersistentUtil.getInstance().write(LoginActivity.this,
							"passWord", pwd);
				}
				PersistentUtil.getInstance().write(LoginActivity.this,
						"loginName", name);

				fetchAccountInfo();
			}

			public void failure() {
				// showToast(R.string.login_failure);
				// popupLogin();//re login

				ToastUtil.toastshort(LoginActivity.this, getResources()
						.getString(R.string.login_faile));
				/*
				 * SharedPreferences sp = getSharedPreferences(FILENAME,
				 * Context.MODE_PRIVATE); Editor editor = sp.edit();
				 * editor.remove("passWord");
				 * 
				 * editor.commit();
				 */
			}

			public void complete() {
				/*
				 * login = null; BusProvider.getInstance().post(new
				 * ProgressEvent(false));
				 */
			}

			public void before() {
				// BusProvider.getInstance().post(new ProgressEvent(true));
			}

			@Override
			public void timeout() {
				// TODO Auto-generated method stub
				super.timeout();

				// Log.e("zyf", "time out.......");

				this.cancel(true);
			}

		};

		// login.setTimeOutEnabled(true, 100);
		login.safeExecute();

	}

	private void fetchAccountInfo() {
		if (!AppConfig.isLoggedIn())
			return;

		getTask = new GetAccountTask() {
			@Override
			public void callback() {
				act = getTask.getAccount();
				AppConfig.act = act;
				if (act != null) {
					Log.i("getAccount", "get accout  success��");
					new UserInformationLocalManagerUtil(getApplicationContext())
							.WriteUserInformation(act);
				}

				// fillAccountView(act);
			}

			@Override
			public void failure() {
				Log.i("getAccount", "get accout  failure��");
			}

			@Override
			public void complete() {
				getTask = null;
			}

			@Override
			public void pullback() {
				getTask = null;
			}

			@Override
			public void before() {
			}
		};
		getTask.safeExecute();
		Log.d("sima", "get account...");
	}

}

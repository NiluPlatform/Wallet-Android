package tech.nilu.wallet.ui.password;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.AndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;
import tech.nilu.wallet.MainActivity;
import tech.nilu.wallet.R;
import tech.nilu.wallet.util.MyKeyStore;

/**
 * Created by root on 2/28/18.
 */

public class SetPasswordActivity extends AppCompatActivity implements HasSupportFragmentInjector {
    @Inject
    PasswordViewModel viewModel;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.passwordTextLayout)
    TextInputLayout passwordTextLayout;
    @BindView(R.id.passwordText)
    TextInputEditText passwordText;
    @BindView(R.id.confirmPasswordTextLayout)
    TextInputLayout confirmPasswordTextLayout;
    @BindView(R.id.confirmPasswordText)
    TextInputEditText confirmPasswordText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_password);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        initUI();
    }

    private void initUI() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_set_password, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_done:
                String password = passwordText.getText().toString();
                String confirmPassword = confirmPasswordText.getText().toString();
                setPassword(password, confirmPassword);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setPassword(String password, String confirmPassword) {
        passwordTextLayout.setError(null);
        confirmPasswordTextLayout.setError(null);
        if (password.length() < 8) {
            passwordText.requestFocus();
            passwordTextLayout.setError(getString(R.string.err_invalid_password));
            return;
        }
        if (!confirmPassword.equals(password)) {
            confirmPasswordText.requestFocus();
            confirmPasswordTextLayout.setError(getString(R.string.err_password_confirm_not_match));
            return;
        }

        viewModel.storeString(MyKeyStore.PASSWORD, password);
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return null;
    }
}

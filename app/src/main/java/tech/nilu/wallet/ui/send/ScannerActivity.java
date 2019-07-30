package tech.nilu.wallet.ui.send;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import tech.nilu.wallet.R;

/**
 * Created by root on 1/23/18.
 */

public class ScannerActivity extends AppCompatActivity {
    private static final int RC_CAMERA = 1001;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);
        ButterKnife.bind(this);

        initUI();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)
            getSupportFragmentManager().beginTransaction().replace(R.id.scanner, CaptureFragment.newInstance(new ArrayList<>())).commitAllowingStateLoss();
        else
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, RC_CAMERA);

        toolbar.setNavigationIcon(R.drawable.ic_back_white_24dp);
        toolbar.setNavigationOnClickListener(view -> onBackPressed());
    }

    private void initUI() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == RC_CAMERA && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            getSupportFragmentManager().beginTransaction().replace(R.id.scanner, CaptureFragment.newInstance(new ArrayList<>())).commitAllowingStateLoss();
    }
}

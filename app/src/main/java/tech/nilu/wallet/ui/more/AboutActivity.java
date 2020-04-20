package tech.nilu.wallet.ui.more;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;

import butterknife.BindView;
import butterknife.OnClick;
import tech.nilu.wallet.R;
import tech.nilu.wallet.ui.common.BaseActivity;
import tech.nilu.wallet.ui.send.SendMoneyActivity;
import tech.nilu.wallet.util.DeviceUtils;

public class AboutActivity extends BaseActivity {
    @BindView(R.id.tvNiluVersion)
    TextView versionText;
    @BindView(R.id.btnDiscord)
    Button discordButton;
    @BindView(R.id.btnTelegram)
    Button telegramButton;
    @BindView(R.id.btnReddit)
    Button redditButton;
    @BindView(R.id.btnVisitWebsite)
    Button visitWebsiteButton;
    @BindView(R.id.btnSendEmail)
    Button sendEmailButton;
    @BindView(R.id.btnDonate)
    Button donateButton;
    @BindView(R.id.btnPrivacyPolicy)
    Button privacyPolicyButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        versionText.setText(getString(R.string.nilu_version, DeviceUtils.getAppVersionName(this), DeviceUtils.getAppVersionCode(this)));
        discordButton.setCompoundDrawablesWithIntrinsicBounds(AppCompatResources.getDrawable(this, R.drawable.ic_discord), null, null, null);
        telegramButton.setCompoundDrawablesWithIntrinsicBounds(AppCompatResources.getDrawable(this, R.drawable.ic_telegram), null, null, null);
        redditButton.setCompoundDrawablesWithIntrinsicBounds(AppCompatResources.getDrawable(this, R.drawable.ic_reddit), null, null, null);
        visitWebsiteButton.setCompoundDrawablesWithIntrinsicBounds(AppCompatResources.getDrawable(this, R.drawable.ic_website), null, null, null);
        sendEmailButton.setCompoundDrawablesWithIntrinsicBounds(AppCompatResources.getDrawable(this, R.drawable.ic_email), null, null, null);
        donateButton.setCompoundDrawablesWithIntrinsicBounds(AppCompatResources.getDrawable(this, R.drawable.ic_donate), null, null, null);
        privacyPolicyButton.setCompoundDrawablesWithIntrinsicBounds(AppCompatResources.getDrawable(this, R.drawable.ic_privacy_policy), null, null, null);
    }

    @OnClick(R.id.btnDiscord)
    public void onDiscordClick() {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://discord.gg/KWfvSC4")));
    }

    @OnClick(R.id.btnTelegram)
    public void onTelegramClick() {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/niluchat")));
    }

    @OnClick(R.id.btnReddit)
    public void onRedditClick() {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.reddit.com/r/nilu")));
    }

    @OnClick(R.id.btnVisitWebsite)
    public void onVisitWebsiteClick() {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.nilu.tech")));
    }

    @OnClick(R.id.btnSendEmail)
    public void onSendEmailClick() {
        startActivity(new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:hi@nilu.tech")));
    }

    @OnClick(R.id.btnDonate)
    public void onDonateClick() {
        startActivity(new Intent(this, SendMoneyActivity.class)
                .setData(Uri.parse("http://nilu.tech/0x058902961713388B145152D4fe2b147285E0c7dA")));
    }

    @OnClick(R.id.btnPrivacyPolicy)
    public void onPrivacyPolicyClick() {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://nilu.tech/app_privacy_policy")));
    }
}

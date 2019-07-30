package tech.nilu.wallet.ui.password;

import android.arch.lifecycle.ViewModel;

import javax.inject.Inject;

import tech.nilu.wallet.repository.SecurityRepository;

/**
 * Created by root on 2/28/18.
 */

public class PasswordViewModel extends ViewModel {
    private final SecurityRepository securityRepository;

    @Inject
    public PasswordViewModel(SecurityRepository securityRepository) {
        this.securityRepository = securityRepository;
    }

    public void storeString(String key, String value) {
        securityRepository.storeString(key, value);
    }
}

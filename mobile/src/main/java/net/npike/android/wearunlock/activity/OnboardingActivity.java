package net.npike.android.wearunlock.activity;

import android.app.Activity;
import android.os.Bundle;

import net.npike.android.util.DevicePasswordManager;
import net.npike.android.wearunlock.R;
import net.npike.android.wearunlock.WearUnlockApp;
import net.npike.android.wearunlock.fragment.EncryptedWarningDialogFragment;
import net.npike.android.wearunlock.fragment.OnboardingConfigurePasswordFragment;
import net.npike.android.wearunlock.fragment.OnboardingDiscoveryFragment;
import net.npike.android.wearunlock.fragment.OnboardingRequestDeviceAdminFragment;
import net.npike.android.wearunlock.interfaces.OnboardingInterface;

public class OnboardingActivity extends Activity implements OnboardingInterface {

    public static final String TAG_WARNING_FRAG = "warning_frag";
    private static final String TAG_CONFIGURE_PASSWORD_FRAG = "configure_password_frag";
    private static final String TAG_REQUEST_ADMIN_FRAG = "request_admin_frag";
    private static final String TAG_WAIT_FOR_PEBBLE_DEVICE = "wait_for_pebble_frag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        DevicePasswordManager passwordManager = new DevicePasswordManager(this);

        if (savedInstanceState == null) {
            if (passwordManager.isDeviceEncrypted()) {
                new EncryptedWarningDialogFragment().show(getFragmentManager(), TAG_WARNING_FRAG);
            }
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_placeholder,
                            OnboardingDiscoveryFragment.getInstance(),
                            TAG_WAIT_FOR_PEBBLE_DEVICE).commit();
        }
    }

    @Override
    public void onPasswordConfigured() {
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_placeholder,
                        OnboardingRequestDeviceAdminFragment.getInstance(),
                        TAG_REQUEST_ADMIN_FRAG).commit();
    }

    @Override
    public void onPebbleFound(String address) {
        WearUnlockApp.getInstance().putPairedAndroidWearId(address);

        getFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_placeholder,
                        OnboardingConfigurePasswordFragment.getInstance(),
                        TAG_CONFIGURE_PASSWORD_FRAG).commit();


    }

}

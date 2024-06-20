package com.twd.chitboyapp.spsskl;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.KeyguardManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.app.ActivityCompat;

import com.twd.chitboyapp.spsskl.Handlers.FingerprintHandler;
import com.twd.chitboyapp.spsskl.constant.ConnectionUpdator;
import com.twd.chitboyapp.spsskl.constant.Constant;
import com.twd.chitboyapp.spsskl.constant.ConstantFunction;
import com.twd.chitboyapp.spsskl.constant.DateTimeChecker;
import com.twd.chitboyapp.spsskl.constant.MenuHandler;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class AuthanticateActivity extends AppCompatActivity implements View.OnClickListener {

    // Variable used for storing the key in the Android Keystore container
    private static final String KEY_NAME = "3wdsoft";
    private KeyStore keyStore;
    private Cipher cipher;

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authanticate);

        ConnectionUpdator connectionUpdator = new ConnectionUpdator(AuthanticateActivity.this);
        connectionUpdator.startObserve(AuthanticateActivity.this);

        AppCompatTextView errorText = findViewById(R.id.errorText);
        AppCompatEditText edtpin = findViewById(R.id.edtpinl);
        AppCompatTextView txtforgotpin = findViewById(R.id.txtforgotpin);
        txtforgotpin.setOnClickListener(this);
        TextView txtchangepin = findViewById(R.id.txtchangepin);
        txtchangepin.setOnClickListener(this);

        DateTimeChecker dateTimeChecker = new DateTimeChecker();
        dateTimeChecker.checkAutoDate(this, true);


        edtpin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String enteredPin = edtpin.getText().toString();
                if (enteredPin.length() == 4) {
                    validatePin();
                }
            }
        });

        edtpin.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    return validatePin();
                }
                return false;
            }
        });

        KeyguardManager keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
        FingerprintManager fingerprintManager = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            fingerprintManager = (FingerprintManager) getSystemService(FINGERPRINT_SERVICE);
            if (fingerprintManager != null) {
                // Check whether the device has a Fingerprint sensor.
                if (!fingerprintManager.isHardwareDetected()) {
                    /**
                     * An error message will be displayed if the device does not contain the fingerprint hardware.
                     * However if you plan to implement a default authentication method,
                     * you can redirect the user to a default authentication activity from here.
                     * Example:
                     * Intent intent = new Intent(this, DefaultAuthenticationActivity.class);
                     * startActivity(intent);
                     */
                    errorText.setText(getResources().getString(R.string.nofigureprintsensor));
                } else {
                    // Checks whether fingerprint permission is set on manifest
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
                        errorText.setText(getResources().getString(R.string.nofigureprintpermission));
                    } else {
                        // Check whether at least one fingerprint is registered
                        if (!fingerprintManager.hasEnrolledFingerprints()) {
                            errorText.setText(getResources().getString(R.string.nofigureprintregister));
                        } else {
                            // Checks whether lock screen security is enabled or not
                            if (!keyguardManager.isKeyguardSecure()) {
                                errorText.setText(getResources().getString(R.string.nolockenabled));
                            } else {
                                generateKey();

                                if (cipherInit()) {
                                    FingerprintManager.CryptoObject cryptoObject = new FingerprintManager.CryptoObject(cipher);
                                    FingerprintHandler helper = new FingerprintHandler(this);
                                    helper.startAuth(fingerprintManager, cryptoObject);
                                }
                            }
                        }
                    }
                }
            } else {
                errorText.setText(getResources().getString(R.string.nofigureprintsensor));
            }
        } else {
            errorText.setText(getResources().getString(R.string.nofigureprintsensor));
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.txtforgotpin) {
            startActivity(new Intent(AuthanticateActivity.this, ForgotPinActivity.class));
        } else if (view.getId() == R.id.txtchangepin) {
            startActivity(new Intent(AuthanticateActivity.this, ChangePinActivity.class));
        }

    }

    private boolean validatePin() {
        ConstantFunction cf = new ConstantFunction();
        String[] key = new String[]{getResources().getString(R.string.chitboyprefloginpin)};
        String[] data = cf.getSharedPrefValue(this, key, new String[]{""});
        String mypin = data[0];
        AppCompatEditText edtpin = findViewById(R.id.edtpinl);
        String enteredPin = edtpin.getText().toString();
        if (enteredPin.equals(mypin)) {
            Intent intent = new Intent(AuthanticateActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
            return true;
        } else {
            Constant.showToast(getResources().getString(R.string.errorwrongpin), AuthanticateActivity.this, R.drawable.ic_wrong);
        }
        return false;
    }

    @TargetApi(Build.VERSION_CODES.M)
    protected void generateKey() {
        try {
            keyStore = KeyStore.getInstance("AndroidKeyStore");
        } catch (Exception e) {
            e.printStackTrace();
        }

        KeyGenerator keyGenerator;
        try {
            keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            throw new RuntimeException("Failed to get KeyGenerator instance", e);
        }

        try {
            keyStore.load(null);
            keyGenerator.init(new
                    KeyGenParameterSpec.Builder(KEY_NAME,
                    KeyProperties.PURPOSE_ENCRYPT |
                            KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    .setUserAuthenticationRequired(true)
                    .setEncryptionPaddings(
                            KeyProperties.ENCRYPTION_PADDING_PKCS7)
                    .build());
            keyGenerator.generateKey();
        } catch (NoSuchAlgorithmException |
                 InvalidAlgorithmParameterException
                 | CertificateException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    public boolean cipherInit() {
        try {
            cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/" + KeyProperties.BLOCK_MODE_CBC + "/" + KeyProperties.ENCRYPTION_PADDING_PKCS7);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new RuntimeException("Failed to get Cipher", e);
        }

        try {
            keyStore.load(null);
            SecretKey key = (SecretKey) keyStore.getKey(KEY_NAME,
                    null);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return true;
        } catch (KeyPermanentlyInvalidatedException e) {
            return false;
        } catch (KeyStoreException | CertificateException | UnrecoverableKeyException |
                 IOException | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("Failed to init Cipher", e);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.setting_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        MenuHandler cf = new MenuHandler();
        return cf.openCommon(this, item, null);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuHandler cf = new MenuHandler();
        return cf.performRefreshOption(menu, AuthanticateActivity.this);
    }
}
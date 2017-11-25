package girard_levasseur.utt.fr.poke_if26.activities.login;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import girard_levasseur.utt.fr.poke_if26.R;
import girard_levasseur.utt.fr.poke_if26.exceptions.BadCredentialsException;
import girard_levasseur.utt.fr.poke_if26.exceptions.ImpossibleActionException;
import girard_levasseur.utt.fr.poke_if26.services.LoginService;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    @Inject
    public LoginService loginService;

    // UI references.
    private EditText mUsernameEditText;
    private EditText mPasswordEditText;
    private ProgressBar mLoginProgressBar;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this); // Dagger stuff.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mUsernameEditText = (EditText) findViewById(R.id.username);

        mPasswordEditText = (EditText) findViewById(R.id.password);
        mPasswordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginProgressBar = findViewById(R.id.login_progress);

        mLoginFormView = findViewById(R.id.login_form);
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        // Reset errors.
        mUsernameEditText.setError(null);
        mPasswordEditText.setError(null);

        // Store values at the time of the login attempt.
        String username = mUsernameEditText.getText().toString();
        String password = mPasswordEditText.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            mPasswordEditText.setError(getString(R.string.invalid_password_error));
            focusView = mPasswordEditText;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(username)) {
            mUsernameEditText.setError(getString(R.string.invalid_username_error));
            focusView = mUsernameEditText;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            try {
                mLoginProgressBar.setVisibility(View.VISIBLE);
                loginService.login(username, password.toCharArray())
                        .delay(2000, TimeUnit.MILLISECONDS, true)
                        .subscribe(loggedInUser -> {
                            mLoginProgressBar.setVisibility(View.INVISIBLE);
                            // TODO: Go to the next activity.
                        }, err -> {
                            mLoginProgressBar.setVisibility(View.INVISIBLE);
                            if (err instanceof BadCredentialsException) {
                                // Display a dialog ON THE UI THREAD, remember that the db call has been done on another thread!
                                this.runOnUiThread(() -> {
                                    // Display the login error dialog.
                                    new AlertDialog.Builder(this)
                                            .setTitle(R.string.bad_credential_dialog_title)
                                            .setMessage(R.string.bad_credential_dialog_message)
                                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                    mPasswordEditText.requestFocus();
                                                }
                                            })
                                            .create()
                                            .show();
                                });
                            }
                        });
            } catch (Exception e) {
                mLoginProgressBar.setVisibility(View.INVISIBLE);
                e.printStackTrace();
            }
        }
    }
}


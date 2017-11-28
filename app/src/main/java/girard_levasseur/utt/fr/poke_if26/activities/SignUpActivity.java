package girard_levasseur.utt.fr.poke_if26.activities;

import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import girard_levasseur.utt.fr.poke_if26.R;
import girard_levasseur.utt.fr.poke_if26.exceptions.AlreadyExistingUsernameException;
import girard_levasseur.utt.fr.poke_if26.services.UserService;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * A login screen that offers login via email/password.
 */
public class SignUpActivity extends AppCompatActivity {

    @Inject
    public UserService userService;

    // UI references.
    private EditText mUsernameEditText;
    private EditText mPasswordEditText;
    private EditText mPasswordConfirmationEditText;
    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this); // Dagger stuff.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        // Set up the login form.
        mUsernameEditText = findViewById(R.id.username);
        mPasswordEditText = findViewById(R.id.password);
        mPasswordConfirmationEditText = findViewById(R.id.password_confirmation);
        mPasswordConfirmationEditText.setOnEditorActionListener((textView, id, keyEvent) -> {
            if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                attemptSignUp();
                return true;
            }
            return false;
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.sign_up_button);
        mEmailSignInButton.setOnClickListener(view -> attemptSignUp());

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    private void attemptSignUp() {
        // Reset errors.
        mUsernameEditText.setError(null);
        mPasswordEditText.setError(null);
        mPasswordConfirmationEditText.setError(null);

        // Store values at the time of the login attempt.
        String username = mUsernameEditText.getText().toString();
        String password = mPasswordEditText.getText().toString();
        String passwordConfirmation = mPasswordConfirmationEditText.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password) || !isPasswordValid(password)) {
            mPasswordEditText.setError(getString(R.string.invalid_password_length_error));
            focusView = mPasswordEditText;
            cancel = true;
        }

        // Check the password confirmation field.
        if (!passwordConfirmation.equals(password)) {
            mPasswordConfirmationEditText
                    .setError(getString(R.string.invalid_password_confirmation_error));
            focusView = mPasswordConfirmationEditText;
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
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            mLoginFormView.setVisibility(View.GONE);
            mProgressView.setVisibility(View.VISIBLE);
            this.userService.registerUser(username, password.toCharArray())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(newUser -> {
                        mLoginFormView.setVisibility(View.VISIBLE);
                        mProgressView.setVisibility(View.GONE);

                        // Display the login error dialog.
                        new AlertDialog.Builder(this)
                                .setTitle(R.string.account_created_modal_title)
                                .setMessage(R.string.account_created_modal_message)
                                .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                                    dialog.dismiss();
                                    NavUtils.navigateUpFromSameTask(this);
                                })
                                .create()
                                .show();
                    }, err -> {
                        if (err instanceof AlreadyExistingUsernameException) {
                            mLoginFormView.setVisibility(View.VISIBLE);
                            mProgressView.setVisibility(View.GONE);

                            mUsernameEditText.setError(
                                    getString(R.string.already_existing_username_error));
                            mUsernameEditText.requestFocus();
                        } else {
                            Log.e(SignUpActivity.class.getName(), "Unrecoverable exception", err);
                        }
                    });
        }
    }

    private boolean isPasswordValid(String password) {
        return password.length() >= 8;
    }
}


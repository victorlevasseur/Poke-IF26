package girard_levasseur.utt.fr.poke_if26.fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import girard_levasseur.utt.fr.poke_if26.R;
import io.reactivex.Completable;

/**
 * A dialog fragment to let the user type a new password.
 */
public class ChangePasswordDialogFragment extends DialogFragment {

    private EditText passwordEditText;
    private EditText passwordConfirmationEditText;
    private ProgressBar progressBar;

    private PasswordListener passwordListener = null;

    public ChangePasswordDialogFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ChangePasswordDialogFragment.
     */
    public static ChangePasswordDialogFragment newInstance() {
        ChangePasswordDialogFragment fragment = new ChangePasswordDialogFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.fragment_change_password_dialog, null);

        passwordEditText = dialogView.findViewById(R.id.newPasswordEditText);
        passwordConfirmationEditText = dialogView.findViewById(R.id.newPasswordConfirmationEditText);
        progressBar = dialogView.findViewById(R.id.progressBar);

        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setView(dialogView)
                .setTitle(R.string.change_password_dialog_title)
                .setPositiveButton(android.R.string.ok, (dialogInterface, button) -> {
                    // See why it's empty in onResume().
                })
                .setNegativeButton(android.R.string.cancel, (dialogInterface, button) -> {

                })
                .create();

        return dialog;
    }

    @Override
    public void onResume() {
        super.onResume();

        AlertDialog dialog = (AlertDialog)getDialog();
        // Override the button listener as the one defined in the builder can't prevent the dialog
        // from closing afterwards.
        dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                .setOnClickListener((view) -> {
                    passwordEditText.setError(null);
                    passwordConfirmationEditText.setError(null);

                    // Check password length
                    if (passwordEditText.getText().toString().length() < 8) {
                        passwordEditText
                                .setError(getString(R.string.invalid_password_length_error));
                        return;
                    }

                    // Check equality with the confirmation password edit text.
                    if (!passwordEditText.getText().toString()
                            .equals(passwordConfirmationEditText.getText().toString())) {
                        passwordConfirmationEditText
                                .setError(getString(R.string.invalid_password_confirmation_error));
                        return;
                    }

                    if (passwordListener != null) {
                        // Display the loading spinner and call the password listener method
                        // waiting for its returned completable to complete.
                        showSpinner(true);
                        passwordListener.onNewPasswordSelected(passwordEditText.getText().toString())
                                .subscribe(() -> {
                                    dialog.dismiss();
                                });
                    } else {
                        dialog.dismiss();
                    }
                });
    }

    public void setPasswordListener(PasswordListener listener) {
        passwordListener = listener;
    }

    public interface PasswordListener {
        Completable onNewPasswordSelected(String password);
    }

    private void showSpinner(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        passwordEditText.setVisibility(show ? View.INVISIBLE : View.VISIBLE);
        passwordConfirmationEditText.setVisibility(show ? View.INVISIBLE : View.VISIBLE);
    }

}

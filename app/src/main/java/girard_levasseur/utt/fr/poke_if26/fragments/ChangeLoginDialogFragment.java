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
 * A dialog fragment to let the user type a new login.
 */
public class ChangeLoginDialogFragment extends DialogFragment {

    private EditText loginEditText;
    private ProgressBar progressBar;

    private LoginListener loginListener = null;

    public ChangeLoginDialogFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ChangeLoginDialogFragment.
     */
    public static ChangeLoginDialogFragment newInstance() {
        ChangeLoginDialogFragment fragment = new ChangeLoginDialogFragment();
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
        View dialogView = inflater.inflate(R.layout.fragment_change_login_dialog, null);

        loginEditText = dialogView.findViewById(R.id.newLoginEditText);
        progressBar = dialogView.findViewById(R.id.progressBar);

        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setView(dialogView)
                .setTitle(R.string.change_login_dialog_title)
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
                    loginEditText.setError(null);

                    // Check password length
                    if (loginEditText.getText().toString().isEmpty()) {
                        // TODO: Text message
                        loginEditText
                                .setError(getString(R.string.invalid_username_error));
                        return;
                    }

                    if (loginListener != null) {
                        // Display the loading spinner and call the password listener method
                        // waiting for its returned completable to complete.
                        showSpinner(true);
                        loginListener.onNewLoginSelected(loginEditText.getText().toString())
                                .subscribe(() -> {
                                    dialog.dismiss();
                                }, (error) -> {
                                    showSpinner(false);
                                    if (error instanceof LoginListener.LoginChangeDialogError) {
                                        loginEditText.setError(getString((
                                                (LoginListener.LoginChangeDialogError) error)
                                                .getLoginChangeDialogErrorStringId()));
                                    }
                                });
                    } else {
                        dialog.dismiss();
                    }
                });
    }

    public void setLoginListener(LoginListener listener) {
        loginListener = listener;
    }

    /**
     * Implement this class to listen to the change login dialog.
     * You may error the Completable with an object implementing LoginChangeError so that
     * the string ID returned by its getLoginChangeDialogErrorStringId() is displayed to the user.
     *
     * Don't forget to register the listener to the dialog with setLoginListener.
     */
    public interface LoginListener {
        Completable onNewLoginSelected(String login);

        interface LoginChangeDialogError {
            int getLoginChangeDialogErrorStringId();
        }
    }

    private void showSpinner(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        loginEditText.setVisibility(show ? View.INVISIBLE : View.VISIBLE);
    }

}

package girard_levasseur.utt.fr.poke_if26.fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import girard_levasseur.utt.fr.poke_if26.R;
import girard_levasseur.utt.fr.poke_if26.activities.LoginActivity;
import girard_levasseur.utt.fr.poke_if26.services.LoginService;
import girard_levasseur.utt.fr.poke_if26.services.UserService;
import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * The settings page fragment
 */
public class SettingsFragment extends Fragment {

    @Inject
    public LoginService loginService;

    @Inject
    public UserService userService;

    private ListView menuListView;

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_settings, container, false);

        menuListView = v.findViewById(R.id.settingsListView);
        menuListView.setOnItemClickListener((adapter, view, index, id) -> {
            if (index == SettingsItems.CHANGE_PASSWORD_ITEM.getIndex()) {
                ChangePasswordDialogFragment modal = ChangePasswordDialogFragment.newInstance();
                modal.setPasswordListener((newPassword) -> {
                    return userService.changeUserPassword(
                            loginService.getConnectedUser(),
                            newPassword.toCharArray())
                            .delay(500, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread(), true)
                            .doAfterTerminate(loginService::refreshConnectedUser); // Refresh the connected user object after its update
                });
                modal.show(getFragmentManager(), "modal");
            } else if (index == SettingsItems.CHANGE_LOGIN_ITEM.getIndex()) {
                ChangeLoginDialogFragment modal = ChangeLoginDialogFragment.newInstance();
                modal.setLoginListener((newLogin) -> {
                    return userService.changeUserLogin(
                            loginService.getConnectedUser(),
                            newLogin)
                            .delay(500, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread(), true)
                            .doAfterTerminate(loginService::refreshConnectedUser); // Refresh the connected user object after its update
                });
                modal.show(getFragmentManager(), "modal");
            } else if (index == SettingsItems.DELETE_ACCOUNT.getIndex()) {
                new AlertDialog.Builder(getActivity())
                        .setTitle(R.string.delete_account_dialog_title)
                        .setMessage(R.string.delete_account_dialog_message)
                        .setPositiveButton(android.R.string.yes, (dialog, button) -> {
                            userService.deleteUser(loginService.getConnectedUser())
                                    .subscribe(() -> {
                                        loginService.logout();
                                        startActivity(new Intent(getActivity(), LoginActivity.class));
                                    });
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .create()
                        .show();
            } else {
                Log.e(SettingsFragment.class.getName(), "Unknown item selected!");
            }
        });

        // Inflate the layout for this fragment
        return v;
    }

    @Override
    public void onAttach(Context context) {
        AndroidInjection.inject(this);

        super.onAttach(context);
    }

}

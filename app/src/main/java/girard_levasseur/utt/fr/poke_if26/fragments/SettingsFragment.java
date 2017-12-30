package girard_levasseur.utt.fr.poke_if26.fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import girard_levasseur.utt.fr.poke_if26.R;

/**
 * The settings page fragment
 */
public class SettingsFragment extends Fragment {

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

            } else if (index == SettingsItems.CHANGE_LOGIN_ITEM.getIndex()) {

            } else if (index == SettingsItems.DELETE_ACCOUNT.getIndex()) {

            } else {
                Log.e(SettingsFragment.class.getName(), "Unknown item selected!");
            }
        });

        // Inflate the layout for this fragment
        return v;
    }

}

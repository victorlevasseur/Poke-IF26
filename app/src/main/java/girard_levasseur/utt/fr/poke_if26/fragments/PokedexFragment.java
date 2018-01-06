package girard_levasseur.utt.fr.poke_if26.fragments;

import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import dagger.android.AndroidInjection;
import girard_levasseur.utt.fr.poke_if26.R;
import girard_levasseur.utt.fr.poke_if26.dto.FetchedPokemonInstance;
import girard_levasseur.utt.fr.poke_if26.entities.User;
import girard_levasseur.utt.fr.poke_if26.services.LoginService;
import girard_levasseur.utt.fr.poke_if26.services.PokemonsService;
import io.reactivex.Single;

import java.util.List;

import javax.inject.Inject;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class PokedexFragment extends Fragment {

    @Inject
    public LoginService loginService;

    @Inject
    public PokemonsService pokemonsService;

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private RecyclerView recyclerView;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public PokedexFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static PokedexFragment newInstance(int columnCount) {
        PokedexFragment fragment = new PokedexFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pokedex_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            this.recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                this.recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                this.recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }

            User currentUser = loginService.getConnectedUser();
            Single<List<FetchedPokemonInstance>> capturedPokemons = this.pokemonsService
                    .getCapturedFetchedPokemonsByUser(currentUser);
            capturedPokemons.subscribe(this::initializeRecyclerView);
        }
        return view;
    }

    public void initializeRecyclerView(List<FetchedPokemonInstance> pokemons) {
        this.recyclerView.setAdapter(new MyPokedexRecyclerViewAdapter(pokemons, mListener));
    }


    @Override
    public void onAttach(Context context) {
        AndroidInjection.inject(this);

        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(FetchedPokemonInstance item);
    }
}

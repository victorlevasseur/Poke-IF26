package girard_levasseur.utt.fr.poke_if26.fragments;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import girard_levasseur.utt.fr.poke_if26.R;
import girard_levasseur.utt.fr.poke_if26.fragments.PokedexFragment.OnListFragmentInteractionListener;
import me.sargunvohra.lib.pokekotlin.model.Pokemon;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Pokemon} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyPokedexRecyclerViewAdapter extends RecyclerView.Adapter<MyPokedexRecyclerViewAdapter.ViewHolder> {

    private final List<Pokemon> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MyPokedexRecyclerViewAdapter(List<Pokemon> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_pokedex, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Pokemon pokemon = mValues.get(position);
        holder.mItem = pokemon;
        String id = (new Integer(pokemon.getId())).toString();
        holder.mIdView.setText(id);
        holder.mContentView.setText(pokemon.getName());

        try {
            Picasso.with(holder.mImageView.getContext()).load(pokemon.getSprites().getFrontDefault()).into(holder.mImageView);
        } catch (Exception e) {
            // Pas d'image
            Log.e("PokedexImg","No image");
        }

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public final ImageView mImageView;
        public Pokemon mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.id_pokemon);
            mContentView = (TextView) view.findViewById(R.id.content);
            mImageView = (ImageView) view.findViewById(R.id.avatar_imageview);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}

package girard_levasseur.utt.fr.poke_if26.services.impl;

import android.app.Activity;
import android.app.AlertDialog;

import java.io.IOException;

import javax.inject.Inject;

import girard_levasseur.utt.fr.poke_if26.R;
import girard_levasseur.utt.fr.poke_if26.activities.MainActivity;
import girard_levasseur.utt.fr.poke_if26.di.PerActivityScope;
import girard_levasseur.utt.fr.poke_if26.services.NetworkErrorRetryerService;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.Single;

/**
 * Created by victor on 05/01/18.
 */
@PerActivityScope
public class NetworkErrorRetryerServiceImpl implements NetworkErrorRetryerService {

    Activity activity;

    @Inject
    public NetworkErrorRetryerServiceImpl(MainActivity activity) {
        // Inject the activity into the service and keep it as a context for the alert dialog.
        this.activity = activity;
    }

    @Override
    public <T> Single<T> retryIfNetworkError(Single<T> source) {
        return source.retryWhen(errors -> errors.flatMap(error -> {
            if (!(error instanceof IOException)) {
                return Flowable.error(error);
            }
            return Flowable.create((emitter) -> {
                new AlertDialog.Builder(activity)
                        .setTitle(R.string.network_error_dialog_title)
                        .setMessage(R.string.network_error_dialog_message)
                        .setPositiveButton(android.R.string.yes, (dialog, button) -> {
                            emitter.onNext(1);
                        })
                        .setNegativeButton(android.R.string.cancel, (dialog, which) -> {
                            emitter.onError(error);
                        })
                        .create()
                        .show();
            }, BackpressureStrategy.LATEST);
        }));
    }

}

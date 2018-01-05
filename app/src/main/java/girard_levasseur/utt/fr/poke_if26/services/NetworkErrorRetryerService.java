package girard_levasseur.utt.fr.poke_if26.services;

import io.reactivex.Single;

/**
 * A service that provides tools to retry observables/singles if a network failure happened
 * (IOException).
 *
 * Note: Only usable in the MainActivity and its child fragments.
 */
public interface NetworkErrorRetryerService {

    /**
     * If the sources fails with an IOException, a dialog will be displayed allowing
     * the user to retry.
     * @param source the single that may throw an UnknownHostException
     * @return a single that will be retries if an UnknownHostException is emitted and the user
     * clicks on retry on the alert dialog.
     */
    <T> Single<T> retryIfNetworkError(Single<T> source);

}

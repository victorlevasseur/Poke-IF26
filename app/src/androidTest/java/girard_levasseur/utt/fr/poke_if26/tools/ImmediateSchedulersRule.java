package girard_levasseur.utt.fr.poke_if26.tools;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;

/**
 * A test rule to make the observable execute in the main thread for testing.
 */
public class ImmediateSchedulersRule implements TestRule {
    @Override
    public Statement apply(final Statement base, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                RxJavaPlugins.setIoSchedulerHandler(scheduler ->
                        Schedulers.trampoline());
                RxJavaPlugins.setComputationSchedulerHandler(scheduler ->
                        Schedulers.trampoline());
                RxJavaPlugins.setNewThreadSchedulerHandler(scheduler ->
                        Schedulers.trampoline());

                try {
                    base.evaluate();
                } finally {
                    RxJavaPlugins.reset();
                }
            }
        };
    }
}
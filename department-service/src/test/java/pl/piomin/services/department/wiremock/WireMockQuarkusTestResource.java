package pl.piomin.services.department.wiremock;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.common.Notifier;
import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import org.jboss.logging.Logger;

import java.util.HashMap;
import java.util.Map;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

public class WireMockQuarkusTestResource implements QuarkusTestResourceLifecycleManager {
    private static final Logger LOGGER = Logger.getLogger(WireMockQuarkusTestResource.class);

    private WireMockServer wireMockServer;

    @Override
    public Map<String, String> start() {
        final HashMap<String, String> result = new HashMap<>();

        this.wireMockServer = new WireMockServer(options()
                .dynamicPort()
                .notifier(createNotifier(true)));
        this.wireMockServer.start();

        return result;
    }

    @Override
    public void stop() {
        if (this.wireMockServer != null) {
            this.wireMockServer.stop();
            this.wireMockServer = null;
        }
    }

    @Override
    public void inject(final TestInjector testInjector) {
        testInjector.injectIntoFields(wireMockServer,
                new TestInjector.AnnotatedAndMatchesType(InjectWireMock.class, WireMockServer.class));
    }

    private static Notifier createNotifier(final boolean verbose) {
        final String prefix = "[WireMock] ";
        return new Notifier() {

            @Override
            public void info(final String s) {
                if (verbose) {
                    LOGGER.info(prefix + s);
                }
            }

            @Override
            public void error(final String s) {
                LOGGER.warn(prefix + s);
            }

            @Override
            public void error(final String s, final Throwable throwable) {
                LOGGER.warn(prefix + s, throwable);
            }
        };
    }
}

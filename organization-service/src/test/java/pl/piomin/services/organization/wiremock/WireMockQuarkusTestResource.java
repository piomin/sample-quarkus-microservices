package pl.piomin.services.organization.wiremock;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

import java.util.HashMap;
import java.util.Map;

import org.jboss.logging.Logger;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.common.Notifier;
import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;

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

        result.put("quarkus.rest-client.farmer-api.url", wireMockServer.baseUrl()/* + "/someBasePath"*/);

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
        // I like the emojis, It is easier for me to distinguish the different logs.
        final String prefix = "\uD83C\uDF44 [WireMock] ";
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
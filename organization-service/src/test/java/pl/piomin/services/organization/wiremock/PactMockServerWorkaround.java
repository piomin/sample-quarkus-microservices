package pl.piomin.services.organization.wiremock;

import java.util.List;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import au.com.dius.pact.consumer.MockServer;
import au.com.dius.pact.consumer.junit5.ProviderInfo;
import kotlin.Pair;

public class PactMockServerWorkaround implements ParameterResolver {
    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
            throws ParameterResolutionException {

        return parameterContext.getParameter().getType() == PactMockServer.class;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
            throws ParameterResolutionException {

        // This is all super hacky, but it works. I guess Markus will kill me though,
        // because we have already so many workarounds ;) Looking forward to junit 5.10 <3.
        final ExtensionContext.Store store = extensionContext.getStore(ExtensionContext.Namespace.create("pact-jvm"));

        if (store.get("providers") == null) {
            return null;
        }

        final List<Pair<ProviderInfo, List<String>>> providers = store.get("providers", List.class);
        var pair = providers.get(0);
        final ProviderInfo providerInfo = pair.getFirst();

        var mockServer = store.get("mockServer:" + providerInfo.getProviderName(),
                MockServer.class);

        return new PactMockServer(mockServer.getUrl(), mockServer.getPort());
    }
}

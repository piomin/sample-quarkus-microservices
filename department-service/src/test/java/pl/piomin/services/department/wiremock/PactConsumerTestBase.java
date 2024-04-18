package pl.piomin.services.department.wiremock;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.BeforeEach;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class PactConsumerTestBase {

    @InjectWireMock
    protected WireMockServer wiremock;

    @BeforeEach
    void initWiremockBeforeEach() {
        wiremock.resetAll();
        configureFor(new WireMock(this.wiremock));
    }

    protected void forwardToPactServer(final PactMockServer wrapper) {
        wiremock.resetAll();
        stubFor(any(anyUrl()).atPriority(1).willReturn(aResponse().proxiedFrom(wrapper.getUrl())));
    }

}

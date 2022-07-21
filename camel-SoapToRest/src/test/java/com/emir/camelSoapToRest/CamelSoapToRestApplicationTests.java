package com.emir.camelSoapToRest;

import com.emir.camelSoapToRest.routes.SoapToRest;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/*@RunWith(SpringRunner.class)
@SpringBootTest*/
public class CamelSoapToRestApplicationTests extends CamelTestSupport {

    @Override
    protected RoutesBuilder createRouteBuilder() throws Exception {
        return new SoapToRest();
    }

    /*@Test
    public void sampleMockTest() throws Exception {
        String expected = "Hello";

        MockEndpoint mock = getMockEndpoint("mock:output");
        mock.expectedBodiesReceived(expected);
        String input = "Hello";
        template.sendBody("direct:celsius-to-fahrenheit", input);
        assertMockEndpointsSatisfied();
    }*/

}

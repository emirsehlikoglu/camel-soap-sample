package com.emir.camelSoapToRest.routes;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.cxf.common.message.CxfConstants;
import org.apache.cxf.message.MessageContentsList;
import org.springframework.stereotype.Component;

import com.learnwebservices.services.tempconverter.CelsiusToFahrenheitRequest;
import com.learnwebservices.services.tempconverter.CelsiusToFahrenheitResponse;
import com.learnwebservices.services.tempconverter.FahrenheitToCelsiusRequest;
import com.learnwebservices.services.tempconverter.FahrenheitToCelsiusResponse;

@Component
public class SoapToRest extends RouteBuilder{

	@Override
	public void configure() throws Exception {

		//deadLetterChannel Route
		/*from("direct:celsius-to-fahrenheit")
				.errorHandler(deadLetterChannel("mock:error")
						.maximumRedeliveries(3)
						.redeliveryDelay(1000).backOffMultiplier(2).useOriginalMessage().useExponentialBackOff())
				.transform(body().append(" Modified data!!!!!!"))
				.to("mock:result");*/
		
		from("direct:celsius-to-fahrenheit")
		.removeHeaders("CamelHttp*")
				.errorHandler(deadLetterChannel("mock:error").onPrepareFailure(new MyPrepareProcessor())
						.maximumRedeliveries(3)
						.redeliveryDelay(100).backOffMultiplier(2).useOriginalMessage().useExponentialBackOff())
				.transform(body().append(" Modified data!!!!!!"))
		.process(new Processor() {
			@Override
			public void process(Exchange exchange) throws Exception {
				CelsiusToFahrenheitRequest c = new CelsiusToFahrenheitRequest();
				c.setTemperatureInCelsius(Double.valueOf(exchange.getIn().getHeader("num").toString()));
				exchange.getIn().setBody(c);
			}
		})
		.setHeader(CxfConstants.OPERATION_NAME, constant("{{endpoint.operation.celsius.to.fahrenheit}}"))
		.setHeader(CxfConstants.OPERATION_NAMESPACE, constant("{{endpoint.namespace}}"))
		.to("cxf:bean:cxfConvertTemp")
		.process(new Processor() {
			@Override
			public void process(Exchange exchange) throws Exception {
				MessageContentsList response = (MessageContentsList) exchange.getIn().getBody();
				CelsiusToFahrenheitResponse r = (CelsiusToFahrenheitResponse) response.get(0);
				exchange.getIn().setBody("Temp in Fahrenheit: "+r.getTemperatureInFahrenheit());
			}
		})
		.to("mock:output");
		
		from("direct:fahrenheit-to-celsius")
		.removeHeaders("CamelHttp*")
		.process(new Processor() {
			@Override
			public void process(Exchange exchange) throws Exception {
				FahrenheitToCelsiusRequest r = new FahrenheitToCelsiusRequest();
				r.setTemperatureInFahrenheit(Double.valueOf(exchange.getIn().getHeader("num").toString()));
				exchange.getIn().setBody(r);
			}
		})
		.setHeader(CxfConstants.OPERATION_NAME, constant("{{endpoint.operation.fahrenheit.to.celsius}}"))
		.setHeader(CxfConstants.OPERATION_NAMESPACE, constant("{{endpoint.namespace}}"))
		.to("cxf:bean:cxfConvertTemp")
		.process(new Processor() {
			@Override
			public void process(Exchange exchange) throws Exception {
				MessageContentsList response = (MessageContentsList) exchange.getIn().getBody();
				FahrenheitToCelsiusResponse r = (FahrenheitToCelsiusResponse) response.get(0);
				exchange.getIn().setBody("Temp in Celsius: "+r.getTemperatureInCelsius());
			}
		})
		.to("mock:output");
		
	}

	public static class MyPrepareProcessor implements Processor {

		@Override
		public void process(Exchange exchange) throws Exception {
			Exception cause = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Exception.class);
			exchange.getIn().setHeader("************Failed because***********:", cause.getMessage());
		}
	}
	
	
	

}

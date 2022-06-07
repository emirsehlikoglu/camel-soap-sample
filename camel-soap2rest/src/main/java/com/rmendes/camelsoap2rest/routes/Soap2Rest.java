package com.rmendes.camelsoap2rest.routes;

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
public class Soap2Rest extends RouteBuilder{

	@Override
	public void configure() throws Exception {
		
		from("direct:celsius-to-fahrenheit")
		.removeHeaders("CamelHttp*")
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
	
	
	

}

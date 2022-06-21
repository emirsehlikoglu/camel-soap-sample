# camel-soap-sample
Using Apache Camel and consuming SOAP web service <br />
<br> REST Service Producer </br>
<br> Using Camel components (direct, routeBuilder, CXF, Undertow etc.) </br>
<br> Using DeadLetterChannel for Error Handling mechanism and implement Processor for Exception Handling </br>

********************Build and Run:********************

Build project: mvn clean install <br />
Running project with Maven: mvn spring-boot:run <br />
API Documantation: http://0.0.0.0:9090/api-doc <br />

WSDL address: http://www.learnwebservices.com/services/tempconverter?wsdl <br />
REST GET request: http://0.0.0.0:9090/convert/celsius/to/fahrenheit/num
(Replace "num" with the number you want to convert celsius to fahrenheit) <br />
REST GET request: http://0.0.0.0:9090/convert/fahrenheit/to/celsius/num <br />



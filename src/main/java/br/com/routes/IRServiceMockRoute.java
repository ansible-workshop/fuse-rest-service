package br.com.routes;

import br.com.domain.ErrorMessage;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.stereotype.Component;

@Component
public class IRServiceMockRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        //@formatter:off
        from("direct:response-mock")
                .routeId("direct-response-mock")
                    .description("")
                    .choice()
                        .when(header("mock_type").isEqualTo("ok"))
                            .log("ROTA 1")
                            .marshal()
                            .json(JsonLibrary.Jackson, true)
                            .to("language:constant:resource:classpath:/response/response_ok.json")
                        .when(header("mock_type").isEqualTo("error"))
                            .log("ROTA 2")
                            .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(400))
                            .setBody(constant(new ErrorMessage(400, "Bad Request :: Body is Null")))
                            .marshal()
                            .json(JsonLibrary.Jackson, true)
                            .log("${body}")
                            .to("language:constant:resource:classpath:/response/response_error.json")
                        .otherwise()
                            .log("ROTA 3")
                            .marshal()
                            .json(JsonLibrary.Jackson, true)
                            .to("language:constant:resource:classpath:/response/response_ok.json")
                    .endChoice()
                .end();
    }
}

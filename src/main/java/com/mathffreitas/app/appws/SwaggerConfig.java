package com.mathffreitas.app.appws;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.hateoas.client.LinkDiscoverer;
import org.springframework.hateoas.client.LinkDiscoverers;
import org.springframework.hateoas.mediatype.collectionjson.CollectionJsonLinkDiscoverer;
import org.springframework.plugin.core.SimplePluginRegistry;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.VendorExtension;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    Contact contact = new Contact(
            "Matheus Freitas",
            "https://www.linkedin.com/in/matheusfilipefreitas/",
            ""
    );

    List<VendorExtension> vendorExtensions = new ArrayList<>();

    ApiInfo apiInfo = new ApiInfo("Photo app RESTful Web Service documentation",
            "This pages documents Photo app RESTful Web Service endpoints",
            "1.0",
            "",
            contact,
            "Matheus Freitas - Github",
            "http://www.github.com/MatheusFilipeFreitas",
            vendorExtensions);

    @Bean
    public Docket apiDocket() {
        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .protocols(new HashSet<>(Arrays.asList("HTTP","HTTPs")))
                .apiInfo(apiInfo)
                .select().apis(RequestHandlerSelectors.basePackage("com.mathffreitas.app.appws"))
                .paths(PathSelectors.any())
                .build();
        return docket;
    }

    @Bean
    public LinkDiscoverers discoverers() {
        List<LinkDiscoverer> plugins = new ArrayList<>();
        plugins.add(new CollectionJsonLinkDiscoverer());
        return new LinkDiscoverers(SimplePluginRegistry.create(plugins));
    }

}

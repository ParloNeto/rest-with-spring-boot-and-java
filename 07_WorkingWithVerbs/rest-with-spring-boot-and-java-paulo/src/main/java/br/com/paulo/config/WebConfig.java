package br.com.paulo.config;

import br.com.paulo.serialization.converter.YamlJackson2HttpMesageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

import static br.com.paulo.util.MediaType.APPLICATION_YML;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private static final MediaType MEDIA_TYPE_APPLICATION_YML = MediaType.valueOf(APPLICATION_YML);

    @Value("${cors.originPatterns:default}")
    private String corsOriginPatterns = "";

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        var allowedOrigins = corsOriginPatterns.split(",");
        registry.addMapping("/**")
//                .allowedMethods("GET", "POST", "PUT")
                .allowedMethods("*")
                .allowedOrigins(allowedOrigins)
                .allowCredentials(true);
    }
    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add((new YamlJackson2HttpMesageConverter()));
    }


    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
//       configurer.favorParameter(true).
//               parameterName("mediaType").
//               ignoreAcceptHeader(true).
//               useRegisteredExtensionsOnly(false).
//               defaultContentType(MediaType.APPLICATION_JSON).mediaType("json", MediaType.APPLICATION_JSON).
//               defaultContentType(MediaType.APPLICATION_JSON).mediaType("xml", MediaType.APPLICATION_XML);

        configurer.favorParameter(false).
                ignoreAcceptHeader(false).
                useRegisteredExtensionsOnly(false).
                    defaultContentType(MediaType.APPLICATION_JSON)
                        .mediaType("json", MediaType.APPLICATION_JSON)
                        .mediaType("xml", MediaType.APPLICATION_XML)
                        .mediaType("x-yaml", MEDIA_TYPE_APPLICATION_YML)
                        ;

    }


}

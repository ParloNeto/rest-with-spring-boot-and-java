package br.com.paulo.serialization.converter;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter;

import static br.com.paulo.util.MediaType.APPLICATION_YML;

public class YamlJackson2HttpMesageConverter extends AbstractJackson2HttpMessageConverter {
    public YamlJackson2HttpMesageConverter() {
        super(new YAMLMapper().setSerializationInclusion(
                JsonInclude.Include.NON_NULL),
                MediaType.parseMediaType(APPLICATION_YML)
        );
    }
}

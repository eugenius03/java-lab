package com.car_rental.serializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class YamlDataSerializer<T> extends AbstractDataSerializer<T> {

    private static final Logger logger = LoggerFactory.getLogger(YamlDataSerializer.class);

    public YamlDataSerializer() {
        super(createDefaultYamlObjectMapper());
        logger.debug("YamlDataSerializer initialized with custom YAML configuration");
    }

    public YamlDataSerializer(ObjectMapper objectMapper) {
        super(objectMapper);
        logger.debug("YamlDataSerializer initialized with custom ObjectMapper");
    }

    private static ObjectMapper createDefaultYamlObjectMapper() {
        YAMLFactory yamlFactory = new YAMLFactory()
                .disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER)
                .enable(YAMLGenerator.Feature.MINIMIZE_QUOTES);

        ObjectMapper mapper = new ObjectMapper(yamlFactory);
        mapper.registerModule(new JavaTimeModule());
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        return mapper;
    }

    @Override
    public String getFormat() {
        return "YAML";
    }
}

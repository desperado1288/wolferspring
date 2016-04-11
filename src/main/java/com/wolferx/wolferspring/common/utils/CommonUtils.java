package com.wolferx.wolferspring.common.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.wolferx.wolferspring.common.exception.InvalidRequestInputException;
import org.slf4j.Logger;

public class CommonUtils {

    public static Object parserJsonNode(final String key, final JsonNode sourceNode, final Class clazz, final Logger logger)
        throws InvalidRequestInputException {

        try {
            if (clazz.equals(String.class)) {
                return sourceNode.get(key).asText();
            } else if (clazz.equals(Long.class)){
                return sourceNode.get(key).asLong();
            } else if (clazz.equals(Integer.class)){
                return sourceNode.get(key).asInt();
            } else {
                throw new InvalidRequestInputException("Missing required input");
            }
        } catch (final NullPointerException nullPointerException) {
            logger.error("<In> parserJsonNode(): Missing required input", nullPointerException);
            throw new InvalidRequestInputException("Missing required input");
        }
    }
}

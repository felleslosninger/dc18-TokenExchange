package com.dc18TokenExchange.STSserver.jwt.impl;

import com.dc18TokenExchange.STSserver.jwt.exceptions.JWTDecodeException;
import com.dc18TokenExchange.STSserver.jwt.interfaces.Header;
import com.dc18TokenExchange.STSserver.jwt.interfaces.Payload;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static com.dc18TokenExchange.STSserver.jwt.impl.JWTParser.getDefaultObjectMapper;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class JWTParserTest {
    @Rule
    public ExpectedException exception = ExpectedException.none();
    private JWTParser parser;

    @Before
    public void setUp() {
        parser = new JWTParser();
    }

    @Test
    public void shouldGetDefaultObjectMapper() {
        ObjectMapper mapper = getDefaultObjectMapper();
        assertThat(mapper, is(notNullValue()));
        assertThat(mapper, is(instanceOf(ObjectMapper.class)));
        assertThat(mapper.isEnabled(SerializationFeature.FAIL_ON_EMPTY_BEANS), is(false));
    }

    @Test
    public void shouldAddDeserializers() {
        ObjectMapper mapper = mock(ObjectMapper.class);
        new JWTParser(mapper);
        verify(mapper).registerModule(any(Module.class));
    }

    @Test
    public void shouldParsePayload() throws Exception {
        ObjectMapper mapper = mock(ObjectMapper.class);
        JWTParser parser = new JWTParser(mapper);
        parser.parsePayload("{}");

        verify(mapper).readValue("{}", Payload.class);
    }

    @Test
    public void shouldThrowOnInvalidPayload() {
        String jsonPayload = "{{";
        exception.expect(JWTDecodeException.class);
        exception.expectMessage(String.format("The string '%s' doesn't have a valid JSON format.", jsonPayload));
        Payload payload = parser.parsePayload(jsonPayload);
        assertThat(payload, is(nullValue()));
    }

    @Test
    public void shouldParseHeader() throws Exception {
        ObjectMapper mapper = mock(ObjectMapper.class);
        JWTParser parser = new JWTParser(mapper);
        parser.parseHeader("{}");

        verify(mapper).readValue("{}", Header.class);
    }

    @Test
    public void shouldThrowOnInvalidHeader() {
        String jsonHeader = "}}";
        exception.expect(JWTDecodeException.class);
        exception.expectMessage(String.format("The string '%s' doesn't have a valid JSON format.", jsonHeader));
        Header header = parser.parseHeader(jsonHeader);
        assertThat(header, is(nullValue()));
    }

    @Test
    public void shouldConvertFromValidJSON() {
        String json = "\r\n { \r\n } \r\n";
        Object object = parser.convertFromJSON(json, Object.class);
        assertThat(object, is(notNullValue()));
    }

    @Test
    public void shouldThrowWhenConvertingIfNullJson() {
        exception.expect(JWTDecodeException.class);
        exception.expectMessage("The string 'null' doesn't have a valid JSON format.");
        String json = null;
        Object object = parser.convertFromJSON(json, Object.class);
        assertThat(object, is(nullValue()));
    }

    @Test
    public void shouldThrowWhenConvertingFromInvalidJson() {
        exception.expect(JWTDecodeException.class);
        exception.expectMessage("The string '}{' doesn't have a valid JSON format.");
        String json = "}{";
        Object object = parser.convertFromJSON(json, Object.class);
        assertThat(object, is(nullValue()));
    }
}
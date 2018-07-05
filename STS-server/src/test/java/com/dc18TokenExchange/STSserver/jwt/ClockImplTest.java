package com.dc18TokenExchange.STSserver.jwt;

import com.dc18TokenExchange.STSserver.jwt.interfaces.Clock;
import org.junit.Test;

import java.util.Date;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

public class ClockImplTest {
    @Test
    public void shouldGetToday() {
        Clock clock = new ClockImpl();
        Date clockToday = clock.getToday();
        assertThat(clockToday, is(notNullValue()));
    }
}
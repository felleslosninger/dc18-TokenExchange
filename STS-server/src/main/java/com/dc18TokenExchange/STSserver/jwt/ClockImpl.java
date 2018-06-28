package com.dc18TokenExchange.STSserver.jwt;


import com.dc18TokenExchange.STSserver.jwt.interfaces.Clock;

import java.util.Date;

final class ClockImpl implements Clock {

    ClockImpl() {
    }

    @Override
    public Date getToday() {
        return new Date();
    }
}

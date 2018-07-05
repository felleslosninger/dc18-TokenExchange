package com.dc18TokenExchange.STSserver.jwt.impl;

import com.dc18TokenExchange.STSserver.jwt.interfaces.Claim;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.TextNode;
import org.hamcrest.collection.IsCollectionWithSize;
import org.hamcrest.core.IsCollectionContaining;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class PayloadImplTest {
    @Rule
    public ExpectedException exception = ExpectedException.none();
    private PayloadImpl payload;
    private Date expiresAt;
    private Date notBefore;
    private Date issuedAt;

    @Before
    public void setUp() {
        expiresAt = Mockito.mock(Date.class);
        notBefore = Mockito.mock(Date.class);
        issuedAt = Mockito.mock(Date.class);
        Map<String, JsonNode> tree = new HashMap<>();
        tree.put("extraClaim", new TextNode("extraValue"));
        payload = new PayloadImpl("issuer", "subject", Collections.singletonList("audience"), expiresAt, notBefore, issuedAt, "jwtId", tree);
    }

    @SuppressWarnings("Convert2Diamond")
    @Test
    public void shouldHaveUnmodifiableTree() {
        exception.expect(UnsupportedOperationException.class);
        PayloadImpl payload = new PayloadImpl(null, null, null, null, null, null, null, new HashMap<String, JsonNode>());
        payload.getTree().put("something", null);
    }

    @Test
    public void shouldGetIssuer() {
        assertThat(payload, is(notNullValue()));
        assertThat(payload.getIssuer(), is("issuer"));
    }

    @Test
    public void shouldGetNullIssuerIfMissing() {
        PayloadImpl payload = new PayloadImpl(null, null, null, null, null, null, null, null);
        assertThat(payload, is(notNullValue()));
        assertThat(payload.getIssuer(), is(nullValue()));
    }

    @Test
    public void shouldGetSubject() {
        assertThat(payload, is(notNullValue()));
        assertThat(payload.getSubject(), is("subject"));
    }

    @Test
    public void shouldGetNullSubjectIfMissing() {
        PayloadImpl payload = new PayloadImpl(null, null, null, null, null, null, null, null);
        assertThat(payload, is(notNullValue()));
        assertThat(payload.getSubject(), is(nullValue()));
    }

    @Test
    public void shouldGetAudience() {
        assertThat(payload, is(notNullValue()));
        assertThat(payload.getAudience(), is(IsCollectionWithSize.hasSize(1)));
        assertThat(payload.getAudience(), is(IsCollectionContaining.hasItems("audience")));
    }

    @Test
    public void shouldGetNullAudienceIfMissing() {
        PayloadImpl payload = new PayloadImpl(null, null, null, null, null, null, null, null);
        assertThat(payload, is(notNullValue()));
        assertThat(payload.getAudience(), is(nullValue()));
    }

    @Test
    public void shouldGetExpiresAt() {
        assertThat(payload, is(notNullValue()));
        assertThat(payload.getExpiresAt(), is(expiresAt));
    }

    @Test
    public void shouldGetNullExpiresAtIfMissing() {
        PayloadImpl payload = new PayloadImpl(null, null, null, null, null, null, null, null);
        assertThat(payload, is(notNullValue()));
        assertThat(payload.getExpiresAt(), is(nullValue()));
    }

    @Test
    public void shouldGetNotBefore() {
        assertThat(payload, is(notNullValue()));
        assertThat(payload.getNotBefore(), is(notBefore));
    }

    @Test
    public void shouldGetNullNotBeforeIfMissing() {
        PayloadImpl payload = new PayloadImpl(null, null, null, null, null, null, null, null);
        assertThat(payload, is(notNullValue()));
        assertThat(payload.getNotBefore(), is(nullValue()));
    }

    @Test
    public void shouldGetIssuedAt() {
        assertThat(payload, is(notNullValue()));
        assertThat(payload.getIssuedAt(), is(issuedAt));
    }

    @Test
    public void shouldGetNullIssuedAtIfMissing() {
        PayloadImpl payload = new PayloadImpl(null, null, null, null, null, null, null, null);
        assertThat(payload, is(notNullValue()));
        assertThat(payload.getIssuedAt(), is(nullValue()));
    }

    @Test
    public void shouldGetJWTId() {
        assertThat(payload, is(notNullValue()));
        assertThat(payload.getId(), is("jwtId"));
    }

    @Test
    public void shouldGetNullJWTIdIfMissing() {
        PayloadImpl payload = new PayloadImpl(null, null, null, null, null, null, null, null);
        assertThat(payload, is(notNullValue()));
        assertThat(payload.getId(), is(nullValue()));
    }

    @Test
    public void shouldGetExtraClaim() {
        assertThat(payload, is(notNullValue()));
        assertThat(payload.getClaim("extraClaim"), is(instanceOf(JsonNodeClaim.class)));
        assertThat(payload.getClaim("extraClaim").asString(), is("extraValue"));
    }

    @Test
    public void shouldGetNotNullExtraClaimIfMissing() {
        PayloadImpl payload = new PayloadImpl(null, null, null, null, null, null, null, null);
        assertThat(payload, is(notNullValue()));
        assertThat(payload.getClaim("missing"), is(notNullValue()));
        assertThat(payload.getClaim("missing"), is(instanceOf(NullClaim.class)));
    }

    @Test
    public void shouldGetClaims() {
        Map<String, JsonNode> tree = new HashMap<>();
        tree.put("extraClaim", new TextNode("extraValue"));
        tree.put("sub", new TextNode("auth0"));
        PayloadImpl payload = new PayloadImpl(null, null, null, null, null, null, null, tree);
        assertThat(payload, is(notNullValue()));
        Map<String, Claim> claims = payload.getClaims();
        assertThat(claims, is(notNullValue()));
        assertThat(claims.get("extraClaim"), is(notNullValue()));
        assertThat(claims.get("sub"), is(notNullValue()));
    }

    @Test
    public void shouldNotAllowToModifyClaimsMap() {
        assertThat(payload, is(notNullValue()));
        Map<String, Claim> claims = payload.getClaims();
        assertThat(claims, is(notNullValue()));
        exception.expect(UnsupportedOperationException.class);
        claims.put("name", null);
    }
}
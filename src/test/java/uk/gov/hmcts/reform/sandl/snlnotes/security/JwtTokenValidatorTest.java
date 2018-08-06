package uk.gov.hmcts.reform.sandl.snlnotes.security;

import io.jsonwebtoken.JwtException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
public class JwtTokenValidatorTest {
    private static final String TOKEN_VALUE = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJvZmZpY2VyMSIsImlhdCI6MTUzMzU0NjY5MS"
            + "wiZXhwIjozMjUyMjQ2MTg5MX0.6m-xy554zHuGCydHVNCikQubfbWv3mkGL1aYiqW0spn7YZ_PX-Xn5EGOJB28w_UWYMMbUQLeGF0"
            + "Yek6Vj_FQOw";
    private static final String INVALID_TOKEN = "invalid_token";

    private JwtTokenValidator jwtTokenValidator;

    public JwtTokenValidatorTest() {
        this.jwtTokenValidator = new JwtTokenValidator();

        ReflectionTestUtils.setField(jwtTokenValidator, "secret", "tajneHasuo");
    }

    @Test
    public void parseToken_shouldParseUsernameCorrectly() {
        String username = jwtTokenValidator.parseToken(TOKEN_VALUE);

        assertEquals("officer1", username);
    }

    @Test(expected = JwtException.class)
    public void parseToken_shouldThrowErrorWhenInvalidToken() {
        jwtTokenValidator.parseToken(INVALID_TOKEN);
    }

}

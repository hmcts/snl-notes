package uk.gov.hmcts.reform.sandl.snlnotes.security;

import io.jsonwebtoken.JwtException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class JwtAuthenticationFilterTest {
    private static final String TOKEN_VALUE = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJvZmZpY2VyMSIsImlhdCI6MTUzMzU0NjY5MS"
            + "wiZXhwIjozMjUyMjQ2MTg5MX0.6m-xy554zHuGCydHVNCikQubfbWv3mkGL1aYiqW0spn7YZ_PX-Xn5EGOJB28w_UWYMMbUQLeGF0"
            + "Yek6Vj_FQOw";
    private static final String BEARER = "Bearer ";
    private static final String INVALID_TOKEN = "invalid_token";

    @InjectMocks
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private FilterChain chain;

    @Mock
    private JwtTokenValidator jwtTokenValidator;

    public JwtAuthenticationFilterTest() {
        this.jwtAuthenticationFilter = new JwtAuthenticationFilter();
    }

    @Before
    public void setup() {
        this.request = new MockHttpServletRequest();
        this.response = new MockHttpServletResponse();
        this.chain = mock(FilterChain.class);
        SecurityContextHolder.clearContext();
    }

    @Test
    public void doFilterInternal_withValidTokenAsProperUser_shouldAuthenticate() throws ServletException, IOException {
        when(jwtTokenValidator.parseToken(TOKEN_VALUE)).thenReturn("officer1");

        request.addHeader("Authorization", BEARER + TOKEN_VALUE);

        jwtAuthenticationFilter.doFilterInternal(request, response, chain);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        assertTrue(auth.isAuthenticated());
        verify(chain, times(1)).doFilter(request, response);
    }

    @Test
    public void doFilterInternal_withInvalidToken_shouldNotAuthenticate() throws ServletException, IOException {
        when(jwtTokenValidator.parseToken(INVALID_TOKEN)).thenThrow(JwtException.class);

        request.addHeader("Authorization", BEARER + INVALID_TOKEN);

        jwtAuthenticationFilter.doFilterInternal(request, response, chain);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        assertNull(auth);
        verify(chain, times(1)).doFilter(request, response);
    }

    @Test
    public void doFilterInternal_withoutBearer_shouldNotAuthenticate() throws ServletException, IOException {
        when(jwtTokenValidator.parseToken(any(String.class))).thenThrow(JwtException.class);

        request.addHeader("Authorization", TOKEN_VALUE);

        jwtAuthenticationFilter.doFilterInternal(request, response, chain);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        assertNull(auth);
        verify(chain, times(1)).doFilter(request, response);
    }
}

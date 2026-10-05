package com.imatcoding.expensetracker.common.config;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ApiLoggingFilterTest {

    private final ApiLoggingFilter filter = new ApiLoggingFilter();
    private ListAppender<ILoggingEvent> appender;
    private Logger logger;

    @BeforeEach
    void setUp() {
        logger = (Logger) LoggerFactory.getLogger(ApiLoggingFilter.class);
        appender = new ListAppender<>();
        appender.start();
        logger.addAppender(appender);
    }

    @AfterEach
    void tearDown() {
        logger.detachAppender(appender);
    }

    @Test
    void doFilterInternal_logsRequestAndResponseKeepingResponseBody() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("POST",
                "/api/test");
        request.setQueryString("a=1");
        request.setContent("{\"name\":\"bob\"}".getBytes(StandardCharsets.UTF_8));
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain chain = (req, res) -> {
            // Fill request cache reading request
            req.getInputStream().readAllBytes();
            // Fill response cache writing response
            ((HttpServletResponse) res).setStatus(201);
            res.getWriter().write("{\"id\":1}");
        };

        filter.doFilter(request, response, chain);

        // Extract log messages for easier assertions
        List<String> logs = appender.list.stream()
                .map(ILoggingEvent::getFormattedMessage).toList();
        assertThat(logs)
                .anySatisfy(m -> assertThat(m)
                        .contains("REQ POST /api/test?a=1")
                        .contains("{\"name\":\"bob\"}"))
                .anySatisfy(m -> assertThat(m)
                        .contains("RES 201")
                        .contains("{\"id\":1}"));

        // Check that response body is still available after filter
        assertThat(response.getContentAsString()).isEqualTo("{\"id\":1}");
        assertThat(response.getStatus()).isEqualTo(201);
    }

    @Test
    void doFilterInternal_stillLogsAndCopiesBodyWhenChainThrows() {
        MockHttpServletRequest request = new MockHttpServletRequest("GET",
                "/api/exception");
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain chain = (req, res) -> {
            throw new ServletException("Exception in chain");
        };

        assertThatThrownBy(() -> filter.doFilter(request, response, chain))
                .isInstanceOf(ServletException.class);
        assertThat(appender.list).isNotEmpty();
    }

    @Test
    void doFilterInternal_notFilteringNonApi() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("GET",
                "/non-api");
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();

        filter.doFilter(request, response, chain);

        assertThat(appender.list).isEmpty();
        // Check that chain.doFilter input are the original ones (filter skipped)
        assertThat(chain.getRequest()).isSameAs(request);
        assertThat(chain.getResponse()).isSameAs(response);
    }
}

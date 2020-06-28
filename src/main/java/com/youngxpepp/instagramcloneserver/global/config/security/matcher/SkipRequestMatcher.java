package com.youngxpepp.instagramcloneserver.global.config.security.matcher;

import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SkipRequestMatcher implements RequestMatcher {

    private RequestMatcher requestMatcher;

    public SkipRequestMatcher(List<String> patternList) {
        this(patternList.toArray(new String[patternList.size()]));
    }

    public SkipRequestMatcher(String ... patterns) {
        List<RequestMatcher> requestMatchers = Arrays.asList(patterns)
                .stream()
                .map(pattern -> new AntPathRequestMatcher(pattern))
                .collect(Collectors.toList());

        this.requestMatcher = new OrRequestMatcher(requestMatchers);
    }

    @Override
    public boolean matches(HttpServletRequest request) {
        return !this.requestMatcher.matches(request);
    }
}

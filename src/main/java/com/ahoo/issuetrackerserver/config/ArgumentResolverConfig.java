package com.ahoo.issuetrackerserver.config;

import com.ahoo.issuetrackerserver.auth.SignInMemberIdArgumentResolver;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class ArgumentResolverConfig implements WebMvcConfigurer {

    private final SignInMemberIdArgumentResolver signInMemberIdArgumentResolver;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(signInMemberIdArgumentResolver);
    }
}

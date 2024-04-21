package edu.java.scrapper.utils;

import edu.java.scrapper.exceptions.TooManyRequestsException;
import io.github.bucket4j.Bucket;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class RateLimitInterceptor implements HandlerInterceptor {
    public static final String X_FORWARDED_FOR = "X-Forwarded-For";
    private final BucketGrabber bucketGrabber;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String clientIP = Optional.ofNullable(request.getHeader(X_FORWARDED_FOR))
            .orElseGet(request::getRemoteAddr);
        Bucket bucket = bucketGrabber.grabBucket(clientIP);
        if (bucket.tryConsume(1)) {
            return true;
        } else {
            throw new TooManyRequestsException();
        }
    }
}

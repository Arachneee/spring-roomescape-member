package roomescape.controller.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.auth.infrastructure.AuthorizationExtractor;
import roomescape.auth.service.AuthService;
import roomescape.auth.exception.AuthorizationException;
import roomescape.controller.dto.LoginMember;
import roomescape.domain.Role;

public class CheckAdminInterceptor implements HandlerInterceptor {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final AuthService authService;
    private final AuthorizationExtractor authorizationExtractor;

    public CheckAdminInterceptor(AuthService authService, AuthorizationExtractor authorizationExtractor) {
        this.authService = authService;
        this.authorizationExtractor = authorizationExtractor;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        logger.trace("request = {}", request.getRequestURI());

        String uri = request.getRequestURI();
        String method = request.getMethod();

        if (method.equals("POST")
                && (uri.startsWith("/reservations") || uri.startsWith("/members"))) {
            return true;
        }
        if (method.equals("GET")
                && (uri.startsWith("/times") || uri.startsWith("/themes"))) {
            return true;
        }

        String token = authorizationExtractor.extract(request);
        LoginMember member = authService.findMemberByToken(token);
        if (Role.ADMIN != member.role()) {
            throw new AuthorizationException();
        }

        return true;
    }
}
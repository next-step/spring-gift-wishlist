package gift.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.exception.ForbiddenException;
import gift.exception.UnauthorizedException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExceptionHandlerFilter extends OncePerRequestFilter {
    private static final List<String> EXCLUDE_PATHS = Arrays.asList(
        "/css", "/js", "/favicon.ico", "/error"
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();
        boolean isExcludable = EXCLUDE_PATHS.stream().anyMatch(path::startsWith);

        if (isExcludable) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            filterChain.doFilter(request, response);
        } catch (UnauthorizedException e) {
            setErrorResponse(response, HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (ForbiddenException e) {
            setErrorResponse(response, HttpStatus.FORBIDDEN, e.getMessage());
        } catch (Exception e) {
            setErrorResponse(response, HttpStatus.INTERNAL_SERVER_ERROR, "토큰 검사 중 오류가 발생했습니다.");
        }
    }

    private void setErrorResponse(HttpServletResponse response, HttpStatus status, String message) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        response.setStatus(status.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        Map<String, String> error = new HashMap<>();
        error.put("error", message);

        try {
            response.getWriter().write(objectMapper.writeValueAsString(error));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

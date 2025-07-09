package gift.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

// 2. using extracted token, checking if the user has the right permissions to access the requested resource
public class AuthorizationFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String role = (String) httpRequest.getAttribute("role");

        String uri = httpRequest.getRequestURI();
        String method = httpRequest.getMethod();

        if (uri.startsWith("/api/products")) {
            if (method.equals("POST")) {
                if (role == null || role.isEmpty()) {
                    httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
                    return;
                } else if (!role.equals("ROLE_SELLER") && !role.equals("ROLE_MD")) {
                    httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied");
                    return;
                }
            } else if (method.equals("PATCH") || method.equals("DELETE")) {
                if (role == null || role.isEmpty()) {
                    httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
                    return;
                } else if (!role.equals("ROLE_MD")) {
                    httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied");
                    return;
                }
            }
        } else if (uri.startsWith("/admin/products")) {
            if (true) {
                if (role == null || role.isEmpty()) {
                    httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
                    return;
                } else if (!role.equals("ROLE_MD")) {
                    httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied");
                    return;
                }
            }
        }

        chain.doFilter(request, response);
    }
}

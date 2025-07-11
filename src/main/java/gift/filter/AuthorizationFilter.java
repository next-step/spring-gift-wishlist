package gift.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import gift.entity.Role;

import java.io.IOException;

// 2. using extracted token, checking if the user has the right permissions to access the requested resource
public class AuthorizationFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String role = (String) httpRequest.getAttribute("role");

        String uri = httpRequest.getRequestURI();
        String method = httpRequest.getMethod().toUpperCase();

        boolean authorized = true;

        if (uri.startsWith("/api/products")) { // GET : public, POST : seller or MD, PATCH/DELETE : MD only
            switch (method) {
                case "POST" ->
                        authorized = (role != null && (role.equals(Role.ROLE_SELLER.name()) || role.equals(Role.ROLE_MD.name())));
                case "PATCH", "DELETE" ->
                        authorized = (role != null && role.equals(Role.ROLE_MD.name()));
            }
        } else if (uri.startsWith("/api/wishes")) {
            switch (method) {
                default ->
                        authorized = (role != null);
            }
        } else if (uri.startsWith("/admin/products/new")) { // only MD can access this endpoint
            switch (method) {
                default ->
                        authorized = (role != null && role.equals(Role.ROLE_MD.name()));
            }
        } else if (uri.startsWith("/admin/products")) { // GET : MD or CS, POST/PATCH/DELETE : MD only
            switch (method) {
                case "GET" ->
                        authorized = (role != null && (role.equals(Role.ROLE_MD.name()) || role.equals(Role.ROLE_CS.name())));
                case "POST", "PATCH", "DELETE" ->
                        authorized = (role != null && role.equals(Role.ROLE_MD.name()));
            }
        } else if (uri.startsWith("/admin/members/new")) { // only ADMIN can access this endpoint
            switch (method) {
                default ->
                        authorized = (role != null && role.equals(Role.ROLE_ADMIN.name()));
            }
        } else if (uri.startsWith("/admin/members")) { // GET : ADMIN or CS, POST/PATCH/DELETE : ADMIN only
            switch (method) {
                case "GET" ->
                        authorized = (role != null && (role.equals(Role.ROLE_ADMIN.name()) || role.equals(Role.ROLE_CS.name())));
                case "POST", "PATCH", "DELETE" ->
                        authorized = (role != null && role.equals(Role.ROLE_ADMIN.name()));
            }
        }

        if (!authorized) {
            if (role == null || role.isEmpty()) {
                httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
            } else {
                httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied");
            }
            return;
        }

        chain.doFilter(request, response);
    }
}

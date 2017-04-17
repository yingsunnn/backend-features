package ying.backend_features.filter;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CorsFilter implements Filter {

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        httpResponse.addHeader("Access-Control-Allow-Origin", "*");
        httpResponse.addHeader("Access-Control-Allow-Credentials", "true");

        if (isPreFlightRequest(httpRequest)) {
            // CORS "pre-flight" request
            httpResponse.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE");
            httpResponse.addHeader("Access-Control-Allow-Headers", "X-Requested-With,Origin,Content-Type,Accept,access_token,user_id");
            return;
        }

        chain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {
    }

    private boolean isCorsRequest(HttpServletRequest request) {
        return (request.getHeader(HttpHeaders.ORIGIN) != null);
    }

    private boolean isPreFlightRequest(HttpServletRequest request) {
        return (isCorsRequest(request) && HttpMethod.OPTIONS.matches(request.getMethod()) &&
                request.getHeader(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD) != null);
    }
}

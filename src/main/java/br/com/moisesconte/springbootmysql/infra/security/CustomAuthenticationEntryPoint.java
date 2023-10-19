package br.com.moisesconte.springbootmysql.infra.security;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
      throws IOException, ServletException {

    response.setContentType("application/json;charset=UTF-8");
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // Defina o status HTTP desejado

    // ObjectMapper mapper = new ObjectMapper();
    // response.getWriter().write(mapper.writeValueAsString(new Error(403,
    // "Forbidden", "Acesso Negado!")));

    // Crie um objeto JSON com a mensagem de erro personalizada
    // ObjectMapper mapper = new ObjectMapper();
    // String json = mapper.writeValueAsString("Mensagem de erro personalizada");

    // // response.getWriter().write(json);

    // // Escreva a resposta no corpo da resposta HTTP
    // PrintWriter out = response.getWriter();
    // out.print(json);
    // out.flush();

    final Map<String, Object> body = new HashMap<>();
    body.put("status", HttpServletResponse.SC_UNAUTHORIZED);
    body.put("error", "Unauthorized");
    body.put("message", authException.getMessage());
    body.put("path", request.getServletPath());

    final ObjectMapper mapper = new ObjectMapper();
    mapper.writeValue(response.getOutputStream(), body);
  }

}

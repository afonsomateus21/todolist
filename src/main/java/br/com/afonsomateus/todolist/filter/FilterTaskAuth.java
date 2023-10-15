package br.com.afonsomateus.todolist.filter;

import java.io.IOException;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import at.favre.lib.crypto.bcrypt.BCrypt;
import at.favre.lib.crypto.bcrypt.BCrypt.Result;
import br.com.afonsomateus.todolist.user.IUserRepository;
import br.com.afonsomateus.todolist.user.UserModel;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class FilterTaskAuth extends OncePerRequestFilter {
  @Autowired
  private IUserRepository userRepository;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
    throws ServletException, IOException {
      String servletPath = request.getServletPath();

      if (servletPath.startsWith("/tasks/")) {
        String authorization = request.getHeader("Authorization");
        String authEncoded = authorization.substring("Basic".length()).trim();

        byte[] authDecoded = Base64.getDecoder().decode(authEncoded);
        String authString = new String(authDecoded);

        String[] credentials = authString.split(":");
        String username = credentials[0];
        String password = credentials[1];

        UserModel user = this.userRepository.findByUsername(username);
        if (user == null) {
          response.sendError(401, "Usuário não autorizado");
        } else {
          Result passwordVerified = BCrypt.verifyer().verify(password.toCharArray(), user.getPassword());
          if (passwordVerified.verified) {
            request.setAttribute("idUser", user.getId());
            filterChain.doFilter(request, response);
          } else {
            response.sendError(401, "Usuário não autorizado");
          }
        }
      } else {
        filterChain.doFilter(request, response);
      }
  }
}
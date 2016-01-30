package com.townscript.todo.authentication;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class AuthenticationInterceptor extends HandlerInterceptorAdapter{
	private AuthenticationManager authenticationManager;
	


	public void setAuthenticationManager(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}



	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {

		/*String key = request.getHeader("api_key");

		if (key == null || !key.equals(AuthenticationProperties.API_KEY)) {
			PrintWriter pw = response.getWriter();
			pw.println("REST 403. You are not autherized.");
			return false;
		}*/

		Authentication authentication = new UsernamePasswordAuthenticationToken(
				AuthenticationProperties.USERNAME, AuthenticationProperties.PASSWORD);

		Authentication successfulAuthentication = authenticationManager.authenticate(authentication);
		SecurityContextHolder.getContext().setAuthentication(successfulAuthentication);
		return true;
	}

}

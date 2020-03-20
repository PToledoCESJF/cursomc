package com.paulotoledo.cursomc.services;

import org.springframework.security.core.context.SecurityContextHolder;

import com.paulotoledo.cursomc.security.UserSS;

public class UserService {
	
	public static UserSS authenticated(){
		try {
			// Retorna o usuário logado
			return (UserSS) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		}catch (Exception e) {
			return null;
		}
	}

}

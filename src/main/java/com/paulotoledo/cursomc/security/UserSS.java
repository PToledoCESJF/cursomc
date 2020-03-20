package com.paulotoledo.cursomc.security;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.paulotoledo.cursomc.domain.enums.Perfil;

public class UserSS implements UserDetails{
	
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String email;
	private String senha;
	private Collection<? extends GrantedAuthority> authorities; 
	
	public UserSS() {	}
	
	public UserSS(Integer id, String email, String senha, Set<Perfil> perfis) {
		super();
		this.id = id;
		this.email = email;
		this.senha = senha;
		this.authorities = perfis.stream().map(x -> new SimpleGrantedAuthority(x.getDescricao())).collect(Collectors.toList());
	}



	public Integer getId() {
		return id;
	}
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getPassword() {
		return senha;
	}

	@Override
	public String getUsername() {
		return email;
	}

	@Override
	public boolean isAccountNonExpired() {
		// Aqui pode-se incluir uma lógica para Expirar contas de usuários.
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// Aqui pode-se incluir uma lógica para Bloquear contas de usuários.
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// Aqui pode-se incluir uma lógica para Expirar credenciais de usuários.
		return true;
	}

	@Override
	public boolean isEnabled() {
		// Aqui pode-se incluir uma lógica para Ativar ou Desativar  usuários.
		return true;
	}
	

}

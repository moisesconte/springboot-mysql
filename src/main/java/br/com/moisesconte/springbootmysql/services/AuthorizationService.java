package br.com.moisesconte.springbootmysql.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.moisesconte.springbootmysql.repositories.IUserRepository;

@Service
public class AuthorizationService implements UserDetailsService {

  @Autowired
  IUserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return this.userRepository.findByLogin(username);
  }

}

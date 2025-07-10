package com.pwii.stock_management.service.impl;

import com.pwii.stock_management.model.Cliente;
import com.pwii.stock_management.model.Usuario;
import com.pwii.stock_management.repository.UsuarioRepository;
import com.pwii.stock_management.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserServiceImpl implements IUserService, UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public Long saveUser(Usuario usuario) {
        if (usuario.getId() == null) {
            String passwd = usuario.getPassword();
            String encodedPassword = passwordEncoder.encode(passwd);
            usuario.setPassword(encodedPassword);
        } else {
            Usuario existente = usuarioRepository.findById(usuario.getId())
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado para edição"));

            usuario.setPassword(existente.getPassword());
        }

        usuario = usuarioRepository.save(usuario);
        return usuario.getId();
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Optional<Usuario> opt = usuarioRepository.findUserByEmail(email);

        org.springframework.security.core.userdetails.User springUser = null;

        if (opt.isEmpty()) {
            throw new UsernameNotFoundException("Usuário com o email: " + email + " não encontrado");
        } else {
            Usuario usuario = opt.get();
            List<String> roles = usuario.getRoles();
            Set<GrantedAuthority> ga = new HashSet<>();
            for (String role : roles) {
                ga.add(new SimpleGrantedAuthority(role));
            }

            springUser = new org.springframework.security.core.userdetails.User(
                    email,
                    usuario.getPassword(),
                    ga);

        }
        return springUser;
    }

    @Override
    public List<Usuario> getAllUsuarios () {
        return usuarioRepository.findAll();
    }


    @Override
    public Usuario getUsuarioById(Long id) {
        Optional<Usuario> optional = usuarioRepository.findById(id);
        Usuario usuario = null;
        if(optional.isPresent()){
            usuario = optional.get();
        } else{
            throw new RuntimeException("Usuário não encontrado para o id " + id);
        }
        return usuario;
    }

    @Override
    public void deletarUsuarioById(Long id) {
        usuarioRepository.deleteById(id);
    }
}
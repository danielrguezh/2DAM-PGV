package com.docencia.tasks.adapters.out.persistence;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.docencia.tasks.adapters.mapper.UserMapper;
import com.docencia.tasks.adapters.out.persistence.interfaces.IUserPersistenceAdapter;
import com.docencia.tasks.adapters.out.persistence.jpa.RolJpaEntity;
import com.docencia.tasks.adapters.out.persistence.jpa.UserJpaEntity;
import com.docencia.tasks.adapters.out.persistence.repository.RolJpaRepository;
import com.docencia.tasks.adapters.out.persistence.repository.UserJpaRepository;
import com.docencia.tasks.domain.model.User;
/**
 * @author nexphernandez
 * @version 1.0.0 Clase persistence de user
 */
@Component
public class UserPersistenceAdapter implements IUserPersistenceAdapter {

    private final UserJpaRepository userJpa;
    private final RolJpaRepository rolJpa;
    private final UserMapper mapper;

    /**
     * Constructor para inicializar la clase
     * @param userJpa interfaz jpa de user
     * @param mapper mapers de user
     * @param rolJpa interfaz jpa de rol
     */
    public UserPersistenceAdapter(UserJpaRepository userJpa,UserMapper mapper,RolJpaRepository rolJpa) {
        this.userJpa = userJpa;
        this.mapper = mapper;
        this.rolJpa = rolJpa;
    }

    @Override
    public List<User> getAll() {
        return userJpa.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    public Optional<User> getById(Long id) {
        return userJpa.findById(id).map(mapper::toDomain);
    }

    @Override
    public boolean deleteById(Long id) {
        if (id == null) {
            return false;
        }
        userJpa.deleteById(id);
        return true;
        
    }

    @Override
    public User save(User user, String rol) {
        UserJpaEntity userEntity = mapper.toJpa(user);
        addRolIfExists(userEntity, rol);
        UserJpaEntity savedUser = userJpa.save(userEntity);
        return mapper.toDomain(savedUser);
    }

    @Override
    public User update(User user, String rol) {
        UserJpaEntity findUser = userJpa.findById(user.getId()).orElse(null);
        if (findUser == null) {
            return save(user, rol);
        }
        findUser.setUserName(user.getUserName());
        findUser.setPassword(user.getPassword());
        
        if (findUser.getRoles() == null) {
            findUser.setRoles(new HashSet<>());
        }

        addRolIfExists(findUser, rol);
        UserJpaEntity updateUser = userJpa.save(findUser);
        return mapper.toDomain(updateUser);
    }

    private void addRolIfExists(UserJpaEntity userEntity, String rol) {
        if (rol != null && !rol.isBlank()) {
            RolJpaEntity rolEntity = rolJpa.findByRol(rol)
                    .orElseThrow(() -> new IllegalArgumentException("El rol '" + rol + "' no existe"));
            userEntity.getRoles().add(rolEntity);
        }
    }
}

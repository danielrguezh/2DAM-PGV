package com.docencia.tasks.business;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.docencia.tasks.adapters.out.persistence.interfaces.IUserPersistenceAdapter;
import com.docencia.tasks.business.interfaces.IUserService;
import com.docencia.tasks.domain.model.User;

/**
 * Clase del servicio de user
 * @author prorix
 * @version 1.0.0
 */
@Service
public class UserService implements IUserService {

    private final IUserPersistenceAdapter repo;

    public UserService(IUserPersistenceAdapter repo) {
        this.repo = repo;
    }

    @Override
    public User create(User user, String rol) {
        user.setId(null);
        return repo.save(user, rol);
    }

    @Override
    public List<User> getAll() {
        return repo.getAll();
    }

    @Override
    public Optional<User> getById(Long id) {
        return repo.getById(id);
    }

    @Override
    public Optional<User> update(Long id, User user, String rol) {
        return repo.getById(id).map(existing -> {
            if (user.getUserName() != null) {
                existing.setUserName(user.getUserName());
            }
            if (user.getPassword() != null) {
                existing.setPassword(user.getPassword());
            }
            return repo.save(existing, rol);
        });
    }

    @Override
    public boolean delete(Long id) {
        if (repo.getById(id).isEmpty()) {
            return false;
        }
        repo.deleteById(id);
        return true;
    }

}

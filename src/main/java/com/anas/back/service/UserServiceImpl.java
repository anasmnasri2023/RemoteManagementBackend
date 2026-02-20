package com.anas.back.service;

import com.anas.back.model.User;
import com.anas.back.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            return user.get();
        }
        return null;
    }

    @Override
    public User getUserByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            return user.get();
        }
        return null;
    }

    @Override
    public User createUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            return null;
        }
        // ✅ Hacher les deux champs avant sauvegarde
        user.setMotDePasse(BCrypt.hashpw(user.getMotDePasse(), BCrypt.gensalt()));
        user.setConfirmeMotDePasse(BCrypt.hashpw(user.getConfirmeMotDePasse(), BCrypt.gensalt()));
        return userRepository.save(user);
    }

    @Override
    public User updateUser(Long id, User userModifie) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User userExistant = optionalUser.get();
            userExistant.setNom(userModifie.getNom());
            userExistant.setPrenom(userModifie.getPrenom());
            userExistant.setEmail(userModifie.getEmail());
            userExistant.setTelephone(userModifie.getTelephone());
            userExistant.setRole(userModifie.getRole());
            if (userModifie.getMotDePasse() != null && !userModifie.getMotDePasse().isEmpty()) {
                // ✅ Hacher aussi lors de la mise à jour
                userExistant.setMotDePasse(BCrypt.hashpw(userModifie.getMotDePasse(), BCrypt.gensalt()));
                userExistant.setConfirmeMotDePasse(BCrypt.hashpw(userModifie.getConfirmeMotDePasse(), BCrypt.gensalt()));
            }
            return userRepository.save(userExistant);
        }
        return null;
    }

    @Override
    public void deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
        }
    }
}
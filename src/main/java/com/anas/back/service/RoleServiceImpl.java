package com.anas.back.service;

import com.anas.back.model.Role;
import com.anas.back.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    @Override
    public Role getRoleById(Long id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role non trouvé avec l'id : " + id));
    }

    @Override
    public Role createRole(Role role) {
        return roleRepository.save(role);
    }

    @Override
    public Role updateRole(Long id, Role role) {
        Role existing = getRoleById(id);
        existing.setNom(role.getNom());
        return roleRepository.save(existing);
    }

    @Override
    public void deleteRole(Long id) {
        roleRepository.deleteById(id);
    }
}
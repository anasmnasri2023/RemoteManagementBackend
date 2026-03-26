package com.anas.back.service;

import com.anas.back.model.User;
import com.anas.back.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    // ─── Patterns de validation ───────────────────────────────────────────────

    /** Commence par majuscule, puis lettres (accents inclus), espaces, tirets, apostrophes */
    private static final Pattern NOM_PATTERN =
            Pattern.compile("^[A-ZÀÂÆÇÉÈÊËÎÏÔŒÙÛÜŸ][a-zA-ZÀ-ÿ' -]*$");

    /** Format email standard */
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    /**
     * Mot de passe fort :
     *  - Au moins 8 caractères
     *  - Au moins 1 lettre minuscule
     *  - Au moins 1 lettre majuscule
     *  - Au moins 1 chiffre
     *  - Au moins 1 symbole parmi : ! @ # $ % ^ & * ( ) _ + - = [ ] { } ; ' : " \ | , . < > / ? ` ~
     */
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile(
                    "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)" +
                            "(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?`~])[\\S]{8,}$"
            );

    // ─── Méthode de validation commune ───────────────────────────────────────

    /**
     * Valide les champs d'un utilisateur et retourne la liste des erreurs trouvées.
     * @param user         l'utilisateur à valider
     * @param isCreation   true = création (on valide aussi le password brut),
     *                     false = mise à jour (password optionnel)
     */
    private List<String> validerUser(User user, boolean isCreation) {
        List<String> erreurs = new ArrayList<>();

        // --- Nom ---
        if (user.getNom() == null || user.getNom().isBlank()) {
            erreurs.add("Le nom est obligatoire");
        } else if (!NOM_PATTERN.matcher(user.getNom()).matches()) {
            erreurs.add("Le nom doit commencer par une majuscule et contenir uniquement des lettres");
        }

        // --- Prénom ---
        if (user.getPrenom() == null || user.getPrenom().isBlank()) {
            erreurs.add("Le prénom est obligatoire");
        } else if (!NOM_PATTERN.matcher(user.getPrenom()).matches()) {
            erreurs.add("Le prénom doit commencer par une majuscule et contenir uniquement des lettres");
        }

        // --- Email ---
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            erreurs.add("L'email est obligatoire");
        } else if (!EMAIL_PATTERN.matcher(user.getEmail()).matches()) {
            erreurs.add("L'email doit être une adresse valide (ex: user@example.com)");
        }

        // --- Téléphone : exactement 8 chiffres (int entre 10_000_000 et 99_999_999) ---
        if (user.getTelephone() < 10_000_000 || user.getTelephone() > 99_999_999) {
            erreurs.add("Le téléphone doit contenir exactement 8 chiffres");
        }

        // --- Mot de passe ---
        if (isCreation || (user.getMotDePasse() != null && !user.getMotDePasse().isEmpty())) {

            if (user.getMotDePasse() == null || user.getMotDePasse().isBlank()) {
                erreurs.add("Le mot de passe est obligatoire");
            } else if (!PASSWORD_PATTERN.matcher(user.getMotDePasse()).matches()) {
                erreurs.add("Le mot de passe doit contenir au moins 8 caractères, " +
                        "une majuscule, une minuscule, un chiffre et un symbole");
            }

            if (user.getConfirmeMotDePasse() == null || user.getConfirmeMotDePasse().isBlank()) {
                erreurs.add("La confirmation du mot de passe est obligatoire");
            } else if (!PASSWORD_PATTERN.matcher(user.getConfirmeMotDePasse()).matches()) {
                erreurs.add("La confirmation doit contenir au moins 8 caractères, " +
                        "une majuscule, une minuscule, un chiffre et un symbole");
            }

            // --- Les deux mots de passe doivent être identiques (avant hachage) ---
            if (user.getMotDePasse() != null && user.getConfirmeMotDePasse() != null
                    && !user.getMotDePasse().equals(user.getConfirmeMotDePasse())) {
                erreurs.add("Le mot de passe et sa confirmation ne correspondent pas");
            }
        }

        return erreurs;
    }

    // ─── CRUD ─────────────────────────────────────────────────────────────────

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    @Override
    public User createUser(User user) {

        // 1. Validation des champs saisis
        List<String> erreurs = validerUser(user, true);
        if (!erreurs.isEmpty()) {
            throw new IllegalArgumentException(String.join(" | ", erreurs));
        }

        // 2. Email déjà utilisé ?
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Un compte existe déjà avec cet email");
        }

        // 3. Hachage des mots de passe puis sauvegarde
        user.setMotDePasse(BCrypt.hashpw(user.getMotDePasse(), BCrypt.gensalt()));
        user.setConfirmeMotDePasse(BCrypt.hashpw(user.getConfirmeMotDePasse(), BCrypt.gensalt()));
        return userRepository.save(user);
    }

    @Override
    public User updateUser(Long id, User userModifie) {

        // 1. Validation des champs saisis
        List<String> erreurs = validerUser(userModifie, false);
        if (!erreurs.isEmpty()) {
            throw new IllegalArgumentException(String.join(" | ", erreurs));
        }

        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) {
            return null;
        }

        User userExistant = optionalUser.get();
        userExistant.setNom(userModifie.getNom());
        userExistant.setPrenom(userModifie.getPrenom());
        userExistant.setEmail(userModifie.getEmail());
        userExistant.setTelephone(userModifie.getTelephone());
        userExistant.setRole(userModifie.getRole());

        // Mise à jour du mot de passe seulement s'il est fourni
        if (userModifie.getMotDePasse() != null && !userModifie.getMotDePasse().isEmpty()) {
            userExistant.setMotDePasse(BCrypt.hashpw(userModifie.getMotDePasse(), BCrypt.gensalt()));
            userExistant.setConfirmeMotDePasse(BCrypt.hashpw(userModifie.getConfirmeMotDePasse(), BCrypt.gensalt()));
        }

        return userRepository.save(userExistant);
    }

    @Override
    public void deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
        }
    }
}
package com.haroldmokam.ma_biblio.services;

import com.haroldmokam.ma_biblio.Repositories.UtilisateurRepository;
import com.haroldmokam.ma_biblio.entites.RoleUtilisateur;
import com.haroldmokam.ma_biblio.entites.Utilisateur;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UtilisateurService {
    private UtilisateurRepository utilisateurRepository;

    //Creer un utilisateur
    public void creerUtilisateur(Utilisateur utilisateur) {
        utilisateur.setNombreEmpruntRestant(5);
        utilisateur.setNombreEmpruntActif(5);
        if (utilisateur.getRole() == null) {
            utilisateur.setRole(RoleUtilisateur.EMPLOYÉ); // Par défaut, l'utilisateur est un employé
        }
        utilisateurRepository.save(utilisateur);
    }

    public void modifierUtilisateur(int id, Utilisateur utilisateur) {
        Utilisateur utilisateurdansLaBd = utilisateurRepository.findById(id).
                orElseThrow(
                        ()-> new RuntimeException("l'utilisateur n'existe pas dans la base de donnes")
                );
        utilisateur.setId(id);
        utilisateurRepository.save(utilisateur);
    }

    public void supprimerUtilisateur(int id) {
        utilisateurRepository.deleteById(id);
    }

    public List<Utilisateur> listeTotaleUtilisateurs() {
        return utilisateurRepository.findAll();
    }
    
    // Obtenir tous les utilisateurs avec pagination
    public Page<Utilisateur> getAllUtilisateurs(Pageable pageable) {
        return utilisateurRepository.findAll(pageable);
    }
    
    // Obtenir tous les utilisateurs sans pagination
    public List<Utilisateur> getAllUtilisateurs() {
        return utilisateurRepository.findAll();
    }
    
    // Compter le total des utilisateurs
    public long countTotalUtilisateurs() {
        return utilisateurRepository.count();
    }
    
    // Obtenir un utilisateur par ID
    public Utilisateur getUtilisateurById(int id) {
        return utilisateurRepository.findById(id).orElseThrow(
                ()-> new EntityNotFoundException("l'utilisateur n'existe pas dans la base de donnes.")
        );
    }
    
    // Rechercher des utilisateurs avec pagination
    public Page<Utilisateur> rechercherUtilisateurs(String search, Pageable pageable) {
        return utilisateurRepository.findByNomContainingIgnoreCaseOrMailContainingIgnoreCaseOrMatriculeContainingIgnoreCase(
                search, search, search, pageable);
    }
    
    // Rechercher des utilisateurs par rôle avec pagination
    public Page<Utilisateur> rechercherUtilisateursParRole(RoleUtilisateur role, Pageable pageable) {
        return utilisateurRepository.findByRole(role, pageable);
    }
    
    public Utilisateur findUtilisateurById(int id) {
        return utilisateurRepository.findById(id).orElseThrow(
                ()-> new EntityNotFoundException("l'utilisateur n'existe pas dans la base de donnes.")
        );
    }
    public Utilisateur findUtilisateurByEmail(String email) {
        return utilisateurRepository.findByMail(email).orElseThrow(
                ()-> new RuntimeException(" l'utilisateur n'existe pas dans la base de donnes.")
        );
    }
    public Utilisateur findUtilisateurByMatricule(String matricule) {
        return utilisateurRepository.findByMatricule(matricule).orElseThrow(
                ()-> new RuntimeException(" l'utilisateur n'existe pas dans la base de donnes.")
        );
    }
    public List<Utilisateur> findUtilisateurByNom(String nom) {
        List<Utilisateur> utilisateurs = utilisateurRepository.findByNomContainingIgnoreCase(nom);
        if(utilisateurs.isEmpty()){
            throw new RuntimeException("La liste est vide.");
        }
        return utilisateurs;
    }
}

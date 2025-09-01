package com.haroldmokam.ma_biblio.Repositories;

import com.haroldmokam.ma_biblio.entites.RoleUtilisateur;
import com.haroldmokam.ma_biblio.entites.Utilisateur;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UtilisateurRepository extends JpaRepository<Utilisateur, Integer> {
   Optional<Utilisateur> findById(int id);
   Optional<Utilisateur> findByMail(String email);
   Optional<Utilisateur> findByMatricule(String nom);
   List<Utilisateur> findByNomContainingIgnoreCase(String nom);
   
   // Méthodes pour la pagination et recherche
   Page<Utilisateur> findByNomContainingIgnoreCaseOrMailContainingIgnoreCaseOrMatriculeContainingIgnoreCase(
           String nom, String mail, String matricule, Pageable pageable);
   Page<Utilisateur> findByRole(RoleUtilisateur role, Pageable pageable);
}

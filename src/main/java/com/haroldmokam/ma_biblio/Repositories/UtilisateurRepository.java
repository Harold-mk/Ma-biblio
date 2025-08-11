package com.haroldmokam.ma_biblio.Repositories;

import com.haroldmokam.ma_biblio.entites.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UtilisateurRepository extends JpaRepository<Utilisateur, Integer> {
   public Optional<Utilisateur> findById(int id);
   Optional<Utilisateur> findByEmail(String email);
   Optional<Utilisateur> findByMatricule(String nom);
   List<Utilisateur> findByNomContainingIgnoreCase(String nom);
}

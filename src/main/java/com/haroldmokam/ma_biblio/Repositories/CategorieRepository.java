package com.haroldmokam.ma_biblio.Repositories;

import com.haroldmokam.ma_biblio.entites.Categorie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategorieRepository extends JpaRepository<Categorie, Integer> {
     Optional<Categorie> findById(int id);
     Optional<Categorie> findByLibelle(String libelle);

    // Cette methode permet de rechercer une liste de Categorie en fonction du libelle en ignorant les Majuscules et Minuscules.
    List<Categorie> findByLibelleContainingIgnoreCase(String libelle);

    List<Categorie> findAll();
    
    // Méthodes pour la pagination
    Page<Categorie> findByLibelleContainingIgnoreCase(String libelle, Pageable pageable);

}

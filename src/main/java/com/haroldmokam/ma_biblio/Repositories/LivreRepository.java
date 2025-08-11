package com.haroldmokam.ma_biblio.Repositories;

import com.haroldmokam.ma_biblio.entites.Categorie;
import com.haroldmokam.ma_biblio.entites.Livre;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LivreRepository extends JpaRepository<Livre, Integer> {
    
    Optional<Livre> findByTitre(String titre);
    List<Livre> findByTitreContainingIgnoreCase(String titre);
    List<Livre> findByCategorie(Categorie categorie);
    List<Livre> findByAuteur(String auteur);
    List<Livre> findByAuteurContainingIgnoreCase(String auteur);
    Optional <Livre> findById(int id);
}

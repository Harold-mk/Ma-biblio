package com.haroldmokam.ma_biblio.Repositories;

import com.haroldmokam.ma_biblio.entites.Categorie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategorieRepository extends JpaRepository<Categorie, Integer> {
    public Categorie findById(int id);
    public Optional<Categorie> findByLibelle(String libelle);

    // Cette methode permet de rechercer une liste de Categorie en fonction du libelle en ignorant les Majuscules et Minuscules.
    public List<Categorie> findByLibelleContainingIgnoreCase(String libelle);

    public List<Categorie> findAll();
    public void deleteById(int id);
    public void deleteByLibelle(String libelle);
}

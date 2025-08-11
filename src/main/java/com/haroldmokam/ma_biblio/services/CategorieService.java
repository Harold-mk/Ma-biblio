package com.haroldmokam.ma_biblio.services;

import com.haroldmokam.ma_biblio.Repositories.CategorieRepository;
import com.haroldmokam.ma_biblio.entites.Categorie;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @Author : Mokam Harold
 *
 */
@Service
@AllArgsConstructor
public class CategorieService {

    //Injection des dependances
    private CategorieRepository categorieRepository;


    public void creerCategorie(Categorie categorie) {
        Optional<Categorie> categorieDansBD = categorieRepository.findByLibelle(categorie.getLibelle());
        if(categorieDansBD.isEmpty()) {
            categorieRepository.save(categorie);
        }
        else{
            throw new RuntimeException("L'element existe deja dans la base de donnees");
        }

    }


    /**
     * @Author Harold Roger
     * @param categorie
     */
    public void supprimerCategorie(Categorie categorie) {
        Optional<Categorie> categorieDansBD = categorieRepository.findByLibelle(categorie.getLibelle());
        if(categorieDansBD.isPresent()) {
            categorieRepository.delete(categorie);
        }
        else{
            throw new RuntimeException("L'element n'existe pas dans la base de donnees ou elle a deja ete supprimmee.");
        }
    }

    public List<Categorie> rechercherCategorie( String libelle) {
        List<Categorie> categorieDansBD = categorieRepository.findByLibelleContainingIgnoreCase(libelle);
        if(categorieDansBD.isEmpty()) {
            throw new RuntimeException("Aucun element trouve dans la base de donnees.");
        }
        return categorieDansBD;
    }

    public List<Categorie> rechercherAllCategories() {
        return categorieRepository.findAll();
    }

    public Categorie rechercherCategorieParId(int id) {
       return categorieRepository.findById(id).
               orElseThrow(()-> new RuntimeException("l'element n'existe pas dans la base de donnees"));
    }
}

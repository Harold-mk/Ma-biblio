package com.haroldmokam.ma_biblio.services;


import com.haroldmokam.ma_biblio.Repositories.LivreRepository;
import com.haroldmokam.ma_biblio.entites.Categorie;
import com.haroldmokam.ma_biblio.entites.Livre;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @Author : Mokam Harold
 * Cette classe defini la logique metiers et tout ce qui doit concerner le traitement des livres.
 */
@AllArgsConstructor
@Service
public class LivreService {


    //Injection des dependances
    private final LivreRepository livreRepository;

    // Creer un Livre
    public void creerLivre(Livre livre){
        Optional<Livre> livreOptional = livreRepository.findByTitre(livre.getTitre());
        if(livreOptional.isEmpty()){
            livreRepository.save(livre);
        }
        else{
            throw new RuntimeException("Le livre existe deja");
        }
    }
    
    // Modifier un Livre
    public void modifierLivre(int id, Livre livre){
        Optional<Livre> livreOptional = livreRepository.findById(id);
        if(livreOptional.isPresent()){
            livre.setId(id);
            livreRepository.save(livre);
        }
        else{
            throw new RuntimeException("Le livre que vous voulez modifier n'existe pas");
        }
    }
    
    // Supprimer un livre
    public void supprimerLivre(int id){
        Optional<Livre> livreOptional = livreRepository.findById(id);
        if(livreOptional.isPresent()){
            livreRepository.deleteById(id);
        }
        else{
            throw new RuntimeException("Le livre que vous voulez supprimer n'existe pas.");
        }
    }

    // Obtenir un livre par ID
    public Livre getLivreById(int id) {
        return livreRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Livre non trouvé avec l'ID: " + id));
    }

    // Obtenir tous les livres avec pagination
    public Page<Livre> getAllLivres(Pageable pageable) {
        return livreRepository.findAll(pageable);
    }

    // Obtenir tous les livres sans pagination
    public List<Livre> getAllLivres() {
        return livreRepository.findAll();
    }

    // Compter le total des livres
    public long countTotalLivres() {
        return livreRepository.count();
    }

    // Rechercher des livres avec pagination
    public Page<Livre> rechercherLivres(String search, Pageable pageable) {
        return livreRepository.findByTitreContainingIgnoreCaseOrAuteurContainingIgnoreCase(search, search, pageable);
    }

    // Rechercher des livres par catégorie avec pagination
    public Page<Livre> rechercherLivresParCategorie(String categorieNom, Pageable pageable) {
        return livreRepository.findByCategorieLibelleContainingIgnoreCase(categorieNom, pageable);
    }

    // rechercher un livre par titre
    public List<Livre> afficherListeLivresParTitre(String titre){
        if(titre.isEmpty()){
            throw new RuntimeException("La liste est vide");
        }
            return livreRepository.findByTitreContainingIgnoreCase(titre);
    }
    
    //rechercher la liste des livres par categorie
    public List<Livre> afficherListeLivreParCategorie(Categorie categorie){
        List<Livre> livres = livreRepository.findByCategorie(categorie);
        if(livres.isEmpty()){
            throw new RuntimeException("La liste est vide");
        }
        return livres;
    }

    //rechercher un livre par auteur
    public List<Livre> afficherListeLivreParAuteur(String auteur){
        List<Livre> livres = livreRepository.findByAuteurContainingIgnoreCase(auteur);
        if(livres.isEmpty()){
            throw new RuntimeException("La liste est vide");
        }
        return livres;
    }

}

package com.haroldmokam.ma_biblio.services;


import com.haroldmokam.ma_biblio.Repositories.LivreRepository;
import com.haroldmokam.ma_biblio.entites.Categorie;
import com.haroldmokam.ma_biblio.entites.Livre;
import lombok.AllArgsConstructor;
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
    public void modifierLivre(Livre livre){
        Optional<Livre> livreOptional = livreRepository.findById(livre.getId());
        if(livreOptional.isPresent()){
            livreRepository.save(livre);
        }
        else{
            throw new RuntimeException("Le livre que vous voulez modifier n'existe pas");
        }
    }
    // Supprimer un livre
    public void supprimerLivre(Livre livre){
        Optional<Livre> livreOptional = livreRepository.findById(livre.getId());
        if(livreOptional.isPresent()){
            livreRepository.delete(livre);
        }
        else{
            throw new RuntimeException("Le livre que vous voulez supprimer n'existe pas.");
        }
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

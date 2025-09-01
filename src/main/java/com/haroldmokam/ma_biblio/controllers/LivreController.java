package com.haroldmokam.ma_biblio.controllers;

import com.haroldmokam.ma_biblio.Repositories.LivreRepository;
import com.haroldmokam.ma_biblio.entites.Categorie;
import com.haroldmokam.ma_biblio.entites.Livre;
import com.haroldmokam.ma_biblio.services.CategorieService;
import com.haroldmokam.ma_biblio.services.LivreService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/Livre")
@AllArgsConstructor
public class LivreController {
    private final LivreService livreService;
    private final LivreRepository livreRepository;
    private final CategorieService categorieService;

    @PostMapping( path = "/ajouter", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void ajouterLivre(@RequestBody Livre livre) {
        livreService.creerLivre(livre);
        log.info("Livre "+livre.getTitre()+" ajouté avec succès!");
    }

    @PutMapping( path = "/modifier/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void modifierLivre(@RequestBody Livre livre, @PathVariable int id) {
        livreService.modifierLivre(id, livre);
        log.info("Modifier livre avec id " + id);
    }

    @DeleteMapping(path = "/supprimer/{id}")
    public void supprimerLivre(@PathVariable int id) {
        livreService.supprimerLivre(id);
        log.info("Livre supprimé avec id " + id);
    }

    /**
     *  ########################### RECHERCHE DES LIVRES PAR LEURS CATEGORIES. ########################
     */
    @GetMapping(path = "/ListeTotale", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Livre> ListeTotale() {
        return livreRepository.findAll();
    }

    @GetMapping(path = "/Auteur/{auteur}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Livre> rechercherParAuteur(@PathVariable String auteur) {
        return livreService.afficherListeLivreParAuteur(auteur);
    }

    @GetMapping(path = "/Titre/{titre}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Livre> rechercherParTitre(@PathVariable String titre) {
        return livreService.afficherListeLivresParTitre(titre);
    }

    @GetMapping(path = "/Categorie/{categorieId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Livre> rechercherParCategorie(@PathVariable int categorieId) {
        Categorie categorie = categorieService.getCategorieById(categorieId);
        return livreService.afficherListeLivreParCategorie(categorie);
    }
}

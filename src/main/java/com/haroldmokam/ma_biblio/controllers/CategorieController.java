package com.haroldmokam.ma_biblio.controllers;

import com.haroldmokam.ma_biblio.entites.Categorie;
import com.haroldmokam.ma_biblio.services.CategorieService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(path = "/categorie")
public class CategorieController {
    private final CategorieService categorieService;

    @PostMapping(path = "/ajouter", produces = MediaType.APPLICATION_JSON_VALUE)
    public void ajouterCategorie(@RequestBody Categorie categorie) {
        log.info(categorie.getLibelle()+ " a bien été créé et enregistré dans la base de données");
        categorieService.creerCategorie(categorie);
    }

    @PutMapping(path = "/modifier/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public void modifierCategorie(@PathVariable int id, @RequestBody Categorie categorie) {
        categorieService.modifierCategorie(id, categorie);
        log.info("La catégorie avec l'ID " + id + " a été modifiée");
    }

    @DeleteMapping(path = "/supprimer/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public void supprimerCategorie(@PathVariable int id) {
        categorieService.supprimerCategorie(id);
        log.info("La catégorie avec l'ID " + id + " a été supprimée de la base de données");
    }

    @GetMapping(path = "/liste")
    public List<Categorie> listerCategorie() {
        log.info("Liste des catégories");
        return categorieService.rechercherAllCategories();
    }
    
    @GetMapping(path = "/{id}")
    public Categorie getCategorieById(@PathVariable int id) {
        return categorieService.getCategorieById(id);
    }
    
    @GetMapping(path = "/recherche/{libelle}")
    public List<Categorie> rechercherParLibelle(@PathVariable String libelle) {
        return categorieService.rechercherCategorie(libelle);
    }

}

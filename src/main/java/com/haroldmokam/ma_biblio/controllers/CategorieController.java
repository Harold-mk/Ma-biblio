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
        log.info(categorie.getLibelle()+ " a bien ete cree et enregistre dans la base de donnees");
        categorieService.creerCategorie(categorie);
    }

    @PutMapping(path = "/modifier/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public void modifierCategorie(@PathVariable int id, @RequestBody Categorie categorie) {
         Categorie categorieDansBD = categorieService.rechercherCategorieParId(id);
         String ancienLibelle = categorieDansBD.getLibelle();
         categorieDansBD.setLibelle(categorie.getLibelle());
        categorieService.creerCategorie(categorieDansBD);
        log.info("La categorie est quitte de "+ancienLibelle+" a %s"+ categorieDansBD.getLibelle());
    }

    @DeleteMapping(path = "/supprimer/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public void supprimerCategorie(@PathVariable int id) {
        Categorie categorieDansBD = categorieService.rechercherCategorieParId(id);
        categorieService.supprimerCategorie(categorieDansBD);
        log.info("La categorie "+categorieDansBD.getLibelle()+" a ete supprimee de la base de donnees");
    }

    @GetMapping(path = "/liste")
    public List<Categorie> listerCategorie() {
        log.info("Liste des categories");
        return categorieService.rechercherAllCategories();
    }

}

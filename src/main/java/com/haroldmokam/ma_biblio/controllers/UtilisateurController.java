package com.haroldmokam.ma_biblio.controllers;

import com.haroldmokam.ma_biblio.entites.Utilisateur;
import com.haroldmokam.ma_biblio.services.UtilisateurService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/utilisateurs")
@AllArgsConstructor
@Slf4j
public class UtilisateurController {
    private final UtilisateurService utilisateurService;

    @PostMapping(path = "/ajouter", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> ajouterUtilisateur(@RequestBody Utilisateur utilisateur) {
        utilisateurService.creerUtilisateur(utilisateur);
        return new ResponseEntity<>(utilisateur.toString(), HttpStatus.CREATED);
    }

    @PutMapping(path = "/modifier/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> modifierUtilisateur(@RequestBody Utilisateur utilisateur, @PathVariable int id) {
        utilisateurService.modifierUtilisateur(id, utilisateur);
        return new ResponseEntity<>(utilisateur.toString(), HttpStatus.ACCEPTED);
    }

    @DeleteMapping(path = "supprimer/{id}")
    public ResponseEntity<String> supprimerUtilisateur(@PathVariable int id) {
        utilisateurService.supprimerUtilisateur(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping(path = "/listetotale", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Utilisateur>> getUtilisateurs() {
       List<Utilisateur> utilisateurs= utilisateurService.listeTotaleUtilisateurs();
        return new ResponseEntity<>(utilisateurs, HttpStatus.OK);
    }
    
    @GetMapping(path = "/recherche/noms", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Utilisateur>> rechercherParNom(@RequestParam String nom) {
        List<Utilisateur> utilisateurs = utilisateurService.findUtilisateurByNom(nom);
        return new ResponseEntity<>(utilisateurs, HttpStatus.OK);
    }
    
    @GetMapping(path = "/recherche/email", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Utilisateur> rechercherParEmail(@RequestParam String email) {
        Utilisateur utilisateur = utilisateurService.findUtilisateurByEmail(email);
        return new ResponseEntity<>(utilisateur, HttpStatus.OK);
    }
    
    @GetMapping(path = "/recherche/matricule", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Utilisateur> rechercherParMatricule(@RequestParam String matricule) {
        Utilisateur utilisateur = utilisateurService.findUtilisateurByMatricule(matricule);
        return new ResponseEntity<>(utilisateur, HttpStatus.OK);
    }
    
    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Utilisateur> getUtilisateurById(@PathVariable int id) {
        Utilisateur utilisateur = utilisateurService.getUtilisateurById(id);
        return new ResponseEntity<>(utilisateur, HttpStatus.OK);
    }
}

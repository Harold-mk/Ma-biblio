package com.haroldmokam.ma_biblio.controllers;

import com.haroldmokam.ma_biblio.Repositories.LivreRepository;
import com.haroldmokam.ma_biblio.Repositories.UtilisateurRepository;
import com.haroldmokam.ma_biblio.entites.Emprunt;
import com.haroldmokam.ma_biblio.entites.Livre;
import com.haroldmokam.ma_biblio.entites.Utilisateur;
import com.haroldmokam.ma_biblio.services.EmpruntService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/emprunts")
@AllArgsConstructor()
public class EmpruntController {
    private final EmpruntService empruntService;
    private final UtilisateurRepository utilisateurRepository;
    private final LivreRepository livreRepository;

    /**
     * Cette methode permet d'ajouter un emprunt en fonction d'une personne et tout.
     * @param emprunt qui va permettre de creer les emprunts dans une version ulterieure on va utiliser les DTO
     *
     */
    @PostMapping(path = "/ajouter", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void ajouterEmprunt(@RequestBody Emprunt emprunt) {
        empruntService.creerEmprunt(emprunt);
        log.info("Emprunt ajouté avec succès");
    }

    /**
     * Ce controleur permet de prolonger un emprunt de deux semaines. Pour l'instant la date d'emprunt est fixe mais selon les besoins de tata Marlyse on peut le modifier.
     * @param id qui represente l'ID denl'emprunt qu'on va prolonger
     */
    @PutMapping(path ="/prolonger/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void prolongerEmprunt(@PathVariable int id) {
        empruntService.prolongerEmprunt(id);
    }

    /**
      * @param id qui represente l'ID denl'emprunt qu'on va annuler
     * Ce controleur permet d'effectuer la remise d'un emprunt par le bibliothecaire.
     */
    @PutMapping(path ="/remise/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void remiseEmprunt(@PathVariable int id) {
        empruntService.retournerLivre(id);
    }

    /**
     *  ############### BON ICI C'EST LA PARTIE DEDIES AUX RECHERCHES DES EMPRUNTS EN FONCTION DES ENTITES QUI LE CONSTITUE.########################
     */

    @GetMapping(path = "/rechercher/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Emprunt rechercherEmpruntParId(@PathVariable int id) {
        return empruntService.getEmpruntById(id);
    }

    @GetMapping(path = "/listeTotale", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Emprunt> listeTotaleEmprunt() {
        return empruntService.listetotaleDesEmprunts();
    }

    @GetMapping(path = "/DelaiExpire", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Emprunt> listeDelaiExpireEmprunt() {
        return empruntService.listeEmpruntsDelaiExpire();
    }

    @GetMapping(path = "/enCours", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Emprunt> listeEnCours() {
        return empruntService.listeEmpruntsEnCours();
    }
    
    @GetMapping(path = "/Utilisateur/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Emprunt> rechercheParUtilisateur(@PathVariable int id) {
        Utilisateur utilisateur = utilisateurRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("l'utilisateur n'existe pas dans la base de donnees."));
        return empruntService.ListeEmpruntsParUtilisateur(utilisateur);
    }

    @GetMapping(path = "/Livre/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Emprunt> rechercheParLivre(@PathVariable int id) {
        Livre livre = livreRepository.findById(id).orElseThrow(()->new EntityNotFoundException("Livre non trouvé"));
        return empruntService.ListeEmpruntsParLivre(livre);
    }
    
    @GetMapping(path = "/remis", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Emprunt> listeRemis() {
        return empruntService.listeLivreDejaRemis();
    }
    
    @GetMapping(path = "/liste/dateDebutEmprunt/{date}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Emprunt> listeDateDebutEmprunt(@PathVariable LocalDate date) {
        return empruntService.ListeEmpruntsParDateDebutEmprunts(date);
    }
    
    @GetMapping(path = "/liste/dateFinEmprunt/{date}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Emprunt> listeDateFinEmprunt(@PathVariable LocalDate date) {
        return empruntService.ListeEmpruntsParDateFinEmprunts(date);
    }

}

package com.haroldmokam.ma_biblio.controllers;

import com.haroldmokam.ma_biblio.entites.Livre;
import com.haroldmokam.ma_biblio.entites.Utilisateur;
import com.haroldmokam.ma_biblio.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * Contrôleur Thymeleaf pour les pages utilisateur
 */
@Controller
@RequestMapping("/user")
public class ThymeleafUserController {

    @Autowired
    private LivreService livreService;

    @Autowired
    private UtilisateurService utilisateurService;

    @Autowired
    private EmpruntService empruntService;

    @Autowired
    private CategorieService categorieService;

    @Autowired
    private NotificationService notificationService;

    /**
     * Catalogue des livres pour les utilisateurs
     */
    @GetMapping("/catalogue")
    public String catalogue(@RequestParam(defaultValue = "0") int page,
                           @RequestParam(defaultValue = "12") int size,
                           @RequestParam(required = false) String search,
                           @RequestParam(required = false) Integer categorie,
                           @RequestParam(required = false) String disponibilite,
                           @RequestParam(defaultValue = "titre,asc") String sort,
                           Model model) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort.split(",")[0]).ascending());
        
        Page<Livre> livres;
        if (search != null && !search.trim().isEmpty()) {
            livres = livreService.rechercherLivres(search, pageable);
        } else if (categorie != null) {
            livres = livreService.rechercherLivresParCategorie(categorie.toString(), pageable);
        } else {
            livres = livreService.getAllLivres(pageable);
        }

        model.addAttribute("pageTitle", "Catalogue des Livres");
        model.addAttribute("title", "Catalogue des Livres");
        model.addAttribute("livres", livres);
        model.addAttribute("categories", categorieService.getAllCategories());
        
        return "user/catalogue";
    }

    /**
     * Prolonger un emprunt
     */
    @PostMapping("/emprunts/{id}/prolonger")
    @ResponseBody
    public String prolongerEmprunt(@PathVariable int id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || auth.getName().equals("anonymousUser")) {
            return "error:Non authentifié";
        }

        try {
            Utilisateur utilisateur = utilisateurService.findUtilisateurByEmail(auth.getName());
            var emprunt = empruntService.getEmpruntById(id);
            
            // Vérifier que l'emprunt appartient à l'utilisateur connecté
            if (emprunt.getUtilisateur().getId() != utilisateur.getId()) {
                return "error:Accès non autorisé";
            }
            
            empruntService.prolongerEmprunt(id);
            return "success:Emprunt prolongé avec succès";
        } catch (Exception e) {
            return "error:" + e.getMessage();
        }
    }

    /**
     * Marquer une notification comme lue
     */
    @PostMapping("/notifications/{id}/marquer-lu")
    @ResponseBody
    public String marquerCommeLu(@PathVariable int id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || auth.getName().equals("anonymousUser")) {
            return "error:Non authentifié";
        }

        try {
            Utilisateur utilisateur = utilisateurService.findUtilisateurByEmail(auth.getName());
            var notification = notificationService.getNotificationById(id);
            
            // Vérifier que la notification appartient à l'utilisateur connecté
            if (notification.getUtilisateurDestinataire().getId() != utilisateur.getId()) {
                return "error:Accès non autorisé";
            }
            
            notificationService.marquerCommeLu(id);
            return "success:Notification marquée comme lue";
        } catch (Exception e) {
            return "error:" + e.getMessage();
        }
    }
}

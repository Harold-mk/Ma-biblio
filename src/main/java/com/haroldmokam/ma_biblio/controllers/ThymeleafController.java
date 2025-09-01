package com.haroldmokam.ma_biblio.controllers;

import com.haroldmokam.ma_biblio.entites.RoleUtilisateur;
import com.haroldmokam.ma_biblio.entites.Utilisateur;
import com.haroldmokam.ma_biblio.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Contrôleur principal pour l'interface Thymeleaf
 * Gère la navigation et l'affichage des pages principales
 */
@Controller
@RequestMapping("/")
public class ThymeleafController {

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
     * Page d'accueil - redirige vers le dashboard approprié
     */
    @GetMapping("/")
    public String home() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && 
            !authentication.getName().equals("anonymousUser")) {
            
            try {
                Utilisateur utilisateur = utilisateurService.findUtilisateurByEmail(authentication.getName());
                if (utilisateur.getRole() == RoleUtilisateur.BIBLIOTHÉCAIRE) {
                    return "redirect:/admin/dashboard";
                } else {
                    return "redirect:/user/dashboard";
                }
            } catch (Exception e) {
                return "redirect:/login";
            }
        }
        return "redirect:/login";
    }

    /**
     * Dashboard administrateur
     */
    @GetMapping("/admin/dashboard")
    public String adminDashboard(Model model) {
        // Statistiques pour le dashboard
        long totalLivres = livreService.countTotalLivres();
        long totalUtilisateurs = utilisateurService.countTotalUtilisateurs();
        long empruntsActifs = empruntService.countEmpruntsActifs();
        long empruntsEnRetard = empruntService.countEmpruntsEnRetard();

        // Derniers emprunts et emprunts en retard pour le dashboard
        var derniersEmprunts = empruntService.getDerniersEmprunts(5);
        var empruntsEnRetardList = empruntService.getEmpruntsEnRetard(5);

        model.addAttribute("pageTitle", "Tableau de bord");
        model.addAttribute("title", "Tableau de bord");
        model.addAttribute("totalLivres", totalLivres);
        model.addAttribute("totalUtilisateurs", totalUtilisateurs);
        model.addAttribute("empruntsActifs", empruntsActifs);
        model.addAttribute("empruntsEnRetard", empruntsEnRetard);
        model.addAttribute("derniersEmprunts", derniersEmprunts);
        model.addAttribute("empruntsEnRetardList", empruntsEnRetardList);

        return "admin/dashboard";
    }

    /**
     * Dashboard utilisateur
     */
    @GetMapping("/user/dashboard")
    public String userDashboard(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && 
            !authentication.getName().equals("anonymousUser")) {
            
            try {
                Utilisateur utilisateur = utilisateurService.findUtilisateurByEmail(authentication.getName());
                
                // Statistiques de l'utilisateur
                int empruntsActifs = utilisateur.getNombreEmpruntActif();
                int empruntsRestants = utilisateur.getNombreEmpruntRestant();
                int empruntsEnRetard = empruntService.countEmpruntsEnRetardByUtilisateur(utilisateur.getId());
                int notificationsNonLues = notificationService.countNotificationsNonLuesByUtilisateur(utilisateur.getId());
                
                // Emprunts actuels de l'utilisateur
                var mesEmprunts = empruntService.getEmpruntsActifsByUtilisateur(utilisateur.getId());
                
                // Notifications récentes de l'utilisateur
                var mesNotifications = notificationService.getNotificationsRecentesByUtilisateur(utilisateur.getId(), 5);
                
                model.addAttribute("pageTitle", "Mon Tableau de bord");
                model.addAttribute("title", "Mon Tableau de bord");
                model.addAttribute("utilisateur", utilisateur);
                model.addAttribute("empruntsActifs", empruntsActifs);
                model.addAttribute("empruntsRestants", empruntsRestants);
                model.addAttribute("empruntsEnRetard", empruntsEnRetard);
                model.addAttribute("notificationsNonLues", notificationsNonLues);
                model.addAttribute("mesEmprunts", mesEmprunts);
                model.addAttribute("mesNotifications", mesNotifications);
                
                return "user/dashboard";
            } catch (Exception e) {
                return "redirect:/login";
            }
        }
        return "redirect:/login";
    }

    /**
     * Page de connexion
     */
    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("pageTitle", "Connexion");
        model.addAttribute("title", "Connexion");
        return "auth/login";
    }

    /**
     * Gestion des erreurs 404
     */
    @GetMapping("/error/404")
    public String error404(Model model) {
        model.addAttribute("pageTitle", "Page non trouvée");
        model.addAttribute("title", "Page non trouvée");
        return "error/404";
    }
}

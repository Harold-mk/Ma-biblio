package com.haroldmokam.ma_biblio.controllers;

import com.haroldmokam.ma_biblio.entites.Utilisateur;
import com.haroldmokam.ma_biblio.entites.RoleUtilisateur;
import com.haroldmokam.ma_biblio.services.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;
import java.util.List;

/**
 * Contrôleur Thymeleaf pour la gestion des utilisateurs
 */
@Controller
@RequestMapping("/admin/utilisateurs")
public class ThymeleafUtilisateurController {

    @Autowired
    private UtilisateurService utilisateurService;

    /**
     * Liste des utilisateurs avec pagination et filtres
     */
    @GetMapping
    public String listUtilisateurs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String role,
            Model model) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("nom").ascending());
        Page<Utilisateur> utilisateursPage;

        if (search != null && !search.trim().isEmpty()) {
            utilisateursPage = utilisateurService.rechercherUtilisateurs(search, pageable);
        } else if (role != null && !role.trim().isEmpty()) {
            utilisateursPage = utilisateurService.rechercherUtilisateursParRole(RoleUtilisateur.valueOf(role), pageable);
        } else {
            utilisateursPage = utilisateurService.getAllUtilisateurs(pageable);
        }

        List<RoleUtilisateur> roles = Arrays.asList(RoleUtilisateur.values());

        model.addAttribute("pageTitle", "Gestion des Utilisateurs");
        model.addAttribute("title", "Gestion des Utilisateurs");
        model.addAttribute("utilisateurs", utilisateursPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", utilisateursPage.getTotalPages());
        model.addAttribute("totalItems", utilisateursPage.getTotalElements());
        model.addAttribute("search", search);
        model.addAttribute("role", role);
        model.addAttribute("roles", roles);

        return "admin/utilisateurs/list";
    }

    /**
     * Formulaire de création d'un nouvel utilisateur
     */
    @GetMapping("/create")
    public String createUtilisateurForm(Model model) {
        List<RoleUtilisateur> roles = Arrays.asList(RoleUtilisateur.values());
        
        model.addAttribute("pageTitle", "Nouvel Utilisateur");
        model.addAttribute("title", "Nouvel Utilisateur");
        model.addAttribute("utilisateur", new Utilisateur());
        model.addAttribute("roles", roles);
        model.addAttribute("isEdit", false);

        return "admin/utilisateurs/form";
    }

    /**
     * Formulaire de modification d'un utilisateur existant
     */
    @GetMapping("/{id}/edit")
    public String editUtilisateurForm(@PathVariable int id, Model model) {
        Utilisateur utilisateur = utilisateurService.getUtilisateurById(id);
        if (utilisateur == null) {
            return "redirect:/admin/utilisateurs";
        }

        List<RoleUtilisateur> roles = Arrays.asList(RoleUtilisateur.values());
        
        model.addAttribute("pageTitle", "Modifier l'Utilisateur");
        model.addAttribute("title", "Modifier l'Utilisateur");
        model.addAttribute("utilisateur", utilisateur);
        model.addAttribute("roles", roles);
        model.addAttribute("isEdit", true);

        return "admin/utilisateurs/form";
    }

    /**
     * Affichage des détails d'un utilisateur
     */
    @GetMapping("/{id}")
    public String viewUtilisateur(@PathVariable int id, Model model) {
        Utilisateur utilisateur = utilisateurService.getUtilisateurById(id);
        if (utilisateur == null) {
            return "redirect:/admin/utilisateurs";
        }

        model.addAttribute("pageTitle", utilisateur.getNom() + " " + utilisateur.getPrenom());
        model.addAttribute("title", utilisateur.getNom() + " " + utilisateur.getPrenom());
        model.addAttribute("utilisateur", utilisateur);

        return "admin/utilisateurs/detail";
    }

    /**
     * Traitement de la création/modification d'un utilisateur
     */
    @PostMapping
    public String saveUtilisateur(@ModelAttribute Utilisateur utilisateur, 
                                 @RequestParam(defaultValue = "false") boolean isEdit,
                                 RedirectAttributes redirectAttributes) {
        try {
            if (isEdit) {
                utilisateurService.modifierUtilisateur(utilisateur.getId(), utilisateur);
                redirectAttributes.addFlashAttribute("success", "Utilisateur modifié avec succès");
            } else {
                utilisateurService.creerUtilisateur(utilisateur);
                redirectAttributes.addFlashAttribute("success", "Utilisateur créé avec succès");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de l'opération: " + e.getMessage());
        }

        return "redirect:/admin/utilisateurs";
    }

    /**
     * Suppression d'un utilisateur
     */
    @PostMapping("/{id}/delete")
    public String deleteUtilisateur(@PathVariable int id, RedirectAttributes redirectAttributes) {
        try {
            utilisateurService.supprimerUtilisateur(id);
            redirectAttributes.addFlashAttribute("success", "Utilisateur supprimé avec succès");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de la suppression: " + e.getMessage());
        }

        return "redirect:/admin/utilisateurs";
    }

    /**
     * Profil utilisateur (pour les utilisateurs connectés)
     */
    @GetMapping("/profile")
    public String userProfile(Model model) {
        // TODO: Récupérer l'utilisateur connecté
        model.addAttribute("pageTitle", "Mon Profil");
        model.addAttribute("title", "Mon Profil");
        
        return "user/profile";
    }
}

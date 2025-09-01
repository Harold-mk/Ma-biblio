package com.haroldmokam.ma_biblio.controllers;

import com.haroldmokam.ma_biblio.entites.Emprunt;
import com.haroldmokam.ma_biblio.entites.EtatEmprunt;
import com.haroldmokam.ma_biblio.entites.Livre;
import com.haroldmokam.ma_biblio.entites.Utilisateur;
import com.haroldmokam.ma_biblio.services.EmpruntService;
import com.haroldmokam.ma_biblio.services.LivreService;
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

import java.time.LocalDate;
import java.util.List;

/**
 * Contrôleur Thymeleaf pour la gestion des emprunts
 */
@Controller
@RequestMapping("/admin/emprunts")
public class ThymeleafEmpruntController {

    @Autowired
    private EmpruntService empruntService;

    @Autowired
    private LivreService livreService;

    @Autowired
    private UtilisateurService utilisateurService;

    /**
     * Liste des emprunts avec pagination et filtres
     */
    @GetMapping
    public String listEmprunts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String etat,
            @RequestParam(required = false) String utilisateur,
            Model model) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("dateDebutEmprunt").descending());
        Page<Emprunt> empruntsPage;

        if (search != null && !search.trim().isEmpty()) {
            empruntsPage = empruntService.rechercherEmprunts(search, pageable);
        } else if (etat != null && !etat.trim().isEmpty()) {
            empruntsPage = empruntService.rechercherEmpruntsParEtat(EtatEmprunt.valueOf(etat), pageable);
        } else if (utilisateur != null && !utilisateur.trim().isEmpty()) {
            empruntsPage = empruntService.rechercherEmpruntsParUtilisateur(utilisateur, pageable);
        } else {
            empruntsPage = empruntService.getAllEmprunts(pageable);
        }

        List<EtatEmprunt> etats = List.of(EtatEmprunt.values());
        List<Utilisateur> utilisateurs = utilisateurService.getAllUtilisateurs();

        model.addAttribute("pageTitle", "Gestion des Emprunts");
        model.addAttribute("title", "Gestion des Emprunts");
        model.addAttribute("emprunts", empruntsPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", empruntsPage.getTotalPages());
        model.addAttribute("totalItems", empruntsPage.getTotalElements());
        model.addAttribute("search", search);
        model.addAttribute("etat", etat);
        model.addAttribute("utilisateur", utilisateur);
        model.addAttribute("etats", etats);
        model.addAttribute("utilisateurs", utilisateurs);

        return "admin/emprunts/list";
    }

    /**
     * Formulaire de création d'un nouvel emprunt
     */
    @GetMapping("/create")
    public String createEmpruntForm(Model model) {
        List<Livre> livres = livreService.getAllLivres();
        List<Utilisateur> utilisateurs = utilisateurService.getAllUtilisateurs();
        
        model.addAttribute("pageTitle", "Nouvel Emprunt");
        model.addAttribute("title", "Nouvel Emprunt");
        model.addAttribute("emprunt", new Emprunt());
        model.addAttribute("livres", livres);
        model.addAttribute("utilisateurs", utilisateurs);
        model.addAttribute("isEdit", false);

        return "admin/emprunts/form";
    }

    /**
     * Formulaire de modification d'un emprunt existant
     */
    @GetMapping("/{id}/edit")
    public String editEmpruntForm(@PathVariable int id, Model model) {
        Emprunt emprunt = empruntService.getEmpruntById(id);
        if (emprunt == null) {
            return "redirect:/admin/emprunts";
        }

        List<Livre> livres = livreService.getAllLivres();
        List<Utilisateur> utilisateurs = utilisateurService.getAllUtilisateurs();
        
        model.addAttribute("pageTitle", "Modifier l'Emprunt");
        model.addAttribute("title", "Modifier l'Emprunt");
        model.addAttribute("emprunt", emprunt);
        model.addAttribute("livres", livres);
        model.addAttribute("utilisateurs", utilisateurs);
        model.addAttribute("isEdit", true);

        return "admin/emprunts/form";
    }

    /**
     * Affichage des détails d'un emprunt
     */
    @GetMapping("/{id}")
    public String viewEmprunt(@PathVariable int id, Model model) {
        Emprunt emprunt = empruntService.getEmpruntById(id);
        if (emprunt == null) {
            return "redirect:/admin/emprunts";
        }

        model.addAttribute("pageTitle", "Détails de l'Emprunt");
        model.addAttribute("title", "Détails de l'Emprunt");
        model.addAttribute("emprunt", emprunt);

        return "admin/emprunts/detail";
    }

    /**
     * Traitement de la création/modification d'un emprunt
     */
    @PostMapping
    public String saveEmprunt(@ModelAttribute Emprunt emprunt, 
                             @RequestParam(defaultValue = "false") boolean isEdit,
                             RedirectAttributes redirectAttributes) {
        try {
            if (isEdit) {
                empruntService.modifierEmprunt(emprunt.getId(), emprunt);
                redirectAttributes.addFlashAttribute("success", "Emprunt modifié avec succès");
            } else {
                empruntService.creerEmprunt(emprunt);
                redirectAttributes.addFlashAttribute("success", "Emprunt créé avec succès");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de l'opération: " + e.getMessage());
        }

        return "redirect:/admin/emprunts";
    }

    /**
     * Retour d'un livre emprunté
     */
    @PostMapping("/{id}/retour")
    public String retournerLivre(@PathVariable int id, RedirectAttributes redirectAttributes) {
        try {
            empruntService.retournerLivre(id);
            redirectAttributes.addFlashAttribute("success", "Livre retourné avec succès");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors du retour: " + e.getMessage());
        }

        return "redirect:/admin/emprunts";
    }

    /**
     * Prolongation d'un emprunt
     */
    @PostMapping("/{id}/prolonger")
    public String prolongerEmprunt(@PathVariable int id, RedirectAttributes redirectAttributes) {
        try {
            empruntService.prolongerEmprunt(id);
            redirectAttributes.addFlashAttribute("success", "Emprunt prolongé avec succès");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de la prolongation: " + e.getMessage());
        }

        return "redirect:/admin/emprunts";
    }

    /**
     * Suppression d'un emprunt
     */
    @PostMapping("/{id}/delete")
    public String deleteEmprunt(@PathVariable int id, RedirectAttributes redirectAttributes) {
        try {
            empruntService.supprimerEmprunt(id);
            redirectAttributes.addFlashAttribute("success", "Emprunt supprimé avec succès");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de la suppression: " + e.getMessage());
        }

        return "redirect:/admin/emprunts";
    }

    /**
     * Emprunts en retard
     */
    @GetMapping("/retard")
    public String empruntsEnRetard(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("dateFinEmprunt").ascending());
        Page<Emprunt> empruntsPage = empruntService.getEmpruntsEnRetard(pageable);

        model.addAttribute("pageTitle", "Emprunts en Retard");
        model.addAttribute("title", "Emprunts en Retard");
        model.addAttribute("emprunts", empruntsPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", empruntsPage.getTotalPages());
        model.addAttribute("totalItems", empruntsPage.getTotalElements());

        return "admin/emprunts/retard";
    }
}

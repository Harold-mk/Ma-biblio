package com.haroldmokam.ma_biblio.controllers;

import com.haroldmokam.ma_biblio.entites.Livre;
import com.haroldmokam.ma_biblio.entites.Categorie;
import com.haroldmokam.ma_biblio.services.LivreService;
import com.haroldmokam.ma_biblio.services.CategorieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * Contrôleur Thymeleaf pour la gestion des livres
 */
@Controller
@RequestMapping("/admin/livres")
public class ThymeleafLivreController {

    @Autowired
    private LivreService livreService;

    @Autowired
    private CategorieService categorieService;

    /**
     * Liste des livres avec pagination et filtres
     */
    @GetMapping
    public String listLivres(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String categorie,
            Model model) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("titre").ascending());
        Page<Livre> livresPage;

        if (search != null && !search.trim().isEmpty()) {
            livresPage = livreService.rechercherLivres(search, pageable);
        } else if (categorie != null && !categorie.trim().isEmpty()) {
            livresPage = livreService.rechercherLivresParCategorie(categorie, pageable);
        } else {
            livresPage = livreService.getAllLivres(pageable);
        }

        List<Categorie> categories = categorieService.getAllCategories();

        model.addAttribute("pageTitle", "Gestion des Livres");
        model.addAttribute("title", "Gestion des Livres");
        model.addAttribute("livres", livresPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", livresPage.getTotalPages());
        model.addAttribute("totalItems", livresPage.getTotalElements());
        model.addAttribute("search", search);
        model.addAttribute("categorie", categorie);
        model.addAttribute("categories", categories);

        return "admin/livres/list";
    }

    /**
     * Formulaire de création d'un nouveau livre
     */
    @GetMapping("/create")
    public String createLivreForm(Model model) {
        List<Categorie> categories = categorieService.getAllCategories();
        
        model.addAttribute("pageTitle", "Nouveau Livre");
        model.addAttribute("title", "Nouveau Livre");
        model.addAttribute("livre", new Livre());
        model.addAttribute("categories", categories);
        model.addAttribute("isEdit", false);

        return "admin/livres/form";
    }

    /**
     * Formulaire de modification d'un livre existant
     */
    @GetMapping("/{id}/edit")
    public String editLivreForm(@PathVariable int id, Model model) {
        Livre livre = livreService.getLivreById(id);
        if (livre == null) {
            return "redirect:/admin/livres";
        }

        List<Categorie> categories = categorieService.getAllCategories();
        
        model.addAttribute("pageTitle", "Modifier le Livre");
        model.addAttribute("title", "Modifier le Livre");
        model.addAttribute("livre", livre);
        model.addAttribute("categories", categories);
        model.addAttribute("isEdit", true);

        return "admin/livres/form";
    }

    /**
     * Affichage des détails d'un livre
     */
    @GetMapping("/{id}")
    public String viewLivre(@PathVariable int id, Model model) {
        Livre livre = livreService.getLivreById(id);
        if (livre == null) {
            return "redirect:/admin/livres";
        }

        model.addAttribute("pageTitle", livre.getTitre());
        model.addAttribute("title", livre.getTitre());
        model.addAttribute("livre", livre);

        return "admin/livres/detail";
    }

    /**
     * Traitement de la création/modification d'un livre
     */
    @PostMapping
    public String saveLivre(@ModelAttribute Livre livre, 
                           @RequestParam(defaultValue = "false") boolean isEdit,
                           RedirectAttributes redirectAttributes) {
        try {
            if (isEdit) {
                livreService.modifierLivre(livre.getId(), livre);
                redirectAttributes.addFlashAttribute("success", "Livre modifié avec succès");
            } else {
                livreService.creerLivre(livre);
                redirectAttributes.addFlashAttribute("success", "Livre créé avec succès");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de l'opération: " + e.getMessage());
        }

        return "redirect:/admin/livres";
    }

    /**
     * Suppression d'un livre
     */
    @PostMapping("/{id}/delete")
    public String deleteLivre(@PathVariable int id, RedirectAttributes redirectAttributes) {
        try {
            livreService.supprimerLivre(id);
            redirectAttributes.addFlashAttribute("success", "Livre supprimé avec succès");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de la suppression: " + e.getMessage());
        }

        return "redirect:/admin/livres";
    }
}

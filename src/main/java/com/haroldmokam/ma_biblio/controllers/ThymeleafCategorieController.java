package com.haroldmokam.ma_biblio.controllers;

import com.haroldmokam.ma_biblio.entites.Categorie;
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
 * Contrôleur Thymeleaf pour la gestion des catégories
 */
@Controller
@RequestMapping("/admin/categories")
public class ThymeleafCategorieController {

    @Autowired
    private CategorieService categorieService;

    /**
     * Liste des catégories avec pagination
     */
    @GetMapping
    public String listCategories(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,
            Model model) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("libelle").ascending());
        Page<Categorie> categoriesPage;

        if (search != null && !search.trim().isEmpty()) {
            categoriesPage = categorieService.rechercherCategories(search, pageable);
        } else {
            categoriesPage = categorieService.getAllCategories(pageable);
        }

        model.addAttribute("pageTitle", "Gestion des Catégories");
        model.addAttribute("title", "Gestion des Catégories");
        model.addAttribute("categories", categoriesPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", categoriesPage.getTotalPages());
        model.addAttribute("totalItems", categoriesPage.getTotalElements());
        model.addAttribute("search", search);

        return "admin/categories/list";
    }

    /**
     * Formulaire de création d'une nouvelle catégorie
     */
    @GetMapping("/create")
    public String createCategorieForm(Model model) {
        model.addAttribute("pageTitle", "Nouvelle Catégorie");
        model.addAttribute("title", "Nouvelle Catégorie");
        model.addAttribute("categorie", new Categorie());
        model.addAttribute("isEdit", false);

        return "admin/categories/form";
    }

    /**
     * Formulaire de modification d'une catégorie existante
     */
    @GetMapping("/{id}/edit")
    public String editCategorieForm(@PathVariable int id, Model model) {
        Categorie categorie = categorieService.getCategorieById(id);
        if (categorie == null) {
            return "redirect:/admin/categories";
        }

        model.addAttribute("pageTitle", "Modifier la Catégorie");
        model.addAttribute("title", "Modifier la Catégorie");
        model.addAttribute("categorie", categorie);
        model.addAttribute("isEdit", true);

        return "admin/categories/form";
    }

    /**
     * Affichage des détails d'une catégorie
     */
    @GetMapping("/{id}")
    public String viewCategorie(@PathVariable int id, Model model) {
        Categorie categorie = categorieService.getCategorieById(id);
        if (categorie == null) {
            return "redirect:/admin/categories";
        }

        model.addAttribute("pageTitle", categorie.getLibelle());
        model.addAttribute("title", categorie.getLibelle());
        model.addAttribute("categorie", categorie);

        return "admin/categories/detail";
    }

    /**
     * Traitement de la création/modification d'une catégorie
     */
    @PostMapping
    public String saveCategorie(@ModelAttribute Categorie categorie, 
                               @RequestParam(defaultValue = "false") boolean isEdit,
                               RedirectAttributes redirectAttributes) {
        try {
            if (isEdit) {
                categorieService.modifierCategorie(categorie.getId(), categorie);
                redirectAttributes.addFlashAttribute("success", "Catégorie modifiée avec succès");
            } else {
                categorieService.creerCategorie(categorie);
                redirectAttributes.addFlashAttribute("success", "Catégorie créée avec succès");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de l'opération: " + e.getMessage());
        }

        return "redirect:/admin/categories";
    }

    /**
     * Suppression d'une catégorie
     */
    @PostMapping("/{id}/delete")
    public String deleteCategorie(@PathVariable int id, RedirectAttributes redirectAttributes) {
        try {
            categorieService.supprimerCategorie(id);
            redirectAttributes.addFlashAttribute("success", "Catégorie supprimée avec succès");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de la suppression: " + e.getMessage());
        }

        return "redirect:/admin/categories";
    }

    /**
     * Catalogue des catégories (pour les utilisateurs)
     */
    @GetMapping("/catalogue")
    public String catalogueCategories(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            Model model) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("libelle").ascending());
        Page<Categorie> categoriesPage = categorieService.getAllCategories(pageable);

        model.addAttribute("pageTitle", "Catalogue des Catégories");
        model.addAttribute("title", "Catalogue des Catégories");
        model.addAttribute("categories", categoriesPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", categoriesPage.getTotalPages());
        model.addAttribute("totalItems", categoriesPage.getTotalElements());

        return "user/categories/catalogue";
    }
}

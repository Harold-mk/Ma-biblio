package com.haroldmokam.ma_biblio.controllers;

import com.haroldmokam.ma_biblio.entites.Notification;
import com.haroldmokam.ma_biblio.entites.Utilisateur;
import com.haroldmokam.ma_biblio.services.NotificationService;
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

import java.util.List;

/**
 * Contrôleur Thymeleaf pour la gestion des notifications
 */
@Controller
@RequestMapping("/admin/notifications")
public class ThymeleafNotificationController {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private UtilisateurService utilisateurService;

    /**
     * Liste des notifications avec pagination et filtres
     */
    @GetMapping
    public String listNotifications(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String utilisateur,
            @RequestParam(required = false) Boolean lu,
            Model model) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("dateCreation").descending());
        Page<Notification> notificationsPage;

        if (search != null && !search.trim().isEmpty()) {
            notificationsPage = notificationService.rechercherNotifications(search, pageable);
        } else if (utilisateur != null && !utilisateur.trim().isEmpty()) {
            notificationsPage = notificationService.rechercherNotificationsParUtilisateur(utilisateur, pageable);
        } else if (lu != null) {
            notificationsPage = notificationService.rechercherNotificationsParStatut(lu, pageable);
        } else {
            notificationsPage = notificationService.getAllNotifications(pageable);
        }

        List<Utilisateur> utilisateurs = utilisateurService.getAllUtilisateurs();

        model.addAttribute("pageTitle", "Gestion des Notifications");
        model.addAttribute("title", "Gestion des Notifications");
        model.addAttribute("notifications", notificationsPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", notificationsPage.getTotalPages());
        model.addAttribute("totalItems", notificationsPage.getTotalElements());
        model.addAttribute("search", search);
        model.addAttribute("utilisateur", utilisateur);
        model.addAttribute("lu", lu);
        model.addAttribute("utilisateurs", utilisateurs);

        return "admin/notifications/list";
    }

    /**
     * Formulaire de création d'une nouvelle notification
     */
    @GetMapping("/create")
    public String createNotificationForm(Model model) {
        List<Utilisateur> utilisateurs = utilisateurService.getAllUtilisateurs();
        
        model.addAttribute("pageTitle", "Nouvelle Notification");
        model.addAttribute("title", "Nouvelle Notification");
        model.addAttribute("notification", new Notification());
        model.addAttribute("utilisateurs", utilisateurs);
        model.addAttribute("isEdit", false);

        return "admin/notifications/form";
    }

    /**
     * Formulaire de modification d'une notification existante
     */
    @GetMapping("/{id}/edit")
    public String editNotificationForm(@PathVariable int id, Model model) {
        Notification notification = notificationService.getNotificationById(id);
        if (notification == null) {
            return "redirect:/admin/notifications";
        }

        List<Utilisateur> utilisateurs = utilisateurService.getAllUtilisateurs();
        
        model.addAttribute("pageTitle", "Modifier la Notification");
        model.addAttribute("title", "Modifier la Notification");
        model.addAttribute("notification", notification);
        model.addAttribute("utilisateurs", utilisateurs);
        model.addAttribute("isEdit", true);

        return "admin/notifications/form";
    }

    /**
     * Affichage des détails d'une notification
     */
    @GetMapping("/{id}")
    public String viewNotification(@PathVariable int id, Model model) {
        Notification notification = notificationService.getNotificationById(id);
        if (notification == null) {
            return "redirect:/admin/notifications";
        }

        model.addAttribute("pageTitle", "Détails de la Notification");
        model.addAttribute("title", "Détails de la Notification");
        model.addAttribute("notification", notification);

        return "admin/notifications/detail";
    }

    /**
     * Traitement de la création/modification d'une notification
     */
    @PostMapping
    public String saveNotification(@ModelAttribute Notification notification, 
                                 @RequestParam(defaultValue = "false") boolean isEdit,
                                 RedirectAttributes redirectAttributes) {
        try {
            if (isEdit) {
                notificationService.modifierNotification(notification.getId(), notification);
                redirectAttributes.addFlashAttribute("success", "Notification modifiée avec succès");
            } else {
                notificationService.creerNotification(notification);
                redirectAttributes.addFlashAttribute("success", "Notification créée avec succès");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de l'opération: " + e.getMessage());
        }

        return "redirect:/admin/notifications";
    }

    /**
     * Marquer une notification comme lue
     */
    @PostMapping("/{id}/marquer-lu")
    public String marquerCommeLu(@PathVariable int id, RedirectAttributes redirectAttributes) {
        try {
            notificationService.marquerCommeLu(id);
            redirectAttributes.addFlashAttribute("success", "Notification marquée comme lue");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de l'opération: " + e.getMessage());
        }

        return "redirect:/admin/notifications";
    }

    /**
     * Suppression d'une notification
     */
    @PostMapping("/{id}/delete")
    public String deleteNotification(@PathVariable int id, RedirectAttributes redirectAttributes) {
        try {
            notificationService.supprimerNotification(id);
            redirectAttributes.addFlashAttribute("success", "Notification supprimée avec succès");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de la suppression: " + e.getMessage());
        }

        return "redirect:/admin/notifications";
    }

    /**
     * Notifications de l'utilisateur connecté
     */
    @GetMapping("/mes-notifications")
    public String mesNotifications(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {
        
        // TODO: Récupérer l'utilisateur connecté
        Pageable pageable = PageRequest.of(page, size, Sort.by("dateCreation").descending());
        Page<Notification> notificationsPage = notificationService.getAllNotifications(pageable);

        model.addAttribute("pageTitle", "Mes Notifications");
        model.addAttribute("title", "Mes Notifications");
        model.addAttribute("notifications", notificationsPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", notificationsPage.getTotalPages());
        model.addAttribute("totalItems", notificationsPage.getTotalElements());

        return "user/notifications/list";
    }
}

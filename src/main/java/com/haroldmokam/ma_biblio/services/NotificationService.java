package com.haroldmokam.ma_biblio.services;

import com.haroldmokam.ma_biblio.Repositories.NotificationRepository;
import com.haroldmokam.ma_biblio.Repositories.UtilisateurRepository;
import com.haroldmokam.ma_biblio.entites.Emprunt;
import com.haroldmokam.ma_biblio.entites.Notification;
import com.haroldmokam.ma_biblio.entites.Utilisateur;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UtilisateurRepository utilisateurRepository;

    // Créer une notification
    public void creerNotification(Notification notification) {
        notification.setDateCreation(LocalDateTime.now());
        notification.setLu(false);
        notificationRepository.save(notification);
    }
    
    // Modifier une notification
    public void modifierNotification(int id, Notification notification) {
        Notification notificationExistant = getNotificationById(id);
        notification.setId(id);
        notificationRepository.save(notification);
    }
    
    // Supprimer une notification
    public void supprimerNotification(int id) {
        notificationRepository.deleteById(id);
    }
    
    // Obtenir une notification par ID
    public Notification getNotificationById(int id) {
        return notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification non trouvée avec l'ID: " + id));
    }
    
    // Obtenir toutes les notifications avec pagination
    public Page<Notification> getAllNotifications(Pageable pageable) {
        return notificationRepository.findAll(pageable);
    }
    
    // Obtenir toutes les notifications sans pagination
    public List<Notification> getAllNotifications() {
        return notificationRepository.findAll();
    }
    
    // Marquer une notification comme lue
    public void marquerCommeLu(int id) {
        Notification notification = getNotificationById(id);
        notification.setLu(true);
        notificationRepository.save(notification);
    }
    
    // Rechercher des notifications avec pagination
    public Page<Notification> rechercherNotifications(String search, Pageable pageable) {
        return notificationRepository.findByTitleContainingIgnoreCaseOrMessageContainingIgnoreCase(
                search, search, pageable);
    }
    
    // Rechercher des notifications par utilisateur avec pagination
    public Page<Notification> rechercherNotificationsParUtilisateur(String utilisateurNom, Pageable pageable) {
        return notificationRepository.findByUtilisateurDestinataireNomContainingIgnoreCase(utilisateurNom, pageable);
    }
    
    // Rechercher des notifications par statut avec pagination
    public Page<Notification> rechercherNotificationsParStatut(boolean lu, Pageable pageable) {
        return notificationRepository.findByLu(lu, pageable);
    }

    // Creation d'une notification de creation d'emprunt et precision sur la date de fin de l'emprunt.
    public void notificationEmprunt(Emprunt emprunt) {
        Notification notification = new Notification();
        notification.setTitle("Vous avez effectué un nouvel emprunt.");
        notification.setMessage("Vous avez emprunté le livre : " + emprunt.getLivre().getTitre() +
                ". La date de remise est le : " + emprunt.getDateFinEmprunt());
        notification.setUtilisateurDestinataire(emprunt.getUtilisateur());
        notification.setDateCreation(LocalDateTime.now());
        notification.setLu(false);
        
        // Sauvegarder la notification
        notificationRepository.save(notification);
        
        // Mettre à jour la liste des notifications de l'utilisateur
        Utilisateur utilisateur = emprunt.getUtilisateur();
        List<Notification> notifications = utilisateur.getNotifications();
        if (notifications == null) {
            notifications = new ArrayList<>();
        }
        notifications.add(notification);
        utilisateur.setNotifications(notifications);
        utilisateurRepository.save(utilisateur);
    }
    
    // Faire la meme notification a envoyer par mail a l'utilisateur
    // Creation d'une notification de la mise en garde du fait que la date de remise du livre approche
    public void notificationAlerteRemise(Emprunt emprunt) {
        Notification notification = new Notification();
        notification.setTitle("Votre date de remise de l'emprunt approche.");
        notification.setMessage("Monsieur, madame : "+emprunt.getUtilisateur().getNom() + " Votre date de remise du livre : "+ emprunt.getLivre().getTitre() + " que vous avez emprunté est proche.</br> Cordialement.");
        notification.setUtilisateurDestinataire(emprunt.getUtilisateur());
        notification.setDateCreation(LocalDateTime.now());
        notification.setLu(false);
        
        // Sauvegarder la notification
        notificationRepository.save(notification);
        
        // Mettre à jour la liste des notifications de l'utilisateur
        Utilisateur utilisateur = emprunt.getUtilisateur();
        List<Notification> notifications = utilisateur.getNotifications();
        if (notifications == null) {
            notifications = new ArrayList<>();
        }
        notifications.add(notification);
        utilisateur.setNotifications(notifications);
        utilisateurRepository.save(utilisateur);
    }
    
    // Faire la meme notification a envoyer par mail a l'utilisateur
    // Creation d'une notification de la mise en garde du fait que la date de remise du livre est depassee
    public void notificationExpirationEmprunt (Emprunt emprunt) {
        Notification notification = new Notification();
        notification.setTitle("L'emprunt du livre : "+emprunt.getLivre().getTitre()+" est arrivé à expiration");
        notification.setMessage("Monsieur, madame : "+emprunt.getUtilisateur().getNom() + " L'emprunt du livre : "+emprunt.getLivre().getTitre()+" est arrivé à expiration. </br> Cordialement.");
        notification.setUtilisateurDestinataire(emprunt.getUtilisateur());
        notification.setDateCreation(LocalDateTime.now());
        notification.setLu(false);
        
        // Sauvegarder la notification
        notificationRepository.save(notification);
        
        // Mettre à jour la liste des notifications de l'utilisateur
        Utilisateur utilisateur = emprunt.getUtilisateur();
        List<Notification> notifications = utilisateur.getNotifications();
        if (notifications == null) {
            notifications = new ArrayList<>();
        }
        notifications.add(notification);
        utilisateur.setNotifications(notifications);
        utilisateurRepository.save(utilisateur);
    }
    
    //Creation d'une notification qui averti du prolongement de la date de remise du livre.
    public void notificationProlongationEmprunt (Emprunt emprunt, LocalDate dateProlongation) {
        Notification notification = new Notification();
        notification.setTitle("Prolongation de votre emprunt pour le livre : "+emprunt.getLivre().getTitre());
        notification.setMessage("Monsieur, madame : "+emprunt.getUtilisateur().getNom() + " Vous avez prolongé l'emprunt du livre: "+ emprunt.getLivre().getTitre() + ". </br> La nouvelle date de remise est: "+dateProlongation +"</br> Cordialement.");
        notification.setUtilisateurDestinataire(emprunt.getUtilisateur());
        notification.setDateCreation(LocalDateTime.now());
        notification.setLu(false);
        
        // Sauvegarder la notification
        notificationRepository.save(notification);
        
        // Mettre à jour la liste des notifications de l'utilisateur
        Utilisateur utilisateur = emprunt.getUtilisateur();
        List<Notification> notifications = utilisateur.getNotifications();
        if (notifications == null) {
            notifications = new ArrayList<>();
        }
        notifications.add(notification);
        utilisateur.setNotifications(notifications);
        utilisateurRepository.save(utilisateur);
    }

    public void notificationRemiseLivre (Emprunt emprunt, Utilisateur utilisateur) {
        Notification notification = new Notification();
        notification.setTitle("Remise du livre : "+emprunt.getLivre().getTitre());
        notification.setMessage("Monsieur, madame : "+emprunt.getUtilisateur().getNom() + " Vous venez de remettre le livre : "+ emprunt.getLivre().getTitre() + " Vous pouvez effectuer à présent : " + utilisateur.getNombreEmpruntRestant()+" emprunt(s)");
        notification.setUtilisateurDestinataire(emprunt.getUtilisateur());
        notification.setDateCreation(LocalDateTime.now());
        notification.setLu(false);
        
        // Sauvegarder la notification
        notificationRepository.save(notification);
        
        // Mettre à jour la liste des notifications de l'utilisateur
        List<Notification> notifications = utilisateur.getNotifications();
        if (notifications == null) {
            notifications = new ArrayList<>();
        }
        notifications.add(notification);
        utilisateur.setNotifications(notifications);
        utilisateurRepository.save(utilisateur);
    }

    /**
     * Compter les notifications non lues pour un utilisateur spécifique
     */
    public int countNotificationsNonLuesByUtilisateur(int utilisateurId) {
        return (int) notificationRepository.findByUtilisateurDestinataireId(utilisateurId)
                .stream()
                .filter(notification -> !notification.lu)
                .count();
    }

    /**
     * Obtenir les notifications récentes d'un utilisateur
     */
    public List<Notification> getNotificationsRecentesByUtilisateur(int utilisateurId, int limit) {
        return notificationRepository.findByUtilisateurDestinataireId(utilisateurId)
                .stream()
                .sorted((n1, n2) -> n2.getDateCreation().compareTo(n1.getDateCreation()))
                .limit(limit)
                .collect(Collectors.toList());
    }

    /**
     * Obtenir les notifications d'un utilisateur avec pagination
     */
    public Page<Notification> getNotificationsByUtilisateur(int utilisateurId, Pageable pageable) {
        List<Notification> notifications = notificationRepository.findByUtilisateurDestinataireId(utilisateurId);
        
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), notifications.size());
        
        return new org.springframework.data.domain.PageImpl<>(
            notifications.subList(start, end), pageable, notifications.size()
        );
    }
}

package com.haroldmokam.ma_biblio.services;

import com.haroldmokam.ma_biblio.Repositories.NotificationRepository;
import com.haroldmokam.ma_biblio.Repositories.UtilisateurRepository;
import com.haroldmokam.ma_biblio.entites.Emprunt;
import com.haroldmokam.ma_biblio.entites.Notification;
import com.haroldmokam.ma_biblio.entites.Utilisateur;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UtilisateurRepository utilisateurRepository;


    // Creation d'une notification de creation d'emprunt et precision sur la date de fin de l'emprunt.

    public void notificationEmprunt(Emprunt emprunt) {
        Notification notification = new Notification();
        notification.setTitle("Vous avez effectue un nouvel emprunt.");
        notification.setMessage("Vous avez emprunte le livre : " + emprunt.getLivre().getTitre() +
                ". La date de remise est le : " + emprunt.getDateFinEmprunt());
        notification.setUtilisateurDestinataire(emprunt.getUtilisateur());
        List<Notification> notifications = emprunt.getUtilisateur().getNotifications();
        notifications.add(notification);
        emprunt.getUtilisateur().setNotifications(notifications);
        utilisateurRepository.save(emprunt.getUtilisateur());
        notificationRepository.save(notification);
    }
    // Faire la meme notification a envoyer par mail a l'utilisateur
    // Creation d'une notification de la mise en garde du fait que la date de remise du livre approche
    public void notificationAlerteRemise(Emprunt emprunt) {
        Notification notification = new Notification();
        notification.setTitle("Votre date de remise de l'emprunt approche.");
        notification.setMessage("Monsieur, madame : "+emprunt.getUtilisateur().getNom() + " Votre date de remise du livre : "+ emprunt.getLivre().getTitre() + " que vous avez emprunte est proche.</br> Cordialement.");
        notification.setUtilisateurDestinataire(emprunt.getUtilisateur());
        List<Notification> notifications = emprunt.getUtilisateur().getNotifications();
        notifications.add(notification);
        emprunt.getUtilisateur().setNotifications(notifications);
        utilisateurRepository.save(emprunt.getUtilisateur());
        notificationRepository.save(notification);
    }
    // Faire la meme notification a envoyer par mail a l'utilisateur
    // Creation d'une notification de la mise en garde du fait que la date de remise du livre est depassee
    public void notificationExpirationEmprunt (Emprunt emprunt) {
        Notification notification = new Notification();
        notification.setTitle("L'emprunt du livre  : "+emprunt.getLivre().getTitre()+"est arrive a expiration");
        notification.setMessage("Monsieur, madame : "+emprunt.getUtilisateur().getNom() + "L'emprunt du livre  : "+emprunt.getLivre().getTitre()+"est arrive a expiration. </br> Cordialement.");
        notification.setUtilisateurDestinataire(emprunt.getUtilisateur());
        List<Notification> notifications = emprunt.getUtilisateur().getNotifications();
        notifications.add(notification);
        emprunt.getUtilisateur().setNotifications(notifications);
        utilisateurRepository.save(emprunt.getUtilisateur());
        notificationRepository.save(notification);
    }
    //Creation d'une notification qui averti du prolongement de la date de remise du livre.
    public void notificationProlongationEmprunt (Emprunt emprunt, LocalDate dateProlongation) {
        Notification notification = new Notification();
        notification.setTitle("Prolongation de votre emprunt pour le livre : "+emprunt.getLivre().getTitre());
        notification.setMessage("Monsieur, madame : "+emprunt.getUtilisateur().getNom() + " Vous avez prolonge l'emprunt du livre: "+ emprunt.getLivre().getTitre() + ". </br> La nouvelle date de remise est: "+dateProlongation +"</br> Cordialement.");
        notification.setUtilisateurDestinataire(emprunt.getUtilisateur());
        List<Notification> notifications = emprunt.getUtilisateur().getNotifications();
        notifications.add(notification);
        emprunt.getUtilisateur().setNotifications(notifications);
        utilisateurRepository.save(emprunt.getUtilisateur());
        notificationRepository.save(notification);
    }

    public void notificationRemiseLivre (Emprunt emprunt, Utilisateur utilisateur) {
        Notification notification = new Notification();
        notification.setTitle("Remise du le livre : "+emprunt.getLivre().getTitre());
        notification.setMessage("Monsieur, madame : "+emprunt.getUtilisateur().getNom() + " Vous venez de remettre le livre : "+ emprunt.getLivre().getTitre() + "Vous pouvez effectuer a present : " + utilisateur.getNombreEmpruntRestant()+" emprunt(s)");
        notification.setUtilisateurDestinataire(emprunt.getUtilisateur());
        List<Notification> notifications = emprunt.getUtilisateur().getNotifications();
        notifications.add(notification);
        emprunt.getUtilisateur().setNotifications(notifications);
        utilisateurRepository.save(emprunt.getUtilisateur());
        notificationRepository.save(notification);
    }
}

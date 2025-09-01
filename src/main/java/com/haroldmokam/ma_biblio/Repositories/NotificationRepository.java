package com.haroldmokam.ma_biblio.Repositories;

import com.haroldmokam.ma_biblio.entites.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {
    
    // Méthodes pour la pagination et recherche
    Page<Notification> findByTitleContainingIgnoreCaseOrMessageContainingIgnoreCase(
            String title, String message, Pageable pageable);
    Page<Notification> findByUtilisateurDestinataireNomContainingIgnoreCase(String utilisateurNom, Pageable pageable);
    Page<Notification> findByLu(boolean lu, Pageable pageable);
    
    // Méthodes pour les utilisateurs
    List<Notification> findByUtilisateurDestinataireId(int utilisateurId);
}

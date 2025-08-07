package com.haroldmokam.ma_biblio.entites;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "Notification")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String title;
    private String message;
    private boolean isRead;

    @ManyToOne
    @JoinColumn(name = "utilisateur_destinataire_id")
    private Utilisateur utilisateurDestinataire;
}

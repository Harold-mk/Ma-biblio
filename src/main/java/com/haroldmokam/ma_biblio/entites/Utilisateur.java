package com.haroldmokam.ma_biblio.entites;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Utilisateur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String nom;
    @Column(unique = true)
    private String mail;
    @Column(unique = true)
    private String matricule;
    private String password;
    private Statut statut;
    private RoleUtilisateur role;
    private int nombreEmpruntActif =5;// Definition du nombre maximal d'emprunt = 5
    private int nombreEmpruntRestant;

    @OneToMany(mappedBy = "utilisateurDestinataire", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Notification> notifications;
    @OneToMany( mappedBy = "utilisateur", cascade = CascadeType.ALL)
    private List<Emprunt> emprunts;

}

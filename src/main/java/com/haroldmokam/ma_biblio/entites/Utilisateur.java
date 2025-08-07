package com.haroldmokam.ma_biblio.entites;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.context.support.BeanDefinitionDsl;

import java.io.Serializable;
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

}

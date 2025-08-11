package com.haroldmokam.ma_biblio.entites;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "Emprunt")
public class Emprunt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private LocalDate dateDebutEmprunt;
    private LocalDate dateFinEmprunt;
    private EtatEmprunt etatEmprunt;
    private boolean estRemis = false;

    @ManyToOne
    @JoinColumn(name = "livre_id")
    private Livre livre;

    @ManyToOne
    @JoinColumn(name = "utilisateur_id")
    private Utilisateur utilisateur;

}

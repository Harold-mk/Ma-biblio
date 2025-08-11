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
@Table(name = "Livre")
public class Livre {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int id;
    private String titre;
    private String auteur;
    private String description;
    private int nbreExemplairesInitial;
    private int nbreExemplairesRestant;
    private String imageUrl;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "categorie_id", nullable = false)
    private Categorie categorie;

    @OneToMany(mappedBy = "livre")
    private List<Emprunt> emprunts;

}

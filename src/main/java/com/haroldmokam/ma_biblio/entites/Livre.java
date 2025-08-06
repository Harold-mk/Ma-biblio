package com.haroldmokam.ma_biblio.entites;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private int nbreExemplairesFinal;
    private String imageUrl;
    @ManyToOne(cascade = CascadeType.ALL)
    private Categorie categorie;

}

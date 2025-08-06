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
@Table(name = "Categorie")
public class Categorie {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int id;
    private String libelle;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Livre> livres;
}

package com.haroldmokam.ma_biblio.Repositories;

import com.haroldmokam.ma_biblio.entites.Emprunt;
import com.haroldmokam.ma_biblio.entites.EtatEmprunt;
import com.haroldmokam.ma_biblio.entites.Livre;
import com.haroldmokam.ma_biblio.entites.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface EmpruntRepository extends JpaRepository<Emprunt, Integer> {
    public Optional<Emprunt> findById(int id);
    public List<Emprunt> findAllByOrderByDateDebutEmpruntAsc();
    public List<Emprunt> findByDateDebutEmpruntLessThanEqual(LocalDate empruntDate);
    public List<Emprunt> findByDateFinEmpruntGreaterThanEqual(LocalDate empruntDate);
    public List <Emprunt> findByDateDebutEmpruntGreaterThanEqual(LocalDate empruntDate);
    public List<Emprunt> findByDateFinEmpruntBetween(LocalDate emprunt1, LocalDate emprunt2);
    public List<Emprunt> findByDateDebutEmpruntBetween(LocalDate emprunt1, LocalDate emprunt2);

    List<Emprunt> findByDateDebutEmprunt(LocalDate dateDebutEmprunt);
    public List<Emprunt> findByDateFinEmprunt(LocalDate empruntDate);

    List<Emprunt> findByUtilisateur(Utilisateur utilisateur);

    List<Emprunt> findByLivre(Livre livre);

    List<Emprunt> findByEtatEmprunt(EtatEmprunt etatEmprunt);
    List<Emprunt> findByEtatEmpruntAndEstRemis(EtatEmprunt etatEmprunt, boolean estRemis);
}

package com.haroldmokam.ma_biblio.Repositories;

import com.haroldmokam.ma_biblio.entites.Emprunt;
import com.haroldmokam.ma_biblio.entites.EtatEmprunt;
import com.haroldmokam.ma_biblio.entites.Livre;
import com.haroldmokam.ma_biblio.entites.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * @author Mokam Harold
 */

 public interface EmpruntRepository extends JpaRepository<Emprunt, Integer> {
     Optional<Emprunt> findById(int id);
     List<Emprunt> findAllByOrderByDateDebutEmpruntAsc();
    /**
     *  ############################# Liste des methode CRUD pour notre filtre ##################################################
     *  List<Emprunt> findByDateDebutEmpruntLessThanEqual(LocalDate empruntDate);
     List<Emprunt> findByDateFinEmpruntGreaterThanEqual(LocalDate empruntDate);
     List <Emprunt> findByDateDebutEmpruntGreaterThanEqual(LocalDate empruntDate);
     List<Emprunt> findByDateFinEmpruntBetween(LocalDate emprunt1, LocalDate emprunt2);
     List<Emprunt> findByDateDebutEmpruntBetween(LocalDate emprunt1, LocalDate emprunt2);
**/
    List<Emprunt> findByDateDebutEmprunt(LocalDate dateDebutEmprunt);
     List<Emprunt> findByDateFinEmprunt(LocalDate empruntDate);

    List<Emprunt> findByUtilisateur(Utilisateur utilisateur);

    List<Emprunt> findByLivre(Livre livre);

    List<Emprunt> findByEtatEmprunt(EtatEmprunt etatEmprunt);
    //List<Emprunt> findByEtatEmpruntAndEstRemis(EtatEmprunt etatEmprunt, boolean estRemis);
}

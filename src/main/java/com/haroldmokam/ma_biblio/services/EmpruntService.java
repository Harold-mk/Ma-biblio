package com.haroldmokam.ma_biblio.services;

import com.haroldmokam.ma_biblio.Repositories.EmpruntRepository;
import com.haroldmokam.ma_biblio.Repositories.LivreRepository;
import com.haroldmokam.ma_biblio.Repositories.UtilisateurRepository;
import com.haroldmokam.ma_biblio.entites.Emprunt;
import com.haroldmokam.ma_biblio.entites.EtatEmprunt;
import com.haroldmokam.ma_biblio.entites.Livre;
import com.haroldmokam.ma_biblio.entites.Utilisateur;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 *
 */
@Service
@AllArgsConstructor
public class EmpruntService {

    //Injection des dependances
    private final EmpruntRepository empruntRepository;
    private final LivreService livreService;
    private final NotificationService notificationService;
    private final UtilisateurRepository utilisateurRepository;
    private final LivreRepository livreRepository;

    //Creation d'un emprunt
    public void creerEmprunt(Utilisateur utilisateur, Livre livre) {
        Emprunt emprunt = new Emprunt();
        LocalDate localDate = LocalDate.now();
        emprunt.setUtilisateur(utilisateur);
        emprunt.setLivre(livre);
        emprunt.setDateDebutEmprunt(localDate);
        emprunt.setEtatEmprunt( EtatEmprunt.EN_COURS);
        emprunt.setDateFinEmprunt(localDate.plusWeeks(2)); // Ajoute deux semaines a la date locale pour la date de remises du document
        // Inserer la methode de notification a l'utilisateur pour les utilisateurs
        notificationService.notificationEmprunt(emprunt);
        // Methode pour inserer le nombre d'emprunts restants...
        Utilisateur utilisateur1 = emprunt.getUtilisateur() ;
        Livre livre1 = emprunt.getLivre() ;
        utilisateurRepository.save(utilisateur1);
        if(utilisateur.getNombreEmpruntActif() >=1 && livre1.getNbreExemplairesRestant()>=1){
            utilisateur1.setNombreEmpruntActif(utilisateur.getNombreEmpruntActif()-1);
            livre1.setNbreExemplairesRestant(livre1.getNbreExemplairesRestant()-1);
            livreRepository.save(livre1);
            utilisateurRepository.save(utilisateur1);
            empruntRepository.save(emprunt);
        }
        else {
        }

    }

    // Changement de l'etat d'un emprunt: Du genre si un le livre a ete remis alors on change l'etat emprunt.
    public void remiseEmprunt(Emprunt emprunt) {

        Utilisateur utilisateur1 = emprunt.getUtilisateur() ;
        Livre livre1 = emprunt.getLivre() ;
        utilisateur1.setNombreEmpruntRestant(utilisateur1.getNombreEmpruntRestant()+1);
        livre1.setNbreExemplairesRestant(livre1.getNbreExemplairesRestant()+1);
        emprunt.setEstRemis(true);
        emprunt.setEtatEmprunt( EtatEmprunt.REMIS);
        utilisateurRepository.save(utilisateur1);
        livreRepository.save(livre1);
        empruntRepository.save(emprunt);
        notificationService.notificationRemiseLivre(emprunt, utilisateur1);
    }

    // Changement de l'etat d'emprunt du pour effectuer lorsque le delai est expire
    public void expirationEmprunt(Emprunt emprunt) {
        // Chercher un moyen de faire en sorte que la verification ci se fasse chaque et a chaque demarrage de l'application.
        LocalDate localDate = LocalDate.now();
        if((localDate.isEqual(emprunt.getDateFinEmprunt()) || localDate.isAfter(emprunt.getDateFinEmprunt())) && !emprunt.isEstRemis()){
            emprunt.setEtatEmprunt( EtatEmprunt.DELAI_EXPIRE);
            emprunt.setEstRemis(false);
        }
        empruntRepository.save(emprunt);
    }

    //Modification d'un emprunt. Ici on va prolonger encore la date de deux semaines.
    public void prolongementDateEmprunt( int idEmprunt) {
       Optional <Emprunt> emprunt1 = empruntRepository.findById(idEmprunt);
       Emprunt emprunt = emprunt1.orElseThrow(()-> new EntityNotFoundException("Emprunt " + idEmprunt + " n'existe pas"));
        emprunt.setDateFinEmprunt(emprunt.getDateFinEmprunt().plusWeeks(2));
        emprunt.setEtatEmprunt(EtatEmprunt.EN_COURS);
        empruntRepository.save(emprunt);
        // Inserer ici la methode de notification a l'utilisateur...
        notificationService.notificationProlongationEmprunt(emprunt,emprunt.getDateFinEmprunt());
    }
    // Ici on va creer une option qui doit gerer faire en sorte que le bibliothecaire valide et puis un message de notification est envoye a l'utilisateur pour lui faire signe

    // Historique de validation
    public List<Emprunt> ListetotaleDesEmprunts(){
        return empruntRepository.findAllByOrderByDateDebutEmpruntAsc();
    }

    // Historique d'emprunts par date de debut de l'emprunt
    public List<Emprunt> ListeEmpruntsParDateDebutEmprunts(LocalDate dateDebut){
        return empruntRepository.findByDateDebutEmprunt(dateDebut);
    }

    // Historique d'emprunt par date de fin de l'emprunt
    public List<Emprunt> ListeEmpruntsParDateFinEmprunts(LocalDate date){
        return empruntRepository.findByDateFinEmprunt(date);
    }
    // Historique d'emprunts par utilisateur
    public List<Emprunt> ListeEmpruntsParUtilisateur(Utilisateur utilisateur){
        return empruntRepository.findByUtilisateur(utilisateur);
    }
    //Historique d'emprunts par livre
    public List<Emprunt> ListeEmpruntsParLivre(Livre livre){
        return empruntRepository.findByLivre(livre);
    }


    // Historique d'emprunts dont le delai n'est pas expire et le livre n'a pas encore ete remis
    public List<Emprunt> listeEmpruntsEnCours(){
        return empruntRepository.findByEtatEmprunt(EtatEmprunt.EN_COURS);
    }

    // Historique d'emprunts dont le delai est expire
    public List<Emprunt> listeEmpruntsDelaiExpire(){
        return empruntRepository.findByEtatEmprunt(EtatEmprunt.DELAI_EXPIRE);
    }

    //Liste des emprunts dont le delai a expire et le livre n'a pas encore ete remis

    // Historique des emprunts dont le livre a deja ete remis
    public List<Emprunt> listeLivreDejaRemis(){
        return empruntRepository.findByEtatEmprunt(EtatEmprunt.REMIS);
    }

    /**
     *
     * @param id
     * @return
     */
    public Emprunt getEmprunt(int id) {
        Optional<Emprunt> empruntOptional = empruntRepository.findById(id);
        return empruntOptional.orElseThrow(()-> new EntityNotFoundException("Emprunt non trouve"));
    }
    //D'autres methodes sont a venir Par exemple pour les filtres beaucoup plus avances des filtres. Pour l'instant on travaille d'abord avec ceux ci.
    /**
     * Ici on va creer des filtres de recherches plus avances par exemples :
     * - La liste des Emprunts dont les dates de remises sont avant une date precise.
     * - La liste des Emprunts dont les dates de debut sont avant une date precise.
     * - La liste des Emprunts dont les dates de debut sont apres une date precise.
     * - La liste des Emprunts dont les dates de debut sont entre deux dates precises.
     * - la liste des emprunts dont les dates de remises  sont entre deux dates precises.
     * - La liste des emprunts en fonction des livres et des dates.
     * - La lite des emprunts en fonction des utilisateurs et des dates. Meme comme je ne vois pas trop la necessite ici mais bon...
     * - Les services pour exporter l'historique des emprunts sous format PDF.
     */
}

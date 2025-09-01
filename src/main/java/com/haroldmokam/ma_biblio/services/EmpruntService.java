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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 *
 */
@Service
@AllArgsConstructor
public class EmpruntService {

    //Injection des dependances
    private final EmpruntRepository empruntRepository;
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
        emprunt.setDateFinEmprunt(localDate.plusWeeks(2)); // Ajoute deux semaines à la date locale pour la date de remises du document
        
        // Vérifier les conditions avant de créer l'emprunt
        if(utilisateur.getNombreEmpruntActif() >= 1 && livre.getNbreExemplairesRestant() >= 1){
            // Mettre à jour les compteurs
            utilisateur.setNombreEmpruntActif(utilisateur.getNombreEmpruntActif() - 1);
            livre.setNbreExemplairesRestant(livre.getNbreExemplairesRestant() - 1);
            
            // Sauvegarder les entités
            livreRepository.save(livre);
            utilisateurRepository.save(utilisateur);
            empruntRepository.save(emprunt);
            
            // Créer la notification
            notificationService.notificationEmprunt(emprunt);
        } else {
            throw new RuntimeException("Opération impossible : Soit l'utilisateur ne dispose plus d'emprunt, soit il n'existe plus d'exemplaire");
        }
    }
    
    // Créer un emprunt avec l'objet Emprunt complet
    public void creerEmprunt(Emprunt emprunt) {
        LocalDate localDate = LocalDate.now();
        emprunt.setDateDebutEmprunt(localDate);
        emprunt.setEtatEmprunt(EtatEmprunt.EN_COURS);
        emprunt.setDateFinEmprunt(localDate.plusWeeks(2));
        
        Utilisateur utilisateur = emprunt.getUtilisateur();
        Livre livre = emprunt.getLivre();
        
        if(utilisateur.getNombreEmpruntActif() >= 1 && livre.getNbreExemplairesRestant() >= 1){
            utilisateur.setNombreEmpruntActif(utilisateur.getNombreEmpruntActif() - 1);
            livre.setNbreExemplairesRestant(livre.getNbreExemplairesRestant() - 1);
            
            livreRepository.save(livre);
            utilisateurRepository.save(utilisateur);
            empruntRepository.save(emprunt);
            
            notificationService.notificationEmprunt(emprunt);
        } else {
            throw new RuntimeException("Opération impossible : Soit l'utilisateur ne dispose plus d'emprunt, soit il n'existe plus d'exemplaire");
        }
    }

    // Changement de l'etat d'un emprunt : Du genre si un le livre a ete remis alors on change l'etat emprunt.
    public void remiseEmprunt(Emprunt emprunt) {
        Utilisateur utilisateur = emprunt.getUtilisateur();
        Livre livre = emprunt.getLivre();
        
        // Mettre à jour les compteurs
        utilisateur.setNombreEmpruntRestant(utilisateur.getNombreEmpruntRestant() + 1);
        livre.setNbreExemplairesRestant(livre.getNbreExemplairesRestant() + 1);
        
        // Mettre à jour l'emprunt
        emprunt.setEstRemis(true);
        emprunt.setEtatEmprunt(EtatEmprunt.REMIS);
        
        // Sauvegarder les entités
        utilisateurRepository.save(utilisateur);
        livreRepository.save(livre);
        empruntRepository.save(emprunt);
        
        // Créer la notification
        notificationService.notificationRemiseLivre(emprunt, utilisateur);
    }
    
    // Retourner un livre (alias pour remiseEmprunt)
    public void retournerLivre(int id) {
        Emprunt emprunt = getEmpruntById(id);
        remiseEmprunt(emprunt);
    }
    
    // Prolonger un emprunt
    public void prolongerEmprunt(int id) {
        Emprunt emprunt = getEmpruntById(id);
        emprunt.setDateFinEmprunt(emprunt.getDateFinEmprunt().plusWeeks(2));
        emprunt.setEtatEmprunt(EtatEmprunt.EN_COURS);
        empruntRepository.save(emprunt);
        notificationService.notificationProlongationEmprunt(emprunt, emprunt.getDateFinEmprunt());
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
    
    // Modifier un emprunt
    public void modifierEmprunt(int id, Emprunt emprunt) {
        Emprunt empruntExistant = getEmpruntById(id);
        emprunt.setId(id);
        empruntRepository.save(emprunt);
    }
    
    // Supprimer un emprunt
    public void supprimerEmprunt(int id) {
        empruntRepository.deleteById(id);
    }
    
    // Obtenir un emprunt par ID
    public Emprunt getEmpruntById(int id) {
        return empruntRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Emprunt non trouvé avec l'ID: " + id));
    }
    
    // Obtenir tous les emprunts avec pagination
    public Page<Emprunt> getAllEmprunts(Pageable pageable) {
        return empruntRepository.findAll(pageable);
    }
    
    // Compter les emprunts actifs
    public long countEmpruntsActifs() {
        return empruntRepository.countByEtatEmprunt(EtatEmprunt.EN_COURS);
    }
    
    // Compter les emprunts en retard
    public long countEmpruntsEnRetard() {
        return empruntRepository.countByEtatEmprunt(EtatEmprunt.DELAI_EXPIRE);
    }
    
    // Obtenir les emprunts en retard avec pagination
    public Page<Emprunt> getEmpruntsEnRetard(Pageable pageable) {
        return empruntRepository.findByEtatEmprunt(EtatEmprunt.DELAI_EXPIRE, pageable);
    }
    
    // Rechercher des emprunts avec pagination
    public Page<Emprunt> rechercherEmprunts(String search, Pageable pageable) {
        return empruntRepository.findByLivreTitreContainingIgnoreCaseOrUtilisateurNomContainingIgnoreCase(
                search, search, pageable);
    }
    
    // Rechercher des emprunts par état avec pagination
    public Page<Emprunt> rechercherEmpruntsParEtat(EtatEmprunt etat, Pageable pageable) {
        return empruntRepository.findByEtatEmprunt(etat, pageable);
    }
    
    // Rechercher des emprunts par utilisateur avec pagination
    public Page<Emprunt> rechercherEmpruntsParUtilisateur(String utilisateurNom, Pageable pageable) {
        return empruntRepository.findByUtilisateurNomContainingIgnoreCase(utilisateurNom, pageable);
    }

    // Ici on va creer une option qui doit gerer faire en sorte que le bibliothecaire valide et puis un message de notification est envoye a l'utilisateur pour lui faire signe

    // Historique de validation
    public List<Emprunt> listetotaleDesEmprunts(){
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
     * @param id il s'agit de l'id de l'emprunt poue oermettre de trouver ce dernier
     * @return Emprunt
     */
    public Emprunt getEmprunt(int id) {
        Optional<Emprunt> empruntOptional = empruntRepository.findById(id);
        return empruntOptional.orElseThrow(()-> new EntityNotFoundException("Emprunt non trouve"));
    }
    //D'autres methodes sont a venir Par exemple pour les filtres beaucoup plus avances des filtres. Pour l'instant on travaille d'abord avec ceux ci.
    /*
      Ici on va creer des filtres de recherches plus avances par exemples :
      - La liste des Emprunts dont les dates de remises sont avant une date precise.
      - La liste des Emprunts dont les dates de debut sont avant une date precise.
      - La liste des Emprunts dont les dates de debut sont apres une date precise.
      - La liste des Emprunts dont les dates de debut sont entre deux dates precises.
      - la liste des emprunts dont les dates de remises  sont entre deux dates precises.
      - La liste des emprunts en fonction des livres et des dates.
      - La lite des emprunts en fonction des utilisateurs et des dates. Meme comme je ne vois pas trop la necessite ici mais bon...
      - Les services pour exporter l'historique des emprunts sous format PDF.
     */

    /**
     * Obtenir les derniers emprunts pour le dashboard
     */
    public List<Emprunt> getDerniersEmprunts(int limit) {
        Pageable pageable = PageRequest.of(0, limit, Sort.by("dateDebutEmprunt").descending());
        return empruntRepository.findAll(pageable).getContent();
    }

    /**
     * Obtenir les emprunts en retard pour le dashboard
     */
    public List<Emprunt> getEmpruntsEnRetard(int limit) {
        Pageable pageable = PageRequest.of(0, limit, Sort.by("dateFinEmprunt").ascending());
        return empruntRepository.findByEtatEmprunt(EtatEmprunt.EN_COURS, pageable)
                .getContent()
                .stream()
                .filter(emprunt -> emprunt.getDateFinEmprunt().isBefore(LocalDate.now()))
                .limit(limit)
                .collect(Collectors.toList());
    }

    /**
     * Compter les emprunts en retard pour un utilisateur spécifique
     */
    public int countEmpruntsEnRetardByUtilisateur(int utilisateurId) {
        return (int) empruntRepository.findByUtilisateurId(utilisateurId)
                .stream()
                .filter(emprunt -> emprunt.getEtatEmprunt() == EtatEmprunt.EN_COURS && 
                                  emprunt.getDateFinEmprunt().isBefore(LocalDate.now()))
                .count();
    }

    /**
     * Obtenir les emprunts actifs d'un utilisateur
     */
    public List<Emprunt> getEmpruntsActifsByUtilisateur(int utilisateurId) {
        return empruntRepository.findByUtilisateurId(utilisateurId)
                .stream()
                .filter(emprunt -> emprunt.getEtatEmprunt() == EtatEmprunt.EN_COURS)
                .collect(Collectors.toList());
    }

    /**
     * Obtenir les emprunts d'un utilisateur avec pagination
     */
    public Page<Emprunt> getEmpruntsByUtilisateur(int utilisateurId, Pageable pageable) {
        List<Emprunt> emprunts = empruntRepository.findByUtilisateurId(utilisateurId);
        
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), emprunts.size());
        
        return new org.springframework.data.domain.PageImpl<>(
            emprunts.subList(start, end), pageable, emprunts.size()
        );
    }

    /**
     * Compter le total des emprunts d'un utilisateur
     */
    public int countTotalEmpruntsByUtilisateur(int utilisateurId) {
        return empruntRepository.findByUtilisateurId(utilisateurId).size();
    }
}

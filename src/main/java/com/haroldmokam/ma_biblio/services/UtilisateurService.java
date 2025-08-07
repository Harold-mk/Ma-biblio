package com.haroldmokam.ma_biblio.services;

import com.haroldmokam.ma_biblio.Repositories.UtilisateurRepository;
import com.haroldmokam.ma_biblio.entites.RoleUtilisateur;
import com.haroldmokam.ma_biblio.entites.Statut;
import com.haroldmokam.ma_biblio.entites.Utilisateur;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UtilisateurService {
    private UtilisateurRepository utilisateurRepository;

    //Creer un utilisateur
    public void creerUtilisateur(String nom, String email, String motDePasse ,Statut statut, String matricule) {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setNom(nom);
        utilisateur.setMail(email);
        utilisateur.setPassword(motDePasse);
        utilisateur.setNombreEmpruntRestant(5);
        utilisateur.setStatut(statut);
        utilisateur.setMatricule( matricule);
        utilisateur.setRole(RoleUtilisateur.UTILISATEUR); // Par défaut, l'utilisateur est un utilisateur normal
    }

    public void modifierUtilisateur(Utilisateur utilisateur) {
        Utilisateur utilisateurdansLaBd = utilisateurRepository.findById(utilisateur.getId());
        if (utilisateurdansLaBd == null) {
            throw new EntityNotFoundException("Utilisateur n'existe pas");
        }
        utilisateurRepository.save(utilisateur);
    }
}

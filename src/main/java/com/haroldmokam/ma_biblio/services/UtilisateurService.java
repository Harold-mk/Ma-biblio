package com.haroldmokam.ma_biblio.services;

import com.haroldmokam.ma_biblio.Repositories.UtilisateurRepository;
import com.haroldmokam.ma_biblio.entites.RoleUtilisateur;
import com.haroldmokam.ma_biblio.entites.Utilisateur;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UtilisateurService {
    private UtilisateurRepository utilisateurRepository;

    //Creer un utilisateur
    public void creerUtilisateur(Utilisateur utilisateur) {
        utilisateur.setNombreEmpruntRestant(5);
        utilisateur.setRole(RoleUtilisateur.UTILISATEUR);// Par défaut, l'utilisateur est un utilisateur normal
        utilisateurRepository.save(utilisateur);
    }

    public void modifierUtilisateur(Utilisateur utilisateur) {
        Utilisateur utilisateurdansLaBd = utilisateurRepository.findById(utilisateur.getId()).
                orElseThrow(
                        ()-> new RuntimeException("l'utilisateur n'existe pas dans la base de donnes")
                );
        utilisateurRepository.save(utilisateur);
    }

    public void supprimerUtilisateur(int id) {
        utilisateurRepository.deleteById(id);
    }

    public List<Utilisateur> listeTotaleUtilisateurs() {
        return utilisateurRepository.findAll();
    }
    public Utilisateur findUtilisateurById(int id) {
        return utilisateurRepository.findById(id).orElseThrow(
                ()-> new EntityNotFoundException("l'utilisateur n'existe pas dans la base de donnes.")
        );
    }
    public Utilisateur findUtilisateurByEmail(String email) {
        return utilisateurRepository.findByMail(email).orElseThrow(
                ()-> new RuntimeException(" l'utilisateur n'existe pas dans la base de donnes.")
        );
    }
    public Utilisateur findUtilisateurByMatricule(String matricule) {
        return utilisateurRepository.findByMatricule(matricule).orElseThrow(
                ()-> new RuntimeException(" l'utilisateur n'existe pas dans la base de donnes.")
        );
    }
    public List<Utilisateur> findUtilisateurByNom(String nom) {
        List<Utilisateur> utilisateurs = utilisateurRepository.findByNomContainingIgnoreCase(nom);
        if(utilisateurs.isEmpty()){
            throw new RuntimeException("La liste est vide.");
        }
        return utilisateurs;
    }
}

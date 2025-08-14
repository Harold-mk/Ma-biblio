package com.haroldmokam.ma_biblio.components;

import com.haroldmokam.ma_biblio.entites.Emprunt;
import com.haroldmokam.ma_biblio.services.EmpruntService;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class EmpruntScheduler {
    private final EmpruntService empruntService;

    @PostConstruct
    public void init() {
        System.out.println("Verification et mis a jour de l'état des emprunts de chaque Utilisateur au démarrage de l'application");
        checkEmprunt();
    }
    @Scheduled( cron = "0 0 */17 * * *")
    public void cron() {
        System.out.println("Verification et mis a jour de l'état des emprunts de chaque Utilisateur a 17h");
        checkEmprunt();
    }
    private void checkEmprunt() {
        List<Emprunt> emprunts = empruntService.listetotaleDesEmprunts();
        for (Emprunt emprunt: emprunts) {
            empruntService.expirationEmprunt(emprunt);
        }
    }
}

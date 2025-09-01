# 📚 Biblio-Tech - Système de Gestion de Bibliothèque d'Entreprise

## 🎯 Vue d'ensemble

**Biblio-Tech** est un système de gestion de bibliothèque moderne et professionnel, spécialement conçu pour les entreprises spécialisées dans les impôts et la fiscalité. L'application offre une interface intuitive et des fonctionnalités complètes pour la gestion des livres, des utilisateurs, des emprunts et des catégories.

## ✨ Fonctionnalités Principales

### 🔐 Authentification et Sécurité
- **Système de connexion sécurisé** avec gestion des rôles
- **Deux niveaux d'accès** : Administrateur et Utilisateur
- **Gestion des sessions** et protection CSRF
- **Validation des formulaires** côté client et serveur

### 📖 Gestion des Livres
- **CRUD complet** des livres (Créer, Lire, Mettre à jour, Supprimer)
- **Gestion des exemplaires** avec suivi des disponibilités
- **Recherche avancée** par titre, auteur et catégorie
- **Upload d'images** pour les couvertures de livres
- **Catégorisation** des ouvrages par domaine

### 👥 Gestion des Utilisateurs
- **Profils utilisateurs** avec statuts (Professeur/Étudiant)
- **Gestion des rôles** (Administrateur/Utilisateur)
- **Suivi des emprunts** actifs et historiques
- **Limitation des emprunts** (maximum 5 simultanés)

### 📚 Gestion des Emprunts
- **Création d'emprunts** avec validation automatique
- **Prolongation** des dates d'échéance
- **Suivi des retards** avec notifications automatiques
- **Historique complet** des emprunts
- **Statuts multiples** : En cours, Délai expiré, Remis

### 🏷️ Gestion des Catégories
- **Organisation hiérarchique** des livres
- **CRUD des catégories** avec validation
- **Recherche par catégorie** pour les utilisateurs

### 🔔 Système de Notifications
- **Notifications en temps réel** pour les retards
- **Historique des notifications** avec statut lu/non-lu
- **Alertes automatiques** pour les échéances

### 📊 Tableaux de Bord
- **KPIs en temps réel** (total livres, emprunts actifs, retards)
- **Graphiques interactifs** avec Chart.js
- **Statistiques détaillées** par période
- **Actions rapides** vers les fonctionnalités principales

## 🏗️ Architecture Technique

### Backend
- **Spring Boot 3.5.3** avec Java 17
- **Spring Data JPA** pour la persistance
- **MariaDB** comme base de données
- **Architecture en couches** : Contrôleurs, Services, Repositories
- **Validation des données** avec Bean Validation
- **Gestion des erreurs** centralisée

### Frontend
- **Thymeleaf** pour le rendu côté serveur
- **Bootstrap 5.3** pour l'interface utilisateur
- **JavaScript ES6+** avec modules
- **Chart.js** pour les graphiques
- **Font Awesome** pour les icônes
- **Responsive design** pour tous les appareils

### Base de Données
- **MariaDB** avec schéma optimisé
- **Relations JPA** bien définies
- **Indexation** pour les performances
- **Contraintes** d'intégrité

## 🚀 Installation et Configuration

### Prérequis
- **Java 17** ou supérieur
- **Maven 3.6+**
- **MariaDB 10.5+**
- **Node.js 16+** (pour le développement)

### Installation

1. **Cloner le repository**
   ```bash
   git clone https://github.com/votre-username/biblio-tech.git
   cd biblio-tech
   ```

2. **Configurer la base de données**
   ```sql
   CREATE DATABASE mabiblio CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   CREATE USER 'biblio_user'@'localhost' IDENTIFIED BY 'votre_mot_de_passe';
   GRANT ALL PRIVILEGES ON mabiblio.* TO 'biblio_user'@'localhost';
   FLUSH PRIVILEGES;
   ```

3. **Configurer l'application**
   ```properties
   # src/main/resources/application.properties
   spring.datasource.url=jdbc:mariadb://localhost:3306/mabiblio
   spring.datasource.username=biblio_user
   spring.datasource.password=votre_mot_de_passe
   ```

4. **Lancer l'application**
   ```bash
   mvn spring-boot:run
   ```

5. **Accéder à l'application**
   - URL : http://localhost:8080
   - Identifiants par défaut : admin/admin

## 📁 Structure du Projet

```
biblio-tech/
├── src/
│   ├── main/
│   │   ├── java/com/haroldmokam/ma_biblio/
│   │   │   ├── components/          # Composants système
│   │   │   ├── controllers/         # Contrôleurs REST
│   │   │   ├── entites/            # Entités JPA
│   │   │   ├── Repositories/       # Interfaces de données
│   │   │   ├── services/           # Logique métier
│   │   │   └── MaBiblioApplication.java
│   │   └── resources/
│   │       ├── static/             # Ressources statiques
│   │       │   ├── css/            # Styles CSS
│   │       │   ├── js/             # JavaScript
│   │       │   └── images/         # Images
│   │       └── templates/          # Templates Thymeleaf
│   │           ├── admin/          # Pages administrateur
│   │           ├── auth/           # Authentification
│   │           ├── error/          # Pages d'erreur
│   │           ├── fragments/      # Fragments réutilisables
│   │           └── layouts/        # Layouts principaux
│   └── test/                       # Tests unitaires
├── pom.xml                         # Configuration Maven
└── README.md                       # Documentation
```

## 🎨 Interface Utilisateur

### Design System
- **Thème corporate fiscal** avec palette de couleurs professionnelles
- **Typographie Inter** pour une excellente lisibilité
- **Composants Bootstrap 5** personnalisés
- **Responsive design** pour tous les écrans

### Couleurs Principales
```css
--primary: #1E40AF    /* Bleu corporate principal */
--secondary: #3B82F6  /* Bleu secondaire */
--success: #059669     /* Vert fiscal/validation */
--warning: #F59E0B     /* Orange alertes/retards */
--danger: #DC2626      /* Rouge urgences */
```

### Composants UI
- **Cards** avec ombres et animations
- **Tables** responsives avec tri et pagination
- **Formulaires** avec validation en temps réel
- **Modales** pour les confirmations
- **Notifications** toast et alertes

## 🔧 Configuration Avancée

### Variables d'Environnement
```bash
# Configuration de la base de données
DB_HOST=localhost
DB_PORT=3306
DB_NAME=mabiblio
DB_USER=biblio_user
DB_PASSWORD=votre_mot_de_passe

# Configuration de l'application
APP_PORT=8080
APP_CONTEXT=/
APP_DEBUG=false
```

### Profils Spring
```properties
# Profil de développement
spring.profiles.active=dev

# Profil de production
spring.profiles.active=prod
```

## 📊 API REST

### Endpoints Principaux

#### Livres
- `GET /Livre/ListeTotale` - Liste complète des livres
- `POST /Livre/ajouter` - Ajouter un livre
- `PUT /Livre/modifier/{id}` - Modifier un livre
- `DELETE /Livre/supprimer/{id}` - Supprimer un livre
- `GET /Livre/Auteur/{auteur}` - Recherche par auteur
- `GET /Livre/Titre/{titre}` - Recherche par titre
- `GET /Livre/categorie/{id}` - Recherche par catégorie

#### Utilisateurs
- `GET /utilisateurs/listetotale` - Liste des utilisateurs
- `POST /utilisateurs/ajouter` - Ajouter un utilisateur
- `PUT /utilisateurs/modifier/{id}` - Modifier un utilisateur
- `DELETE /utilisateurs/supprimer/{id}` - Supprimer un utilisateur
- `GET /utilisateurs/rechercher/{id}` - Recherche par ID
- `GET /utilisateurs/rechercher/matricule` - Recherche par matricule
- `GET /utilisateurs/rechercher/mail` - Recherche par email

#### Emprunts
- `GET /emprunts/listeTotale` - Liste des emprunts
- `POST /emprunts/ajouter` - Créer un emprunt
- `PUT /emprunts/prolonger/{id}` - Prolonger un emprunt
- `PUT /emprunts/remise/{id}` - Remettre un emprunt
- `GET /emprunts/DelaiExpire` - Emprunts en retard
- `GET /emprunts/enCours` - Emprunts actifs
- `GET /emprunts/remis` - Emprunts remis

#### Catégories
- `GET /categorie/liste` - Liste des catégories
- `POST /categorie/ajouter` - Ajouter une catégorie
- `PUT /categorie/modifier/{id}` - Modifier une catégorie
- `DELETE /categorie/supprimer/{id}` - Supprimer une catégorie

## 🧪 Tests

### Tests Unitaires
```bash
# Lancer tous les tests
mvn test

# Lancer les tests avec couverture
mvn test jacoco:report

# Lancer les tests d'intégration
mvn verify
```

### Tests d'API
```bash
# Tests avec Postman
# Collection disponible dans /docs/postman/

# Tests avec curl
curl -X GET http://localhost:8080/Livre/ListeTotale
```

## 🚀 Déploiement

### Environnement de Développement
```bash
mvn spring-boot:run -Dspring.profiles.active=dev
```

### Environnement de Production
```bash
# Build du JAR
mvn clean package -DskipTests

# Lancement en production
java -jar -Dspring.profiles.active=prod target/ma_biblio-0.0.1-SNAPSHOT.jar
```

### Docker
```dockerfile
FROM openjdk:17-jre-slim
COPY target/ma_biblio-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

## 📈 Monitoring et Maintenance

### Logs
- **Logback** pour la gestion des logs
- **Niveaux configurables** (DEBUG, INFO, WARN, ERROR)
- **Rotation automatique** des fichiers de logs

### Métriques
- **Actuator Spring Boot** pour les métriques
- **Health checks** pour la base de données
- **Monitoring des performances** JVM

### Sauvegarde
- **Scripts de sauvegarde** automatiques
- **Export des données** en format JSON/CSV
- **Restauration** en cas de problème

## 🤝 Contribution

### Guide de Contribution
1. **Fork** le projet
2. **Créer** une branche pour votre fonctionnalité
3. **Commit** vos changements
4. **Push** vers la branche
5. **Créer** une Pull Request

### Standards de Code
- **Java** : Google Java Style Guide
- **JavaScript** : ESLint + Prettier
- **CSS** : Stylelint
- **Commits** : Conventional Commits

## 📄 Licence

Ce projet est sous licence **MIT**. Voir le fichier `LICENSE` pour plus de détails.

## 📞 Support

### Contact
- **Développeur** : Harold Mokam
- **Email** : contact@biblio-tech.com
- **Documentation** : https://docs.biblio-tech.com

### Ressources
- **Wiki** : https://github.com/votre-username/biblio-tech/wiki
- **Issues** : https://github.com/votre-username/biblio-tech/issues
- **Discussions** : https://github.com/votre-username/biblio-tech/discussions

## 🔮 Roadmap

### Version 1.1
- [ ] **Système de réservation** des livres
- [ ] **Notifications push** en temps réel
- [ ] **API mobile** pour applications mobiles
- [ ] **Système de tags** avancé

### Version 1.2
- [ ] **Gestion des amendes** pour retards
- [ ] **Rapports avancés** avec export PDF
- [ ] **Intégration LDAP** pour l'authentification
- [ ] **Audit trail** complet des actions

### Version 2.0
- [ ] **Interface multi-tenant** pour plusieurs bibliothèques
- [ ] **Système de recommandations** IA
- [ ] **Gestion des ressources numériques** (e-books, vidéos)
- [ ] **API GraphQL** pour les requêtes complexes

---

**Biblio-Tech** - Simplifiez la gestion de votre bibliothèque d'entreprise ! 📚✨

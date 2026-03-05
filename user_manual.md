# Manuel d'Utilisation - MyCashPoint

Bienvenue dans le manuel d'utilisation de l'application **MyCashPoint**. Ce document détaille toutes les fonctionnalités de l'application pour les administrateurs et les agents.

---

## 1. Introduction

MyCashPoint est une solution de gestion de trésorerie et de transactions pour les points de services financiers (Mobile Money, Banking, etc.). Elle permet de gérer plusieurs agences, de suivre les soldes en temps réel et d'effectuer des transactions sécurisées.

---

## 2. Connectivité et Pré-requis

L'application **MyCashPoint** a été conçue pour s'adapter à votre environnement de travail. La gestion de la connexion internet diffère selon votre rôle :

### Pour l'Administrateur (Le Boss)
*   **Internet OBLIGATOIRE :** L'accès à l'interface d'administration nécessite une connexion internet **permanente et stable**. Toutes les actions (création d'agence, rapports, suivi des soldes) se font en temps réel sur le serveur distant.

### Pour l'Agent (Sur le terrain)
*   **Première Connexion (Internet Requis) :** Lors de la toute première utilisation, l'agent **doit avoir internet** pour s'authentifier et télécharger les configurations initiales.
*   **Mode Hors-Ligne (Offline) :** Une fois cette étape passée, l'agent peut travailler **sans connexion internet** toute la journée. L'application est autonome et enregistre toutes les transactions localement sur le téléphone.
*   **Synchronisation (Envoi des données) :** L'internet est nécessaire uniquement à des moments clés (ex: fin de journée) pour **envoyer les données vers le serveur**. L'agent peut alors activer ses données mobiles ou se connecter au Wi-Fi pour synchroniser son travail.

---

## 3. Authentification et Démarrage

### Connexion
À l'ouverture de l'application, l'écran de connexion s'affiche.
1.  **Nom d'utilisateur :** Entrez votre identifiant.
2.  **Mot de passe :** Entrez votre mot de passe.
3.  Cliquez sur **"Se connecter"**.

> **Note :** Selon votre rôle (Administrateur ou Agent), vous serez redirigé vers l'interface correspondante.

### Inscription (Création d'Agent)
L'inscription de nouveaux utilisateurs se fait généralement par un administrateur depuis le tableau de bord.
*   Un administrateur peut créer des comptes pour des agents ou d'autres administrateurs.

---

## 4. Interface Administrateur (Back-Office)

Si vous êtes connecté en tant qu'administrateur, vous accédez au **Tableau de Bord (Dashboard)**.

### A. Tableau de Bord (Dashboard)
Le tableau de bord est votre centre de contrôle. Il affiche :
*   **Sélecteur d'Agence :** En haut, permet de filtrer les données par agence.
*   **Filtres :** Sélectionnez un opérateur (Airtel, Orange, M-Pesa, etc.) et une devise (USD/CDF) pour voir les soldes spécifiques.
*   **Graphique (Pie Chart) :** Répartition des transactions par opérateur.
*   **Carte de Solde :** Affiche le solde actuel et le seuil d'alerte configuré pour l'opérateur sélectionné.

### B. Actions Rapides
En bas du tableau de bord, une grille d'actions rapides vous permet d'accéder aux fonctionnalités principales :

1.  **Créer une Agence :** Formulaire pour ajouter une nouvelle agence au réseau.
2.  **Créer un Agent :** 
    *   Remplissez les informations (Nom, Mot de passe, Rôle).
    *   Assignez l'agent à une agence existante.
3.  **Initialiser le Solde :** Définir le solde de départ pour un opérateur dans une agence spécifique.
4.  **Agence la plus performante :** Affiche des statistiques sur les performances des agences.
5.  **Solde en rupture :** Liste les comptes dont le solde est inférieur au seuil d'alerte.
6.  **Opérations Caisse :** (Voir section détaillée ci-dessous).
7.  **Info de l'entreprise :** Gérer les établissements.
8.  **Modifier Profil :** Mettre à jour vos identifiants.
9.  **Se déconnecter.**

### C. Opérations de Caisse (Approvisionnement / Décaissement)
Accessible via le bouton "Opérations Caisse". Cette interface permet de gérer les mouvements de fonds internes.

1.  **Contexte :** Sélectionnez l'Agence, l'Opérateur et le Type de Solde à impacter.
2.  **Configuration :**
    *   **Nature :** _Approvisionnement_ (Ajout de fonds) ou _Décaissement_ (Retrait de fonds).
    *   **Devise :** USD ou CDF.
3.  **Détails :** Saisissez le montant et un motif ou justificatif.
4.  Validez l'opération.

---

## 5. Interface Agent (Front-Office)

Si vous êtes connecté en tant qu'agent, vous accédez à l'écran **Opérateur**.

### A. Écran Principal (Choix de l'Opérateur)
Vous verrez une grille avec les logos des différents opérateurs (Orange, Airtel, Canal+, etc.).
*   Cliquez sur le logo de l'opérateur avec lequel vous souhaitez effectuer une transaction.
*   **Menu (3 points) :** En haut à droite, permet de :
    *   *Premier sync :* Télécharger les données initiales.
    *   *Actualiser Ets Info :* Mettre à jour les infos de l'établissement.
    *   *Modifier Profil :* Changer votre mot de passe.
    *   *Se déconnecter.*

### B. Effectuer une Transaction
Une fois l'opérateur choisi, vous arrivez sur l'écran de transaction.

1.  **Devise :** Sélectionnez USD ou CDF en haut.
2.  **Type de Transaction :** Choisissez DÉPÔT ou RETRAIT.
3.  **Formulaire :**
    *   **Montant :** La somme à transiger.
    *   **Nom Client & Téléphone :** Informations du client en face.
    *   **Bénéficiaire (Pour Dépôt) :** Nom et téléphone du destinataire.
    *   **Commission :** Pourcentage ou montant de commission (si applicable).
    *   **Note :** Commentaire optionnel.
4.  **Validation :**
    *   Cliquez sur **"Enregistrer"**.
    *   Une boîte de dialogue de confirmation s'affiche.
    *   Une fois confirmé, un reçu peut être imprimé (si un appareil d'impression est connecté) et un message de succès s'affiche.

---

## 6. Gestion de Profil (Commun)

Accessible depuis le menu pour les Agents et via les actions rapides pour les Admins.
*   Permet de modifier son **Nom d'utilisateur** et son **Mot de passe**.
*   **Important :** Après une modification réussie, vous serez automatiquement déconnecté et devrez vous reconnecter avec vos nouveaux identifiants.

---

## 7. Support

En cas de problème technique (bug, erreur de synchronisation), veuillez contacter l'administrateur système ou le support technique de MyCashPoint.

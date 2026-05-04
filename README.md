# Microservices Stack Orchestration - E-Banking System

Ce projet est une application de gestion bancaire basée sur une architecture microservices robuste utilisant **Spring Boot**, **Spring Cloud** et **Angular**.

## 🚀 Architecture du Système

L'écosystème est composé des modules suivants :

*   **Discovery Service (Eureka)** : Service de découverte permettant l'enregistrement et la localisation des microservices.
*   **Config Server** : Gestion centralisée de la configuration via un dépôt Git.
*   **Gateway Service** : Point d'entrée unique de l'application (Routing dynamique).
*   **Customer Service** : Gestion des clients (Données métier).
*   **Account Service** : Gestion des comptes bancaires et transactions.
*   **Angular Front-End** : Interface utilisateur moderne pour interagir avec le système.

## 🛠 Tech Stack

*   **Backend** : Java 17, Spring Boot 3, Spring Cloud (Gateway, Config, Eureka).
*   **Frontend** : Angular, Bootstrap.
*   **DevOps** : Docker, Docker Compose.
*   **Database** : H2 (In-Memory) pour le développement.

## 🏁 Démarrage Rapide

### Prérequis
*   Docker & Docker Compose
*   JDK 17+
*   Maven

### Lancement avec Docker (Recommandé)
```bash
docker-compose up -d --build
```

### Accès aux Services
*   **Eureka Dashboard** : [http://localhost:8761](http://localhost:8761)
*   **Config Server** : [http://localhost:9999](http://localhost:9999)
*   **Gateway Service** : [http://localhost:8888](http://localhost:8888)
*   **Angular Frontend** : [http://localhost:82](http://localhost:82)

## 📖 Documentation Détaillée
*   [Architecture & Design](./docs/ARCHITECTURE.md)
*   [Guide de Déploiement](./docs/DEPLOYMENT_GUIDE.md)
*   [API & Endpoints](./docs/API_GUIDE.md)

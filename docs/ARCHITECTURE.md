# Architecture & Design

Ce document détaille les choix architecturaux et les patterns mis en œuvre dans le projet.

## 1. Patterns Microservices Utilisés

### Service Discovery (Netflix Eureka)
Chaque instance de microservice s'enregistre auprès du serveur Eureka au démarrage. Cela permet :
*   Le **Load Balancing** côté client.
*   L'abstraction des adresses IP et des ports.
*   La haute disponibilité.

![Dashboard Eureka](./images/discoveryeureka.png)

### Configuration Centralisée (Spring Cloud Config)
Toutes les configurations (fichiers `.properties` ou `.yml`) sont stockées dans un dépôt Git externe ou local.
*   **Avantage** : Modification des paramètres sans recompiler ou redémarrer les services (via `/actuator/refresh`).
*   **Sécurité** : Séparation stricte entre le code et la configuration.

### API Gateway (Spring Cloud Gateway)
Toutes les requêtes externes passent par ce composant.
*   **Routage Dynamique** : Utilise le Discovery Client pour router les requêtes vers les services disponibles (ex: `/CUSTOMER-SERVICE/**`).
*   **Sécurité Centralisée** : Point idéal pour implémenter l'authentification (JWT) et le Rate Limiting.

## 2. Diagramme de Flux (Conceptuel)

```mermaid
graph TD
    Client[Angular Frontend] --> Gateway[API Gateway :8888]
    Gateway --> Discovery[Eureka Server :8761]
    Gateway --> CustomerService[Customer Service :8081]
    Gateway --> AccountService[Account Service :8082]
    
    CustomerService --> Config[Config Server :9999]
    AccountService --> Config[Config Server :9999]
    CustomerService --> Discovery
    AccountService --> Discovery
```

## 3. Communication Inter-Service
Le système utilise principalement des communications synchrones via **REST** (RestTemplate ou OpenFeign).
*   `Account Service` communique avec `Customer Service` pour valider l'existence d'un client lors de la création d'un compte.

## 4. Base de Données
Chaque service possède sa propre base de données (Pattern **Database per Service**). Actuellement, des bases H2 sont utilisées pour faciliter le développement, mais elles peuvent être remplacées par PostgreSQL ou MySQL via la configuration centralisée.

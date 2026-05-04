# Guide de Test sous Docker Compose

Ce guide détaille les étapes pour valider le bon fonctionnement de la stack microservices une fois déployée avec Docker.

## 1. Préparation et Lancement

Assurez-vous qu'aucun service local (hors Docker) n'utilise les ports requis (8761, 9999, 8888, 8081, 8082, 82).

```bash
# Compiler les JARs (si nécessaire)
mvn clean package -DskipTests
```
![Compilation Maven](./images/mvncleanpackage-DskipTests.png)

```bash
# Lancer la stack
docker-compose up -d --build
```
![Lancement Docker Compose](./images/dockercomposeup-d--build.png)

## 2. Vérification de l'Infrastructure (Orchestration)

Avant de tester les APIs métiers, il faut valider que les services d'infrastructure sont opérationnels.

### A. Discovery Service (Eureka)
Ouvrez votre navigateur sur : [http://localhost:8761](http://localhost:8761)
*   **Critère de succès** : Vous devez voir tous les services (`CUSTOMER-SERVICE`, `ACCOUNT-SERVICE`, `GATEWAY-SERVICE`, `CONFIG-SERVICE`) apparaître dans la liste "Instances currently registered with Eureka".

![Dashboard Eureka](./images/discoveryeureka.png)

### B. Config Service
Vérifiez que le serveur de configuration distribue bien les propriétés :
```bash
curl http://localhost:9999/customer-service/default
```
*   **Critère de succès** : Réponse JSON contenant les propriétés du dépôt Git.

## 3. Tests de Connectivité et Santé (Health Checks)

Chaque conteneur dispose d'un HealthCheck configuré. Vous pouvez vérifier l'état global via Docker :

```bash
docker ps
```
*   L'état doit être `(healthy)` pour les services critiques.

Vous pouvez aussi interroger les Actuators :
*   **Customer Service** : `curl http://localhost:8081/actuator/health`
*   **Gateway Service** : `curl http://localhost:8888/actuator/health`

![Actuator Health](./images/localhost8081actuatorhealth.png)

## 4. Tests Fonctionnels via la Gateway

La Gateway (port 8888) est le point d'entrée unique. Il est crucial de tester via ce port pour valider le routage dynamique.

### A. Tester le Customer Service
```bash
# Récupérer la liste des clients
curl -v http://localhost:8888/CUSTOMER-SERVICE/customers
```
![API Customers via Gateway](./images/localhost8888CUSTOMER-SERVICEcustomers.png)

### B. Tester l'Account Service
```bash
# Récupérer la liste des comptes
curl -v http://localhost:8888/ACCOUNT-SERVICE/accounts
```
![API Accounts via Gateway](./images/localhost8888ACCOUNT-SERVICEaccounts.png)

### C. Tester l'interaction entre services
L' `account-service` appelle le `customer-service` via OpenFeign. Testez un endpoint de détail de compte :
```bash
curl -v http://localhost:8888/ACCOUNT-SERVICE/accounts/{id}
```
*   **Vérification** : Le JSON de réponse doit contenir un objet `customer` imbriqué (non null).

## 5. Test de l'Interface Angular

Ouvrez [http://localhost:82](http://localhost:82) dans votre navigateur.
*   Naviguez dans les sections "Customers" et "Accounts".
*   Ouvrez la console de développement du navigateur (F12) pour vérifier qu'aucune erreur 404 ou 500 n'apparaît lors des appels vers `localhost:8888`.

![Interface Angular](./images/localhost82customers.png)

## 6. Scénarios de Test DevOps

### Test de la Résilience (Failover)
1.  Arrêtez une instance du Customer Service : `docker-compose stop enset-customer-service`
2.  Tentez d'accéder aux comptes : `curl http://localhost:8888/ACCOUNT-SERVICE/accounts`
3.  **Résultat attendu** : Une erreur gracieuse ou un timeout, prouvant que la Gateway gère la perte de service.

### Test du Rafraîchissement de Config
1.  Modifiez une propriété dans votre dépôt de configuration.
2.  Envoyez un refresh au service concerné :
```bash
curl -X POST http://localhost:8081/actuator/refresh
```
3.  Vérifiez que la modification est prise en compte sans redémarrer le conteneur.

## 7. Analyse des Logs en cas d'échec

Si un service ne démarre pas ou n'est pas "Healthy" :
```bash
# Voir les logs en temps réel
docker-compose logs -f [nom_du_service]

# Exemple pour la Gateway
docker-compose logs -f enset-gateway-service
```

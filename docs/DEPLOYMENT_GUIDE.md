# Guide de Déploiement (DevOps)

Ce document explique comment compiler, emballer et déployer l'ensemble de la stack microservices.

## 1. Build de l'Application (Maven)

Avant de lancer Docker, il est recommandé de compiler les JARs localement pour s'assurer que le code est valide.

```bash
# À la racine du projet
mvn clean package -DskipTests
```
![Compilation Maven](./images/mvncleanpackage-DskipTests.png)

## 2. Déploiement avec Docker Compose

Le fichier `docker-compose.yml` automatise la création des images et le lancement des conteneurs.

### Commandes Utiles
*   **Démarrer tout** : `docker-compose up -d --build`
![Lancement Docker Compose](./images/dockercomposeup-d--build.png)
*   **Arrêter tout** : `docker-compose down`
*   **Voir les logs** : `docker-compose logs -f [service_name]`
*   **Redémarrer un service spécifique** : `docker-compose up -d --no-deps --build [service_name]`

### Ordre de Démarrage (Géré par `depends_on`)
1.  `enset-ebank-discovery-service` (Eureka)
2.  `enset-ebank-config-service` (Config Server)
3.  Microservices métier (`customer-service`, `account-service`)
4.  `enset-gateway-service`
5.  `front-send-angular`

## 3. Monitoring en Production (Suggestions)

Bien que ce projet utilise Actuator pour le monitoring basique, pour un environnement de production, il est recommandé d'ajouter :
*   **Prometheus & Grafana** : Pour la visualisation des métriques.
*   **Zipkin / Sleuth** : Pour le traçage distribué des requêtes à travers les microservices.
*   **ELK Stack (Elasticsearch, Logstash, Kibana)** : Pour la centralisation des logs.

## 4. Troubleshooting

*   **Problème d'enregistrement Eureka** : Vérifiez que les services peuvent pinger `enset-ebank-discovery-service` à l'intérieur du réseau Docker.
*   **Config Server inaccessible** : Assurez-vous que le dépôt Git est accessible ou que le chemin vers `config-repo` est correct si vous utilisez le profil local.
*   **CORS Issues** : Si le frontend Angular ne parvient pas à appeler la Gateway, vérifiez la configuration CORS dans `GatewayServiceApplication` ou dans les propriétés.

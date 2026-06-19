# Masterclass

_How to launch the masterclass application :D_

## Masterclass - Backend

### 1. Prérequis
- Java 21
- Maven 3.9.12

### 2. Installation
- mvn clean install
- docker-compose up
- mvn spring-boot:run

## Masterclass - Frontend

```sh
npm install
```

### Compile and Hot-Reload for Development

```sh
npm run dev
```

### Type-Check, Compile and Minify for Production

```sh
npm run build
```


## About the app

C'est une application de planning collaborative permettant à tout le monde de pouvoir y insérer les devoirs de la semaine, les différents évènements (qu'ils soient sportifs ou culturels).

Une fois les devoirs et évènements insérés, l'application permet de générer un planning de la semaine pour chaque utilisateur.

Chaque utilisateur aura un affichage personnalisé en fonction de leur avancé sur les devoirs (faits ou non faits), des notes personnelles qu'ils ont mis sur les différents évènements.

Dans le cas où un utilisateur se connecte sur téléphone, tous les affichages sont responsifs :D

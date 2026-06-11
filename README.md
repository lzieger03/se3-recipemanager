# Recipe Management – SE3 Projekt

Projekt für das Modul **Software Engineering 3** an der DHBW.

Eine kleine Web-App zum Verwalten von Rezepten und Zutaten. Man kann Rezepte anlegen, Zutaten mit Lagerbestand pflegen und beim Kochen werden die benötigten Mengen automatisch abgezogen.

---

## Starten

```bash
mvn spring-boot:run
```

Danach läuft die App unter `http://localhost:8080`.

> **Hinweis:** Beim ersten Start werden automatisch Testdaten geladen (3 Rezepte, 12 Zutaten).  
> Falls die App nicht startet und ein H2-Fehler kommt, einmal `rm -rf data/` ausführen und neu starten.

---

## Technologien

- Java 21
- Spring Boot 3.4.5
- Spring Data JPA + Hibernate
- H2 Datenbank (file-based, liegt in `data/`)
- Thymeleaf (Server-Side Rendering)
- Bootstrap 5

---

## Features

- Zutaten verwalten (anlegen, bearbeiten, löschen)
- Rezepte anlegen mit Zutatenliste und Mengenangaben
- Kochmodus: zeigt welche Zutaten vorhanden sind und welche fehlen
- Beim Kochen werden Zutaten automatisch aus dem Bestand abgezogen
- Testdaten werden beim ersten Start automatisch geladen

---

## Datenbankstruktur

Drei Tabellen:
- `ingredients` – Zutaten mit Lagerbestand
- `recipes` – Rezepte mit Beschreibung und Anleitung
- `recipe_ingredients` – Verknüpfungstabelle (welche Zutat in welcher Menge)

H2-Konsole erreichbar unter `http://localhost:8080/h2-console`  
JDBC-URL: `jdbc:h2:file:./data/recipedb` | User: `sa` | Passwort: empty

---

## Projektstruktur

```
src/main/java/.../
├── controller/     # Spring MVC Controller
├── model/          # JPA Entities
├── repository/     # Spring Data Repositories
├── service/        # Business Logic
└── DataLoader.java # Testdaten beim Start

src/main/resources/
├── templates/      # Thymeleaf HTML Templates
└── application.properties
```

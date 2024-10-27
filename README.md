# IPWA02-01 – Programmierung von industriellen Informationssystemen mit Java EE

Dieses Repository enthält meine Lösung zur Aufgabenstellung im Kurs **IPWA02-01**.

## Projektübersicht

**Like Hero To Zero** ist eine Java-EE-Webanwendung zur Anzeige und Verwaltung von öffentlich zugänglichen **CO₂-Emissionsdaten**. Ziel des Projekts ist die Bereitstellung einer intuitiven Plattform, die Transparenz bei CO₂-Emissionen fördert und auf Nachhaltigkeit abzielt.

### Nutzerrollen

- **Allgemeine Nutzer**: Können ohne Anmeldung auf CO₂-Daten zugreifen.
- **Wissenschaftler**: Können sich anmelden, um Daten hinzuzufügen oder zu aktualisieren.
- **Publisher**: Verfügen über die Berechtigung, Änderungen zu validieren und die Daten zur Veröffentlichung freizugeben.

## Technologie-Stack

- **Java EE**: Basis für die Backend-Entwicklung.
- **JSF (JavaServer Faces)**: Frontend-Technologie zur Darstellung dynamischer Inhalte.
- **Hibernate ORM**: Für die nahtlose Integration der Datenbankstrukturen.
- **MariaDB**: Relationales Datenbankmanagementsystem zur Speicherung der Emissionsdaten.
- **Apache Tomcat**: Server zur Bereitstellung der Anwendung.

## Projektstruktur

Die Anwendung folgt dem **Model-View-Controller (MVC)**-Muster zur Trennung von Logik und Darstellung:

- **Controller**: Verwaltung der Benutzerinteraktionen und Routing (z. B. `LoginController`, `EmissionDataController`).
- **Service Layer**: Geschäftslogik zur Bearbeitung und Verarbeitung von Daten.
- **Data Access Layer (DAO)**: Abstraktion für den Datenzugriff.
- **Entity Layer**: Datenmodellierung und Abbildung der Datenbanktabellen.

## Installation und Konfiguration

1. **Repository klonen**:
   ```bash
   git clone https://github.com/ceji1/IPWA02_01_LikeHeroToZero.git
   ```

2. **Datenbank einrichten**:
   - Erstelle eine MariaDB-Datenbank und importiere die CO₂-Daten.
   - Aktualisiere die Konfigurationsdatei (`persistence.xml`) mit den Datenbankzugangsdaten.

3. **Anwendung starten**:
   - Die Anwendung kann über einen Java-EE-Server (z. B. Tomcat) bereitgestellt werden.
   - Aufruf der Anwendung über `http://localhost:8080/LikeHeroToZero`.

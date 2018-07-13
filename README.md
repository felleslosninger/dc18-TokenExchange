# dc18-TokenExchange

Prosjekt fra DifiCamp 2018 for å utvikle metode for autorisasjon basert på brukertilhørigheter.

## Motivasjon

Oppgaven er utformet av direktoratet for forvaltning og IKT, Leikanger i forbindelse med DifiCamp 18.
Målet for prosjektet var å imøtekomme spesifikasjonen fra oppgaven og finne områder å ta i bruk teknologien. 

### Hovedoppgave: Nett-tjeneste fra etat A har behov for å vite hvilken etat B brukeren kommer fra
1.  Teneste A er en OIDC-klient
2.  Bruker gjennomfører autentisering gjennom ID-porten og mottar standard id_token
3.  A bytter aksess-tokenet inn i en tokenutvekslingstjeneste (STS) og får tilbake et nytt token som har blitt beriket fra autorativ kilde med org.no tilhøyrande arbeidsgiver
4.  Tjeneste A sjekker at beriket token inneholder org.no til lokalt godkjent virksomhet
 
Det skal lages en enkel demo-tjeneste A, men hovedjobben er å laga ein frittståande STS-applikasjon som realiserer steg 3. Applikasjonen skal snakke Ouauth2 Token Exchange spesifikasjonen ut mot tjeneste A.  På "baksida" skal den sjekke mot ulike autorative kilder (kandidatar er Altinn Autorisasjon, AA-registeret, DFØ-ID, Azure AD).  
 
### Oppgaven kan utvides med tilleggsfunksjonalitet

Diverse use cases og notater for prosjektet.
 
* Se på hvordan man kan realisere identisk etats-identifisering på tvers av ulike autorative kilder. 
* Se på hvordan tjenestene kan bestemme hvilke autorative kilder som skal brukes
* Se på andre kontekster som fullmakter eller vergemål  
 
* Pilot på noen Difi-tenester: t.d arbeidsgiverportalen, samarbeidsportalen (DFØ)
 
* Føderasjon mellom ID-porten og STS (la ID-porten publisere sine integrasjoner over OIDC Federations spec slik at STS kan gjere tilgangskontroll på tokenexchange-endepunktet)
 
Hvilke eksterne kan vi samarbeide med?
* Ta kontakt med DFØ. Enten integrasjon mot DFØ-ID, eller få dump av Difi-ansatte i DFØ og innstallere lokalt. 
* Ta kontakt med Altinn. Tilgang til Altinn Autorisasjon
* NAV: AA-registeret
* Konsument/tjenestesida: DFØ. Sjekk med forvaltning

### Målsetting
Gi kunnskap inn i utredningen til høsten - kan difi / andre ta en nasjonal rolle på feltet?
Hvordan kan man gi tjenester / API-konsumenter "tilleggsinformasjon" om en innlogget bruker på ein skalerbar og desentralisert måte?

## Oppstart

Kjør testmiljø for ID-porten og testendepunktene. 
Endepunktene er 
* OIDC-klienten
* STS-serveren 
* Ressurs-serveren

I browseren bruker man kun localhost:OIDCport, der OIDCport er portnummeret brukt på OIDC-endepuntktet.

### Forhåndskrav

Hva du trenger og hvordan du kan sette opp systemet.

```
Maven 3.5.3
Java SE Development Kit 8
PostgreSQL, eller andre Hibernatekompatible databasesystemer
```

### Installering

Slik setter du opp installasjonen av alt nødvendig i prosjektet.

For å kjøre postgresdatabasene der filepath er lokasjonen til databasen skriver man som følger
```
postgres -D C:\filepath
```



## Kjøring av tester

Mer info kommer.

## Laget med

* [Java SDK](http://www.oracle.com/technetwork/java/javase/downloads/jdk9-downloads-3848520.html) - Språk brukt for backend
* [Maven](https://maven.apache.org/) - Dependency Management
* [Spring Boot](https://spring.io/) - Application development tool

## Deltakende medlemmer

Utviklingen består av medlemmer fra DifiCamp 2018. Se listen over [contributors](https://github.com/difi/dc18-TokenExchange/edit/master/) som deltok på dette prosjektet.

## Takk til

* Direktoratet for forvaltning og IKT
* OpenID Foundation

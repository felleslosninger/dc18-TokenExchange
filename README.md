"# dc18-TokenExchange" 
Nett-tjeneste hjå etat A har behov for å vite kva etat B som brukaren kjem frå.
1.  Teneste A er OIDC-klient
2.  Bruker gjennomfører autentisering gjennom ID-porten og mottek standard id_token
3.  A bytter id_token'et inn i ei tokenutvekslingsteneste (STS) og får tilbake eit nytt token som har vorte berika  frå autorativ kjelde med org.no tilhøyrande arbeidsgjevar
4.  Teneste A sjekker at berika token innheld org.no til lokalt godkjend verksemd
 
Det skal lagast ein enkel demo-teneste A, men hovudjobben er å laga ein frittståande STS-applikasjon som realiserer steg 3.  Applikasjonen skal snakka  Ouauth2 Token Exchange spesifikasjonen ut mot teneste A.  På "baksida" skal den sjekke mot ulike autorative kjelder (kandidatar er Altinn Autorisasjon, AA-registeret, DFØ-ID, Azure AD).  
 
Oppgåva kan utaukast med tilleggsfunksjonalitet:
 
• Sjå på korleis realisere identisk etats-identifisering på tvers av ulike autorative kjelder. 
• Sjå på korleis tenestene kan bestemme kva autorative kjelder som skal brukast
• Sjå på andre kontekster som fullmakter eller vergemål  
 
• Pilot på nokre Difi-tenester: t.d arbeidsgjevarportalen, samarbeidsportalen (DFØ)
 
• Føderasjon mellom ID-porten og STS (la ID-porten publisere sine integrasjoner over OIDC Federations spec slik at STS kan gjere tilgangskontroll på tokenexchange-endepunktet)
 
Kva eksterne kan vi samarbeide med ?
- Ta kontakt med DFØ. Anten integrasjon mot DFØ-ID, eller få dump av Difi-tilsette i DFØ og innstallere lokalt. 
• Ta kontakt med Altinn.  Tilgang til Altinn Autorisasjon
• NAV: AA-registeret
• Konsument/tjenestesida: DFØ.  Sjekk med forvaltning
Målsetting: Gje kunnskap inn i utredninga til hausten - kan vi / andre ta ei nasjonal rolle på feltet?
Korleis kan ein gje tjenester / API-konsumenter "tilleggsinformasjon" kring ein innlogga brukar på ein skalerbar og desentralisert måte  ?

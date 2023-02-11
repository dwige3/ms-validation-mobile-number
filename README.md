South African Mobile Number Validation Project
>Questo progetto è stato sviluppato per validare i numeri di cellulare sudafricani presenti in un file CSV.

FUNZIONALITA

>Consumare il file CSV tramite un'API.
>Testare ogni numero e verificare la sua corretta formattazione.
>Tentare di correggere i numeri non formati correttamente.


PREREQUISITI

Per eseguire questo progetto, è necessario avere i seguenti software installati:

Java 8 o successivo
Apache Maven
Database (ad esempio, SQlServer)
Il file TEMP_FILE_NAME (in questo caso "interlogica-phoneNumber.tmp") è un file temporaneo che viene creato sul file system della macchina in esecuzione dell'applicazione.

API

L'API ha 6 endpoint:

>/api/v1/phone-numbers/validate/{mobileNumber}: questo endpoint accetta un numero di cellulare sudafricano e restituisce il risultato della sua validazione.

>/api/v1/phone-numbers/tmp/uploadFileCsv: questo endpoint accetta un file CSV contenente numeri di cellulare sudafricani e restituisce i risultati della loro validazione.

>/api/v1/phone-numbers/tmp/checkNumber/{originalNumber}:check Validate a mobile number

>/api/v1/phone-numbers/tmp/corrected:questo endpoint fornisce un Servizio per il recupero dei numeri telefoni sud africani corretti

> /api/v1/phone-numbers/tmp/invalid: Get all invalid Phone Number

> /api/v1/phone-numbers/tmp/acceptable: Get all acceptable Phone Number


ESECUZIONE

Scaricare o clonare il progetto da questa repository.
Configurare il file di configurazione per la connessione al database.
Eseguire la build del progetto con il comando mvn clean install.
Avviare il server con il comando mvn spring-boot:run.
Accedere all'API tramite Swagger all'indirizzo http://localhost:8020/interlogica/swagger-ui.html

TEST

Per eseguire i test unitari, utilizzare il comando mvn test.

CONCLUSIONE

Questo progetto fornisce una soluzione per la validazione dei numeri di cellulare sudafricani presenti in un file CSV.
L'API fornita consente di testare singoli numeri di cellulare e fornisce una risposta con un messaggio di successo o errore.
La documentazione Swagger fornisce informazioni dettagliate su come utilizzare l'API.

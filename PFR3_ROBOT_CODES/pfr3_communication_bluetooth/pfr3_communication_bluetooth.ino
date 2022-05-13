
#include <newSoftwareSerial.h>

// UTILISER CA
SoftwareSerial mavoieserie(10, 11); // (TX, RX)

void setup()
{   // JIMMY , MDP: 4321
    // Ouvre la voie série avec l'ordinateur
    Serial.begin(9600 );
    // Ouvre la voie série avec le module BT
    mavoieserie.begin(9600 ); // 38400 pour la configuration
}

void loop() // run over and over
{
    if (mavoieserie.available()) {
        Serial.write(mavoieserie.read());
    }
    if (Serial.available()) {
        mavoieserie.write(Serial.read());
    }
}

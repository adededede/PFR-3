#include <SoftwareSerial.h>

#define BLUETOOTH_RX 9
#define BLUETOOTH_TX 8
boolean controleBluetoothActif;
SoftwareSerial mavoieserie(BLUETOOTH_TX, BLUETOOTH_RX);
void setup() {
  // Ouvre la voie série avec l'ordinateur
  Serial.begin(9600 );
  // Ouvre la voie série avec le module BT
  mavoieserie.begin(9600 ); // 38400 pour la configuration
 Serial.write("début du programme ");
}

void loop() {
  if(!controleBluetoothActif){
    Serial.write("controle bluetooth non activé \n");
    delay (2000);
  }
  char commande = ' '; // manuel "m" , cartographie "c"
  if (mavoieserie.available()) {
    commande = mavoieserie.read();
  }
  if (!controleBluetoothActif && commande == 'm' ) {
    controleBluetoothActif = true;
    commande = ' ';
  } else {// controle bluetooth activé 
    while ( controleBluetoothActif ) {
       commande = mavoieserie.read();
      switch (commande) {
        case 'z':
          // avancer
          Serial.write("j'avance \n");
          break;
        case 's':
          // reculer
          Serial.write("je recule \n");
          break;
        case 'q':
          // tourner à gauche
          Serial.write("je tourne à gauche \n");
          break;
        case 'd':
          // tourner a droite
           Serial.write("je tourne a droite \n");
          break;
        case 'c':
          // quitter la cartographie
          Serial.write("je passe en mode cartographie \n");
          controleBluetoothActif = false;
        default:
        //  Serial.write("pas de commande \n");
          
          break;
      }
    }

  }

}
/*
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
*/

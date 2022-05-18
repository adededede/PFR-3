
#include "bluetooth.h"
char commande; // manuel "m" , cartographie "c"
boolean controleBluetoothActif = false;
void initBluetooth(void) {
  // Ouvre la voie série avec le module BT
  Serial1.begin(9600);//19 RX, 18 TX
  Serial.println("initialisation bluetooth");

}



void ecouterBluetooth(Servo d, Servo g) {



  if (!controleBluetoothActif) {
    Serial.println("bluetooth non connecté");
  }
  if (Serial1.available()) {
    commande = Serial1.read();

  }
  if (!controleBluetoothActif && commande == 'm' ) {
    controleBluetoothActif = true;
    commande = ' ';
    Serial.println("bluetooth connecté");
  }
  while ( controleBluetoothActif ) {

    if (Serial1.available()) {
      commande = Serial1.read();

    }
    switch (commande) {
      case 'z':
        // avancer
        Serial.println("avancer");
        avancer(g, d, 1600);
        break;
      case 's':
        // reculer
        arretTotal(d, g, 500);
        reculer(d, g);
        Serial.println("reculer");
        break;
      case 'q':
        // tourner à gauche
        Serial.println(" tourner à gauche");
        tournerGaucheBT( g,  d);
        break;
      case 'd':
        // tourner a droite
        tournerDroiteBT( g,  d);
        Serial.println("  tourner a droite");
        break;
      case 'c':
        // passer en mode auto
        arretTotal(d, g, 1000);
        controleBluetoothActif = false;
      default:

        break;
    }


  }
}

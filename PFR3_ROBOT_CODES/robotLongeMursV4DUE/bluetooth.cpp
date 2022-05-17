/*
#include "bluetooth.h"
#include "fonctions_Moteurs.h"
#include <SoftwareSerial.h>
#define BLUETOOTH_RX 12
#define BLUETOOTH_TX 13
boolean controleBluetoothActif;
SoftwareSerial mavoieserie(BLUETOOTH_TX, BLUETOOTH_RX);
void initBluetooth(void) {
  // Ouvre la voie série avec le module BT
  mavoieserie.begin(9600);
}



void ecouterBluetooth(void) {

  char commande; // manuel "m" , cartographie "c"
  if (mavoieserie.available()) {
    commande = mavoieserie.read();
  }
  if (!controleBluetoothActif && commande == 'm' ) {
    controleBluetoothActif = true;
    commande = ' ';
  } else {
    while ( controleBluetoothActif ) {
      switch (commande) {
        case 'z':
          // avancer
          break;
        case 's':
        // reculer
          break;
        case 'q':
        // tourner à gauche
          break;
        case 'd':
        // tourner a droite 
          break;
        case 'c':
        // quitter la cartographie
        controleBluetoothActif = false;
        default:

          break;
      }
    }

  }
}*/

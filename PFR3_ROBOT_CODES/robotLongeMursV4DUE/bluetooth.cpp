
#include "bluetooth.h"

char commande = 'm'; // manuel "m" , cartographie "c"
boolean isQuit = false;

void initBluetooth(void) {
  // Ouvre la voie série avec le module BT
  Serial1.begin(9600);//19 RX, 18 TX
  Serial.println("initialisation bluetooth");

}



boolean ecouterBluetooth(Servo d, Servo g, boolean _CartographieActive) {
  if (Serial1.available()) {
    commande = Serial1.read();

  }
  // au départ on est en mode manuel
  if (commande == 'm' ) {
    arretTotal(d, g, 500);
    commande = ' ';
    Serial.println("Mode manuel !");
    _CartographieActive = false ;
    while ( !_CartographieActive ) {
      if (Serial1.available()) {
        commande = Serial1.read();
      }

      switch (commande) {
        case 'z':
          // avancer
          avancer(g, d, 1800);
          commande = ' ';
          Serial.println("avancer");
          break;
        case 's':
          // arreter
          arretTotal(d, g, 500);
          //reculer(d, g);
          Serial.println("reculer");
          break;
        case 'q':
          // tourner à gauche
          Serial.println(" tourner à gauche");
          tournerGauche_90_BT( g,  d);
          break;
        case 'd':
          // tourner a droite
          tournerDroite_90_BT( g,  d);
          Serial.println("  tourner a droite");
          break;
        case 'c':
          // passer en mode auto
          Serial.println("quitter");
          arretTotal(d, g, 1000);
          _CartographieActive = true;
          break;
        case 'a':
          tournerGauche_petit_BT(g,  d);
          break;
        case 'e':
          tournerDroite_petit_BT(g,  d);
          break;
        default:
          break;
      }//fin switch
    }//fin while
    return _CartographieActive;
  }//fin if
  else {
    return true;
  }
}
void envoyerEtat (char c) {
  Serial1.write(c);
}


#include "bluetooth.h"
//convention des commandes : manuel 'm', cartographie/longer le mur 'c'
char commande = 'm';
boolean isQuit = false;
int evitmentUrgence ;

/**

*/
void initBluetooth(void) {
  //  ouvre le port série de la due relié au bluetooth
  Serial1.begin(9600);//19 RX, 18 TX ,
  Serial.println("initialisation bluetooth");

}


/**
   Servo d : moteur droit, Servo g : moteur gauche
   Boolean _CartographieActive
*/
boolean ecouterBluetooth(Servo d, Servo g, boolean _CartographieActive) {
  if (Serial1.available()) {
    commande = Serial1.read();

  }
  // au départ le robot est déja en mode  manuel
  if (commande == 'm' ) {
    arretTotal(d, g, 500); // si il était en mode cartographie on l'immobilise
    commande = ' ';
    Serial.println("Mode manuel !");
    _CartographieActive = false ;
    while ( !_CartographieActive ) { // tant que la cartographie est désactivé on reste en mode manuel
      evitmentUrgence = digitalRead(2);
      if (Serial1.available()) {
        commande = Serial1.read();
      }

      if (evitmentUrgence == FALLING) { // ce if  ne fonctionne pas , peut être faut il déclarer une interruption
        Serial.println("STOP !");
        arretTotal(d, g, 500);
      }
      switch (commande) {
        case 'z':
          // avancer
          avancer(g, d, 1600);
          commande = ' ';
          Serial.println("avancer");
          break;
        case 's':
          // arreter
          arretTotal(d, g, 500);
          //reculer(d, g);
          Serial.println("arreter");
          break;
        case 'q':
          // tourner de 90° à gauche
          Serial.println(" tourner à gauche");
          tournerGauche90( g,  d);
          break;
        case 'd':
          // tourner de 90° à droite
          tournerDroite90( g,  d);
          Serial.println("  tourner a droite");
          break;
        case 'c':
          // passer en mode auto
          Serial.println("quitter");
          arretTotal(d, g, 1000);
          _CartographieActive = true;
          break;
        case 'a': // petite rotation a gauche
          tournerGauche_petit_BT(g,  d);
          break;
        case 'e':// petite rotation a droite
          tournerDroite_petit_BT(g,  d);
          break;
        default:
          break;
      }//fin switch
    }//fin while
    return _CartographieActive;// quand on sort du while _CartographieActive est a true et on passe en mode cartographie
  }//fin if
  else { // si la première commande en mode auto/cartographie n'est pas m alors on reste en mode cartographie 
    return true;
  }
}

// envoie un charactère a l'application android pour signaler son état
void envoyerEtat (char c) {
  Serial1.write(c);
}

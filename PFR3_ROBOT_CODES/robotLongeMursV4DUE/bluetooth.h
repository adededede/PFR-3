#if !defined BLUETOOTH_H
  #define BLUETOOTH_H
  #include <Arduino.h>
  #include "fonctions_Moteurs.h"
/**
 * initialise les pins reliés au module bluetooth
 */
void initBluetooth(void);
/**
 *  reçoit les commande de l'application android 
 */
boolean ecouterBluetooth(Servo d,Servo g,boolean CartographieActive); 
/**
 *  envoie unn charactère a l'application android pour signaler son état
 */
void envoyerEtat (char c);
#endif

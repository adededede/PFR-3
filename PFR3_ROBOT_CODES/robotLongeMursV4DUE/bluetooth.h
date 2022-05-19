#if !defined BLUETOOTH_H
  #define BLUETOOTH_H
  #include <Arduino.h>
  #include "fonctions_Moteurs.h"

void initBluetooth(void);
boolean ecouterBluetooth(Servo d,Servo g,boolean CartographieActive);
void envoyerEtat (char c);
#endif

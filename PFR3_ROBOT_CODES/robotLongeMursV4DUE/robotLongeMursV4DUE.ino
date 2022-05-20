/*---------------------------------------------------------------*/
/*--------------------------ROBOT ARDUINO------------------------*/
/*---------------------------------------------------------------*/
/* AVANT LA MISE EN MARCHE                                       */
/* PLACER LE ROBOT A 20 CM D'UN MUR A LA GAUCHE DU ROBOT         */
/*---------------------------------------------------------------*/

#include "fonctions_robot.h"
#include "fonctions_Moteurs.h"
#include "bluetooth.h"
#include <Scheduler.h>

#define AVANCER 'z'
#define TOURNER_GAUCHE 'q'
#define TOURNER_DROIT 'd'
//déclare sd et sg de type Servo
Servo sg, sd;

//variables globales
float c1distance = 100;//on met par défaut une valeur non critique pour ne pas entrer
float c2distance = 100;//dans un if dès la mise en marche du robot
float c3distance = 30;

volatile bool isEvitementObstacle = false;//volatile car ces variables peuvent etre modifiees par une ISR(interrupt service routine)
volatile bool isPlusDeMur = false;
volatile bool isRedresseDroit = false;
volatile bool isRedresseGauche = false;
volatile bool isFinPlusDeMur = true;

unsigned long startTime;
unsigned long currentTime;
const unsigned long period = 1000;
volatile boolean CartographieActive = false;



//ISR
void evitementObstacle(void) {
  isEvitementObstacle = true;
}
void plusDeMur(void) {
  isPlusDeMur = true;
}
void redresseDroit(void) {
  isRedresseDroit = true;
}
void redresseGauche(void) {
  isRedresseGauche = true;
}
void finPlusDeMur(void) {
  isFinPlusDeMur = true;
}

void setup()
{
  //initialise le timer
  startTime = millis();
  //initialisation de la communication avec le moniteur série
  Serial.begin(9600);
  initBluetooth();
  //initEncodeurs() ;
  //servo.attach() positionne moteurs à la derniere valeur utilisee via servo.write();
  sg.write(1500);//positionne les roues à l'arret
  sd.write(1500);
  sg.attach(9);  //  paire droite
  sd.attach(10); // paire gauche (oui il y a inversion)

  //déclaration des pins qui lisent les déclenchement d'interruptions
  pinMode(2, INPUT_PULLUP);//INUPUT_PULLUP ; utilisation résistance interne
  pinMode(3, INPUT_PULLUP);
  pinMode(4, INPUT_PULLUP);
  pinMode(5, INPUT_PULLUP);
  pinMode(6, INPUT_PULLUP);
  //pin qui déclenchent les interruptions chez la UNO
  pinMode(13, OUTPUT);
  digitalWrite(13, HIGH); //déclenche interruption en passant à LOW


  //déclaration des interruptions
  attachInterrupt(digitalPinToInterrupt(2), evitementObstacle, FALLING);//quand pin 2 passe de HIGH à LOW execute l'ISR arretUrgence
  attachInterrupt(digitalPinToInterrupt(3), plusDeMur,  FALLING);
  attachInterrupt(digitalPinToInterrupt(4), redresseDroit, FALLING);
  attachInterrupt(digitalPinToInterrupt(5), redresseGauche, FALLING);
  attachInterrupt(digitalPinToInterrupt(6), finPlusDeMur, FALLING);

  //faire 3 bips pour annoncer départ du robot
  //bipInitialisation();

  //Scheduler.startLoop(loopBluetooth);

}//fin setup

void loop() {

  CartographieActive = ecouterBluetooth(sg, sd, CartographieActive);

  if (CartographieActive) {

    if (isEvitementObstacle) {
      //arret
      arretTotal(sg, sd, 500);
      //tourne à droite pour éviter obstacle (tourne à 90° a droite)
      envoyerEtat(TOURNER_DROIT);
      tournerDroite_90_BT( sd,  sg);
      //on remet le robot droit en marche avant
      envoyerEtat(AVANCER);
      avancer(sg, sd, 1580);
      //on prépare la prochaine interruption en cas de d'obstacle
      isEvitementObstacle = false;
    }

    else if (isPlusDeMur) { //fonctionne si au moins un des deux capteurs voit le mur
      //arret
      delay(500);
      arretTotal(sg, sd, 500);
      //tourne à gauche pour relonger le du mur (tourne à 90° a gauche)
      envoyerEtat(TOURNER_GAUCHE);
      tournerGauche_90_BT( sd,  sg);
      //on enleve interruption plus de mur le temps que le robot reviennent pres du mur
      //apres avoir tourne a gauche
      detachInterrupt(digitalPinToInterrupt(3));
      //previent la UNO qu'il doit scruter la fin de plus de mur
      digitalWrite(13, LOW);
      delay(50);
      digitalWrite(13, HIGH);
      delay(500);
      //on remet le robot droit en marche avant
      envoyerEtat(AVANCER);
      avancer(sg, sd, 1580);
      //on prépare la prochaine interruption en cas d'abscence de mur
      isPlusDeMur = false;
    }

    else if (isRedresseDroit) {
      sd.writeMicroseconds(1650);
      delay(90);
      sd.writeMicroseconds(1580);
      //on prépare la prochaine interruption en cas de redressage à droite
      isRedresseDroit = false;
    }

    else if (isRedresseGauche) {
      sg.writeMicroseconds(1650);
      delay(100);
      sg.writeMicroseconds(1580);
      //on prépare la prochaine interruption en cas de redressage à gauche
      isRedresseGauche = false;
    }

    if (isFinPlusDeMur) {
      attachInterrupt(digitalPinToInterrupt(3), plusDeMur,  FALLING);
      isFinPlusDeMur = false;
    }
  }

}//fin loop

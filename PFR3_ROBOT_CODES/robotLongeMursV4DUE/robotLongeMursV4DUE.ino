/*---------------------------------------------------------------*/
/*--------------------------ROBOT ARDUINO------------------------*/
/*---------------------------------------------------------------*/
/* AVANT LA MISE EN MARCHE                                       */
/* PLACER LE ROBOT A 20 CM D'UN MUR A LA GAUCHE DU ROBOT         */
/*---------------------------------------------------------------*/

/* ROLE DE LA DUE : INTERPRETER LES MESSAGES DE LA UNO ET EFFECTUER
    LES ACTIONS NECESSAIRES + COMMUNICATION VIA BLUETOOTH POUR LE
    MODE MANUEL ET LA CARTOGRAPHIE (AUTOMATIQUE)*/

#include "fonctions_robot.h"
#include "fonctions_Moteurs.h"
#include "bluetooth.h"
#include <Scheduler.h>

//commandes envoyees via bluetooth pour commande mode manuel
#define AVANCER 'z'
#define TOURNER_GAUCHE 'q'
#define TOURNER_DROIT 'd'

//déclare sd et sg de type Servo
Servo sg, sd;

//variables globales
float c1distance = 100;//on met par défaut une valeur non critique pour ne pas entrer
float c2distance = 100;//dans un if dès la mise en marche du robot
float c3distance = 30;

volatile bool isEvitementObstacle = false;//volatile car ces variables peuvent etre modifiees par une ISR(Interrupt Service Routine)
volatile bool isPlusDeMur = false;
volatile bool isRedresseDroit = false;
volatile bool isRedresseGauche = false;
volatile bool isFinPlusDeMur = true;
volatile boolean CartographieActive = false;

unsigned long startTime;
unsigned long currentTime;
const unsigned long period = 1000;

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

  //à l'allumage du robot, on force le mode manuel (CartographieActive = false)
  //on écoute le bluetooth en permanence pour attendre qu'il nous dise de passer en
  //mode cartographie automatique (CartographieActive = true)
  CartographieActive = ecouterBluetooth(sg, sd, CartographieActive);

  if (CartographieActive) {

    //si détecte un mur ou un obstacle
    if (isEvitementObstacle) {
      //arret
      arretTotal(sg, sd, 500);
      //indique via bluetooth que le robot s'est arrete et qu'il tourne
      envoyerEtat(TOURNER_DROIT);
      //tourne à droite pour éviter obstacle (tourne à 90° a droite)
      tournerDroite90(sd, sg);
      //indique via bluetooth que le robot se remet à avance
      envoyerEtat(AVANCER);
      //on remet le robot droit en marche avant
      avancer(sg, sd, 1580);
      //on prépare la prochaine interruption en cas de d'obstacle
      isEvitementObstacle = false;
    }

    //si detecte une absence de mur a gauche (quand distance mesuree > 40 cm a gauche du robot)
    else if (isPlusDeMur) {
      //delai de 0.5s pour eviter qu'apres avoir tourne le robot ne se retrouve trop
      //colle au mur a sa gauche
      delay(500);
      //arret
      arretTotal(sg, sd, 500);
      //indique via bluetooth que le robot s'est arrete et qu'il tourne
      envoyerEtat(TOURNER_GAUCHE);
      //tourne à gauche pour relonger le du mur (tourne à 90° a gauche)
      tournerGauche90( sd,  sg);
      //on enleve interruption plus de mur le temps que le robot revienne pres du mur
      //apres avoir tourne a gauche
      detachInterrupt(digitalPinToInterrupt(3));
      //previent la UNO qu'il faut scruter la fin d'une absencce de mur
      //la UNO enverra un message pour prevenir que le robot est de nouveau pres du mur
      digitalWrite(13, LOW);
      delay(50);
      digitalWrite(13, HIGH);
      delay(500);
      //indique via bluetooth que le robot se remet à avance
      envoyerEtat(AVANCER);
      //on remet le robot droit en marche avant
      avancer(sg, sd, 1580);
      //on prépare la prochaine interruption en cas d'abscence de mur
      isPlusDeMur = false;
    }

    //si le robot est trop près du mur ou alors pas parallele (orienté vers la gauche)
    else if (isRedresseDroit) {
      //décale le robot sur la droite et le remet parallele au mur
      sd.writeMicroseconds(1650);
      delay(90);
      sd.writeMicroseconds(1580);
      //on prépare la prochaine interruption en cas de redressage à droite
      isRedresseDroit = false;
    }

    //si le robot est trop loin du mur ou alors pas parallele (orienté vers la droite)
    else if (isRedresseGauche) {
      //décale le robot sur la gauche et le remet parallele au mur
      sg.writeMicroseconds(1650);
      delay(100);
      sg.writeMicroseconds(1580);
      //on prépare la prochaine interruption en cas de redressage à gauche
      isRedresseGauche = false;
    }

    //si le robot est a nouveau pres du mur
    if (isFinPlusDeMur) {
      //on reactive l'interruption "plusDeMur"
      attachInterrupt(digitalPinToInterrupt(3), plusDeMur,  FALLING);
      isFinPlusDeMur = false;
    }

  }//fin if(CartographieActive)

}//fin loop

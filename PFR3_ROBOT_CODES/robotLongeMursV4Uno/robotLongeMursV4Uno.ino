/*---------------------------------------------------------------*/
/*--------------------------ROBOT ARDUINO------------------------*/
/*---------------------------------------------------------------*/
/* AVANT LA MISE EN MARCHE                                       */
/* PLACER LE ROBOT A 20 CM D'UN MUR A LA GAUCHE DU ROBOT         */
/*---------------------------------------------------------------*/

/* ROLE DE LA CARTE UNO : MESURER CONTINUELLEMENT VIA LES 3 CAPTEURS
   ET NE COMMUNIQUER AVEC LA CARTE DUE QUE QUAND UNE DISTANCE
   CRITIQUE EST MESUREE */

#include "fonctions_robot.h"

float c1distance = 100;  //on met par défaut une valeur non critique pour ne pas entrer
float c2distance = 100;  //dans un if dès la mise en marche du robot
float c3distance = 30;
int diffLaterale = 0;

volatile bool listenFinPlusDeMur = false;

//ISR
void listenFinPlusDeMurISR(void) {
  listenFinPlusDeMur = true;
}

void setup() {
  //déclaration des modes des  pins utilisés
  pinMode(c1TrigPin, OUTPUT);
  pinMode(c1EchoPin, INPUT);
  pinMode(c2TrigPin, OUTPUT);
  pinMode(c2EchoPin, INPUT);
  pinMode(c3TrigPin, OUTPUT);
  pinMode(c3EchoPin, INPUT);

  pinMode(obstaclePin, OUTPUT);
  pinMode(plusDeMurPin, OUTPUT);
  pinMode(redresseDPin, OUTPUT);
  pinMode(redresseGPin, OUTPUT);
  pinMode(finPlusDeMurPin, OUTPUT);

  pinMode(interruptFromDUE, INPUT_PULLUP);


  //met les pins qui déclenchent interruptions sur la DUE à HIGH
  //car ils déclenchent interuptions sur la DUE en passant de HIGH à LOW (falling)
  digitalWrite(obstaclePin, HIGH);
  digitalWrite(plusDeMurPin, HIGH);
  digitalWrite(redresseDPin, HIGH);
  digitalWrite(redresseGPin, HIGH);
  digitalWrite(finPlusDeMurPin, HIGH);

  //declaration interuption déclenchée par la DUE pour dire à la UNO de ne plus envoyer
  //le signal d'interruption "plusDeMur" tant que le robot n'est pas de retour près d'un mur
  //la DUE prévient la UNO via cette interruption quand c'est bon
  attachInterrupt(digitalPinToInterrupt(interruptFromDUE), listenFinPlusDeMurISR, FALLING);

}  //fin setup
void loop() {

  //on déclenche les capteurs et on lit leurs valeurs en continu
  //tant qu'aucun capteur ne détecte une distance critique
  c1distance = lectureCapteurAvant();
  c2distance = lectureCapteurLateralAvant();
  c3distance = lectureCapteurLateralArriere();

  //si différence mesuree entre capteurs latéraux alors c'est que le robot n'est pas parralele au mur
  diffLaterale = c2distance - c3distance;

  //evitement d'obstacle
  if (c1distance < 25 && c1distance > 5 ) { //pour eviter les valeurs extremes en cas de non detection de mur
    digitalWrite(obstaclePin, LOW);//déclenche une interruption sur le programme de la DUE
    delay(50);
    digitalWrite(obstaclePin, HIGH);//prépare le prochain passage à LOW (déclenche interruption)
  }
  //indique a la DUE que le robot est a nouveau pres d'un mur
  else if ((c2distance < 60 && c3distance < 60) && listenFinPlusDeMur) {
    digitalWrite(finPlusDeMurPin, LOW);
    delay(50);
    digitalWrite(finPlusDeMurPin, HIGH);
    listenFinPlusDeMur = false;
  }
  //declenche "redresseGauche" dans le programme de la DUE
  //diffLaterale < 5 pour éviter le cas où un seul capteur voit le mur après interruption "plusDeMur" dans le programme DUE
  else if ((diffLaterale > 0 && diffLaterale < 5) || ((c2distance > 45 || c3distance > 45) && (c2distance < 70 &&  c3distance < 70)))  { //si positif, alors robot trop vers la droite
    digitalWrite(redresseGPin, LOW);
    delay(50);
    digitalWrite(redresseGPin, HIGH);
    //Serial.println("redresse gauche");
  }
  //declenche "redresseDroit" dans le programme de la DUE
  //diffLaterale < 5 pour la même raison
  else if ((diffLaterale < 0 && diffLaterale < 5) || ((c2distance < 50 || c3distance < 50) && (c2distance < 70 &&  c3distance < 70))) { //si négatif, alors robot trop vers la gauche
    digitalWrite(redresseDPin, LOW);
    delay(50);
    digitalWrite(redresseDPin, HIGH);
    //Serial.println("redresse droit");
  }
  //si "plus de mur à gauche" alors tourne de 90° à gauche
  if ((c2distance >= 60 && c3distance >= 60) && !listenFinPlusDeMur) {
    digitalWrite(plusDeMurPin, LOW);
    delay(50);
    digitalWrite(plusDeMurPin, HIGH);
    //Serial.println("plus de mur");
  }
}//fin loop

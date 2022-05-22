#if !defined FONCTIONS_MOTEURS_H
#define FONCTIONS_MOTEURS_H
#include <Arduino.h>
#include <Servo.h>
/**initialise les pin associés aux encodeurs
 * 
 */
void initEncodeurs(void);
/**
 * lis la vitesse de la paire de roue gauche (non fonctionnelle) 
 */
void readEncodeurGauche(void);

/**
 * lis la vitesse de la paire de roue droite (non fonctionnelle) 
 */
void readEncodeurDroit(void);

/**immobilise un côté du robot 
 * Servo paireDeRoue : paire de roues a immobilisé 
 */
void StopPaireDeRoue(Servo paireDeRoue);
/**
 * * Servo rouesGauches, : paire de moteurs  gauche du robot
 * Servo rouesDroites  : paire de moteurs  droit  du robot
 * immobilise le robot pour le delai défini 
 */
void arretTotal(Servo rouesGauches, Servo RouesDroites, int delai);

/**Fait avancer le robot
 * Servo rouesGauches, : paire de moteurs  gauche du robot
 * Servo rouesDroites  : paire de moteurs  droit  du robot
 * 
 *  int millisecondes : 
 */
void avancer(Servo rouesDroites, Servo rouesGauches, int millisecondes);

/**Fait reculer le robot
 * Servo rouesGauches, : paire de moteurs  gauche du robot
 * Servo rouesDroites  : paire de moteurs  droit  du robot
 */
void reculer (Servo rouesDroites, Servo rouesGauches);

/**
 * Servo rouesGauches, : paire de moteurs  gauche du robot
 * Servo rouesDroites  : paire de moteurs  droit  du robot
 * 
 * fait tourner le robot de 90 degrés dans la direction voulu 
 */
void tournerGauche90(Servo rouesGauches, Servo rouesDroites);
void tournerDroite90(Servo rouesGauches, Servo rouesDroites);

/**
 * Servo rouesGauches, : paire de moteurs  gauche du robot
 * Servo rouesDroites  : paire de moteurs  droit  du robot
 * 
 * fait tourner le robot dans la direction voulu un court instant
 */
void tournerGauche_petit_BT(Servo rouesGauches, Servo rouesDroites);
void tournerDroite_petit_BT(Servo rouesGauches, Servo rouesDroites);

#endif

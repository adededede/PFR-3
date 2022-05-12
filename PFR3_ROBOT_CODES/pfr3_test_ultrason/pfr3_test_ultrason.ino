const int c1TrigPin = 2; 
const int c1EchoPin = 3; 
const int c2TrigPin = 7;
const int c2EchoPin = 8;
float c1distance; 
float c2distance;

void setup() 
{   
        Serial.begin(9600); 
        
        pinMode(c1TrigPin, OUTPUT); 
        pinMode(c2TrigPin, OUTPUT); 
        pinMode(c1EchoPin, INPUT);
        pinMode(c2EchoPin, INPUT);
        
        Serial.println("Ultrasonic sensor:");
} 

void loop() 
{       
        //on déclenche le capteur 1 et on lit sa valeur
        digitalWrite(c1TrigPin, LOW); 
        delayMicroseconds(2); 
        digitalWrite(c1TrigPin, HIGH); 
        delayMicroseconds(10);
        digitalWrite(c1TrigPin, LOW); 
        c1distance = pulseIn(c1EchoPin, HIGH) / 58.00; //commence un timer jusqu'à que c1EchoPin soit à HIGH
        delayMicroseconds(2); 
        
        //PUIS on déclenche le capteur 2 et on lit sa valeur
        digitalWrite(c2TrigPin, LOW);
        delayMicroseconds(2); 
        digitalWrite(c2TrigPin, HIGH);
        delayMicroseconds(10); 
        digitalWrite(c2TrigPin, LOW);
        c2distance = pulseIn(c2EchoPin, HIGH) / 58.00;
        
        Serial.print("capteur 1 distance :"); 
        Serial.print(c1distance);
        Serial.print("cm"); 
        Serial.print("               "); 
        Serial.print("capteur 2 distance :"); 
        Serial.print(c2distance);
        Serial.print("cm\n");
        
        delay(1000); 
}

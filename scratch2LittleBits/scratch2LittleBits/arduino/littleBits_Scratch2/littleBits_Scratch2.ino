      /****************************************************/
/*                                                  */
/*    littleBits Scratch 2.0 extension              */
/*    created by Kreg Hanning 2014                  */
/*                                                  */
/****************************************************/

// Status codes sent from Scratch
const int READ_PINS = 1;
const int WRITE_ANALOG = 2;
const int WRITE_DIGITAL = 3;
const int BYTE_CONTROL = 30;

// Analog input smoothing
// http://arduino.cc/en/Tutorial/Smoothing

const int sensorChannels = 20;
const int maxNumReadings = 7;

int smoothingValues[sensorChannels][maxNumReadings];
int smoothingIndex[sensorChannels];
int smoothingTotal[sensorChannels];


int averageA1;
int averageA0;


int mlUltValor[3];

void setup() {
  
  // Set the Serial baud rate to 38400
  Serial.begin(38400);
  
  // Set up digital pins 1, 5, and 9 as outputs
  pinMode(1, OUTPUT);
  pinMode(5, OUTPUT);
  pinMode(9, OUTPUT);

  mlUltValor[0]=-1;
  mlUltValor[1]=-1;
  mlUltValor[2]=-1;
  
  setupSmoothing();

}

void setupSmoothing() {
     averageA1 = 0;
     averageA0 = 0;
  
     for(int i = 0; i < sensorChannels; i++) {
       for(int j = 0 ; j < maxNumReadings ; j++) {
         smoothingValues[i][j]=0;
       }
       smoothingTotal[i]=0;
       smoothingIndex[i]=0;
     }
     
}

void loop() {
  int incomingByte = 0;
  
  // Check if there are bytes on the Serial port
  if (Serial.available() > 0) {
    
    // 1 byte control
    incomingByte = Serial.read();
    if(BYTE_CONTROL == incomingByte){
      // Get first available byte
      incomingByte = Serial.read();
      
      if (incomingByte == READ_PINS) {
      
        // Read digital pin 0
        Serial.write(BYTE_CONTROL);
        Serial.write(digitalRead(0));
        
        // Get averages for analog pins 0 and 1
        Serial.write(averageA0);
        Serial.write(averageA1);
        
      } else if (incomingByte == WRITE_ANALOG) {
      
        // Next byte from Scratch is pin number
        int outputPin = Serial.read();
        
        // Next byte from Scratch is pin value
        int outputVal = Serial.read();
        
        if(outputPin==5 && mlUltValor[1]!=outputVal){
            mlUltValor[1]=outputVal;
            analogWrite(outputPin, outputVal);
        }
        if(outputPin==9 && mlUltValor[2]!=outputVal){
            mlUltValor[2]=outputVal;
            analogWrite(outputPin, outputVal);
        }
      } else if (incomingByte == WRITE_DIGITAL) {
      
        // Next byte from Scratch is pin number
        int outputPin = Serial.read();
        
        // Next byte from Scratch is pin value
        int outputVal = Serial.read();
        
        if(mlUltValor[0]!=outputVal){
           mlUltValor[0]=outputVal;
           digitalWrite(outputPin, outputVal);
        }
      
      } 
    }
  }
  // Analog input smoothing
  // http://arduino.cc/en/Tutorial/Smoothing
  averageA0 = readAnalogPort(A0);
  averageA1 = readAnalogPort(A1);
  
  delay(1);
}


int smoothingValue(int channel, int value) {
    int total;
    int index = smoothingIndex[channel];
    total = smoothingTotal[channel] - smoothingValues[channel][index];
    smoothingValues[channel][index] = value;
    smoothingTotal[channel] = total + value;
    smoothingIndex[channel]++;
    if(smoothingIndex[channel] >=maxNumReadings) {
      smoothingIndex[channel]=0;
    }

    return int(round(smoothingTotal[channel] / (maxNumReadings)));
}

int readAnalogPort(int plPin) {
  int value;
  value = analogRead(plPin);
  value = smoothingValue(plPin, value);
  if (value >= 1022) value = 1024;
  return value/4;
}

//int readDigitalPort(int plPin) {
//  if (digitalRead(plPin) == LOW) {
//    return 0;
//  }
//  else {
//    return 1023;
//  };
//}


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scratch2littlebits;


import java.io.IOException;
import java.io.OutputStream;
import java.util.Timer;
import java.util.TimerTask;
import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;
import jssc.SerialPortList;

/**
 *
 * @author eduardo
 */
public class ScratchArduino {

    public static final int INPUT = 0;
    public static final int OUTPUT = 1;
    public static final int ANALOG = 2;
    public static final int PWM = 3;
    public static final int SERVO = 4;
    public static final int SHIFT = 5;
    public static final int I2C = 6;
    public static final int LOW = 0;
    public static final int HIGH = 1;
    public static final int READ_PINS = 1;
    public static final int WRITE_ANALOG = 2;
    public static final int WRITE_DIGITAL = 3;
    public static final String mcsHIGH="true";
    public static final String mcsLOW="false";
    private String msPuerto;
    private SerialPort serialPort;
//    private CommPortIdentifier portIdentifier=null;
//    private OutputStream out;

    

    

    public static class InputVals {

        int d0 = 0;
        int a0 = 0;
        int a1 = 0;
    }

    public static class OutputPins {

        int d1 = 1;
        int d5 = 5;
        int d9 = 9;

    }    

    private int sendAttempts = 0;
    private byte[] rawData = null;
    private int[] pingCmd = new int[]{1};
    private boolean connected = false;
    private Timer moTimer;
    
    private InputVals inputVals = new InputVals();
    private OutputPins outputPins = new OutputPins();
    
    

    public ScratchArduino(String PuertoIdent) throws Exception {
   
            msPuerto = PuertoIdent;
            
            moTimer = new Timer();
            moTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    try {

                        if (sendAttempts >= 10) {
                            sendAttempts=0;
                            try{
                                Thread.sleep(2000);
                            }catch(Exception e){}
                            closePort();
                            openPort();
                        }

                        try{
                            write(pingCmd);
                        }catch(Throwable e){
                            sendAttempts++;
                        }

                    } catch (Throwable ex) {
                        ex.printStackTrace();
                    }
                }
            }, 50, 50);
            openPort();
    }

    public static String[] getListaPuertos(){
        return SerialPortList.getPortNames();
    }
    
    public synchronized void openPort() throws Exception{
        if(SerialPortList.getPortNames().length>0){
            try{
                serialPort = new SerialPort(msPuerto);
                serialPort.openPort();
            }catch(Throwable e){
                e.printStackTrace();
                serialPort = new SerialPort(SerialPortList.getPortNames()[0]);
                serialPort.openPort();
                msPuerto=SerialPortList.getPortNames()[0];
            }


            serialPort.setParams(SerialPort.BAUDRATE_38400, 
                                 SerialPort.DATABITS_8,
                                 SerialPort.STOPBITS_1,
                                 SerialPort.PARITY_NONE);

            serialPort.addEventListener(new SerialPortEventListener() {        
                public void serialEvent(SerialPortEvent event) {
                    try {
                        if(event.isRXCHAR()){//If data is available
                           if(event.getEventValue() >= 3){//Check bytes count in the input buffer
                               //Read data, if 10 bytes available 
                               try {
                                   rawData = serialPort.readBytes(3);
                                   processData();
                               }
                               catch (SerialPortException ex) {
                                   System.out.println(ex);
                               }
                           }
                       }
                       else if(event.isCTS()){//If CTS line has changed state
                           if(event.getEventValue() == 1){//If line is ON
                               System.out.println("CTS - ON");
                           }
                           else {
                               closePort();
                               System.out.println("CTS - OFF");
                           }
                       }
                       else if(event.isDSR()){///If DSR line has changed state
                           closePort();
                           if(event.getEventValue() == 1){//If line is ON
                               System.out.println("DSR - ON");
                           }
                           else {
                               System.out.println("DSR - OFF");
                           }
                       }
                    } catch (Throwable err) {
                        err.printStackTrace();
                    }
                }


            });
            int mask = SerialPort.MASK_RXCHAR + SerialPort.MASK_CTS + SerialPort.MASK_DSR;//Prepare mask
            serialPort.setEventsMask(mask);//Set mask

            try {
                Thread.sleep(3000); // let bootloader timeout
            } catch (InterruptedException e) {
            }
            connected = true;
        }
    }

    public String getPuerto() {
        return msPuerto;
    }    
    public void setPuerto(String psPuerto) throws Exception{
        msPuerto=psPuerto;
        closePort();
    }
    public synchronized void closePort() throws Exception {
        connected = false;
        rawData=null;
        if (serialPort != null) {
            SerialPort loAux = serialPort;
            serialPort = null;
            loAux.removeEventListener();
            loAux.closePort();
        }
    }

    public synchronized void close() throws Exception {
        closePort();
        if (moTimer != null) {
            moTimer.cancel();
            moTimer = null;
        }
    }

    private void processData() {
        inputVals.d0 = rawData[0] & 0xFF;
        inputVals.a0 = rawData[1] & 0xFF;
        inputVals.a1 = rawData[2] & 0xFF;
        rawData = null;
    }
    
    private int[] appendBuffer(int[] buffer1, int buffer2) {
        int[] tmp = new int[buffer1.length + 1];
        System.arraycopy(buffer1, 0, tmp, 0, buffer1.length);
        tmp[buffer1.length] = buffer2;
        return tmp;
    }

    public synchronized void write(int[] b) throws Exception {
        for(int i = 0; i < b.length; i++){
            serialPort.writeByte((byte)b[i]);
        }
    }

    public int analogRead(int pin) {
        int lResult = 0;
        switch (pin) {
            case 0:
                lResult = inputVals.d0;
                break;
            case 1:
                lResult = inputVals.a0;
                break;
            case 2:
                lResult = inputVals.a1;
                break;
        }
        return lResult;
    }

    public boolean digitalRead(int pin) {
        if (analogRead(pin) > 0) {
            return true;
        }
        return false;
    }

    public void analogWrite(int pin, int val) throws Exception {
        int[] output = new int[3];
        output[0] = 2;
        switch (pin) {
            case 0:
                output[1] = outputPins.d1;
                break;
            case 1:
                output[1] = outputPins.d5;
                break;
            case 2:
                output[1] = outputPins.d9;
                break;
        }
        output[2] = val;
        write(output);
    }
    public void analogWrite(String pin, int val) throws Exception {
        if(pin.equalsIgnoreCase("d1")){
            analogWrite(0, val);
        }else if(pin.equalsIgnoreCase("d5")){
            analogWrite(1, val);
        }else if(pin.equalsIgnoreCase("d9")){
            analogWrite(2, val);
        }
    }
    public void digitalWrite(String pin, String val) throws Exception {
        if(pin.equalsIgnoreCase("d1")){
            digitalWrite(0, val);
        }else if(pin.equalsIgnoreCase("d5")){
            digitalWrite(1, val);
        }else if(pin.equalsIgnoreCase("d9")){
            digitalWrite(2, val);
        }
    }

    public void digitalWrite(int pin, String val) throws Exception {
        int[] output = new int[3];
        output[0] = 3;

        switch (pin) {
            case 0:
                output[1] = outputPins.d1;
                break;
            case 1:
                output[1] = outputPins.d5;
                break;
            case 2:
                output[1] = outputPins.d9;
                break;
        }
        if (val.equals(mcsHIGH)) {
            output[2] = 1;
        } else {
            output[2] = 0;
        }
        write(output);
    }

    public boolean whenAnalogRead(int pin, char op, int val) {
        if (op == '>') {
            return analogRead(pin) > val;
        } else if (op == '<') {
            return analogRead(pin) < val;
        } else if (op == '=') {
            return analogRead(pin) == val;
        } else {
            return false;
        }
    }

    public boolean whenDigitalRead(int pin, String val) {
        if (val.equalsIgnoreCase("HIGH")) {
            return digitalRead(pin);
        } else {
            return digitalRead(pin) == false;
        }
    }
    public InputVals getInputVals(){
        return inputVals;
    }
    public OutputPins getOutputPins(){
        return outputPins;
    }
    public void resetAll() throws Exception {
//        analogWrite(1, 100);
//        analogWrite(2, 0);
//        digitalWrite(0, mcsLOW);
//        inputVals.a0=0;
//        inputVals.a1=0;
//        inputVals.d0=0;
        
    }
    /**
     * @return the connected
     */
    public synchronized boolean isConnected() {
        return connected;
    }
    
}

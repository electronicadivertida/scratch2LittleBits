/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scratch2littlebits;


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
    public static final int READ_PINS = 1;
    public static final int WRITE_ANALOG = 2;
    public static final int WRITE_DIGITAL = 3;
    public static final int BYTE_CONTROL = 30;
    
    public static final String mcsHIGH="true";
    public static final String mcsLOW="false";
    private String msPuerto;
    private SerialPort serialPort;
    

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
    private int[] pingCmd = new int[]{READ_PINS};
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
                if(msPuerto==null || msPuerto.equals("")){
                    serialPort = new SerialPort(SerialPortList.getPortNames()[0]);
                }else{
                    serialPort = new SerialPort(msPuerto);
                }
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
                           if(event.getEventValue() >= 4){//Check bytes count in the input buffer
                               //Read data, if 10 bytes available 
                               try {
                                   synchronized(this){
                                    byte[] rawData = serialPort.readBytes(4);
                                    if(rawData[0]==BYTE_CONTROL){
                                        processData(rawData, null);
                                    }else if(rawData[1]==BYTE_CONTROL){
                                        byte[] b = serialPort.readBytes(1);
                                        processData(rawData, b);
                                    }else if(rawData[2]==BYTE_CONTROL){
                                        byte[] b = serialPort.readBytes(2);
                                        processData(rawData, b);
                                    }else if(rawData[3]==BYTE_CONTROL){
                                        byte[] b = serialPort.readBytes(3);
                                        processData(rawData, b);
                                    }
                                   }
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

    private void processData(byte[] rawData, byte[] rawData2) {
        byte[] lab=rawData;
        if(rawData2!=null){
            lab = new byte[4];
            
            for(int i = rawData2.length; i <rawData.length; i++){
               lab[i-rawData2.length]=rawData[i]; 
            }
            for(int i = 0; i <rawData2.length; i++){
               lab[i+rawData.length-rawData2.length]=rawData2[i]; 
            }
            
        }
        inputVals.d0 = lab[1] & 0xFF;
        inputVals.a0 = lab[2] & 0xFF;
        inputVals.a1 = lab[3] & 0xFF;
        
    }
    
    private int[] appendBuffer(int[] buffer1, int buffer2) {
        int[] tmp = new int[buffer1.length + 1];
        System.arraycopy(buffer1, 0, tmp, 0, buffer1.length);
        tmp[buffer1.length] = buffer2;
        return tmp;
    }

    public synchronized void write(int[] b) throws Exception {
        byte[] lab = new byte[b.length+1];
        lab[0]=(byte)BYTE_CONTROL;
        for(int i = 0; i < b.length; i++){
            lab[i+1]=(byte)b[i];
//            System.out.print((int)b[i]);
//            System.out.print(" ");
        }
//        System.out.println();
        serialPort.writeBytes(lab);
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

    public int analogRead(String pspin) {
        if(pspin.equalsIgnoreCase("d0")){
            return analogRead(0);
        } else if(pspin.equalsIgnoreCase("a0")){
            return analogRead(1);
        } else {
            return analogRead(2);
        }
    }

    public boolean digitalRead(int pin) {
        if (analogRead(pin) > 0) {
            return true;
        }
        return false;
    }

    public synchronized void analogWrite(int pin, int val) throws Exception {
        int[] output = new int[3];
        output[0] = WRITE_ANALOG;
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
//        System.out.println("analogWrite pin: " + pin + "   valor:" + val);
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

    public synchronized void digitalWrite(int pin, String val) throws Exception {
//        System.out.println("digitalWrite pin: " + pin + "   valor:" + val);
        int[] output = new int[3];
        output[0] = WRITE_DIGITAL;

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

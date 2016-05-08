/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scratch2littlebits;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import jssc.SerialPort;
import jssc.SerialPortException;
import jssc.SerialPortList;
import jssc.SerialPortTimeoutException;

/**
 *
 * @author eduardo
 */
public class ScratchArduino {

    public static final int READ_PINS = 1;
    public static final int WRITE_ANALOG = 2;
    public static final int WRITE_DIGITAL = 3;

    public static final String mcsHIGH = "true";
    public static final String mcsLOW = "false";

    public static final String mcsPuertoEntradaA0 = "a0";
    public static final String mcsPuertoEntradaA1 = "a1";
    public static final String mcsPuertoEntradaD0 = "d0";

    public static final String mcsPuertoSalidaD1 = "d1";
    public static final String mcsPuertoSalidaD5 = "d5";
    public static final String mcsPuertoSalidaD9 = "d9";

    private String msPuerto;
    private SerialPort moSerialPort;
    private boolean mbAbriendo;

    public static class InputVals {

        int d0 = 0;
        boolean d0Externo = false;
        int a0 = 0;
        boolean a0Externo = false;
        int a1 = 0;
        boolean a1Externo = false;
    }

    public static class OutputPins {

        int d1 = 0;
        int d5 = 0;
        int d9 = 0;

    }

    private int[] pingCmd = new int[]{READ_PINS};
    private boolean connected = false;
    private Timer moTimer;
    private long mlTimeDatos;

    private InputVals inputVals = new InputVals();
    private OutputPins outputPins = new OutputPins();

    public ScratchArduino(String PuertoIdent) throws Exception {

        msPuerto = PuertoIdent;
        try{
            openPort();
        }catch(Exception e){
            e.printStackTrace();
        }
        moTimer = new Timer();
        moTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    if ((moSerialPort == null
                            || (mlTimeDatos > 0
                            && (new Date().getTime() - mlTimeDatos) > 3000))
                            && !mbAbriendo) {
                        mlTimeDatos = 0;
                        System.out.println("Comprobar si cerrado ->cerrado");
                        connected = false;
                        closePort();
                        try {
                            Thread.sleep(2000); // let bootloader timeout
                        } catch (InterruptedException e) {}
                        openPort();
                    }

                    try {
                        if (moSerialPort != null) {
                            write(pingCmd);
                            byte[] rawData = readBytes(3);
//                                        System.out.println("rawData: " + String.valueOf(rawData[0]));
                            processData(rawData);
                            connected = true;
                            mlTimeDatos = new Date().getTime();                            
                        }
                    } catch (Throwable e) {
                        e.printStackTrace();
                        Thread.sleep(100);
                    }
                } catch (Throwable ex) {
                    ex.printStackTrace();
                }
            }
        }, 50, 50);
    }

    public synchronized String[] getListaPuertos() {
        return SerialPortList.getPortNames();
    }

    private boolean isPuertoValido(String lasPuertos[]) {
        boolean lbResult = false;
        for (int i = 0; i < lasPuertos.length && lbResult; i++) {
            if (lasPuertos[i].equals(msPuerto)) {
                lbResult = true;
            }
        }
        return lbResult;
    }

    private synchronized byte[] readBytes(int plNumero) throws SerialPortException, SerialPortTimeoutException {
        return moSerialPort.readBytes(plNumero, 100);
    }

    public synchronized void openPort() throws Exception {
        mbAbriendo = true;
        try {
            System.out.println("Abriendo puerto");

            //se comprueba q el puerto cerrado
            if (moSerialPort != null) {
                try {
                    closePort();
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }

            String lasPuertos[] = SerialPortList.getPortNames();
            int lNumeroPuertos = SerialPortList.getPortNames().length;
            boolean lbAbierto = false;
            if (lNumeroPuertos > 0) {
                try {
                    if (msPuerto == null || msPuerto.equals("") || !isPuertoValido(lasPuertos)) {
                        moSerialPort = new SerialPort(SerialPortList.getPortNames()[lNumeroPuertos - 1]);
                    } else {
                        moSerialPort = new SerialPort(msPuerto);
                    }
                    System.out.println(moSerialPort.getPortName());
                    lbAbierto = moSerialPort.openPort();
                    if(!lbAbierto){
                        closePort();
                    }else{
                        moSerialPort.setParams(SerialPort.BAUDRATE_38400,
                                SerialPort.DATABITS_8,
                                SerialPort.STOPBITS_1,
                                SerialPort.PARITY_NONE);
                        moSerialPort.purgePort(SerialPort.PURGE_RXCLEAR );
                        moSerialPort.purgePort(SerialPort.PURGE_TXCLEAR);
                        
        //                int mask = SerialPort.MASK_RXCHAR + SerialPort.MASK_CTS + SerialPort.MASK_DSR;//Prepare mask
        //                moSerialPort.setEventsMask(mask);//Set mask
//
//                        moSerialPort.addEventListener(new SerialPortEventListener() {
//                            @Override
//                            public void serialEvent(SerialPortEvent event) {
//                                try {
//                                    if (event.isRXCHAR()) {//If data is available
//                                        System.out.print("Datos ");
//                                        connected = true;
//                                        mlTimeDatos = new Date().getTime();
//                                        if (event.getEventValue() >= 3) {//Check bytes count in the input buffer
//                                            try {
//                                                byte[] rawData = readBytes(3);
//                                                //                                        System.out.println("rawData: " + String.valueOf(rawData[0]));
//                                                processData(rawData);
//                                            } catch (SerialPortException ex) {
//                                                System.out.println(ex);
//                                            }
//                                        }
//                                    } else if (event.isCTS()) {//If CTS line has changed state
//                                        mlTimeDatos = new Date().getTime();
//                                        if (event.getEventValue() == 1) {//If line is ON
//                                            System.out.println("CTS - ON");
//                                        } else {
//                                            //                               closePort();
//                                            System.out.println("CTS - OFF");
//                                        }
//                                    } else if (event.isDSR()) {///If DSR line has changed state
//                                        //                           closePort();
//                                        mlTimeDatos = new Date().getTime();
//                                        if (event.getEventValue() == 1) {//If line is ON
//                                            System.out.println("DSR - ON");
//                                        } else {
//                                            System.out.println("DSR - OFF");
//                                        }
//                                    }
//                                } catch (Throwable err) {
//                                    err.printStackTrace();
//                                }
//                            }
//                        }, SerialPort.MASK_RXCHAR + SerialPort.MASK_CTS + SerialPort.MASK_DSR      );//
                        try {
                            Thread.sleep(1000); // let bootloader timeout
                        } catch (InterruptedException e) {}
                        mlTimeDatos = new Date().getTime();                        
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }

            }
        } finally {
            mbAbriendo = false;
            System.out.println("Abriendo puerto-fin");
        }
    }

    public String getPuerto() {
        return msPuerto;
    }

    public synchronized void setPuerto(String psPuerto) throws Exception {
        msPuerto = psPuerto;
        closePort();
    }

    public synchronized void closePort() {
        connected = false;
        if (moSerialPort != null) {
            System.out.println("Cerramos puerto");
            SerialPort loAux = moSerialPort;
            moSerialPort = null;
            try {
                loAux.removeEventListener();
            } catch (SerialPortException ex) {
                ex.printStackTrace();

            }
            try {
                loAux.closePort();
            } catch (SerialPortException ex) {
                ex.printStackTrace();

            }
        }
    }

    public synchronized void close() throws Exception {
        closePort();
        if (moTimer != null) {
            moTimer.cancel();
            moTimer = null;
        }
    }

    private void processData(byte[] rawData) {
        byte[] lab = rawData;
        if (!inputVals.d0Externo) {
            inputVals.d0 = (int) (((lab[0] & 0xFF) * 100.0));//digital 0 o 1 -> 0 o 100
        }
        if (!inputVals.a0Externo) {
            inputVals.a0 = (int) (((lab[1] & 0xFF) * 100.0) / 255.0);
        }
        if (!inputVals.a1Externo) {
            inputVals.a1 = (int) (((lab[2] & 0xFF) * 100.0) / 255.0);
        }
//        System.out.println("d0: " + String.valueOf(inputVals.d0));
//        System.out.println("a0: " + String.valueOf(inputVals.a0));
//        System.out.println("a1: " + String.valueOf(inputVals.a1));
    }

//    private int[] appendBuffer(int[] buffer1, int buffer2) {
//        int[] tmp = new int[buffer1.length + 1];
//        System.arraycopy(buffer1, 0, tmp, 0, buffer1.length);
//        tmp[buffer1.length] = buffer2;
//        return tmp;
//    }
    public synchronized void write(int[] b) throws Exception {
        byte[] lab = new byte[b.length];
        for (int i = 0; i < b.length; i++) {
            lab[i] = (byte) b[i];
//            System.out.print((int)b[i]);
//            System.out.print(" ");
        }
//        System.out.println();
        moSerialPort.writeBytes(lab);
    }

//    public int analogRead(int pin) {
//        int lResult = 0;
//        switch (pin) {
//            case 0:
//                lResult = inputVals.d0;
//                break;
//            case 1:
//                lResult = inputVals.a0;
//                break;
//            case 2:
//                lResult = inputVals.a1;
//                break;
//        }
//        return lResult;
//    }
    public int analogRead(String pspin) {
        if (pspin.equalsIgnoreCase(mcsPuertoEntradaD0)) {
            return inputVals.d0;
        } else if (pspin.equalsIgnoreCase(mcsPuertoEntradaA0)) {
            return inputVals.a0;
        } else if (pspin.equalsIgnoreCase(mcsPuertoEntradaA1)) {
            return inputVals.a1;
        }
        return 0;
    }
//
//    public boolean digitalRead(int pin) {
//        return (analogRead(pin) > 0);
//    }

    public boolean digitalRead(String pspin) {
        return analogRead(pspin) > 0;
    }

    private synchronized void analogWrite(int pin, int val) throws Exception {
        int[] output = new int[3];
        output[0] = WRITE_ANALOG;
        switch (pin) {
            case 1:
                output[1] = 1;//d1
                outputPins.d1 = val;
                break;
            case 5:
                output[1] = 5;//d5
                outputPins.d5 = val;
                break;
            case 9:
                output[1] = 9;//d9
                outputPins.d9 = val;
                break;
        }
        if (val > 100) {
            val = 100;
        }
        if (val < 0) {
            val = 0;
        }
        output[2] = (int) ((val * 255.0) / 100.0);
        write(output);
    }

    private int getValorDeDigital(String val) {
        int ll = 0;
        if (val.equals(mcsHIGH)) {
            ll = 100;
        } else {
            try {
                if (Double.valueOf(val).intValue() > 0) {
                    ll = 100;
                } else {
                    ll = 0;
                }
            } catch (Throwable e) {
                ll = 0;
            }
        }
        return ll;
    }

    private synchronized void digitalWrite(int pin, String val) throws Exception {
//        System.out.println("digitalWrite pin: " + pin + "   valor:" + val);
        int[] output = new int[3];
        output[0] = WRITE_DIGITAL;
        int ll = getValorDeDigital(val);
        switch (pin) {
            case 1:
                output[1] = 1;//d1
                outputPins.d1 = ll;
                break;
            case 5:
                output[1] = 5;//d5
                outputPins.d5 = ll;
                break;
            case 9:
                output[1] = 9;//d9
                outputPins.d9 = ll;
                break;
        }
        if (ll > 0) {
            output[2] = 1;
        } else {
            output[2] = 0;
        }
        write(output);
    }

    public void analogWrite(String pin, int val) throws Exception {
//        System.out.println("analogWrite pin: " + pin + "   valor:" + val);
        if (pin.equalsIgnoreCase(mcsPuertoSalidaD1)) {
            analogWrite(1, val);
        } else if (pin.equalsIgnoreCase(mcsPuertoSalidaD5)) {
            analogWrite(5, val);
        } else if (pin.equalsIgnoreCase(mcsPuertoSalidaD9)) {
            analogWrite(9, val);
        }
    }

    public void digitalWrite(String pin, String val) throws Exception {
        if (pin.equalsIgnoreCase(mcsPuertoSalidaD1)) {
            digitalWrite(1, val);
        } else if (pin.equalsIgnoreCase(mcsPuertoSalidaD5)) {
            digitalWrite(5, val);
        } else if (pin.equalsIgnoreCase(mcsPuertoSalidaD9)) {
            digitalWrite(9, val);
        }
    }

    public void setDigitalReadExterno(String pin, String psValor) {
        int val = 0;
        if (psValor.equals(mcsHIGH)) {
            val = 100;
        }
        if (pin.equalsIgnoreCase(mcsPuertoEntradaD0)) {
            inputVals.d0 = val;
            inputVals.d0Externo = true;
        } else if (pin.equalsIgnoreCase(mcsPuertoEntradaA0)) {
            inputVals.a0 = val;
            inputVals.a0Externo = true;
        } else if (pin.equalsIgnoreCase(mcsPuertoEntradaA1)) {
            inputVals.a1 = val;
            inputVals.a1Externo = true;
        }
    }

    public void setAnalogReadExterno(String pin, int val) {
        if (pin.equalsIgnoreCase(mcsPuertoEntradaD0)) {
            inputVals.d0 = val;
            inputVals.d0Externo = true;
        } else if (pin.equalsIgnoreCase(mcsPuertoEntradaA0)) {
            inputVals.a0 = val;
            inputVals.a0Externo = true;
        } else if (pin.equalsIgnoreCase(mcsPuertoEntradaA1)) {
            inputVals.a1 = val;
            inputVals.a1Externo = true;
        }
    }

    public void setReadExternoNormal(String pin) {
        if (pin.equalsIgnoreCase(mcsPuertoEntradaD0)) {
            inputVals.d0Externo = false;
        } else if (pin.equalsIgnoreCase(mcsPuertoEntradaA0)) {
            inputVals.a0Externo = false;
        } else if (pin.equalsIgnoreCase(mcsPuertoEntradaA1)) {
            inputVals.a1Externo = false;
        }
    }
//    
//
//    public boolean whenAnalogRead(int pin, char op, int val) {
//        if (op == '>') {
//            return analogRead(pin) > val;
//        } else if (op == '<') {
//            return analogRead(pin) < val;
//        } else if (op == '=') {
//            return analogRead(pin) == val;
//        } else {
//            return false;
//        }
//    }
//
//    public boolean whenDigitalRead(int pin, String val) {
//        if (val.equalsIgnoreCase("HIGH")) {
//            return digitalRead(pin);
//        } else {
//            return digitalRead(pin) == false;
//        }
//    }

    public InputVals getInputVals() {
        return inputVals;
    }

    public OutputPins getOutputPins() {
        return outputPins;
    }

    public void resetAll() throws Exception {
//        analogWrite(1, 0);
//        analogWrite(2, 0);
//        digitalWrite(0, mcsLOW);
        inputVals.a0 = 0;
        inputVals.a0Externo = false;
        inputVals.a1 = 0;
        inputVals.a1Externo = false;
        inputVals.d0 = 0;
        inputVals.d0Externo = false;

    }

    /**
     * @return the connected
     */
    public synchronized boolean isConnected() {
        return connected;
    }

}

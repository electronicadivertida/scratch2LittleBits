/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package scratch2littlebits;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.Date;

/**
 *
 * @author eduardo
 */
public class ScratchHttp implements Runnable{
    private static final int PORT = 8099; // Número de puerto, debe coincidir con archivo scratch2LittleBitsDEF.json
    private InputStream sockIn;
    private OutputStream sockOut;
    private final Scratch2LittleBits moControl;
    private boolean mbconectado=false;
    int mlContador;
    Date moDate = new Date();
//    private String msWhenDigital="";
//    private String msWhenAnalog="";
//    

    public ScratchHttp(Scratch2LittleBits poControl){
        moControl=poControl;
    }
    
    @Override
    public void run() {

        try {
            InetAddress addr = InetAddress.getLocalHost();
            System.out.println("HTTPExtensionExample helper app started on " + addr.toString());
            
            ServerSocket serverSock = new ServerSocket(PORT);
            while (true) {
                Socket sock = serverSock.accept();
                mbconectado=true;
                sockIn = sock.getInputStream();
                sockOut = sock.getOutputStream();
                try {
                    handleRequest();
                } catch (Throwable e) {
                    mbconectado=false;
                    e.printStackTrace();
                    try {
                        sendResponse("_problem "+e.toString());
                    } catch (Throwable e1) {}
                }
                sock.close();
            }
        } catch (Throwable  ex) {
            ex.printStackTrace();
        }
    }

    private  void handleRequest() throws Throwable {
        String httpBuf = "";
        int i;

// read data until the first HTTP header line is complete (i.e. a '\n' is seen)
        while ((i = httpBuf.indexOf('\n')) < 0) {
            byte[] buf = new byte[5000];
            int bytes_read = sockIn.read(buf, 0, buf.length);
            if (bytes_read < 0) {
                System.out.println("Socket closed; no HTTP header.");
                return;
            }
            httpBuf += new String(Arrays.copyOf(buf, bytes_read));
        }

        String header = httpBuf.substring(0, i);
        if (header.indexOf("GET ") != 0) {
            System.out.println("Este servidor solo acepta conexiones HTTP GET");
            return;
        }
        i = header.indexOf("HTTP/1");
        if (i < 0) {
            System.out.println("Cabezera HTTP GET incorracta.");
            return;
        }
        header = header.substring(5, i - 1);
        if (header.equals("favicon.ico")) {
            return; // igore browser favicon.ico requests
        } else if (header.equals("crossdomain.xml")) {
            sendPolicyFile();
        } else if (header.length() == 0) {
            doHelp();
        } else {
            doCommand(header);
        }
    }

    private  void sendPolicyFile() throws IOException {
// Send a Flash null-teriminated cross-domain policy file.
        String policyFile
                = "<cross-domain-policy>\n"
                + " <allow-access-from domain=\"*\" to-ports=\"" + PORT + "\"/>\n"
                + "</cross-domain-policy>\n\0";
        sendResponse(policyFile);
    }

    private  void sendResponse(String s) throws IOException {
        String crlf = "\r\n";
        String httpResponse="";
        httpResponse = "HTTP/1.1 200 OK" + crlf;
        httpResponse += "Content-Type: text/html; charset=ISO-8859-1" + crlf;
        httpResponse += "Access-Control-Allow-Origin: *" + crlf;
        httpResponse += crlf;
        httpResponse += s + crlf;
        byte[] outBuf = httpResponse.getBytes();
        sockOut.write(outBuf, 0, outBuf.length);
        
    }

    private void doHelp() throws IOException {
// Optional: return a list of commands understood by this server
        String help = "Servidor HTTP Extension LittleBits<br><br>";
        sendResponse(help);
    }

    private void doCommand(String header) throws Throwable {
//        mlContador++;
//        Date loDate = new Date();
//        System.out.println(String.valueOf(mlContador) + " " + String.valueOf(moDate.getTime() - loDate.getTime()));
//        moDate = loDate;
            if(moControl.getArduino()!=null && moControl.getArduino().isConnected()){
                if(header.equalsIgnoreCase("poll")){
                    sendResponse(""
                            + "analogRead/a0 " + String.valueOf((int)moControl.getArduino().analogRead(ScratchArduino.mcsPuertoEntradaA0) ) + Character.toString((char)10)
                            + "analogRead/a1 " + String.valueOf((int)moControl.getArduino().analogRead(ScratchArduino.mcsPuertoEntradaA1)) + Character.toString((char)10)
                            + "digitalRead/d0 " + (moControl.getArduino().digitalRead(ScratchArduino.mcsPuertoEntradaD0)?"true":"false") + Character.toString((char)10)
//                            + msWhenDigital
//                            + msWhenAnalog
                    );
//                    if(moControl.getArduino().getInputVals().d0!=0){
//                        String ls=String.valueOf(moControl.getArduino().getInputVals().d0);
//                        System.out.println(ls);
//                    }
                }else if(header.equalsIgnoreCase("reset_all")){
                    moControl.getArduino().resetAll();
                    sendResponse("ok");
                }else {
                    String[] las = header.split("/");
                    if(las[0].equalsIgnoreCase("digitalWrite")){
                        moControl.getArduino().digitalWrite(las[1], las[2]);
                    }else if(las[0].equalsIgnoreCase("analogWrite")){
                        moControl.getArduino().analogWrite(las[1], Double.valueOf(las[2]).intValue());
//                    }else if(las[0].equalsIgnoreCase("whenDigitalRead")){
//                        //no va
//                        msWhenDigital=header + " " + (moControl.getArduino().getInputVals().d0!=0?"true":"false") +"\n";// + Character.toString((char)10);
////                        msWhenDigital="_result" + " " + (moControl.getArduino().getInputVals().d0!=0?"true":"false") +"\n";// + Character.toString((char)10);
//                        sendResponse(msWhenDigital);
//                    }else if(las[0].equalsIgnoreCase("whenAnalogRead")){
//                        //no va
//                        char op = las[2].charAt(0);
//                        if( "%3D".equalsIgnoreCase(las[2]) ){
//                            op = '=';
//                        } else if( "%3C".equalsIgnoreCase(las[2]) ){
//                            op = '<';
//                        } else if( "%3E".equalsIgnoreCase(las[2]) ){
//                            op = '>';
//                        }
//                        double val = Double.valueOf(las[3]).doubleValue();
//                        boolean lbR = false;    
//                        if (op == '>'){
//                          lbR = moControl.getArduino().analogRead(las[1]) > val;
//                        }else if (op == '<'){
//                          lbR = moControl.getArduino().analogRead(las[1]) < val;
//                        }else if (op == '='){
//                          lbR = moControl.getArduino().analogRead(las[1]) == val;
//                        }
//                        
//                        msWhenAnalog=header + " " + (lbR?"true":"false")  + Character.toString((char)10);
//                        //"whenAnalogRead/"+las[1] + "/"+ op + "/" + las[3]
//                        sendResponse(msWhenAnalog);
                    } else{
                        System.out.println("Comando no procesado: " + header);
                    }
                    sendResponse("ok");
                }
            }
     
    }

    /**
     * @return the mbconectado
     */
    public boolean isConectado() {
        return mbconectado;
    }
}

package com.electDiver.littleBits.scratch2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	private CheckBox chkd0;
	private SeekBar seekBara0;
	private SeekBar seekBara1;
	
	private CheckBox chkd0Bit;
	private CheckBox chka1Bit;
	private CheckBox chka0Bit;
	

	private CheckBox chkd1;
	private SeekBar seekBard5;
	private SeekBar seekBard9;
	
	private CheckBox chkd1Bit;
	private CheckBox chkd5Bit;
	private CheckBox chkd9Bit;
	private EditText txtServidor;
	private TextView textViewEstado;
	private Button btnConectar;
	
	private ScratchDatos moDatos;
	private ConnectedThread moConnectar;
	private boolean mbPrimeraVez=true;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		moDatos = new ScratchDatos(this);
		moConnectar = new ConnectedThread("");
		
		chkd0 = (CheckBox) findViewById(R.id.chkd0);
		seekBara0 = (SeekBar) findViewById(R.id.seekBara0);
		seekBara1 = (SeekBar) findViewById(R.id.seekBara1);

		chkd1 = (CheckBox) findViewById(R.id.chkd1);
		seekBard5 = (SeekBar) findViewById(R.id.seekBard5);
		seekBard9 = (SeekBar) findViewById(R.id.seekBard9);

		chkd0Bit = (CheckBox) findViewById(R.id.chkd0Bit);
		chka0Bit = (CheckBox) findViewById(R.id.chka0Bit);
		chka1Bit = (CheckBox) findViewById(R.id.chka1Bit);
		
		chkd1Bit = (CheckBox) findViewById(R.id.chkd1Bit);
		chkd5Bit = (CheckBox) findViewById(R.id.chkd5Bit);
		chkd9Bit = (CheckBox) findViewById(R.id.chkd9Bit);
		
		txtServidor = (EditText) findViewById(R.id.txtServidor);
		btnConectar = (Button) findViewById(R.id.btnConectar);
		textViewEstado = (TextView) findViewById(R.id.textViewEstado);
		
		txtServidor.setText("172.16.0.4");
		
		chkd0Bit.setChecked(true);
		chka0Bit.setChecked(true);
		chka1Bit.setChecked(true);
		chkd1Bit.setChecked(true);
		chkd5Bit.setChecked(true);
		chkd9Bit.setChecked(true);
		
		btnConectar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                	conectarSiProcede();
                } catch (Throwable e) {
                	e.printStackTrace();
                }
            }
        });
		
		chkd0Bit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
            	moDatos.setD0Externo(!arg1); 
            	if(arg1){
            		enviarReadNormal(ScratchDatos.mcsPuertoEntradaD0);
            	}
            }
        });
		chka0Bit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
            	moDatos.setA0Externo(!arg1); 
            	if(arg1){
            		enviarReadNormal(ScratchDatos.mcsPuertoEntradaA0);
            	}
            }
        });
		chka1Bit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
            	moDatos.setA1Externo(!arg1); 
            	if(arg1){
            		enviarReadNormal(ScratchDatos.mcsPuertoEntradaA1);
            	}
            }
        });

		chkd1Bit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
            	moDatos.setD1Externo(!arg1); 
            }
        });
		chkd5Bit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
            	moDatos.setD5Externo(!arg1); 
            }
        });
		chkd9Bit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
            	moDatos.setD9Externo(!arg1); 
            }
        });
		
		chkd0.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
            	if(moDatos.isD0Externo()){
            		enviarReadValor(ScratchDatos.mcsPuertoEntradaD0, chkd0.isChecked()? 100:0);
            	}
            }

        });
		seekBara0.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {       
		    @Override       
		    public void onStopTrackingTouch(SeekBar seekBar) {      }       
		    @Override       
		    public void onStartTrackingTouch(SeekBar seekBar) {     }       
		    @Override       
		    public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
		    	if(fromUser){
		    		chka0Bit.setChecked(false);
		    		moDatos.setA0Externo(true);
		    	}
		    	if(moDatos.isA0Externo() && fromUser){
            		enviarReadValor(ScratchDatos.mcsPuertoEntradaA0, progress);
            	}
		    }       
		});
		seekBara1.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {       
		    @Override       
		    public void onStopTrackingTouch(SeekBar seekBar) {      }       
		    @Override       
		    public void onStartTrackingTouch(SeekBar seekBar) {     }       
		    @Override       
		    public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {     
		    	if(fromUser){
		    		chka1Bit.setChecked(false);
		    		moDatos.setA1Externo(true);
		    	}
		    	if(moDatos.isA1Externo() && fromUser){
            		enviarReadValor(ScratchDatos.mcsPuertoEntradaA1, progress);
            	}
		    }       
		});
		chkd1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
            	if(moDatos.isD1Externo()){
            		enviarDigitalWriteValor(ScratchDatos.mcsPuertoSalidaD1, chkd1.isChecked()? 100:0);
            	}
            }
        });
		seekBard5.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {       
		    @Override       
		    public void onStopTrackingTouch(SeekBar seekBar) {      }       
		    @Override       
		    public void onStartTrackingTouch(SeekBar seekBar) {     }       
		    @Override       
		    public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {     
		    	if(fromUser){
		    		chkd5Bit.setChecked(false);
		    		moDatos.setD5Externo(true);
		    	}
		    	if(moDatos.isD5Externo() && fromUser){
		    		enviarAnalogWriteValor(ScratchDatos.mcsPuertoSalidaD5, progress);
            	}
		    }       
		});
		seekBard9.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {       
		    @Override       
		    public void onStopTrackingTouch(SeekBar seekBar) {      }       
		    @Override       
		    public void onStartTrackingTouch(SeekBar seekBar) {     }       
		    @Override       
		    public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {     
		    	if(fromUser){
		    		chkd9Bit.setChecked(false);
		    		moDatos.setD9Externo(true);
		    	}
		    	if(moDatos.isD9Externo() && fromUser){
		    		enviarAnalogWriteValor(ScratchDatos.mcsPuertoSalidaD9, progress);
            	}
		    }       
		});
		
		
		seekBara0.setMax(0);
		seekBara0.setMax(100);
		seekBara1.setMax(0);
		seekBara1.setMax(100);
		seekBard5.setMax(0);
		seekBard5.setMax(100);
		seekBard9.setMax(0);
		seekBard9.setMax(100);
		

	}
	
	private void conectarSiProcede(){
		moConnectar.setIP(txtServidor.getText().toString());
    	if(mbPrimeraVez){
        	mbPrimeraVez=false;
        	moConnectar.start();
    	}
	}

	private void enviarReadNormal(String psPuerto) {
		moDatos.setExterno(psPuerto, false);
		conectarSiProcede();
		moConnectar.enviar("readNormal", psPuerto, "");
		

	}


	private void enviarReadValor(String psPuerto, int plValor) {

		conectarSiProcede();
		moDatos.setDigitalReadExterno(psPuerto, String.valueOf(plValor));
		moConnectar.enviar("analogRead", psPuerto, String.valueOf(plValor));
		

	}

	private void enviarAnalogWriteValor(String psPuerto, int plValor) {

		conectarSiProcede();
		moDatos.analogWrite(psPuerto, plValor);
		moConnectar.enviar("analogWrite", psPuerto, String.valueOf(plValor));
		

	}
	private void enviarDigitalWriteValor(String psPuerto, int plValor) {

		conectarSiProcede();
		moDatos.digitalWrite(psPuerto, String.valueOf(plValor));
		moConnectar.enviar("digitalWrite", psPuerto, String.valueOf(plValor));
		

	}
	public void propertyChange(final String psPuerto, final int plValor, final boolean pbCambiado) {
		runOnUiThread(new Runnable() {
            public void run() {
//                Toast.makeText(MainActivity.this, "read " + psPuerto + "; valor " + String.valueOf(plValor), Toast.LENGTH_SHORT).show();
                if(psPuerto.equalsIgnoreCase(ScratchDatos.mcsPuertoEntradaD0)){
                	MainActivity.this.chkd0.setChecked(plValor>0);
                }
                if(psPuerto.equalsIgnoreCase(ScratchDatos.mcsPuertoEntradaA0)){
                	MainActivity.this.seekBara0.setProgress(plValor);
                }
                if(psPuerto.equalsIgnoreCase(ScratchDatos.mcsPuertoEntradaA1)){
                	MainActivity.this.seekBara1.setProgress(plValor);
                }
                if(psPuerto.equalsIgnoreCase(ScratchDatos.mcsPuertoSalidaD1)){
                	MainActivity.this.chkd1.setChecked(plValor>0);
                }
                if(psPuerto.equalsIgnoreCase(ScratchDatos.mcsPuertoSalidaD5)){
                	MainActivity.this.seekBard5.setProgress(plValor);
                }
                if(psPuerto.equalsIgnoreCase(ScratchDatos.mcsPuertoSalidaD9)){
                	MainActivity.this.seekBard9.setProgress(plValor);
                }
                if(psPuerto.equalsIgnoreCase("mensaje")){
                	MainActivity.this.textViewEstado.setText(moConnectar.getMensaje());
                }
            }
        });
	}	

    private class ConnectedThread extends Thread {

        private static final int PORT = 8100; // Número de puerto, debe coincidir con archivo scratch2LittleBitsDEF.json
        private boolean mbconectado=false;
        private String msMensaje="";
		private String msIP;
        
        public ConnectedThread(String psIP) {
        	msIP=psIP;
        }

        public void setIP(String psIP){
        	msIP=psIP;
        }

        
        public void enviar(final String psAccion, final String psPuerto, final String psValor) {
            new Thread(new Runnable(){

				@Override
				public void run() {
		            InputStream sockIn;
		            OutputStream sockOut;
					try {
			    		Socket loSocket = new Socket(msIP, PORT);
			    		
			        	sockOut = loSocket.getOutputStream();
			            sockIn = loSocket.getInputStream();
			
			            sockOut.write(("GET  " +  psAccion + "/" + psPuerto + "/" + psValor+" HTTP/1\n").getBytes());
			            sockOut.flush();
			            
			            BufferedReader in = new BufferedReader(new InputStreamReader(sockIn));
			            
			            String inputLine;
			            while ((inputLine = in.readLine()) != null) {
			            	
			            }
			            in.close();
			            sockIn.close();
			            sockOut.close();
			            setConectado(true);
			            setMensaje(psAccion + "/" + psPuerto + "/" + psValor);
				    } catch (Throwable e) {
				    	setMensaje(e.getMessage());
				        setConectado(false);
				        e.printStackTrace();
				    }        
				}
            }).start();
                
        }
        
        public void run() {
            InputStream sockIn;
            OutputStream sockOut;

            while (true) {
                	
            	try {
            		Thread.sleep(1000);
                } catch (Throwable  ex) {
                    ex.printStackTrace();
                }
            	if(msIP!=null && !msIP.equals("")){
            	try {
            		Socket loSocket = new Socket(msIP, PORT);
            		
                	sockOut = loSocket.getOutputStream();
                    sockIn = loSocket.getInputStream();

                    sockOut.write("GET  poll HTTP/1\n".getBytes());
                    sockOut.flush();
                    
                    BufferedReader in = new BufferedReader(new InputStreamReader(sockIn));
                    
                    String inputLine;
                    while ((inputLine = in.readLine()) != null) {
                    	String[] las = inputLine.split(" ");
                    	if("analogRead/a0".equalsIgnoreCase(las[0])){
                			moDatos.setA0(Double.valueOf(las[1]).intValue());
                    	}
                    	if("analogRead/a1".equalsIgnoreCase(las[0])){
                			moDatos.setA1(Double.valueOf(las[1]).intValue());
                    	}
                    	if("digitalRead/d0".equalsIgnoreCase(las[0])){                    		
                    		if(las[1].equalsIgnoreCase(ScratchDatos.mcsHIGH)){
                    			moDatos.setD0(100);
                    		}else if(las[1].equalsIgnoreCase(ScratchDatos.mcsLOW)){
                    			moDatos.setD0(0);
                    		}else{
                    			moDatos.setD0(Double.valueOf(las[1]).intValue());
                    		}
                    	}
                    	if("analogWrite/d5".equalsIgnoreCase(las[0])){
                    		moDatos.setD5(Double.valueOf(las[1]).intValue());
                    	}
                    	if("analogWrite/d9".equalsIgnoreCase(las[0])){
                			moDatos.setD9(Double.valueOf(las[1]).intValue());
                    	}
                    	if("digitalWrite/d1".equalsIgnoreCase(las[0])){
                    		if(las[1].equalsIgnoreCase(ScratchDatos.mcsHIGH)){
                    			moDatos.setD1(100);
                    		}else if(las[1].equalsIgnoreCase(ScratchDatos.mcsLOW)){
                    			moDatos.setD1(0);
                    		}else{
                    			moDatos.setD1(Double.valueOf(las[1]).intValue());
                    		}
                    	}
                    }
                    in.close();
                    sockIn.close();
                    sockOut.close();
                    setConectado(true);
                    setMensaje("pool");
                } catch (Throwable e) {
                	setMensaje(e.getMessage());
                    setConectado(false);
                    e.printStackTrace();
                }
            	}
                
            }
        }
        /**
         * @return the mbconectado
         */
        public boolean isConectado() {
            return mbconectado;
        }

		private void setConectado(boolean mbconectado) {
			this.mbconectado = mbconectado;
		}

		private String getMensaje() {
			return msMensaje;
		}

		private void setMensaje(String msMensaje) {
			this.msMensaje = msMensaje;
			MainActivity.this.propertyChange("mensaje", 0, true);
		}
    }



}

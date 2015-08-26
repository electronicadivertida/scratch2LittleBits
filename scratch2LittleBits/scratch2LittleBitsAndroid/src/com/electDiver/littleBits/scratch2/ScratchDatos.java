package com.electDiver.littleBits.scratch2;


public class ScratchDatos {

    
    public static final String mcsHIGH="true";
    public static final String mcsLOW="false";

    public static final String mcsPuertoEntradaA0="a0";
    public static final String mcsPuertoEntradaA1="a1";
    public static final String mcsPuertoEntradaD0="d0";
        
    public static final String mcsPuertoSalidaD1="d1";
    public static final String mcsPuertoSalidaD5="d5";
    public static final String mcsPuertoSalidaD9="d9";



    private int d0 = 0;
    private boolean d0Externo = false;
    private int a0 = 0;
    private boolean a0Externo = false;
    private int a1 = 0;
    private boolean a1Externo = false;
    private int d1 = 0;
    private boolean d1Externo = false;
    private int d5 = 0;
    private boolean d5Externo = false;
    private int d9 = 0;
    private boolean d9Externo = false;
    
    

	private MainActivity moActividad;
    
    public ScratchDatos(MainActivity poActividad){
    	moActividad=poActividad;
    }
	int getD0() {
		return d0;
	}
	int getA0() {
		return a0;
	}
	int getA1() {
		return a1;
	}
	boolean isD0Externo() {
		return d0Externo;
	}
	void setD0Externo(boolean d0Externo) {
		this.d0Externo = d0Externo;
	}
	boolean isA0Externo() {
		return a0Externo;
	}
	void setA0Externo(boolean a0Externo) {
		this.a0Externo = a0Externo;
	}
	boolean isA1Externo() {
		return a1Externo;
	}
	void setA1Externo(boolean a1Externo) {
		this.a1Externo = a1Externo;
	}
	void setExterno(String psPuerto, boolean pb){
		if(psPuerto.equalsIgnoreCase(mcsPuertoEntradaA0)){
			setA0Externo(pb);
		} else if(psPuerto.equalsIgnoreCase(mcsPuertoEntradaA1)){
			setA1Externo(pb);
		} else if(psPuerto.equalsIgnoreCase(mcsPuertoEntradaD0)){
			setD0Externo(pb);
		} else if(psPuerto.equalsIgnoreCase(mcsPuertoSalidaD1)){
			setD1Externo(pb);
		} else if(psPuerto.equalsIgnoreCase(mcsPuertoSalidaD5)){
			setD5Externo(pb);
		} else if(psPuerto.equalsIgnoreCase(mcsPuertoSalidaD9)){
			setD9Externo(pb);
		}
	}
	int getD1() {
		return d1;
	}
	int getD5() {
		return d5;
	}
	int getD9() {
		return d9;
	}
	boolean isD1Externo() {
		return d1Externo;
	}
	void setD1Externo(boolean d1Externo) {
		this.d1Externo = d1Externo;
	}
	boolean isD5Externo() {
		return d5Externo;
	}
	void setD5Externo(boolean d5Externo) {
		this.d5Externo = d5Externo;
	}
	boolean isD9Externo() {
		return d9Externo;
	}
	void setD9Externo(boolean d9Externo) {
		this.d9Externo = d9Externo;
	}

	void setD0(int plValor) {
		if(!d0Externo){
			int lAux = this.d0;
			this.d0 = plValor;
			moActividad.propertyChange(mcsPuertoEntradaD0, this.d0, lAux!=plValor);
		}
	}
	void setA0(int plValor) {
		if(!a0Externo){
			int lAux = this.a0;
			this.a0 = plValor;
			moActividad.propertyChange(mcsPuertoEntradaA0, this.a0, lAux!=plValor);
		}
	}
	void setA1(int plValor) {
		if(!a1Externo){
			int lAux = this.a1;
			this.a1 = plValor;
			moActividad.propertyChange(mcsPuertoEntradaA1, this.a1, lAux!=plValor);
		}
	}
	void setD1(int plValor) {
		if(!d1Externo){
			int lAux = this.d1;
			this.d1 = plValor;
			moActividad.propertyChange(mcsPuertoSalidaD1, this.d1, lAux!=plValor);

		}
	}
	void setD5(int plValor) {
		if(!d5Externo){
			int lAux = this.d5;
			this.d5 = plValor;
			moActividad.propertyChange(mcsPuertoSalidaD5, this.d5, lAux!=plValor);
		}
	}
	void setD9(int plValor) {
		if(!d9Externo){
			int lAux = this.d9;
			this.d9 = plValor;
			moActividad.propertyChange(mcsPuertoSalidaD9, this.d9, lAux!=plValor);

		}
	}

	void setD0Android(int plValor) {
		this.d0 = plValor;
		this.d0Externo=true;
	}
	void setA0Android(int plValor) {
		this.a0 = plValor;
		this.a0Externo=true;
	}
	void setA1Android(int plValor) {
		this.a1 = plValor;
		this.a1Externo=true;
	}
	void setD1Android(int plValor) {
		this.d1 = plValor;
		this.d1Externo=true;
	}
	void setD5Android(int plValor) {
		this.d5 = plValor;
		this.d5Externo=true;
	}
	void setD9Android(int plValor) {
		this.d9 = plValor;
		this.d9Externo=true;
	}
//
//
//    public int analogRead(String pspin) {
//        if(pspin.equalsIgnoreCase(mcsPuertoEntradaD0)){
//            return getD0();
//        } else if(pspin.equalsIgnoreCase(mcsPuertoEntradaA0)){
//            return getA0();
//        } else if(pspin.equalsIgnoreCase(mcsPuertoEntradaA1)){
//            return getA1();
//        }
//        return 0;
//    }
//
//    public boolean digitalRead(String pspin) {
//        return analogRead(pspin)>0;
//    }

    public void analogWrite(String pin, int val)  {
//        System.out.println("analogWrite pin: " + pin + "   valor:" + val);
        if(pin.equalsIgnoreCase(mcsPuertoSalidaD1)){
        	setD1Android(val);
        }else if(pin.equalsIgnoreCase(mcsPuertoSalidaD5)){
        	setD5Android(val);
        }else if(pin.equalsIgnoreCase(mcsPuertoSalidaD9)){
        	setD9Android(val);
        }
    }
    public void digitalWrite(String pin, String val) {
    	analogWrite(pin, getValorDeDigital(val));
    }
    public void setDigitalReadExterno(String pin, String psValor) {
        int val = 0;
        if (psValor.equals(mcsHIGH)) {
            val = 100;
        }
        if(pin.equalsIgnoreCase(mcsPuertoEntradaD0)){
            setD0Android(val);
            setD0Externo(true);
        }else if(pin.equalsIgnoreCase(mcsPuertoEntradaA0)){
            setA0Android(val);
            setA0Externo(true);
        }else if(pin.equalsIgnoreCase(mcsPuertoEntradaA1)){
            setA1Android(val);
            setA1Externo(true);
        }
    }

    public void setAnalogReadExterno(String pin, int val) {
        if(pin.equalsIgnoreCase(mcsPuertoEntradaD0)){
            setD0(val);
            setD0Externo(true);
        }else if(pin.equalsIgnoreCase(mcsPuertoEntradaA0)){
            setA0(val);
            setA0Externo(true);
        }else if(pin.equalsIgnoreCase(mcsPuertoEntradaA1)){
            setA1(val);
            setA1Externo(true);
        }
    }
    private int getValorDeDigital(String val){
        int ll=0;
        if (val.equals(mcsHIGH)) {
            ll=100;
        } else {
            try{
	      if(Double.valueOf(val).intValue()>0){
		ll=100;
	      }else{
		ll=0;
	      }
	    }catch(Throwable e){
	      ll=0;
	    }            
        }
        return ll;
    }    
    public synchronized void digitalWrite(int pin, String val) {

        switch (pin) {
            case 1:
                setD1(getValorDeDigital(val));
                break;
            case 5:
                setD5(getValorDeDigital(val));
                break;
            case 9:
                setD9(getValorDeDigital(val));
                break;
        }
    }
    public void resetAll() throws Exception {
//        analogWrite(1, 0);
//        analogWrite(2, 0);
//        digitalWrite(0, mcsLOW);
        setA0(0);
        setA0Externo(false);
        setA1(0);
        setA1Externo(false);
        setD0(0);
        setD0Externo(false);
        
    }        
}
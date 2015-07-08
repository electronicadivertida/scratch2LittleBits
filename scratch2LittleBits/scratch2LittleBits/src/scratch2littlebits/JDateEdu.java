package scratch2littlebits;

/**
 * <p>Title: JDateEdu</p>
 * <p>Description: Wrapper de Date</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: creativa3d</p>
 * @author Eduardo Gonzalez
 * @version 1.0
 */
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Calendar;
import java.io.Serializable;
import java.text.DateFormatSymbols;

/**Objeto fecha*/
public class JDateEdu implements Serializable, Cloneable, Comparable {

    private static final long serialVersionUID = 33333330L;
    private int mlAno = 0;
    private int mlMes = 0;
    private int mlDia = 0;
    private int mlHora = 0;
    private int mlMinuto = 0;
    private int mlSegundo = 0;
    //dias del mes
    private static int[] malDiasMes = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
    /**variable de compracion, si la fecha es igual*/
    public static final int mclFechaIgual = 0;
    /**variable de compracion, si la fecha es menor*/
    public static final int mclFechaMenor = -1;
    /**variable de compracion, si la fecha es mayor*/
    public static final int mclFechaMayor = 1;
    /**unidad dia*/
    public static final int mclDia = GregorianCalendar.DATE;
    /**unidad mes*/
    public static final int mclMes = GregorianCalendar.MONTH;
    /**unidad ano*/
    public static final int mclAno = GregorianCalendar.YEAR;
    /**unidad hora*/
    public static final int mclHoras = GregorianCalendar.HOUR;
    /**unidad minuto*/
    public static final int mclMinutos = GregorianCalendar.MINUTE;
    /**unidad segundo*/
    public static final int mclSegundos = GregorianCalendar.SECOND;

    //
    //Constructores
    //
    /**Constructor, pone la fecha a dia de hoy*/
    public JDateEdu() {
        Date loDate = new Date();
        setDate(loDate);
    }

    /**Constructor, pone la fecha segun Date*/
    public JDateEdu(Date poDate) {
        setDate(poDate);
    }

    /**
     * Constructor, pone la fecha en funcion del long en formato AAAAMMDDHHMMSS
     * @param plFecha long fecha
     * @throws FechaMalException error
     */
    public JDateEdu(long plFecha) throws FechaMalException {
        try {
            String lsFecha = String.valueOf(plFecha);
            mlAno = Integer.valueOf(lsFecha.substring(1, 4)).intValue();
            mlMes = Integer.valueOf(lsFecha.substring(5, 2)).intValue();
            mlDia = Integer.valueOf(lsFecha.substring(7, 2)).intValue();
            mlHora = Integer.valueOf(lsFecha.substring(9, 2)).intValue();
            mlMinuto = Integer.valueOf(lsFecha.substring(11, 2)).intValue();
            mlSegundo = Integer.valueOf(lsFecha.substring(13, 2)).intValue();
            if (!isDate(this)) {
                throw new FechaMalException();
            }
        } catch (Exception e) {
            throw new FechaMalException(e);
        }
    }

    /**
     * Constructor, pone la fecha en funcion del double, dias desde 1900,franccion que ha pasado del dia (WINDOWS)
     * @param pdFecha double fecha windows
     * @throws FechaMalException error
     */
    public JDateEdu(double pdFecha) throws FechaMalException {
        try {
            mlAno = (int) (pdFecha / 366) + 1900;
            mlMes = 1;
            mlDia = 1;
            while (getFechaEnNumero() < pdFecha) {
                add(mclMes, 1);
            }
            if (Math.abs(getFechaEnNumero() - pdFecha) > .0000001) {
                add(mclMes, -1);
                while (getFechaEnNumero() < pdFecha) {
                    add(mclDia, 1);
                }
                if (Math.abs(getFechaEnNumero() - pdFecha) > .0000001) {
                    add(mclDia, -1);
                    while (getFechaEnNumero() < pdFecha) {
                        add(mclHoras, 1);
                    }
                    if (Math.abs(getFechaEnNumero() - pdFecha) > .0000001) {
                        add(mclHoras, -1);
                        while (getFechaEnNumero() < pdFecha) {
                            add(mclMinutos, 1);
                        }
                        if (Math.abs(getFechaEnNumero() - pdFecha) > .0000001) {
                            add(mclMinutos, -1);
                            while (getFechaEnNumero() < pdFecha) {
                                add(mclSegundos, 1);
                            }
                        }
                    }
                }
            }
            if (!isDate(this)) {
                throw new FechaMalException();
            }
        } catch (Exception e) {
            throw new FechaMalException(e);
        }

    }

    /**
     * Constructor, pone la fecha en funcion la cadena pasada por parametro
     * @param psFecha fecha string
     * @throws FechaMalException error
     */
    public JDateEdu(String psFecha) throws FechaMalException {
        setDate(psFecha);
    }

    /**
     * Constructor, ano, mes y dia
     * @param plAno ano
     * @param plMes mes 
     * @param plDia dia
     * @throws FechaMalException error
     */
    public JDateEdu(int plAno, int plMes, int plDia) throws FechaMalException {
        mlAno = plAno;
        mlMes = plMes;
        mlDia = plDia;
        mlHora = 0;
        mlMinuto = 0;
        mlSegundo = 0;
        if (!isDate(this)) {
            throw new FechaMalException();
        }
    }

    /**
     * Constructor, ano, mes , dia , hora , minuto , segundo
     * @param plHora hora 
     * @param plMinuto minuto
     * @param plSegundo segundo
     * @param plAno ano
     * @param plMes mes
     * @param plDia dia
     * @throws FechaMalException error
     */
    public JDateEdu(int plAno, int plMes, int plDia, int plHora, int plMinuto, int plSegundo) throws FechaMalException {
        mlAno = plAno;
        mlMes = plMes;
        mlDia = plDia;
        mlHora = plHora;
        mlMinuto = plMinuto;
        mlSegundo = plSegundo;
        if (!isDate(this)) {
            throw new FechaMalException();
        }
    }

    /**
     * Constructor, Dado el segundo del dia establece la hora
     * @param plSegundo segundo
     * @throws FechaMalException error
     */
    public JDateEdu(int plSegundo) throws FechaMalException {
        mlAno = 0;
        mlMes = 0;
        mlDia = 0;
        mlHora = new Double(plSegundo / 3600).intValue();
        plSegundo -= mlHora * 3600;
        mlMinuto = new Double(plSegundo / 60).intValue();
        plSegundo -= mlMinuto * 60;
        mlSegundo = plSegundo;
        if (!isDate(this)) {
            throw new FechaMalException();
        }
    }

    /**
     * Devuelve el numero de segundo del dia
     * @return int Segundo
     */
    public int getNumSegundos() {
        return ((mlHora * 3600) + (mlMinuto * 60) + mlSegundo);
    }

    //
    //propiedades para devolver/establecer valores de la fecha actual
    //
    /**
     * Establece el ano
     * @param plAno ano
     */
    public void setAno(int plAno) {
        mlAno = plAno;
    }

    /**
     * Establece el mes
     * @param plMes mes
     */
    public void setMes(int plMes) {
        mlMes = plMes;
    }

    /**
     * Establece el dia
     * @param plDia dia
     */
    public void setDia(int plDia) {
        mlDia = plDia;
    }

    /**
     * Establece el la hora
     * @param plHora hora
     */
    public void setHora(int plHora) {
        mlHora = plHora;
    }

    /**
     * Establece el minuto
     * @param plMinuto minuto
     */
    public void setMinuto(int plMinuto) {
        mlMinuto = plMinuto;
    }

    /**
     * Establece el segundo
     * @param plSegundo segundo
     */
    public void setSegundo(int plSegundo) {
        mlSegundo = plSegundo;
    }

    /**
     * Devuelve el ano
     * @return ano
     */
    public int getAno() {
        return mlAno;
    }

    /**
     * Devuelve el mes
     * @return mes
     */
    public int getMes() {
        return mlMes;
    }

    /**
     * Devuelve el dia
     * @return dia
     */
    public int getDia() {
        return mlDia;
    }

    /**
     * Devuelve la  hora
     * @return hora
     */
    public int getHora() {
        return mlHora;
    }

    /**
     * Devuelve el minuto
     * @return minuto
     */
    public int getMinuto() {
        return mlMinuto;
    }

    /**
     * Devuelve el segundo
     * @return segundos
     */
    public int getSegundo() {
        return mlSegundo;
    }
    //
    //valores generales de las fechas
    //

    /**
     * devolvemos los meses del ano
     * @return meses ano
     */
    public static int mlMesesAno() {
        return 12;
    }

    /**
     * devolvemos las horas dia
     * @return horas dia
     */
    public static int mlHorasDia() {
        return 24;
    }

    /**
     * devolvemos los minutos hora
     * @return minutos hora
     */
    public static int mlMinutosHora() {
        return 60;
    }

    /**
     * devolvemos los minutos hora
     * @return segundos minuto
     */
    public static int mlSegudosMinuto() {
        return 60;
    }

    /**
     * devolvemos los dias del mes para el ano concreto
     * @return dias
     * @param plAno ano
     * @param plMes mes
     */
    public static int mlDiasMes(int plAno, int plMes) {
        int lDiasMes = 0;
        //paso especial para fecha vacia
        if ((plAno != 0) || (plMes != 0)) {
            if (plMes == 2) {
                if ((plAno % 4) == 0) {
                    if ((plAno % 100) == 0) {
                        if ((plAno % 400) != 0) {
                            lDiasMes = malDiasMes[plMes - 1];
                        } else {
                            lDiasMes = 29;
                        }
                    } else {
                        lDiasMes = 29;
                    }
                } else {
                    lDiasMes = malDiasMes[plMes - 1];
                }
            } else {
                lDiasMes = malDiasMes[plMes - 1];
            }
        }
        return lDiasMes;
    }

    /**
     * dias mes actual
     * @return dias
     */
    public int mlDiasMesActual() {
        return mlDiasMes(mlAno, mlMes);
    }

    /**
     * devuelve una lista de nombresmes
     * @return lista de nombres
     */
    public static String[] masNombresMes() {
        DateFormatSymbols l = new DateFormatSymbols();
        return l.getMonths();
    }

    /**
     * devuelve una lista de nombres dias semana
     * @return nombre de los dias de la semana
     */
    public static String[] masDiasSemana() {
        DateFormatSymbols l = new DateFormatSymbols();
        return l.getWeekdays();
    }

    private void analizarSinSeparadores(String psFecha) throws Exception {
        mlDia = Long.valueOf(psFecha.substring(0, 2)).intValue();
        mlMes = Long.valueOf(psFecha.substring(2, 4)).intValue();
        mlAno = Long.valueOf(psFecha.substring(4, psFecha.length())).intValue();
    }

    private void analizarConSeparadores(String psFecha) throws Exception {
        //valores auxiliares
        StringBuilder lsValor = new StringBuilder(4);
//      int lFinAno;
        boolean lbDia = false;
        boolean lbMes = false;
        boolean lbAno = false;
        boolean lbHora = false;
        boolean lbMinuto = false;
        boolean lbSegundo = false;
        boolean lbAnoMesDia = false;
        int lValor = 0;

        for (int i = 0; i <= psFecha.length() - 1; i++) {
            if ((psFecha.charAt(i) >= '0') && (psFecha.charAt(i) <= '9')) {
                lsValor.append(psFecha.charAt(i));
            } else {
                lValor = Integer.valueOf(lsValor.toString()).intValue();
                if ((lValor > 31) && (!lbDia) && (!lbAno) && (!lbMes)) {
                    lbAnoMesDia = true;
                }
                if (psFecha.charAt(i) == ':') {
                    lbAno = true;
                    lbMes = true;
                    lbDia = true;
                }
                if (lbAnoMesDia) {
                    if (!lbAno) {
                        mlAno = lValor;
                        lbAno = true;
                    } else if (!lbMes) {
                        mlMes = lValor;
                        lbMes = true;
                    } else if (!lbDia) {
                        mlDia = lValor;
                        lbDia = true;
                    } else if (!lbHora) {
                        mlHora = lValor;
                        lbHora = true;
                    } else if (!lbMinuto) {
                        mlMinuto = lValor;
                        lbMinuto = true;
                    } else if (!lbSegundo) {
                        mlSegundo = lValor;
                        lbSegundo = true;
                    }
                } else {
                    if (!lbDia) {
                        mlDia = lValor;
                        lbDia = true;
                    } else if (!lbMes) {
                        mlMes = lValor;
                        lbMes = true;
                    } else if (!lbAno) {
                        mlAno = lValor;
                        lbAno = true;
                    } else if (!lbHora) {
                        mlHora = lValor;
                        lbHora = true;
                    } else if (!lbMinuto) {
                        mlMinuto = lValor;
                        lbMinuto = true;
                    } else if (!lbSegundo) {
                        mlSegundo = lValor;
                        lbSegundo = true;
                    }
                }
                lsValor.setLength(0);
            }
        }
        if (lsValor.length()>0) {
            lValor = Integer.valueOf(lsValor.toString()).intValue();
            if (lbAnoMesDia) {
                if (!lbAno) {
                    mlAno = lValor;
                    lbAno = true;
                } else if (!lbMes) {
                    mlMes = lValor;
                    lbMes = true;
                } else if (!lbDia) {
                    mlDia = lValor;
                    lbDia = true;
                } else if (!lbHora) {
                    mlHora = lValor;
                    lbHora = true;
                } else if (!lbMinuto) {
                    mlMinuto = lValor;
                    lbMinuto = true;
                } else if (!lbSegundo) {
                    mlSegundo = lValor;
                    lbSegundo = true;
                }
            } else {
                if (!lbDia) {
                    mlDia = lValor;
                    lbDia = true;
                } else if (!lbMes) {
                    mlMes = lValor;
                    lbMes = true;
                } else if (!lbAno) {
                    mlAno = lValor;
                    lbAno = true;
                } else if (!lbHora) {
                    mlHora = lValor;
                    lbHora = true;
                } else if (!lbMinuto) {
                    mlMinuto = lValor;
                    lbMinuto = true;
                } else if (!lbSegundo) {
                    mlSegundo = lValor;
                    lbSegundo = true;
                }
            }
        }
    }

    //
    //funciones para la fecha
    //
     /** establece la fecha*/
    public void setDate(String psFecha) throws FechaMalException{
        try {

            //iniciamos valores
            mlDia = 0;
            mlMes = 0;
            mlAno = 0;
            mlHora = 0;
            mlMinuto = 0;
            mlSegundo = 0;
            psFecha = psFecha.trim();

            //analisis preliminar
            boolean lbConSeparadores = false;
            for (int i = 0; i <= psFecha.length() - 1; i++) {
                if (!((psFecha.charAt(i) >= '0') && (psFecha.charAt(i) <= '9'))) {
                    lbConSeparadores = true;
                }
            }

            //analizamos segun analisis preliminar 
            if (lbConSeparadores) {
                analizarConSeparadores(psFecha);
            } else {
                analizarSinSeparadores(psFecha);
            }

            //comprobacion Final de si la fecha es correcta
            if (!((mlAno == 0) && (mlMes == 0) && (mlDia == 0))) {
                if (mlAno <= 99) {
                    if (mlAno > 30) {
                        mlAno = mlAno + 1900;
                    } else {
                        mlAno = mlAno + 2000;
                    }
                }
            }
            if (!isDate(this)) {
                throw new FechaMalException();
            }
        } catch (Exception e) {
            throw new FechaMalException(e);
        }
    }
     /** establece la fecha*/
    public void setDate(Calendar poDate) {
        try {
            mlDia = poDate.get(Calendar.DATE);
            mlMes = poDate.get(Calendar.MONTH) + 1;
            mlAno = poDate.get(Calendar.YEAR);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        try {
            mlHora = poDate.get(Calendar.HOUR_OF_DAY);
            mlMinuto = poDate.get(Calendar.MINUTE);
            mlSegundo = poDate.get(Calendar.SECOND);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
    /**
     * establece la fecha
     * @param poDate date
     */
    public void setDate(Date poDate) {
        Calendar loCalendar = new GregorianCalendar();
        loCalendar.setTime(poDate);
        try {

            mlDia = loCalendar.get(loCalendar.DAY_OF_MONTH);
            mlMes = loCalendar.get(loCalendar.MONTH) + 1;
            mlAno = loCalendar.get(loCalendar.YEAR);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        try {
            mlHora = loCalendar.get(loCalendar.HOUR_OF_DAY);
            mlMinuto = loCalendar.get(loCalendar.MINUTE);
            mlSegundo = loCalendar.get(loCalendar.SECOND);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    /**
     * Devuelve el dia de la semana actual
     * @return dia de la semaan
     */
    public int getDiaSemana() {
        GregorianCalendar l;
        l = new GregorianCalendar(mlAno, mlMes - 1, mlDia, mlHora, mlMinuto, mlSegundo);
        return l.get(GregorianCalendar.DAY_OF_WEEK);
    }

    /**
     * Devuelve el dia de la semana, para espana
     * @return dia de la semana
     */
    public int getDiaSemanaEspana() {
        int lDia = 0;
        switch (getDiaSemana()) {
            case Calendar.MONDAY:
                lDia = 0;
                break;
            case Calendar.TUESDAY:
                lDia = 1;
                break;
            case Calendar.WEDNESDAY:
                lDia = 2;
                break;
            case Calendar.THURSDAY:
                lDia = 3;
                break;
            case Calendar.FRIDAY:
                lDia = 4;
                break;
            case Calendar.SATURDAY:
                lDia = 5;
                break;
            default:
                lDia = 6;
        }
        return lDia;
    }

    /**Anade tiempo, 
     * @param plUnidad (Dia, mes,...) 
     * @param plUnidades numero de veces 
     */
    public void add(int plUnidad, int plUnidades) {
        GregorianCalendar l;
        l = new GregorianCalendar(mlAno, mlMes - 1, mlDia, mlHora, mlMinuto, mlSegundo);
        l.add(plUnidad, plUnidades);
        setDate(l.getTime());
    }

    /**
     * diferencia entre fechas
     * @return diferencia de fechas en formato windows (double)
     * @param poDate1 fecha 1
     * @param poDate2 fecha 2
     */
    public static double diffNum(JDateEdu poDate1, JDateEdu poDate2) {
        return poDate1.getFechaEnNumero() - poDate2.getFechaEnNumero();
    }

    /**
     * diferencia entre fechas
     * @return Date diferencia de fechas
     * @param poDate1 fecha 1
     * @param poDate2 fecha 2
     * @throws Exception error
     */
    public static JDateEdu diffDate(JDateEdu poDate1, JDateEdu poDate2) throws Exception {
        return new JDateEdu(diffNum(poDate1, poDate2));
    }

    /**
     * diferencia entre fechas
     * @return diferencia entre fechas
     * @param plUnidad unidad devuelta
     * @param poDate1 fecha 1
     * @param poDate2 fecha 2
     * @throws Exception error
     */
    public static double diff(int plUnidad, JDateEdu poDate1, JDateEdu poDate2) throws Exception {
        double dDiff = diffNum(poDate1, poDate2);
        switch (plUnidad) {
            case mclAno:
                dDiff = (int) (dDiff / 365);
                break;
            case mclMes:
                dDiff = (int) (dDiff / 30);
                break;
            case mclDia:
                dDiff = (int) dDiff;
                break;
            case mclHoras:
                dDiff = dDiff * 24.0;
                break;
            case mclMinutos:
                dDiff = dDiff * 1440.0;
                break;
            case mclSegundos:
                dDiff = dDiff * 86400.0;
                break;
            default:
                throw new Exception("Unidad no soportada");
        }
        return dDiff;
    }

    /**
     * comprueba si es fecha correcta el String
     * @return si es fecha
     * @param psFecha cadena fecha
     */
    public static boolean isDate(String psFecha) {
        boolean lbValor;
        try {
            new JDateEdu(psFecha);
            lbValor = true;
        } catch (Exception e) {
            lbValor = false;
        }
        return lbValor;
    }
    /**
     * Fecha razonable es entre 1900 y 2100
     * 
     */
    public static boolean isDateRazonable(JDateEdu poFecha) {
        return poFecha.getAno()>1900 && poFecha.getAno()<2100; 
    }
    public boolean isDateRazonable() {
        return isDateRazonable(this);
    }
    

    /**
     * comprueba si es fecha correcta el objeto
     * @return si es fecha
     * @param poFecha Fecha
     */
    public boolean isDate(JDateEdu poFecha) {
        boolean lbValor = true;
        if (mlMes > mlMesesAno()) {
            lbValor = false;
        }
        if (lbValor) {
            if (mlDia > mlDiasMes(mlAno, mlMes)) {
                lbValor = false;
            }
        }
        if (lbValor) {
            if (mlHora > (mlHorasDia() - 1)) {
                lbValor = false;
            }
        }
        if (lbValor) {
            if (mlMinuto > (mlMinutosHora() - 1)) {
                lbValor = false;
            }
        }
        if (lbValor) {
            if (mlSegundo > (mlSegudosMinuto() - 1)) {
                lbValor = false;
            }
        }
        if (lbValor) {
            if ((mlDia < 0) || (mlMes < 0) || (mlAno < 0)
                    || (mlHora < 0) || (mlMinuto < 0) || (mlSegundo < 0)) {
                lbValor = false;
            }
        }
        return lbValor;
    }

    /**
     * Devuelve un objeto fecha a partir de un string
     * @return Fecha
     * @param psFecha cadena
     */
    public static JDateEdu CDate(String psFecha) {
        JDateEdu loDate;
        try {
            loDate = new JDateEdu(psFecha);
        } catch (Exception e) {
            System.out.println(e.toString());
            loDate = null;
        }
        return loDate;
    }

    /**
     * Devuelve un objeto fecha a partir de un double
     * @return Fecha
     * @param pdFecha double fecha
     */
    public static JDateEdu CDate(double pdFecha) {
        JDateEdu loFecha;
        try {
            loFecha = new JDateEdu(pdFecha);
        } catch (Exception e) {
            System.out.println(e.toString());
            loFecha = null;
        }
        return loFecha;
    }

    /**
     * compara dos fechas
     * @return mclFechaIgual, ...
     * @param poFecha1 fecha 1
     * @param poFecha2 fecha 2
     */
    public static int compareTo(JDateEdu poFecha1, JDateEdu poFecha2) {
        int lComparar;
        lComparar = mlComparar(poFecha1.mlAno, poFecha2.mlAno);
        if (lComparar == mclFechaIgual) {
            lComparar = mlComparar(poFecha1.mlMes, poFecha2.mlMes);
            if (lComparar == mclFechaIgual) {
                lComparar = mlComparar(poFecha1.mlDia, poFecha2.mlDia);
                if (lComparar == mclFechaIgual) {
                    lComparar = mlComparar(poFecha1.mlHora, poFecha2.mlHora);
                    if (lComparar == mclFechaIgual) {
                        lComparar = mlComparar(poFecha1.mlMinuto, poFecha2.mlMinuto);
                        if (lComparar == mclFechaIgual) {
                            lComparar = mlComparar(poFecha1.mlSegundo, poFecha2.mlSegundo);
                        }
                    }
                }
            }
        }
        return lComparar;
    }

    /**
     * implementa el interfaz comparacion
     * @return comparacion
     * @param o objeto a comaprar
     */
    public int compareTo(Object o) {
        return compareTo(this, (JDateEdu) o);
    }

    /**
     * comparamos pofecha con fecha actual
     * @return comparacion
     * @param poFecha Fecha
     */
    public int compareTo(JDateEdu poFecha) {
        return compareTo(this, poFecha);
    }

    /**comparamos 2 numeros*/
    private static int mlComparar(int plNumero, int plNumero2) {
        int lComparar;
        if (plNumero < plNumero2) {
            lComparar = mclFechaMenor;
        } else {
            if (plNumero > plNumero2) {
                lComparar = mclFechaMayor;
            } else {
                lComparar = mclFechaIgual;
            }
        }
        return lComparar;
    }

   


    public Date getDate() {
        return moDate();
    }

    /**
     * Devuelve un objeto date a partir de la fecha actual
     * @return fecha
     */
    public Date moDate() {
        Date loResult;
        if (mlAno == 0 && mlMes == 0 && mlDia == 0) {
//se queda en 1970        
//        loResult=new Date((mlHora-1)*60*60*1000+mlMinuto*60*1000+mlSegundo*1000);
            GregorianCalendar l = new GregorianCalendar(1899, 11, 30, mlHora, mlMinuto, mlSegundo);
            loResult = l.getTime();
        } else if (mlHora == 0 && mlMinuto == 0 && mlSegundo == 0) {
            GregorianCalendar l = new GregorianCalendar(mlAno, mlMes - 1, mlDia);
            loResult = l.getTime();
        } else {
            GregorianCalendar l = new GregorianCalendar(mlAno, mlMes - 1, mlDia, mlHora, mlMinuto, mlSegundo);
            loResult = l.getTime();
        }
        return loResult;
    }

    /**
     * Devuelve la fecha para paso de parametros en conexiones a base de datos
     * @return fecha sql
     */
    public java.util.Date moDateSQL() {
        java.util.Date loDate;
        if (mlAno == 0 && mlMes == 0 && mlDia == 0) {
            loDate = new java.sql.Time(moDate().getTime());
        } else {
            loDate = new java.sql.Timestamp(moDate().getTime());
        }

        return loDate;
    }

    /**
     * Numero de dias desde la fecha 1/1/1900, la fraccion es la parte proporcional del dia
     * @return fecha en numero
     */
    public double getFechaEnNumero() {
        //return Long.parseLong(msFormatear("yyyyMMddHHmmss"));
        //return Long.parseLong(String.valueOf(mlAno) + String.valueOf(mlMes) + String.valueOf(mlDia) + String.valueOf(mlHora) + String.valueOf(mlMinuto) + String.valueOf(mlSegundo));
        //return new Long(1);
        int lAno1900 = 693960;
        int lAno = (mlAno);
        int lSumaMes = 0;
        int lDiaExtra = 1;
        for (int i = 1; i < mlMes; i++) {
            lSumaMes += mlDiasMes(mlAno, i);
        }
        if (((mlAno % 4) == 0) && (mlMes < 3)) {
            lDiaExtra = 0;
        }
        return (double) ((lAno * 365 + (int) (lAno / 4) - (int) (lAno / 100) + (int) (lAno / 400) + lDiaExtra)
                - lAno1900
                + (lSumaMes) + (mlDia))
                + (double) (((mlHora * 3600.0) + (mlMinuto * 60.0) + mlSegundo) / 86400.0);
    }

    /**Devuelve el objeto clonado*/
    public Object clone() throws CloneNotSupportedException {
//      Object loDate;
//      try{
//        loDate=new JDateEdu(getAno(), getMes(), getDia(), getHora(), getMinuto(), getSegundo());
//      }catch(Exception e){
//        loDate=null;
//      }
        return super.clone();
    }

}

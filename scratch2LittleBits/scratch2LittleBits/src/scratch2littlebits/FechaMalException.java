package scratch2littlebits;

/**
 * <p>Title: GrafXY</p>
 * <p>Description: Generador de graficos XY</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Intecsa</p>
 * @author Eduardo Gonzalez
 * @version 1.0
 */

public class FechaMalException extends Exception {
  public FechaMalException(Throwable e) {
      super(e.toString());
  }
  /**Contrutor */
  public FechaMalException() {
      this("");
  }
  /**
   * Contrutor con mensaje extra
   * @param psExtra mensaje extra
   */
  public FechaMalException(String psExtra) {
    super("Fecha incorrecta " + psExtra);
  }

}
package error;

/**
 * The GlobalErrors class contains the string for an error message and a boolean
 * value hasError that is used as a flag for displaying errors.
 * 
 * @author Brandon Hua
 */

public class GlobalErrors {

  private static String errorMessage;
  private static boolean error = false;

  /**
   * A method to set the current error message.
   * 
   * @param errorMsg The string message of the thrown error.
   */

  public static void setError(String errorMsg) {
    errorMessage = errorMsg;
    error = true;
  }

  /**
   * A method that sets the error boolean back to false.
   */

  public static void resetError() {
    error = false;
  }
  
  public static String getErrorMessage() {
    return errorMessage;
  }

  public static boolean hasError() {
    return error;
  }

}

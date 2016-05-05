package br.com.luizfp.segredosufsc.exceptions;

/**
 * Exceção lançada pela aplicação quando não houver conexão com a internet.
 */
public class NoNetworkConnectionException extends Exception {

  public NoNetworkConnectionException() {
    super();
  }

  public NoNetworkConnectionException(final String message) {
    super(message);
  }

  public NoNetworkConnectionException(final String message, final Throwable cause) {
    super(message, cause);
  }

  public NoNetworkConnectionException(final Throwable cause) {
    super(cause);
  }
}

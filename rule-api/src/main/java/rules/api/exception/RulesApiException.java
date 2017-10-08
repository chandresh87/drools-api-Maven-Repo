/** */
package rules.api.exception;

/**
 * Exception class for rules engine.It implements RuntimeException
 *
 * @author chandresh.mishra
 */
public class RulesApiException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  private final String errorMessage;

  /** @param message */
  public RulesApiException(String message) {
    super();
    this.errorMessage = message;
  }

  /** @return the message */
  @Override
  public String getMessage() {
    return errorMessage;
  }
}

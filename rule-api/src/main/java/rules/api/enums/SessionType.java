/** */
package rules.api.enums;

/**
 * Enum holding constant for session type.
 *
 * @author chandresh.mishra
 */
public enum SessionType {
  STATEFUL("stateful"),
  STATELESS("stateless");

  private String type;

  private SessionType(String type) {
    this.type = type;
  }

  /** @return the type */
  public String getType() {
    return type;
  }
}

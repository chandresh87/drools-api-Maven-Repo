/** */
package rules.api.message;

import java.util.List;

/**
 * This class is responsible for sending back the response from rules engine
 *
 * @author chandresh.mishra
 */
public class RulesResponse {

  // Number of rules fired
  private int numberOfRulesFired;

  // List of objects from session
  private List<Object> factsFromSession;

  /**
   * @param factsFromSession
   * @param numberOfRulesFired
   */
  public RulesResponse(int numberOfRulesFired, List<Object> factsFromSession) {
    super();
    this.factsFromSession = factsFromSession;
    this.numberOfRulesFired = numberOfRulesFired;
  }

  /** @return the factsFromSession */
  public List<Object> getFactsFromSession() {
    return factsFromSession;
  }

  /** @return the numberOfRulesFired */
  public int getNumberOfRulesFired() {
    return numberOfRulesFired;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append("RulesResponse [numberOfRulesFired=");
    builder.append(numberOfRulesFired);
    builder.append(", factsFromSession=");
    builder.append(factsFromSession);
    builder.append("]");
    return builder.toString();
  }
}

package rules.api.engine;

import java.util.List;
import rules.api.message.RulesRequest;
import rules.api.message.RulesResponse;

/**
 * This interface has method to fire rules by passing the RulesRequest and get the response.
 *
 * @author chandresh.mishra
 */
public interface RulesEngine {

  /**
   * This method is used to fire the rules and return number of rules fired.
   *
   * @param rulesRequestParams - All the parameter required to fire the rule
   * @param returnedFactsClass - List of class for facts returned from session.
   * @return integer - Number of rules fired
   */
  public int fireRules(RulesRequest rulesRequestParams);

  /**
   * This method is used to fire the rules and return the response containing number of rules fired
   * and requested facts from the session.
   *
   * @param rulesRequestParams - All the parameter required to fire the rule
   * @param returnedFactsClass - List of class for facts returned from session.
   * @return RulesResponse
   */
  RulesResponse fireRules(RulesRequest rulesRequestParams, List<Class> returnedFactsClass);
}

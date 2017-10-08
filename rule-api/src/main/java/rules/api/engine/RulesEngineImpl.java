/** */
package rules.api.engine;

import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.StatelessKieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rules.api.enums.SessionType;
import rules.api.exception.RulesApiException;
import rules.api.message.RulesRequest;
import rules.api.message.RulesResponse;

/**
 * This class provides the core methods to run the rule engine
 *
 * @author chandresh.mishra
 */
@Component
public class RulesEngineImpl implements RulesEngine {

  @Autowired private RulesEngineHelper rulesEngineHelper;

  private Logger logger = LogManager.getLogger(this);

  /**
   * This method is used to fire the rules and return number of rules fired.
   *
   * @param rulesRequestParams - All the parameter required to fire the rule
   * @param returnedFactsClass - List of class for facts returned from session.
   * @return integer - Number of rules fired
   */
  @Override
  public int fireRules(RulesRequest rulesRequestParams) {

    logger.traceEntry("START - method - [fireRules(RulesRequest)]");
    RulesResponse rulesResponse = this.fireRules(rulesRequestParams, null);
    int rulesFiredCount = 0;
    if (null != rulesResponse) {
      rulesFiredCount = rulesResponse.getNumberOfRulesFired();
    }
    logger.debug("Number of rules fired {}", rulesFiredCount);
    logger.traceExit("END - method - [fireRules(RulesRequest)]");
    return rulesFiredCount;
  }

  /**
   * This method is used to fire the rules and return the response containing number of rules fired
   * and requested facts from the session.
   *
   * @param rulesRequestParams - All the parameter required to fire the rule
   * @param returnedFactsClass - List of class for facts returned from session.
   * @return RulesResponse
   */
  @Override
  public RulesResponse fireRules(RulesRequest rulesRequestParams, List<Class> returnedFactsClass) {

    logger.traceEntry("START - method - [fireRules(RulesRequest,List<Class>)]");

    KieSession kSession = null;
    RulesResponse rulesResponse = null;

    logger.info(rulesRequestParams);

    if (null == rulesRequestParams) {

      logger.error("Missing mandatory details in rulesRequest to run the rules");

      throw new RulesApiException("Missing mandatory details in rulesRequest to run the rules");
    }

    // Using a default stateful session in case session type is not given OR session type is stateful
    if (null == rulesRequestParams.getSessionType()
        || rulesRequestParams.getSessionType() == SessionType.STATEFUL) {

      kSession = rulesEngineHelper.getStatefulKieSession(rulesRequestParams);

      if (null == kSession) {
        logger.error("Can not instantiate KieSession.Please check configuration");
        throw new RulesApiException("Can not instantiate KieSession.Please check configuration");
      }

      rulesResponse =
          rulesEngineHelper.fireStatefulRules(kSession, rulesRequestParams, returnedFactsClass);

    }

    // If session type passed is state less
    else if (rulesRequestParams.getSessionType() == SessionType.STATELESS) {

      StatelessKieSession statelessKieSession =
          rulesEngineHelper.getStatelessKieSession(rulesRequestParams);

      if (null == statelessKieSession) {
        logger.error("Can not instantiate stateless KieSession.Please check configuration");
        throw new RulesApiException(
            "Can not instantiate stateless KieSession.Please check configuration");
      }

      rulesResponse =
          rulesEngineHelper.fireRuleStateless(
              statelessKieSession, rulesRequestParams, returnedFactsClass);
    }
    logger.info(rulesResponse);
    logger.traceExit("END - method - [fireRules(RulesRequest,List<Class>)]");
    return rulesResponse;
  }
}

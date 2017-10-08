/** */
package rules.api.engine;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.kie.api.KieServices;
import org.kie.api.command.Command;
import org.kie.api.runtime.ExecutionResults;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.StatelessKieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rules.api.channels.SendData;
import rules.api.exception.RulesApiException;
import rules.api.listener.RuleAgendaListener;
import rules.api.message.RulesRequest;
import rules.api.message.RulesResponse;

/**
 * This class contains helper method used by the rule engine.It has default visibility.
 *
 * @author chandresh.mishra
 */
@Component
class RulesEngineHelper {

  @Autowired private KieContainer kContainer;

  private Logger logger = LogManager.getLogger(this);

  private KieServices kieService = KieServices.Factory.get();

  /**
   * This method returns the state full kieSession from the container
   *
   * @param rulesRequestParams
   * @return KieSession
   */
  public KieSession getStatefulKieSession(RulesRequest rulesRequestParams) {

    logger.traceEntry("START - method - [getStatefulKieSession(RulesRequest)]");

    if (null == kContainer) {
      logger.error("Can not initialise container");

      throw new RulesApiException("an not initialise container");
    }

    KieSession kSession = null;

    // Getting a default kiesession from kieBase if kiebase name is provided
    if (rulesRequestParams.isBuildSessionByKieBase()
        && !StringUtils.isEmpty(rulesRequestParams.getKieBasename())) {

      kSession = kContainer.getKieBase(rulesRequestParams.getKieBasename()).newKieSession();
    }

    // If session type passed is state full and session name is given
    else if (!rulesRequestParams.isBuildSessionByKieBase()
        && !StringUtils.isEmpty(rulesRequestParams.getSessionName())) {

      logger.debug("Initialise session with session name {}" + rulesRequestParams.getSessionName());
      // Initialise session with session name
      kSession = kContainer.newKieSession(rulesRequestParams.getSessionName());

    } else {

      logger.debug("Getting a default kiesession from Container");
      // Getting a default kiesession from Container
      kSession = kContainer.newKieSession();
    }

    logger.traceExit("END - method - [getStatefulKieSession(RulesRequest)]");
    return kSession;
  }

  /**
   * This method returns the state less kieSession from the container
   *
   * @param rulesRequestParams
   * @return StatelessKieSession
   */
  public StatelessKieSession getStatelessKieSession(RulesRequest rulesRequestParams) {

    logger.traceEntry("START - method - [getStatelessKieSession(RulesRequest)]");

    if (null == kContainer) {
      logger.error("Can not initialise container");

      throw new RulesApiException("an not initialise container");
    }

    StatelessKieSession statelessKieSession = null;

    // Getting a default session from kieBase if kiebase name is provided
    if (rulesRequestParams.isBuildSessionByKieBase()
        && !StringUtils.isEmpty(rulesRequestParams.getKieBasename())) {

      statelessKieSession =
          kContainer.getKieBase(rulesRequestParams.getKieBasename()).newStatelessKieSession();

    }
    // If session type passed is state less and session name is given
    else if (!rulesRequestParams.isBuildSessionByKieBase()
        && !StringUtils.isEmpty(rulesRequestParams.getSessionName())) {

      logger.debug(
          "Initialise state less session with session name {}"
              + rulesRequestParams.getSessionName());
      // Initialise session with session name
      statelessKieSession = kContainer.newStatelessKieSession(rulesRequestParams.getSessionName());

    } else {
      logger.debug("Getting a default StatelessKieSession from Container");
      // Getting a default StatelessKieSession from Container
      statelessKieSession = kContainer.newStatelessKieSession();
    }

    logger.traceExit("END - method - [getStatelessKieSession(RulesRequest)]");
    return statelessKieSession;
  }

  /**
   * This method fire the rule using stateful session and returns the rulesResponse
   *
   * @param kSession
   * @param droolsParam
   * @param returnedFactsClass
   * @return
   */
  public RulesResponse fireStatefulRules(
      KieSession kSession, RulesRequest droolsParam, List<Class> returnedFactsClass) {

    logger.traceEntry("START - method - [fireStatefulRules(KieSession,RulesRequest,List<Class>)]");

    if (null != kSession && null != droolsParam) {

      int numberOfFiredRules = 0;
      List<Object> factsFromSession = null;

      RuleAgendaListener ruleAgendaListner = new RuleAgendaListener();

      SendData sendDataChannel = new SendData();

      //Adding the listener to session
      kSession.addEventListener(ruleAgendaListner);

      //Adding the channel to session
      kSession.registerChannel("send-channel", sendDataChannel);

      // Setting global variables and Services
      setGlobalElement(kSession, droolsParam.getGlobalElement());

      // firing rules by passing the facts
      numberOfFiredRules = fireRulesWithFact(kSession, droolsParam.getFacts());

      if (!CollectionUtils.isEmpty(returnedFactsClass)) {
        // filter the facts that has been returned from session
        factsFromSession = filterFacts(sendDataChannel, returnedFactsClass);
      } else factsFromSession = sendDataChannel.getNewObjectInsterted();

      // disposing the session
      kSession.dispose();

      logger.traceEntry("END - method - [fireStatefulRules(KieSession,RulesRequest,List<Class>)]");

      return new RulesResponse(numberOfFiredRules, factsFromSession);
    } else {
      logger.error("KieSession and RulesRequest are mandatory feilds");
      throw new RulesApiException("KieSession and RulesRequest are mandatory feilds");
    }
  }

  /**
   * This method fires rules using stateless session
   *
   * @param ruleRuntimeListener
   * @param ruleAgendaListner
   * @param facts
   * @return RulesResponse
   */
  public RulesResponse fireRuleStateless(
      StatelessKieSession statelessKieSession,
      RulesRequest rulesRequest,
      List<Class> returnedFactsClass) {

    logger.traceEntry("START - method - [fireRuleStateless(KieSession,RulesRequest,List<Class>)]");

    RuleAgendaListener ruleAgendaListner = new RuleAgendaListener();

    SendData sendDataChannel = new SendData();

    if (null != statelessKieSession && null != rulesRequest) {

      int numberOfFiredRules = 0;
      List<Object> factsFromSession = null;

      // Setting global variables and Services
      setGlobalElement(statelessKieSession, rulesRequest.getGlobalElement());

      // Adding the listener for the session
      statelessKieSession.addEventListener(ruleAgendaListner);

      //Register Channel
      statelessKieSession.registerChannel("send-channel", sendDataChannel);

      List<Command> commandList = new ArrayList<>();

      if (!CollectionUtils.isEmpty(rulesRequest.getFacts())) { //Inserting the facts in the session
        Command newInsertOrder =
            kieService.getCommands().newInsertElements(rulesRequest.getFacts());
        commandList.add(newInsertOrder);
      }
      Command newFireAllRules = kieService.getCommands().newFireAllRules("outFired");

      commandList.add(newFireAllRules);

      //Executing the command as a batch process
      ExecutionResults execResults =
          statelessKieSession.execute(kieService.getCommands().newBatchExecution(commandList));

      numberOfFiredRules = (int) execResults.getValue("outFired");

      //Filtering the facts that would be returned as a part of response
      if (!CollectionUtils.isEmpty(returnedFactsClass)) {
        factsFromSession = filterFacts(sendDataChannel, returnedFactsClass);
      } else {
        factsFromSession = sendDataChannel.getNewObjectInsterted();
      }

      logger.traceEntry(
          "START - method - [fireRuleStateless(KieSession,RulesRequest,List<Class>)]");
      return new RulesResponse(numberOfFiredRules, factsFromSession);
    } else {
      logger.error("statelessKieSession and RulesRequest are mandatory feilds");
      throw new RulesApiException("statelessKieSession and RulesRequest are mandatory feilds");
    }
  }

  /**
   * This method will fire all the rules using the facts passed
   *
   * @param facts
   * @param ruleRuntimeListener
   * @return int -the number of matching rules fired
   */
  private int fireRulesWithFact(KieSession kSession, List<Object> facts) {

    if (!CollectionUtils.isEmpty(facts)) { //Adding all the facts to the session
      facts.forEach(kSession::insert);
    }
    // firing the rules
    return kSession.fireAllRules();
  }

  /**
   * This method will filter the facts that need to be returned in response
   *
   * @param returned Facts Class
   * @return list of objects
   */
  private List<Object> filterFacts(SendData sendDataChannel, List<Class> returnedFactsClass) {

    return sendDataChannel
        .getNewObjectInsterted()
        .stream()
        .filter(element -> returnedFactsClass.contains(element.getClass()))
        .collect(Collectors.toList());
  }

  /**
   * This method set global service and variables to stateful session
   *
   * @param session
   * @param globalElement
   */
  private void setGlobalElement(KieSession session, Map<String, Object> globalElement) {

    if (globalElement != null && globalElement.size() > 0 && session != null) {

      globalElement.forEach(session::setGlobal);
    }
  }

  /**
   * This method set global service and variables to stateless session
   *
   * @param session
   * @param globalElement
   */
  private void setGlobalElement(StatelessKieSession session, Map<String, Object> globalElement) {

    if (globalElement != null && globalElement.size() > 0 && session != null) {

      globalElement.forEach(session::setGlobal);
    }
  }
}

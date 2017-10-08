/** */
package com.rules.base;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.drools.core.event.DefaultAgendaEventListener;
import org.kie.api.KieBase;
import org.kie.api.KieServices;
import org.kie.api.builder.Message;
import org.kie.api.builder.Results;
import org.kie.api.command.Command;
import org.kie.api.event.rule.AfterMatchFiredEvent;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.ClassObjectFilter;
import org.kie.api.runtime.ExecutionResults;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.StatelessKieSession;
import org.kie.internal.io.ResourceFactory;
import org.kie.internal.utils.KieHelper;

/**
 * This is a base test class for testing rules. It provide convenience method for creating
 * container,session and firing rules.
 *
 * @author chandresh.mishra
 */
public class RulesBaseTest {

  private KieServices kService;
  private KieContainer kContainer;

  private Logger logger = LogManager.getLogger(this);

  /**
   * This method provides the default stateful kiesession
   *
   * @return KieSession
   */
  protected KieSession createDefaultSession() {

    KieSession ksession = this.createContainer().newKieSession();
    setDefaultListner(ksession);
    return ksession;
  }

  /**
   * This method provides the default StatelessKieSession
   *
   * @return statelessKieSession
   */
  protected StatelessKieSession createDefaultStatelessSession() {

    StatelessKieSession statelessKieSession = this.createContainer().newStatelessKieSession();
    setAgendaListner(statelessKieSession);
    return statelessKieSession;
  }

  /**
   * This method gives the kiebase from the container
   *
   * @param KieBase name
   * @return KieBase
   */
  protected KieBase createKnowledgeBase(String name) {
    KieContainer kContainer = this.createContainer();
    KieBase kbase = kContainer.getKieBase(name);

    if (kbase == null) {
      throw new IllegalArgumentException("Unknown Kie Base with name '" + name + "'");
    }

    return kbase;
  }

  /**
   * This method returns a given session from container
   *
   * @param name
   * @return KieSession
   */
  protected KieSession createSession(String name) {

    KieContainer kContainer = this.createContainer();
    KieSession ksession = kContainer.newKieSession(name);

    if (ksession == null) {
      throw new IllegalArgumentException("Unknown Session with name '" + name + "'");
    }
    setDefaultListner(ksession);
    return ksession;
  }

  /**
   * This method gives a state less kiesession from container
   *
   * @param name
   * @return StatelessKieSession
   */
  protected StatelessKieSession createStatelessSession(String name) {

    KieContainer kContainer = this.createContainer();
    StatelessKieSession ksession = kContainer.newStatelessKieSession(name);

    if (ksession == null) {
      throw new IllegalArgumentException("Unknown Session with name '" + name + "'");
    }

    setAgendaListner(ksession);
    return ksession;
  }

  /**
   * Set agenda Listener to StatelessKieSession
   *
   * @param ksession
   */
  private void setAgendaListner(StatelessKieSession statelessKieSession) {
    statelessKieSession.addEventListener(
        new DefaultAgendaEventListener() {
          @Override
          public void afterMatchFired(AfterMatchFiredEvent event) {
            logger.debug("Rules getting fired" + event.getMatch().getRule().getName());
          }
        });
  }

  /**
   * This method is used to test a particular drl file. It build a session using a given drl file.
   *
   * @param drl
   * @return KieSession
   */
  protected KieSession createKieSessionFromDRL(String drl) {
    KieHelper kieHelper = new KieHelper();
    kieHelper.addResource(ResourceFactory.newClassPathResource(drl), ResourceType.DRL);

    Results results = kieHelper.verify();

    if (results.hasMessages(Message.Level.WARNING, Message.Level.ERROR)) {
      List<Message> messages = results.getMessages(Message.Level.WARNING, Message.Level.ERROR);
      for (Message message : messages) {
        logger.debug("Error: " + message.getText());
      }

      throw new IllegalStateException("Compilation errors were found. Check the logs.");
    }
    KieSession ksession = kieHelper.build().newKieSession();
    setDefaultListner(ksession);
    return ksession;
  }

  /**
   * It fires a rule using state full session and returns number of rules fired
   *
   * @param kSession
   * @param facts
   * @param globalElement
   * @return Number of rules fired
   */
  protected int fireRule(
      KieSession kSession, List<Object> facts, Map<String, Object> globalElement) {
    if (null != kSession && null != facts) {
      facts.forEach(fact -> kSession.insert(fact));

      if (globalElement != null && globalElement.size() > 0) {
        globalElement.forEach(
            (key, value) -> {
              kSession.setGlobal(key, value);
            });
      }
    } else throw new IllegalArgumentException("Session and facts are mandatory");

    return kSession.fireAllRules();
  }

  /**
   * It fires a rule using state less session and returns number of rules fired
   *
   * @param statelessKieSession
   * @param facts
   * @param globalElement
   * @return
   */
  protected int fireRule(
      StatelessKieSession statelessKieSession,
      List<Object> facts,
      Map<String, Object> globalElement) {
    ExecutionResults execResults;
    if (null != statelessKieSession && null != facts) {
      Command newInsertOrder = getKieServices().getCommands().newInsertElements(facts);
      Command newFireAllRules = getKieServices().getCommands().newFireAllRules("outFired");
      List<Command> commandList = new ArrayList<>();
      commandList.add(newInsertOrder);
      commandList.add(newFireAllRules);

      execResults =
          statelessKieSession.execute(
              getKieServices().getCommands().newBatchExecution(commandList));

      if (globalElement != null && globalElement.size() > 0) {
        globalElement.forEach(
            (key, value) -> {
              statelessKieSession.setGlobal(key, value);
            });
      }
    } else throw new IllegalArgumentException("Session and facts are mandatory");

    return (int) execResults.getValue("outFired");
  }

  /**
   * It is used to filter facts from session.
   *
   * @param ksession
   * @param classType
   * @return collection of facts
   */
  protected <T> Collection<T> getFactsFromKieSession(KieSession ksession, Class<T> classType) {
    return (Collection<T>) ksession.getObjects(new ClassObjectFilter(classType));
  }

  /**
   * Destroy the session and free all the resources.
   *
   * @param kSession
   */
  protected void destroy(KieSession kSession) {
    kSession.dispose();
  }

  /**
   * It builds the container using class path resources.
   *
   * @return KieContainer
   */
  private KieContainer createContainer() {

    if (kContainer != null) {
      return kContainer;
    }

    KieContainer Container = getKieServices().getKieClasspathContainer();

    Results results = Container.verify();

    if (results.hasMessages(Message.Level.WARNING, Message.Level.ERROR)) {

      List<Message> messages = results.getMessages(Message.Level.WARNING, Message.Level.ERROR);

      for (Message message : messages) {

        logger.error(
            "Compilation errors in rules: {} in file {} at line number  {} and coloumn {}.Error message is: {}",
            message.getLevel(),
            message.getPath(),
            message.getLine(),
            message.getColumn(),
            message.getText());
      }

      throw new IllegalStateException("Compilation errors were found. Check the logs.");
    }

    kContainer = Container;
    return kContainer;
  }

  /**
   * It gives a kieservice
   *
   * @return KieServices
   */
  private KieServices getKieServices() {
    if (null == kService) kService = KieServices.Factory.get();

    return kService;
  }

  /**
   * This method sets the default agenda listener to a session
   *
   * @param kSession
   */
  private void setDefaultListner(KieSession kSession) {
    kSession.addEventListener(
        new DefaultAgendaEventListener() {
          @Override
          public void afterMatchFired(AfterMatchFiredEvent event) {
            logger.debug("Rules getting fired " + event.getMatch().getRule().getName());
          }
        });
  }
}

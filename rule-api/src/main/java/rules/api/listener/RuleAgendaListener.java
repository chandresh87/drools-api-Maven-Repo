/** */
package rules.api.listener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.kie.api.event.rule.AfterMatchFiredEvent;
import org.kie.api.event.rule.AgendaEventListener;
import org.kie.api.event.rule.AgendaGroupPoppedEvent;
import org.kie.api.event.rule.AgendaGroupPushedEvent;
import org.kie.api.event.rule.BeforeMatchFiredEvent;
import org.kie.api.event.rule.MatchCancelledEvent;
import org.kie.api.event.rule.MatchCreatedEvent;
import org.kie.api.event.rule.RuleFlowGroupActivatedEvent;
import org.kie.api.event.rule.RuleFlowGroupDeactivatedEvent;

/**
 * This class act as a rule agenda listener.It would be used for the auditing to print all the rules
 * matched and fired. It implements AgendaEventListener
 *
 * @author chandresh.mishra
 */
public class RuleAgendaListener implements AgendaEventListener {

  private Logger log = LogManager.getLogger(this);

  /* (non-Javadoc)
   * @see org.kie.api.event.rule.AgendaEventListener#afterMatchFired(org.kie.api.event.rule.AfterMatchFiredEvent)
   */
  @Override
  public void afterMatchFired(AfterMatchFiredEvent event) {
    log.info("Rules fired : " + event.getMatch().getRule().getName());
  }

  @Override
  public void matchCreated(MatchCreatedEvent event) {
    // No Implementation
  }

  @Override
  public void matchCancelled(MatchCancelledEvent event) {
    // No Implementation
  }

  @Override
  public void beforeMatchFired(BeforeMatchFiredEvent event) {
    // No Implementation

  }

  @Override
  public void agendaGroupPopped(AgendaGroupPoppedEvent event) {

    // No Implementation
  }

  @Override
  public void agendaGroupPushed(AgendaGroupPushedEvent event) {
    // No Implementation

  }

  @Override
  public void beforeRuleFlowGroupActivated(RuleFlowGroupActivatedEvent event) {

    // No Implementation
  }

  @Override
  public void afterRuleFlowGroupActivated(RuleFlowGroupActivatedEvent event) {

    // No Implementation
  }

  @Override
  public void beforeRuleFlowGroupDeactivated(RuleFlowGroupDeactivatedEvent event) {

    // No Implementation
  }

  @Override
  public void afterRuleFlowGroupDeactivated(RuleFlowGroupDeactivatedEvent event) {

    // No Implementation
  }
}

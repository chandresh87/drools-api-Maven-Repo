/** */
package rules.api.listener;

import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.kie.api.event.rule.ObjectDeletedEvent;
import org.kie.api.event.rule.ObjectInsertedEvent;
import org.kie.api.event.rule.ObjectUpdatedEvent;
import org.kie.api.event.rule.RuleRuntimeEventListener;

/**
 * This class act as a rule runtime listener.It has method to collect all the facts inserted in
 * working memory of rule engine. It implements RuleRuntimeEventListener
 *
 * @author chandresh.mishra
 */
public class RuleRuntimeListener implements RuleRuntimeEventListener {

  //List of all the facts inserted
  private List<Object> newObjectInsterted = new ArrayList<>();

  private Logger log = LogManager.getLogger(this);

  /* (non-Javadoc)
   * @see org.kie.api.event.rule.RuleRuntimeEventListener#objectInserted(org.kie.api.event.rule.ObjectInsertedEvent)
   */
  @Override
  public void objectInserted(ObjectInsertedEvent event) {
    newObjectInsterted.add(event.getObject());
    log.debug("inserted new fact" + event.getObject().toString());
  }

  @Override
  public void objectUpdated(ObjectUpdatedEvent event) {

    // No Implementation
  }

  @Override
  public void objectDeleted(ObjectDeletedEvent event) {

    // No Implementation
  }

  /** @return the newObjectInsterted */
  public List<Object> getNewObjectInsterted() {
    return newObjectInsterted;
  }
}

/** */
package rules.api.channels;

import java.util.ArrayList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.kie.api.runtime.Channel;

/**
 * This class is used for collecting all the data inserted as a part of rule RHS and passed to a
 * channel.
 *
 * @author chandresh.mishra
 */
public class SendData implements Channel {

  //List of new facts inserted
  private ArrayList<Object> newObjectInsterted = new ArrayList<>();

  private Logger log = LogManager.getLogger(this);

  @Override
  public void send(Object object) {

    newObjectInsterted.add(object);
    newObjectInsterted.trimToSize();
    log.debug("inserted new fact in channels" + object.toString());
  }

  /** @return the newObjectInsterted */
  public ArrayList<Object> getNewObjectInsterted() {
    return newObjectInsterted;
  }
}

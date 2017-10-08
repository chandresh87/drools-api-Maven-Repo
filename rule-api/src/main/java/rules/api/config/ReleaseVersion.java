/** */
package rules.api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * This class holds the KieJar's groupID,artifactID and version. which is populated from property
 * file.
 *
 * @author chandresh.mishra
 */
@PropertySource("classpath:rulesConfig.properties")
@Component
public class ReleaseVersion {

  // Group ID for the Kiejar
  @Value("${drools.groupID}")
  private String groupID;

  // Artifact ID for the Kiejar
  @Value("${drools.artifactID}")
  private String artifactID;

  // Version for the Kiejar
  @Value("${drools.version}")
  private String version;

  //Scan Interval
  @Value("${drools.scanner}")
  private Long scanInterval;

  /** @return the groupID */
  public String getGroupID() {
    return groupID;
  }

  /** @return the artifactID */
  public String getArtifactID() {
    return artifactID;
  }

  /** @return the version */
  public String getVersion() {
    return version;
  }

  /** @return the scanInterval */
  public Long getScanInterval() {
    return scanInterval;
  }
}

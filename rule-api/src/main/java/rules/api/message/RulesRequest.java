package rules.api.message;

import java.util.List;
import java.util.Map;
import rules.api.enums.SessionType;

/**
 * This class is responsible to populate the parameters needed by the rule engine
 *
 * @author chandresh.mishra
 */
public class RulesRequest {

  // KieSession name from Kiemodule
  private String sessionName;
  // KieBase name from Kiemodule
  private String kieBasename;
  // Type of session .Stateful or Stateless
  private SessionType sessionType;
  // Facts passes to session.It is mandatory field.
  private List<Object> facts;
  // Map of global elements if present in rule.
  private Map<String, Object> globalElement;
  // Build session by using kiebase name or kieSession name.It is mandatory field.
  private boolean buildSessionByKieBase;

  public RulesRequest() { // default constructor
  }

  /**
   * Parameterised constructor for RulesRequest
   *
   * @param sessionName
   * @param kieBasename
   * @param sessionType
   * @param facts
   * @param globalElement
   * @param buildSessionByKieBase
   */
  public RulesRequest(
      String sessionName,
      String kieBasename,
      SessionType sessionType,
      List<Object> facts,
      Map<String, Object> globalElement,
      boolean buildSessionByKieBase) {
    super();
    this.sessionName = sessionName;
    this.kieBasename = kieBasename;
    this.sessionType = sessionType;
    this.facts = facts;
    this.globalElement = globalElement;
    this.buildSessionByKieBase = buildSessionByKieBase;
  }

  /** @return the kieBasename */
  public String getKieBasename() {
    return kieBasename;
  }

  /** @return the buildSessionByKieBase */
  public boolean isBuildSessionByKieBase() {
    return buildSessionByKieBase;
  }

  /** @return the sessionName */
  public String getSessionName() {
    return sessionName;
  }

  /** @return the sessionType */
  public SessionType getSessionType() {
    return sessionType;
  }

  /** @return the facts */
  public List<Object> getFacts() {
    return facts;
  }

  /** @return the globalElement */
  public Map<String, Object> getGlobalElement() {
    return globalElement;
  }

  /** @param sessionName the sessionName to set */
  public void setSessionName(String sessionName) {
    this.sessionName = sessionName;
  }

  /** @param kieBasename the kieBasename to set */
  public void setKieBasename(String kieBasename) {
    this.kieBasename = kieBasename;
  }

  /** @param sessionType the sessionType to set */
  public void setSessionType(SessionType sessionType) {
    this.sessionType = sessionType;
  }

  /** @param facts the facts to set */
  public void setFacts(List<Object> facts) {
    this.facts = facts;
  }

  /** @param globalElement the globalElement to set */
  public void setGlobalElement(Map<String, Object> globalElement) {
    this.globalElement = globalElement;
  }

  /** @param buildSessionByKieBase the buildSessionByKieBase to set */
  public void setBuildSessionByKieBase(boolean buildSessionByKieBase) {
    this.buildSessionByKieBase = buildSessionByKieBase;
  }

  // Builder pattern
  public static class RulesRequestBuilder {

    private String sessionName;
    private String kieBasename;
    private SessionType sessionType;
    private List<Object> facts;
    private Map<String, Object> globalElement;
    private boolean buildSessionByKieBase;

    public RulesRequestBuilder() {
      // default constructor
    }

    public RulesRequestBuilder facts(List<Object> facts) {
      this.facts = facts;
      return this;
    }

    public RulesRequestBuilder buildSessionByKieBase(boolean buildSessionByKieBase) {
      this.buildSessionByKieBase = buildSessionByKieBase;
      return this;
    }

    public RulesRequestBuilder sessionName(String sessionName) {
      this.sessionName = sessionName;
      return this;
    }

    public RulesRequestBuilder sessionType(SessionType sessionType) {
      this.sessionType = sessionType;
      return this;
    }

    public RulesRequestBuilder globalService(Map<String, Object> globalService) {
      this.globalElement = globalService;
      return this;
    }

    public RulesRequestBuilder kieBasename(String kieBasename) {
      this.kieBasename = kieBasename;
      return this;
    }

    public RulesRequest build() {

      RulesRequest rulesRequest = new RulesRequest();
      rulesRequest.globalElement = this.globalElement;
      rulesRequest.sessionName = this.sessionName;
      rulesRequest.sessionType = this.sessionType;
      rulesRequest.kieBasename = this.kieBasename;
      rulesRequest.facts = this.facts;
      rulesRequest.buildSessionByKieBase = this.buildSessionByKieBase;

      return rulesRequest;
    }
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append("RulesRequest [sessionName=");
    builder.append(sessionName);
    builder.append(", kieBasename=");
    builder.append(kieBasename);
    builder.append(", sessionType=");
    builder.append(sessionType);
    builder.append(", facts=");
    builder.append(facts);
    builder.append(", globalElement=");
    builder.append(globalElement);
    builder.append(", buildSessionByKieBase=");
    builder.append(buildSessionByKieBase);
    builder.append("]");
    return builder.toString();
  }
}

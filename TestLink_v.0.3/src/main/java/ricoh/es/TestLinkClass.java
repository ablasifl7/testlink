package ricoh.es;

import java.util.HashMap;
import java.util.List;

import br.eti.kinoshita.testlinkjavaapi.constants.ExecutionStatus;
import br.eti.kinoshita.testlinkjavaapi.model.CustomField;

public class TestLinkClass {
	
	

	protected Integer getProjectId() {
		return projectId;
	}
	protected void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}
	protected String getProjectPrefix() {
		return projectPrefix;
	}
	protected void setProjectPrefix(String projectPrefix) {
		this.projectPrefix = projectPrefix;
	}
	protected String getProjectName() {
		return projectName;
	}
	protected void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	protected String getProjectNotes() {
		return projectNotes;
	}
	protected void setProjectNotes(String projectNotes) {
		this.projectNotes = projectNotes;
	}

	protected List<CustomField> getCustomFields() {
		return CustomFields;
	}
	protected void setCustomFields(List<CustomField> customFields) {
		CustomFields = customFields;
	}
	protected String getTestPlanProjectName() {
		return testPlanProjectName;
	}
	protected void setTestPlanProjectName(String testPlanProjectName) {
		this.testPlanProjectName = testPlanProjectName;
	}
	protected Integer getTestPlanId() {
		return testPlanId;
	}
	protected void setTestPlanId(Integer testPlanId) {
		this.testPlanId = testPlanId;
	}
	protected String getTestPlanName() {
		return testPlanName;
	}
	protected void setTestPlanName(String testPlanName) {
		this.testPlanName = testPlanName;
	}
	protected String getTestPlanNotes() {
		return testPlanNotes;
	}
	protected void setTestPlanNotes(String testPlanNotes) {
		this.testPlanNotes = testPlanNotes;
	}

	protected Integer getPlatformId() {
		return platformId;
	}
	protected void setPlatformId(Integer platformId) {
		this.platformId = platformId;
	}
	protected String getPlatformName() {
		return platformName;
	}
	protected void setPlatformName(String platformName) {
		this.platformName = platformName;
	}
	protected String getPlatformNotes() {
		return platformNotes;
	}
	protected void setPlatformNotes(String platformNotes) {
		this.platformNotes = platformNotes;
	}

	protected static boolean isError() {
		return error;
	}
	protected static void setError(boolean error) {
		TestLinkClass.error = error;
	}

	protected static String getProject() {
		return project;
	}
	protected static void setProject(String project) {
		TestLinkClass.project = project;
	}
	protected static String getTestPlant() {
		return testPlant;
	}
	protected static void setTestPlant(String testPlant) {
		TestLinkClass.testPlant = testPlant;
	}
	protected ExecutionStatus getTestCaseExecutionStatus() {
		return testCaseExecutionStatus;
	}
	protected void setTestCaseExecutionStatus(ExecutionStatus testCaseExecutionStatus) {
		this.testCaseExecutionStatus = testCaseExecutionStatus;
	}
	protected String getTestCaseFullExternalId() {
		return testCaseFullExternalId;
	}
	protected void setTestCaseFullExternalId(String testCaseFullExternalId) {
		this.testCaseFullExternalId = testCaseFullExternalId;
	}
	protected Integer getTestCaseId() {
		return testCaseId;
	}
	protected void setTestCaseId(Integer testCaseId) {
		this.testCaseId = testCaseId;
	}
	protected Integer getTestCaseInternalId() {
		return testCaseInternalId;
	}
	protected void setTestCaseInternalId(Integer testCaseInternalId) {
		this.testCaseInternalId = testCaseInternalId;
	}
	protected String getTestCaseName() {
		return testCaseName;
	}
	protected void setTestCaseName(String testCaseName) {
		this.testCaseName = testCaseName;
	}
	protected String getTestCasePreconditions() {
		return testCasePreconditions;
	}
	protected void setTestCasePreconditions(String testCasePreconditions) {
		this.testCasePreconditions = testCasePreconditions;
	}
	protected String getTestCaseSummary() {
		return testCaseSummary;
	}
	protected void setTestCaseSummary(String testCaseSummary) {
		this.testCaseSummary = testCaseSummary;
	}



	private List<CustomField> CustomFields;
	private String testPlanProjectName;
	private Integer testPlanId;
	private String testPlanName;
	private String testPlanNotes;
	private Integer platformId;
	private String platformName;
	private String platformNotes;
	private Integer projectId;
	private String projectPrefix;
	private String projectName;
	private String projectNotes;
	private static boolean error;
	private static String project;
	private static String testPlant;
	

	private ExecutionStatus testCaseExecutionStatus;
	private String testCaseFullExternalId;
	private Integer testCaseId;
	private Integer testCaseInternalId;
	private String testCaseName;
	private String testCasePreconditions;
	private String testCaseSummary;
	
	
	protected static HashMap<String,String> hmProjectPrefix = new HashMap<String,String>();
	protected static HashMap<String,String> hmPrefixProjName = new HashMap<String,String>();
	
	private static TestLink testLink;

	protected static TestLink getTestLink() {
		return testLink;
	}
	protected static void setTestLink(TestLink testLink) {
		TestLinkClass.testLink = testLink;
	}


	
}

package loader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Util {

    private static final String QAURL = "qaUrl";
    private static final String PRODURL = "prodUrl";
    private static final String LOGIN = "login";
    private static final String PASSWORD = "password";
    private static final String EMAILUSERNAME = "emailUserName";
    private static final String EMAILPASSWORD = "emailPassword";
    private static final String MAIL_FOLDERS = "mailFolders";
    private static final String JIRAUSERNAME = "jiraUserName";
    private static final String JIRAPASSWORD = "jiraPassword";
    private static final String JIRAURL = "jiraURL";
    private static final String JIRAJQL = "jirajql";
    private static final String JIRAPROJECT = "jiraproject";
    private static final String JIRAENVIRONMENT = "jiraenvironment";
    private static final String JIRAISSUETYPEID = "jiraIssueTypeId";
    private static final String MAILERRORADDRESS = "mailErrorAddress";
    private static final PropertiesLoader prop = new PropertiesLoader();

    public static String getQaUrl() {
        return prop.getPropertyValue(QAURL);
    }

    public static String getProdUrl() {
        return prop.getPropertyValue(PRODURL);
    }

    public static String getJiraUrl() {
        return prop.getPropertyValue(JIRAURL);
    }

    public static String getLogin() {
        return prop.getPropertyValue(LOGIN);
    }

    public static String getPassword() {
        return prop.getPropertyValue(PASSWORD);
    }

    public static String getUserNameMail() {
        return prop.getPropertyValue(EMAILUSERNAME);
    }

    public static String getPasswordMail() {
        return prop.getPropertyValue(EMAILPASSWORD);
    }

    public static List<String> getMailFolders() {
        String s = prop.getPropertyValue(MAIL_FOLDERS);
        return new ArrayList<>(Arrays.asList(s.split(",")));
    }

    public static String getJiraLogin() {
        return prop.getPropertyValue(JIRAUSERNAME);
    }

    public static String getJiraPassword() {
        return prop.getPropertyValue(JIRAPASSWORD);
    }


    public static String getJiraJql() {
        return prop.getPropertyValue(JIRAJQL);
    }

    public static String getJiraProject() {
        return prop.getPropertyValue(JIRAPROJECT);
    }

    public static String getJiraEnvironment() {
        return prop.getPropertyValue(JIRAENVIRONMENT);
    }

    public static String getJiraIssueTypeId() {
        return prop.getPropertyValue(JIRAISSUETYPEID);
    }

    public static String getMailErrorAdress() {
        return prop.getPropertyValue(MAILERRORADDRESS);
    }
}



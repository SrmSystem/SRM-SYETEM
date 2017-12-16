package com.qeweb.scm.basemodule.mail;


/**
 *
 * @author micfans
 */
public class MailTemplate {

    private String name = "";
    private String sender = "";
    private String subject = "";
    private String template = "";

    /**
     * Creates a new instance of MailTemplate
     */
    public MailTemplate() {
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}

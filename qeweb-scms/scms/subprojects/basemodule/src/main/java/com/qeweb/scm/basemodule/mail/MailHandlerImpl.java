package com.qeweb.scm.basemodule.mail;

import java.util.HashMap;
import org.xml.sax.*;

/**
 *
 * @author micfans
 */
public class MailHandlerImpl implements MailHandler {

    public static final boolean DEBUG = false;
    public static HashMap<String, MailTemplate> mailTemplateList = new HashMap();

    @Override
    public void handle_template(final java.lang.String data, final AttributeList meta) throws SAXException {
        if (DEBUG) {
            System.err.println("handle_template: " + data);
        }
        MailTemplate m = new MailTemplate();
        m.setName(meta.getValue("name"));
        m.setSender(meta.getValue("sender"));
        m.setSubject(meta.getValue("subject"));
        m.setTemplate(data);
        mailTemplateList.put(m.getName(), m);
    }

    @Override
    public void start_configs(final AttributeList meta) throws SAXException {
        if (DEBUG) {
            System.err.println("start_configs: " + meta);
        }
    }

    @Override
    public void end_configs() throws SAXException {
        if (DEBUG) {
            System.err.println("end_configs()");
        }
    }
}

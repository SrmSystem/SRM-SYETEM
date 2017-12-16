package com.qeweb.scm.basemodule.mail;

import org.xml.sax.*;

/**
 *
 * @author micfans
 */
public interface MailHandler {

    /**
     *
     * A data element event handling method.
     *
     * @param data value or null
     * @param meta attributes
     */
    public void handle_template(final java.lang.String data, final AttributeList meta) throws SAXException;

    /**
     *
     * A container element start event handling method.
     *
     * @param meta attributes
     */
    public void start_configs(final AttributeList meta) throws SAXException;

    /**
     *
     * A container element end event handling method.
     */
    public void end_configs() throws SAXException;
}

package com.qeweb.scm.basemodule.mail;

import org.xml.sax.*;

/**
 *
 * The class reads XML documents according to specified DTD and translates all
 * related events into MailHandler events. <p>Usage sample:
 * <pre>
 *    MailParser parser = new MailParser(...);
 *    parser.parse(new InputSource("..."));
 * </pre> <p><b>Warning:</b> the class is machine generated. DO NOT MODIFY</p>
 *
 * @author micfans
 */
public class MailParser implements DocumentHandler {

    private java.lang.StringBuffer buffer;
    private MailHandler handler;
    private java.util.Stack context;
    private EntityResolver resolver;

    /**
     *
     * Creates a parser instance.
     *
     * @param handler handler interface implementation (never <code>null</code>
     * @param resolver SAX entity resolver implementation or <code>null</code>.
     * It is recommended that it could be able to resolve at least the DTD.
     */
    public MailParser(final MailHandler handler, final EntityResolver resolver) {
        this.handler = handler;
        this.resolver = resolver;
        buffer = new StringBuffer(111);
        context = new java.util.Stack();
    }

    /**
     *
     * This SAX interface method is implemented by the parser.
     */
    @Override
    public final void setDocumentLocator(Locator locator) {
    }

    /**
     *
     * This SAX interface method is implemented by the parser.
     */
    @Override
    public final void startDocument() throws SAXException {
    }

    /**
     *
     * This SAX interface method is implemented by the parser.
     */
    @Override
    public final void endDocument() throws SAXException {
    }

    /**
     *
     * This SAX interface method is implemented by the parser.
     */
    @Override
    public final void startElement(java.lang.String name, AttributeList attrs) throws SAXException {
        dispatch(true);
        context.push(new Object[]{name, new org.xml.sax.helpers.AttributeListImpl(attrs)});
        if ("configs".equals(name)) {
            handler.start_configs(attrs);
        }
    }

    /**
     *
     * This SAX interface method is implemented by the parser.
     */
    public final void endElement(java.lang.String name) throws SAXException {
        dispatch(false);
        context.pop();
        if ("configs".equals(name)) {
            handler.end_configs();
        }
    }

    /**
     *
     * This SAX interface method is implemented by the parser.
     */
    public final void characters(char[] chars, int start, int len) throws SAXException {
        buffer.append(chars, start, len);
    }

    /**
     *
     * This SAX interface method is implemented by the parser.
     */
    public final void ignorableWhitespace(char[] chars, int start, int len) throws SAXException {
    }

    /**
     *
     * This SAX interface method is implemented by the parser.
     */
    public final void processingInstruction(java.lang.String target, java.lang.String data) throws SAXException {
    }

    private void dispatch(final boolean fireOnlyIfMixed) throws SAXException {
        if (fireOnlyIfMixed && buffer.length() == 0) {
            return; //skip it
        }
        Object[] ctx = (Object[]) context.peek();
        String here = (String) ctx[0];
        AttributeList attrs = (AttributeList) ctx[1];
        if ("template".equals(here)) {
            if (fireOnlyIfMixed) {
                throw new IllegalStateException("Unexpected characters() event! (Missing DTD?)");
            }
            handler.handle_template(buffer.length() == 0 ? null : buffer.toString(), attrs);
        } else {
            //do not care
        }
        buffer.delete(0, buffer.length());
    }

    /**
     *
     * The recognizer entry method taking an InputSource.
     *
     * @param input InputSource to be parsed.
     * @throws java.io.IOException on I/O error.
     * @throws SAXException propagated exception thrown by a DocumentHandler.
     * @throws javax.xml.parsers.ParserConfigurationException a parser
     * satisfining requested configuration can not be created.
     * @throws javax.xml.parsers.FactoryConfigurationRrror if the implementation
     * can not be instantiated.
     */
    public void parse(final InputSource input) throws SAXException, javax.xml.parsers.ParserConfigurationException, java.io.IOException {
        parse(input, this);
    }

    /**
     *
     * The recognizer entry method taking a URL.
     *
     * @param url URL source to be parsed.
     * @throws java.io.IOException on I/O error.
     * @throws SAXException propagated exception thrown by a DocumentHandler.
     * @throws javax.xml.parsers.ParserConfigurationException a parser
     * satisfining requested configuration can not be created.
     * @throws javax.xml.parsers.FactoryConfigurationRrror if the implementation
     * can not be instantiated.
     */
    public void parse(final java.net.URL url) throws SAXException, javax.xml.parsers.ParserConfigurationException, java.io.IOException {
        parse(new InputSource(url.toExternalForm()), this);
    }

    /**
     *
     * The recognizer entry method taking an Inputsource.
     *
     * @param input InputSource to be parsed.
     * @throws java.io.IOException on I/O error.
     * @throws SAXException propagated exception thrown by a DocumentHandler.
     * @throws javax.xml.parsers.ParserConfigurationException a parser
     * satisfining requested configuration can not be created.
     * @throws javax.xml.parsers.FactoryConfigurationRrror if the implementation
     * can not be instantiated.
     */
    public static void parse(final InputSource input, final MailHandler handler) throws SAXException, javax.xml.parsers.ParserConfigurationException, java.io.IOException {
        parse(input, new MailParser(handler, null));
    }

    /**
     *
     * The recognizer entry method taking a URL.
     *
     * @param url URL source to be parsed.
     * @throws java.io.IOException on I/O error.
     * @throws SAXException propagated exception thrown by a DocumentHandler.
     * @throws javax.xml.parsers.ParserConfigurationException a parser
     * satisfining requested configuration can not be created.
     * @throws javax.xml.parsers.FactoryConfigurationRrror if the implementation
     * can not be instantiated.
     */
    public static void parse(final java.net.URL url, final MailHandler handler) throws SAXException, javax.xml.parsers.ParserConfigurationException, java.io.IOException {
        parse(new InputSource(url.toExternalForm()), handler);
    }

    private static void parse(final InputSource input, final MailParser recognizer) throws SAXException, javax.xml.parsers.ParserConfigurationException, java.io.IOException {
        javax.xml.parsers.SAXParserFactory factory = javax.xml.parsers.SAXParserFactory.newInstance();
        //factory.setValidating(true);  //the code was generated according DTD
        factory.setNamespaceAware(false);  //the code was generated according DTD
        Parser parser = factory.newSAXParser().getParser();
        parser.setDocumentHandler(recognizer);
        parser.setErrorHandler(recognizer.getDefaultErrorHandler());
        if (recognizer.resolver != null) {
            parser.setEntityResolver(recognizer.resolver);
        }
        parser.parse(input);
    }

    /**
     *
     * Creates default error handler used by this parser.
     *
     * @return org.xml.sax.ErrorHandler implementation
     */
    protected ErrorHandler getDefaultErrorHandler() {
        return new ErrorHandler() {
            public void error(SAXParseException ex) throws SAXException {
                if (context.isEmpty()) {
                    System.err.println("Missing DOCTYPE.");
                }
                throw ex;
            }

            public void fatalError(SAXParseException ex) throws SAXException {
                throw ex;
            }

            public void warning(SAXParseException ex) throws SAXException {
                // ignore
            }
        };

    }
}

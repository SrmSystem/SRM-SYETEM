/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qeweb.scm.basemodule.mail;

import com.qeweb.scm.basemodule.mail.MailHandler;
import com.qeweb.scm.basemodule.mail.MailHandlerImpl;
import com.qeweb.scm.basemodule.mail.MailParser;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.FileSystemResourceLoader;
import org.springframework.core.io.Resource;
import org.xml.sax.InputSource;

/**
 *
 * @author micfans
 */
public class MailTemplateInitor  {

    /**
     * 邮件模板配置文件
     */
    private final String[] files = new String[]{"template/mail_template.xml"};

    /**
     *
     * @param config
     * @throws Exception
     */
    public void init(Object[] config) throws Exception {
        InputStream s1 = (InputStream) config[0];
        MailHandler mailHandler = new MailHandlerImpl();
        if (s1 != null) {
            MailParser.parse(new InputSource(s1), mailHandler);
            s1.close();
        }
    }
    
    public void init() throws Exception {
        String[] rs = getResource();
        Object[] config = new Object[rs.length];
        int i = 0;
        Resource[] res = readResource(rs);
        List<InputStream> iss = new ArrayList();
        for (Resource resource : res) {
            if (resource.exists()) {
                InputStream is = getClass().getClassLoader().getResourceAsStream("template/mail_template.xml");
                		//this.getClass().getResourceAsStream("/resource/res.txt");   
                		//new FileInputStream(resource.getFile());
                iss.add(is);
                String ext = FilenameUtils.getExtension(resource.getFilename());
                
                    config[i] = is;
                
            } 
            i++;
        }
        init(config);
        
        for (InputStream is : iss) {
            try {
                is.close();
            } catch (IOException ex) {
            }
        }
    }
    
    private static Resource[] readResource(String[] rs) {
        Resource[] res = new Resource[rs.length];
        int i = 0;
        for (String r : rs) {
            
            r = "classpath:" + r;
            
            Resource resource = new FileSystemResourceLoader().getResource(r);
            res[i] = resource;
            i++;
        }
        return res;
    }

    public String[] getResource() {
        return files;
    }

    public int sort() {
        return -1000;
    }

    public String[] monitorResources() {
        return files;
    }
}

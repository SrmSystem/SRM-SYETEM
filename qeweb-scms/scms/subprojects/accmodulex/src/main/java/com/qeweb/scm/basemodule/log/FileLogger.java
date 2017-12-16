package com.qeweb.scm.basemodule.log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.aop.support.AopUtils;

import com.qeweb.modules.utils.PropertiesUtil;
import com.qeweb.scm.basemodule.utils.ClassUtil;
import com.qeweb.scm.basemodule.utils.DateUtil;

public class FileLogger implements ILogger {
    
    private static final Log _logger = LogFactory.getLog(FileLogger.class);
    /**
     * log writer
     */
    private BufferedWriter logWriter;

    /**
     * 有缺陷，如果同一个类，不同方法被同时调用，将有日志输出的问题
     * 而且始终需要传递该类
     * @param object
     * @throws Exception
     */
    @Override
	public String init(ILog object) throws Exception {
        if (logWriter != null) {
            throw new Exception("Can not create logger, The logWriter is outting.");
        }
        String cName = AopUtils.getTargetClass(object).getSimpleName();  
        Date d = new Date();
        String dstr = DateUtil.dateToString(d, DateUtil.DATE_FORMAT_YYYYMMDD);
        String fstr = DateUtil.dateToString(d, "yyyyMMdd_HH_mm_ss");
        File file = new File(PropertiesUtil.getProperty("file.log.dir", "/logs") + "/down/" + cName + "/" + dstr + "/" + fstr + ".log");
        
        //add by zhangjiejun 2016.02.01 start
        if (file.exists()) {		//存在文件，则删除，防止测试时，修改系统时间的时候，报错
        	 _logger.info("file already exists, so delete " + file.getName());
        	file.delete();
        }
        //add by zhangjiejun 2016.02.01 end
        
        file.getParentFile().mkdirs();
        boolean b = file.createNewFile();
        if (!b || !file.exists()) {
            throw new Exception("Can not create logger file on " + file.getAbsolutePath());
        }
        logWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file),"UTF-8"));
        _logger.info("Open logger at " + file.getAbsolutePath());
        return file.getPath();
    }

    /**
     *
     */
    @Override
	public void destory() {
        try {
            if (logWriter != null) {
                logWriter.close();
            } else {
                _logger.warn("Log writer aleady null.");
            }
        } catch (IOException ex) {
            _logger.error("Close log file faild.", ex);
        } finally {
            logWriter = null;
        }
    }

    /**
     *
     * @param object
     * @return
     */
    protected String formatMessage(Object object) {
        String q = object.toString();
        if (!ClassUtil.isBasicClassType(object.getClass()) && !(object instanceof Map) && !(object instanceof Collection)) {
            try {
                StringBuilder sb = new StringBuilder();
                Field[] fields = object.getClass().getDeclaredFields();
                for (Field field : fields) {
                    field.setAccessible(true);
                    if (ClassUtil.isBasicClassType(field.getDeclaringClass())) {
                        sb.append(field.getName()).append(":").append(field.get(object)).append("\n");
                    }
                }
                q = sb.toString();
            } catch (SecurityException ex) {
                _logger.warn("Can not format message for bean '" + object + "', message: " + ex.getMessage());
            } catch (IllegalArgumentException ex) {
                _logger.warn("Can not format message for bean '" + object + "', message: " + ex.getMessage());
            } catch (IllegalAccessException ex) {
                _logger.warn("Can not format message for bean '" + object + "', message: " + ex.getMessage());
            }
        }
        return q;
    }

    /**
     *
     * @param object
     */
    @Override
	public void log(Object object) {
        String message = formatMessage(object);
        try {
            if (logWriter != null) {
                SimpleDateFormat _sdf = new SimpleDateFormat("HH:mm:ss");
                Date d = new Date();
                String t = _sdf.format(d);
                logWriter.write(t + "\t" + message + "\n");
                logWriter.flush();
            } else {
                _logger.warn("NO FILE LOG: " + object);
            }
        } catch (IOException ex) {
            _logger.warn("NO FILE LOG: " + object);
            _logger.error(ex.getMessage(), ex);
        }
    }
    
}

package com.qeweb.scm.basemodule.web.common;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jbarcode.JBarcode;
import org.jbarcode.encode.Code128Encoder;
import org.jbarcode.encode.Code39Encoder;
import org.jbarcode.encode.EAN13Encoder;
import org.jbarcode.paint.BaseLineTextPainter;
import org.jbarcode.paint.EAN13TextPainter;
import org.jbarcode.paint.WideRatioCodedPainter;
import org.jbarcode.paint.WidthCodedPainter;

public class BarCodeServlet extends HttpServlet {

	private static final long serialVersionUID = -6149461563818756066L;
	
	private static final Log _logger = LogFactory.getLog(BarCodeServlet.class);

    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setHeader("Cache-Control", "no-store");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/png");
        ServletOutputStream out = response.getOutputStream();
        String code = request.getParameter("code");
        String type = request.getParameter("type");
        if (StringUtils.isEmpty(type)) {   
            type = "code128";
        }
        try {
            JBarcode localJBarcode = null;
            if (type.equals("code128")) {
                localJBarcode = new JBarcode(Code128Encoder.getInstance(), WideRatioCodedPainter.getInstance(), BaseLineTextPainter.getInstance());
            } else if (type.equals("code39")) {
                localJBarcode = new JBarcode(Code39Encoder.getInstance(), WideRatioCodedPainter.getInstance(), BaseLineTextPainter.getInstance());
                //localJBarcode.setWideRatio(3);
            } else if (type.equals("ean13")) {
                localJBarcode = new JBarcode(EAN13Encoder.getInstance(), WidthCodedPainter.getInstance(), EAN13TextPainter.getInstance());
            } else {
                throw new Exception("Not support type for barcode '" + type + "'.");
            }
            localJBarcode.setCheckDigit(false);
            BufferedImage challenge = localJBarcode.createBarcode(code);
            ImageIO.write(challenge, "png", out);
            out.flush();
        } catch (Exception ex) {
            _logger.error("Gen barcode faild for '" + code + "'.", ex);
        } finally {
            out.close();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}

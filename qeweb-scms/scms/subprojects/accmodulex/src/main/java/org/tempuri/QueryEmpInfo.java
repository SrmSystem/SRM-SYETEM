/**
 * QueryEmpInfo.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.tempuri;

public class QueryEmpInfo  implements java.io.Serializable {
    private java.lang.String lastdatetime;

    public QueryEmpInfo() {
    }

    public QueryEmpInfo(
           java.lang.String lastdatetime) {
           this.lastdatetime = lastdatetime;
    }


    /**
     * Gets the lastdatetime value for this QueryEmpInfo.
     * 
     * @return lastdatetime
     */
    public java.lang.String getLastdatetime() {
        return lastdatetime;
    }


    /**
     * Sets the lastdatetime value for this QueryEmpInfo.
     * 
     * @param lastdatetime
     */
    public void setLastdatetime(java.lang.String lastdatetime) {
        this.lastdatetime = lastdatetime;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof QueryEmpInfo)) return false;
        QueryEmpInfo other = (QueryEmpInfo) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.lastdatetime==null && other.getLastdatetime()==null) || 
             (this.lastdatetime!=null &&
              this.lastdatetime.equals(other.getLastdatetime())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getLastdatetime() != null) {
            _hashCode += getLastdatetime().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(QueryEmpInfo.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://tempuri.org/", ">QueryEmpInfo"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("lastdatetime");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "lastdatetime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}

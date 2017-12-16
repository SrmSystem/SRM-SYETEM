/**
 * IsAuthenticatedResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.tempuri;

public class IsAuthenticatedResponse  implements java.io.Serializable {
    private boolean isAuthenticatedResult;

    public IsAuthenticatedResponse() {
    }

    public IsAuthenticatedResponse(
           boolean isAuthenticatedResult) {
           this.isAuthenticatedResult = isAuthenticatedResult;
    }


    /**
     * Gets the isAuthenticatedResult value for this IsAuthenticatedResponse.
     * 
     * @return isAuthenticatedResult
     */
    public boolean isIsAuthenticatedResult() {
        return isAuthenticatedResult;
    }


    /**
     * Sets the isAuthenticatedResult value for this IsAuthenticatedResponse.
     * 
     * @param isAuthenticatedResult
     */
    public void setIsAuthenticatedResult(boolean isAuthenticatedResult) {
        this.isAuthenticatedResult = isAuthenticatedResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof IsAuthenticatedResponse)) return false;
        IsAuthenticatedResponse other = (IsAuthenticatedResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.isAuthenticatedResult == other.isIsAuthenticatedResult();
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
        _hashCode += (isIsAuthenticatedResult() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(IsAuthenticatedResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://tempuri.org/", ">IsAuthenticatedResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("isAuthenticatedResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "IsAuthenticatedResult"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
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

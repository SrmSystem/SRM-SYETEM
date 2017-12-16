/**
 * QueryEmpInfoID.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.tempuri;

public class QueryEmpInfoID  implements java.io.Serializable {
    private int startID;

    private int endID;

    public QueryEmpInfoID() {
    }

    public QueryEmpInfoID(
           int startID,
           int endID) {
           this.startID = startID;
           this.endID = endID;
    }


    /**
     * Gets the startID value for this QueryEmpInfoID.
     * 
     * @return startID
     */
    public int getStartID() {
        return startID;
    }


    /**
     * Sets the startID value for this QueryEmpInfoID.
     * 
     * @param startID
     */
    public void setStartID(int startID) {
        this.startID = startID;
    }


    /**
     * Gets the endID value for this QueryEmpInfoID.
     * 
     * @return endID
     */
    public int getEndID() {
        return endID;
    }


    /**
     * Sets the endID value for this QueryEmpInfoID.
     * 
     * @param endID
     */
    public void setEndID(int endID) {
        this.endID = endID;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof QueryEmpInfoID)) return false;
        QueryEmpInfoID other = (QueryEmpInfoID) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.startID == other.getStartID() &&
            this.endID == other.getEndID();
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
        _hashCode += getStartID();
        _hashCode += getEndID();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(QueryEmpInfoID.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://tempuri.org/", ">QueryEmpInfoID"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("startID");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "startID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("endID");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "endID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
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

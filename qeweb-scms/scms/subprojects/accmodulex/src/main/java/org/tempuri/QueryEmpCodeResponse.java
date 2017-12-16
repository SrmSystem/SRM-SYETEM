/**
 * QueryEmpCodeResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.tempuri;

public class QueryEmpCodeResponse  implements java.io.Serializable {
    private java.lang.String queryEmpCodeResult;

    public QueryEmpCodeResponse() {
    }

    public QueryEmpCodeResponse(
           java.lang.String queryEmpCodeResult) {
           this.queryEmpCodeResult = queryEmpCodeResult;
    }


    /**
     * Gets the queryEmpCodeResult value for this QueryEmpCodeResponse.
     * 
     * @return queryEmpCodeResult
     */
    public java.lang.String getQueryEmpCodeResult() {
        return queryEmpCodeResult;
    }


    /**
     * Sets the queryEmpCodeResult value for this QueryEmpCodeResponse.
     * 
     * @param queryEmpCodeResult
     */
    public void setQueryEmpCodeResult(java.lang.String queryEmpCodeResult) {
        this.queryEmpCodeResult = queryEmpCodeResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof QueryEmpCodeResponse)) return false;
        QueryEmpCodeResponse other = (QueryEmpCodeResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.queryEmpCodeResult==null && other.getQueryEmpCodeResult()==null) || 
             (this.queryEmpCodeResult!=null &&
              this.queryEmpCodeResult.equals(other.getQueryEmpCodeResult())));
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
        if (getQueryEmpCodeResult() != null) {
            _hashCode += getQueryEmpCodeResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(QueryEmpCodeResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://tempuri.org/", ">QueryEmpCodeResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("queryEmpCodeResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "QueryEmpCodeResult"));
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

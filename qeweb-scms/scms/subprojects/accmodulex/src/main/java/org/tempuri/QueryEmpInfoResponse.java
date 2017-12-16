/**
 * QueryEmpInfoResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.tempuri;

public class QueryEmpInfoResponse  implements java.io.Serializable {
    private java.lang.String queryEmpInfoResult;

    public QueryEmpInfoResponse() {
    }

    public QueryEmpInfoResponse(
           java.lang.String queryEmpInfoResult) {
           this.queryEmpInfoResult = queryEmpInfoResult;
    }


    /**
     * Gets the queryEmpInfoResult value for this QueryEmpInfoResponse.
     * 
     * @return queryEmpInfoResult
     */
    public java.lang.String getQueryEmpInfoResult() {
        return queryEmpInfoResult;
    }


    /**
     * Sets the queryEmpInfoResult value for this QueryEmpInfoResponse.
     * 
     * @param queryEmpInfoResult
     */
    public void setQueryEmpInfoResult(java.lang.String queryEmpInfoResult) {
        this.queryEmpInfoResult = queryEmpInfoResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof QueryEmpInfoResponse)) return false;
        QueryEmpInfoResponse other = (QueryEmpInfoResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.queryEmpInfoResult==null && other.getQueryEmpInfoResult()==null) || 
             (this.queryEmpInfoResult!=null &&
              this.queryEmpInfoResult.equals(other.getQueryEmpInfoResult())));
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
        if (getQueryEmpInfoResult() != null) {
            _hashCode += getQueryEmpInfoResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(QueryEmpInfoResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://tempuri.org/", ">QueryEmpInfoResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("queryEmpInfoResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "QueryEmpInfoResult"));
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

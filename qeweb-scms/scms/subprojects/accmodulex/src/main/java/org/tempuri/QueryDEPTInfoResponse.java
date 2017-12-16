/**
 * QueryDEPTInfoResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.tempuri;

public class QueryDEPTInfoResponse  implements java.io.Serializable {
    private java.lang.String queryDEPTInfoResult;

    public QueryDEPTInfoResponse() {
    }

    public QueryDEPTInfoResponse(
           java.lang.String queryDEPTInfoResult) {
           this.queryDEPTInfoResult = queryDEPTInfoResult;
    }


    /**
     * Gets the queryDEPTInfoResult value for this QueryDEPTInfoResponse.
     * 
     * @return queryDEPTInfoResult
     */
    public java.lang.String getQueryDEPTInfoResult() {
        return queryDEPTInfoResult;
    }


    /**
     * Sets the queryDEPTInfoResult value for this QueryDEPTInfoResponse.
     * 
     * @param queryDEPTInfoResult
     */
    public void setQueryDEPTInfoResult(java.lang.String queryDEPTInfoResult) {
        this.queryDEPTInfoResult = queryDEPTInfoResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof QueryDEPTInfoResponse)) return false;
        QueryDEPTInfoResponse other = (QueryDEPTInfoResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.queryDEPTInfoResult==null && other.getQueryDEPTInfoResult()==null) || 
             (this.queryDEPTInfoResult!=null &&
              this.queryDEPTInfoResult.equals(other.getQueryDEPTInfoResult())));
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
        if (getQueryDEPTInfoResult() != null) {
            _hashCode += getQueryDEPTInfoResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(QueryDEPTInfoResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://tempuri.org/", ">QueryDEPTInfoResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("queryDEPTInfoResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "QueryDEPTInfoResult"));
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

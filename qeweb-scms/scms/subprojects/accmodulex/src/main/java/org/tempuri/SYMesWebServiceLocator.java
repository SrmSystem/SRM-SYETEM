/**
 * SYMesWebServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.tempuri;

import com.qeweb.modules.utils.PropertiesUtil;

public class SYMesWebServiceLocator extends org.apache.axis.client.Service implements org.tempuri.SYMesWebService {

    public SYMesWebServiceLocator() {
    }


    public SYMesWebServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public SYMesWebServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for SYMesWebServiceSoap12
    private java.lang.String SYMesWebServiceSoap12_address = PropertiesUtil.getProperty("webservice.login.url","");

    public java.lang.String getSYMesWebServiceSoap12Address() {
        return SYMesWebServiceSoap12_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String SYMesWebServiceSoap12WSDDServiceName = "SYMesWebServiceSoap12";

    public java.lang.String getSYMesWebServiceSoap12WSDDServiceName() {
        return SYMesWebServiceSoap12WSDDServiceName;
    }

    public void setSYMesWebServiceSoap12WSDDServiceName(java.lang.String name) {
        SYMesWebServiceSoap12WSDDServiceName = name;
    }

    public org.tempuri.SYMesWebServiceSoap getSYMesWebServiceSoap12() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(SYMesWebServiceSoap12_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getSYMesWebServiceSoap12(endpoint);
    }

    public org.tempuri.SYMesWebServiceSoap getSYMesWebServiceSoap12(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            org.tempuri.SYMesWebServiceSoap12Stub _stub = new org.tempuri.SYMesWebServiceSoap12Stub(portAddress, this);
            _stub.setPortName(getSYMesWebServiceSoap12WSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setSYMesWebServiceSoap12EndpointAddress(java.lang.String address) {
        SYMesWebServiceSoap12_address = address;
    }


    // Use to get a proxy class for SYMesWebServiceSoap
    private java.lang.String SYMesWebServiceSoap_address = PropertiesUtil.getProperty("webservice.login.url","");

    public java.lang.String getSYMesWebServiceSoapAddress() {
        return SYMesWebServiceSoap_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String SYMesWebServiceSoapWSDDServiceName = "SYMesWebServiceSoap";

    public java.lang.String getSYMesWebServiceSoapWSDDServiceName() {
        return SYMesWebServiceSoapWSDDServiceName;
    }

    public void setSYMesWebServiceSoapWSDDServiceName(java.lang.String name) {
        SYMesWebServiceSoapWSDDServiceName = name;
    }

    public org.tempuri.SYMesWebServiceSoap getSYMesWebServiceSoap() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(SYMesWebServiceSoap_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getSYMesWebServiceSoap(endpoint);
    }

    public org.tempuri.SYMesWebServiceSoap getSYMesWebServiceSoap(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            org.tempuri.SYMesWebServiceSoapStub _stub = new org.tempuri.SYMesWebServiceSoapStub(portAddress, this);
            _stub.setPortName(getSYMesWebServiceSoapWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setSYMesWebServiceSoapEndpointAddress(java.lang.String address) {
        SYMesWebServiceSoap_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     * This service has multiple ports for a given interface;
     * the proxy implementation returned may be indeterminate.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (org.tempuri.SYMesWebServiceSoap.class.isAssignableFrom(serviceEndpointInterface)) {
                org.tempuri.SYMesWebServiceSoap12Stub _stub = new org.tempuri.SYMesWebServiceSoap12Stub(new java.net.URL(SYMesWebServiceSoap12_address), this);
                _stub.setPortName(getSYMesWebServiceSoap12WSDDServiceName());
                return _stub;
            }
            if (org.tempuri.SYMesWebServiceSoap.class.isAssignableFrom(serviceEndpointInterface)) {
                org.tempuri.SYMesWebServiceSoapStub _stub = new org.tempuri.SYMesWebServiceSoapStub(new java.net.URL(SYMesWebServiceSoap_address), this);
                _stub.setPortName(getSYMesWebServiceSoapWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("SYMesWebServiceSoap12".equals(inputPortName)) {
            return getSYMesWebServiceSoap12();
        }
        else if ("SYMesWebServiceSoap".equals(inputPortName)) {
            return getSYMesWebServiceSoap();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://tempuri.org/", "SYMesWebService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://tempuri.org/", "SYMesWebServiceSoap12"));
            ports.add(new javax.xml.namespace.QName("http://tempuri.org/", "SYMesWebServiceSoap"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("SYMesWebServiceSoap12".equals(portName)) {
            setSYMesWebServiceSoap12EndpointAddress(address);
        }
        else 
if ("SYMesWebServiceSoap".equals(portName)) {
            setSYMesWebServiceSoapEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}

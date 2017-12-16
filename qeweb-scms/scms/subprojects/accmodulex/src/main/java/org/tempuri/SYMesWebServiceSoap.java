/**
 * SYMesWebServiceSoap.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.tempuri;

public interface SYMesWebServiceSoap extends java.rmi.Remote {
    public org.tempuri.GetEmpInfoResponseGetEmpInfoResult getEmpInfo() throws java.rmi.RemoteException;
    public java.lang.String queryEmpInfo(java.lang.String lastdatetime) throws java.rmi.RemoteException;
    public java.lang.String queryEmpCode(java.lang.String code) throws java.rmi.RemoteException;
    public java.lang.String queryEmpSfz(java.lang.String sfz) throws java.rmi.RemoteException;
    public java.lang.String queryEmpInfoID(int startID, int endID) throws java.rmi.RemoteException;
    public java.lang.String queryDEPTInfo() throws java.rmi.RemoteException;
    public boolean isAuthenticated(java.lang.String username, java.lang.String password) throws java.rmi.RemoteException;
}

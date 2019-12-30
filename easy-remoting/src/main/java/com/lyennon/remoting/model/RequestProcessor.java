package com.lyennon.remoting.model;

public interface RequestProcessor {

    RemotingTransporter processRequest(RemotingTransporter request);
}

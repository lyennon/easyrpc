package com.lyennon.common.exception;

/**
 * @author yong.liang 2019/12/11
 */
public class RemotingException extends Exception {

    private static final long serialVersionUID = -7532093092603532518L;

    public RemotingException(String message) {
        super(message);
    }


    public RemotingException(String message, Throwable cause) {
        super(message, cause);
    }
}
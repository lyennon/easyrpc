package com.lyennon.common.exception;

/**
 * @author yong.liang
 */
public class RemotingCommmonCustomException extends RemotingException{

    private static final long serialVersionUID = 1845091289462462017L;

    public RemotingCommmonCustomException(String message) {
        super(message);
    }

    public RemotingCommmonCustomException(String message, Throwable cause) {
        super(message, cause);
    }
}

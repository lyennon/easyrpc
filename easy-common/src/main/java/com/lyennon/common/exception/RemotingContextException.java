package com.lyennon.common.exception;

/**
 * @author yong.liang 2019/12/11
 */
public class RemotingContextException extends RemotingException {
    private static final long serialVersionUID = 8667116691623853270L;

    public RemotingContextException(String message) {
        super(message);
    }

    public RemotingContextException(String message, Throwable cause) {
        super(message, cause);
    }
}

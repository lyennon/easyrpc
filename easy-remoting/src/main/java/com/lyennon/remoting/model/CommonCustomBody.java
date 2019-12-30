package com.lyennon.remoting.model;

import com.lyennon.common.exception.RemotingCommmonCustomException;

/**
 * @author yong.liang 2019/12/1
 */
public interface CommonCustomBody {

    void checkFields() throws RemotingCommmonCustomException;

}

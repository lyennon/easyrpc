package com.lyennon.remoting.common;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

public class RemotingUtils {

    public static final String OS_NAME = System.getProperty("os.name");
    private static boolean isLinuxPlatform = false;
    private static boolean isWindowsPlatform = false;
    static {
        if (OS_NAME != null && OS_NAME.toLowerCase().contains("linux")){
            isLinuxPlatform = true;
        }else if (OS_NAME != null && OS_NAME.toLowerCase().contains("windows")){
            isWindowsPlatform = true;
        }
    }

    public static boolean isWindowsPlatform() {
        return isWindowsPlatform;
    }

    public static boolean isLinuxPlatform() {
        return isLinuxPlatform;
    }

    public static SocketAddress string2SocketAddress(final String addr) {
        String[] s = addr.split(":");
        return new InetSocketAddress(s[0], Integer.parseInt(s[1]));
    }
}

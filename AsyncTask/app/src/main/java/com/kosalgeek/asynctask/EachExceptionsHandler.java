package com.kosalgeek.asynctask;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.ProtocolException;

/**
 * Created by Oum Saokosal (2016)
 * Source Code at https://github.com/kosalgeek/generic_asynctask_v2
 * If you have any questions or bugs, drop a comment on
 * my Facebook Page https://facebook.com/kosalgeek or
 * Twitter https://twitter.com/kosalgeek
 * Watch video tutorial at https://youtube.com/user/oumsaokosal
 */
public interface EachExceptionsHandler {
    void handleIOException(IOException e);
    void handleMalformedURLException(MalformedURLException e);
    void handleProtocolException(ProtocolException e);
    void handleUnsupportedEncodingException(UnsupportedEncodingException e);
}

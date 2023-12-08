package kr.co.library.api.common.util;

import org.joda.time.Instant;

import java.util.Date;


public class DateUtils {

    public Instant getInstantNow(){
        return Instant.now();
    }

    public Date getInstantNowToDate(){
        return Instant.now().toDate();
    }

}

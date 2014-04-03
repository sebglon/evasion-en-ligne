/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evasion.cloud.api.data.adapter;

import java.util.Calendar;
import java.util.Date;
import javax.xml.bind.DatatypeConverter;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 *
 * @author sgl
 */
public class DateAdapter extends XmlAdapter<String, Date> {

    @Override
    public String marshal(Date v) throws Exception {
        if (v == null) {
            return null;
        } else {
            Calendar tCalendar = Calendar.getInstance();
            tCalendar.setTime(v);
            return DatatypeConverter.printDate(tCalendar);
        }
    }

    @Override
    public Date unmarshal(String v) throws Exception {
        if (v.length() == 0) {
            return null;
        }
        return javax.xml.bind.DatatypeConverter.parseDateTime(v).getTime();
    }

}

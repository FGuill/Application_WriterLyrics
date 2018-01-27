package com.guilla.lyricswriter.Utils;

import com.guilla.lyricswriter.BO.Group;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Comparator;


public class SortDate implements Comparator<Group> {

        public int compare(Group anEmployee, Group anotherEmployee) {
            DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
            String dateanEmployee= df.format(anEmployee.getDate());
            String dateanotherEmployee= df.format(anotherEmployee.getDate());
            return dateanEmployee.compareTo(dateanotherEmployee);
        }
    }
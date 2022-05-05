package com.faircom.playground.util;

import com.google.common.flogger.FluentLogger;
import org.apache.commons.beanutils.PropertyUtils;

import java.util.Comparator;
import java.util.List;

public class CompoundComparator implements Comparator<Object> {

    private static final FluentLogger logger = FluentLogger.forEnclosingClass();

    private List<SortField> sortFields;

    public CompoundComparator(List<SortField> sortFields) {

        this.sortFields = sortFields;
    }

    public int compare(Object o1, Object o2) {

        int result = 0;

        for (SortField sortField : sortFields) {

            try {

                Object p1 = PropertyUtils.getProperty(o1, sortField.getName());
                Object p2 = PropertyUtils.getProperty(o2, sortField.getName());

                String typeName = PropertyUtils.getPropertyType(o1, sortField.getName()).getName();

                result = switch (typeName) {

                    case "String" -> ((String) p1).compareTo((String) p2);
                    case "int" -> ((Integer) p1).compareTo((Integer) p2);
                    case "float" -> ((Float) p1).compareTo((Float) p2);
                    default -> 0;
                };

                if (sortField.getDir().equals(SortDirection.DESC))
                    result = result * -1;

                if (result != 0)
                    break;

            }
            catch (Exception ex) {

                logger.atSevere().withCause(ex).log(ex.getMessage());
            }
        }

        return result;
    }
}

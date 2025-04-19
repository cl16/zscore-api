package com.zscore_api.zscore_api.helper;

import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Generic validating of params that can apply to many services.
 */
public class ParamValidator {

    private static final String pageArgPatternString = "0|[1-9][0-9]*";
    private static final String sortArgPatternString = "([a-zA-Z]+)(,(?:asc|desc))?";

    public static void validatePagingAndSortingArgs(Map<String, String> params, Set<String> validSortArgs) {
        if (!params.get("page").matches(pageArgPatternString)) {
            throw new IllegalArgumentException("Invalid request argument: page argument must be integer >= 0");
        }

        Pattern sortArgPattern = Pattern.compile(sortArgPatternString);
        Matcher sortArgMatcher = sortArgPattern.matcher(params.get("sort"));
        if (!sortArgMatcher.matches()) {
            throw new IllegalArgumentException("Invalid request argument: sort argument must be '[column],[asc or desc]'");
        }
        String sortColumn = sortArgMatcher.group(1);

        if (!validSortArgs.contains(sortColumn)) {
            throw new IllegalArgumentException("Invalid request argument: unexpected sort column provided, expected: " +
                            String.join(", ", validSortArgs));
        }
    }
}

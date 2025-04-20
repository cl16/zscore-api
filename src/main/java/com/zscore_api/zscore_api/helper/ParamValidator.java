package com.zscore_api.zscore_api.helper;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Generic validating of params that can apply to many services.
 */
public class ParamValidator {

    private static final Set<String> pagingAndSortingParams = new HashSet<>(Arrays.asList("page", "sort"));
    private static final String pageArgPatternString = "0|[1-9][0-9]*";
    private static final String sortArgPatternString = "([a-zA-Z]+)(,(?:asc|desc))?";

    public static void validatePagingAndSortingArgs(Map<String, String> params, Set<String> validSortArgs) {
        if (params.containsKey("page")) {
            if (!params.get("page").matches(pageArgPatternString)) {
                throw new IllegalArgumentException("Invalid request argument: page argument must be integer >= 0");
            }
        }

        if (params.containsKey("sort")) {
            Pattern sortArgPattern = Pattern.compile(sortArgPatternString);
            Matcher sortArgMatcher = sortArgPattern.matcher(params.get("sort"));
            if (!sortArgMatcher.matches()) {
                throw new IllegalArgumentException("Invalid request arguments: sort argument must be '[column],[asc or desc]'");
            }
            String sortColumn = sortArgMatcher.group(1);

            if (!validSortArgs.contains(sortColumn)) {
                throw new IllegalArgumentException("Invalid request arguments: unexpected sort column provided, expected 1 of " +
                        String.join(", ", validSortArgs));
            }
        }
    }

    /**
     * Ensure provided params contain no params that are 1) not in set of valid domain params and 2) not in set
     * of paging & sorting params.
     * @param params
     * @param validParams
     */
    public static void validateDomainRequestParams(Map<String, String> params, Set<String> validParams) {
        Set<String> nonPagingAndSortingParams = ParamValidator.nonPagingAndSortingParams(params.keySet());
        if (!validParams.containsAll(nonPagingAndSortingParams)) {
            Set<String> unexpected = SetOps.subtract(nonPagingAndSortingParams, validParams);
            throw new IllegalArgumentException("Invalid request parameters: " + String.join(", ", unexpected));
        }
    }

    public static void blockAllRequestParams(Map<String, String> params) {
        Set<String> providedParams = params.keySet();
        if (!providedParams.isEmpty()) {
            throw new IllegalArgumentException("Invalid request parameters: " + String.join(", ", providedParams));
        }
    }

    public static Set<String> nonPagingAndSortingParams(Set<String> params) {
        return SetOps.subtract(params, pagingAndSortingParams);
    }
}

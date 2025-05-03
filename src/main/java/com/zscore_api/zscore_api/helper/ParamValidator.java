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

    private static final Set<String> pagingAndSortingParams = new HashSet<>(Arrays.asList("page", "size", "sort"));
    private static final String pageArgPatternString = "0|[1-9][0-9]*";
    private static final String sortArgPatternString = "([a-zA-Z]+)(,(?:asc|desc))?";
    private static final String integerPatternString = "^-?[0-9]+$";
    private static final String numericPatternString = "^-?[0-9]+\\.?[0-9]*$";

    public static void validatePagingAndSortingArgs(Map<String, String> params, Set<String> validSortArgs) {
        if (params.containsKey("page")) {
            if (!params.get("page").matches(pageArgPatternString)) {
                throw new IllegalArgumentException("Invalid request argument: page argument must be integer >= 0");
            }
        }

        if (params.containsKey("size")) {
            if (!params.get("size").matches(integerPatternString)) {
                throw new IllegalArgumentException("Invalid request argument: size argument must be integer");
            }
            if (Integer.parseInt(params.get("size")) < 1) {
                throw new IllegalArgumentException("Invalid request argument: size argument must be >= 1");
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

    public static void validateNumericArg(String param, String arg) {
        if (!arg.matches(numericPatternString)) {
            throw new IllegalArgumentException(String.format("Invalid argument type: param %s has non-numeric type %s", param, arg));
        }
    }

    public static void validateNumericRange(String param, String arg, Float min, Float max) {
        float argFloat = Float.parseFloat(arg);
        if (!(argFloat >= min) || !(argFloat <= max)) {
            throw new IllegalArgumentException(
                String.format(
                    "Invalid argument values: param %s value %s must be between %s and %s, inclusive",
                    param,
                    arg,
                    min,
                    max
                )
            );
        }
    }

    /**
     * Validate that argMin is less than or equal to argMax. If not, throw IllegalArgumentException with param names in message.
     * @param paramMin Parameter name for minimum value
     * @param paramMax Parameter name for maximum value
     * @param argMin Minimum value
     * @param argMax Maximum value
     */
    public static void validateMinLOEMax(String paramMin, String paramMax, String argMin, String argMax) {
        float argMinFloat = Float.parseFloat(argMin);
        float argMaxFloat = Float.parseFloat(argMax);
        if (!(argMinFloat <= argMaxFloat)) {
            throw new IllegalArgumentException(
                String.format(
                    "Invalid argument values: %s value %s must be less then or equal to %s value %s",
                    paramMin,
                    argMin,
                    paramMax,
                    argMax
                )
            );
        }
    }

    public static void validateIncompatibleParams(Map<String, String> params, Set<String> incompatible) {
        if (SetOps.numIntersecting(params.keySet(), incompatible) > 1) {
            throw new IllegalArgumentException(
                    "Invalid request parameters: only 1 allowed from " + String.join(", ", incompatible)
            );
        }
    }
}

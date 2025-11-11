package com.zscore_api.zscore_api.helper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
    private static final String sortArgPatternString = "([a-zA-Z_]+)(,(?:asc|desc))?";
    private static final String integerPatternString = "^-?[0-9]+$";
    private static final String numericPatternString = "^-?[0-9]+\\.?[0-9]*$";

    private static final Logger logger = LogManager.getLogger(ParamValidator.class);

    public static void validatePagingAndSortingArgs(Map<String, String> params, Set<String> validSortArgs) {
        logger.debug(String.format("params %s, validSortArgs %s", params, validSortArgs));
        if (params.containsKey("page")) {
            if (!params.get("page").matches(pageArgPatternString)) {
                logger.debug(String.format("page arg %s does not match pattern", params.get("page")));
                throw new IllegalArgumentException("Invalid request argument: page argument must be integer >= 0");
            }
        }

        if (params.containsKey("size")) {
            if (!params.get("size").matches(integerPatternString)) {
                logger.debug(String.format("size arg %s does not match pattern", params.get("size")));
                throw new IllegalArgumentException("Invalid request argument: size argument must be integer");
            }
            if (Integer.parseInt(params.get("size")) < 1) {
                logger.debug(String.format("size arg %s is < 1", params.get("size")));
                throw new IllegalArgumentException("Invalid request argument: size argument must be >= 1");
            }
        }

        if (params.containsKey("sort")) {
            Pattern sortArgPattern = Pattern.compile(sortArgPatternString);
            Matcher sortArgMatcher = sortArgPattern.matcher(params.get("sort"));
            if (!sortArgMatcher.matches()) {
                logger.debug(String.format("size arg %s does not match pattern", params.get("sort")));
                throw new IllegalArgumentException("Invalid request arguments: sort argument must be '[column],[asc or desc]'");
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
            logger.debug(String.format("invalid request parameters provided %s", unexpected));
            throw new IllegalArgumentException("Invalid request parameters: " + String.join(", ", unexpected));
        }
    }

    public static void blockAllRequestParams(Map<String, String> params) {
        Set<String> providedParams = params.keySet();
        if (!providedParams.isEmpty()) {
            logger.debug(String.format("request parameters included but none accepted: %s", params));
            throw new IllegalArgumentException("Invalid request parameters: " + String.join(", ", providedParams));
        }
    }

    public static Set<String> nonPagingAndSortingParams(Set<String> params) {
        return SetOps.subtract(params, pagingAndSortingParams);
    }

    public static void validateNumericArg(String param, String arg) {
        if (!arg.matches(numericPatternString)) {
            logger.debug(String.format("param %s value %s failed numeric arg validation", param, arg));
            throw new IllegalArgumentException(String.format("Invalid argument type: param %s has non-numeric type %s", param, arg));
        }
    }

    public static void validateNumericRange(String param, String arg, Float min, Float max) {
        float argFloat = Float.parseFloat(arg);
        if (!(argFloat >= min) || !(argFloat <= max)) {
            logger.debug(String.format("numeric range test failed on param %s for %s <= %s <= %s", min, arg, max));
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
            logger.debug(String.format("min <= max test failed on params %s, %s for %s <= %s", paramMin, paramMax, argMin, argMax));
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

    public static void validateMutuallyIncompatibleParams(Map<String, String> params, Set<String> incompatible) {
        if (SetOps.numIntersecting(params.keySet(), incompatible) > 1) {
            logger.debug(String.format("params %s contain mutually-incompatible params from %s", params, incompatible));
            throw new IllegalArgumentException(
                    "Invalid request parameters: only 1 allowed from " + String.join(", ", incompatible)
            );
        }
    }
}

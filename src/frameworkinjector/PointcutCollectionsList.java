/**
 *
      ___         ___           ___           ___           ___
     /  /\       /  /\         /  /\         /  /\         /  /\
    /  /::\     /  /:/_       /  /::\       /  /:/        /  /:/_
   /  /:/\:\   /  /:/ /\     /  /:/\:\     /  /:/        /  /:/ /\
  /  /:/ /:/  /  /:/ /:/_   /  /:/ /::\   /  /:/  ___   /  /:/ /:/_
 /__/:/ /:/  /__/:/ /:/ /\ /__/:/ /:/\:\ /__/:/  /  /\ /__/:/ /:/ /\
 \  \:\/:/   \  \:\/:/ /:/ \  \:\/:/__\/ \  \:\ /  /:/ \  \:\/:/ /:/
  \  \::/     \  \::/ /:/   \  \::/       \  \:\  /:/   \  \::/ /:/
   \  \:\      \  \:\/:/     \  \:\        \  \:\/:/     \  \:\/:/
    \  \:\      \  \::/       \  \:\        \  \::/       \  \::/
     \__\/       \__\/         \__\/         \__\/         \__\/
      ___           ___           ___           ___           ___
     /__/\         /  /\         /__/|         /  /\         /  /\
    |  |::\       /  /::\       |  |:|        /  /:/_       /  /::\
    |  |:|:\     /  /:/\:\      |  |:|       /  /:/ /\     /  /:/\:\
  __|__|:|\:\   /  /:/ /::\   __|  |:|      /  /:/ /:/_   /  /:/ /:/
 /__/::::| \:\ /__/:/ /:/\:\ /__/\_|:|____ /__/:/ /:/ /\ /__/:/ /:/___
 \  \:\~~\__\/ \  \:\/:/__\/ \  \:\/:::::/ \  \:\/:/ /:/ \  \:\/:::::/
  \  \:\        \  \::/       \  \::/~~~~   \  \::/ /:/   \  \::/~~~~
   \  \:\        \  \:\        \  \:\        \  \:\/:/     \  \:\
    \  \:\        \  \:\        \  \:\        \  \::/       \  \:\
     \__\/         \__\/         \__\/         \__\/         \__\/
 *
 * Filename     @ PointcutCollectionsList.java
 * Create date  @ 2021-12-27 11:21:58
 * Description  @
 * version      @ V1.0.0
 */

package peacemaker.frameworkinjector;

import java.util.regex.*;

public class PointcutCollectionsList extends java.util.LinkedList<BasePointcutCollections> {
    private java.util.Map<String, Pattern> mPatterns;

    public PointcutCollectionsList() {
        super();
    }

    public boolean containsMatched(CharSequence identifier) {
        return null != getMatched(identifier);
    }

    public java.util.Collection<? extends BasePointcutCollections> getMatched(CharSequence identifier) {
        assert !Utils.isEmpty(identifier);

        final java.util.List ret = new java.util.LinkedList<BasePointcutCollections>();

        mPatterns.forEach((regexIdentifier, pattern) -> {
            final boolean matched = pattern.matcher(identifier.toString()).matches();

            if (Main.DEBUG)
                Utils.message("PointcutCollectionsList",
                        "getMatched: pattern: " + regexIdentifier +
                        " identifier: " + identifier +
                        " matched: " + matched);

            if (matched)
                ret.add(get(regexIdentifier));
        });

        return ret.isEmpty() ? null : ret;
    }

    public boolean containsStyledIdentifier(CharSequence identifier) {
        return null != get(identifier);
    }

    public BasePointcutCollections get(CharSequence identifier) {
        assert !Utils.isEmpty(identifier);

        for (BasePointcutCollections e : this)
            if (Utils.equals(identifier, e.getStyledIdentifier()))
                return e;

        return null;
    }

    public BasePointcutCollections getOrCreate(CharSequence identifier) {
        if (containsStyledIdentifier(identifier))
            return get(identifier);

        BasePointcutCollections e = new ClassPointcutCollections(identifier);
        add(e);
        return e;
    }

    public BasePointcutCollections add(CharSequence styledIdentifier) {
        assert !Utils.isEmpty(styledIdentifier);

        return getOrCreate(styledIdentifier);
    }

    public BasePointcutCollections add(BaseMethodPointcut pointcut) {
        assert null != pointcut;

        ClassPointcutCollections e =
            (ClassPointcutCollections) getOrCreate(pointcut.getStyledIdentifier());
        e.ate(pointcut);
        return e;
    }

    public void prepare() {
        preparePatterns();
    }

    private void preparePatterns() throws PatternSyntaxException {
        assert null == mPatterns : "DENIED!";

        mPatterns = new java.util.HashMap<>(size());
        forEach(e -> {
            final String styledId = e.getStyledIdentifier();
            if (styledId.contains("*") ||
                    styledId.contains("^") ||
                    // TODO: 暂不处理$,因类名包含$是合法的
                    // styledId.contains("$") ||
                    styledId.contains("+") ||
                    styledId.contains("?") ||
                    styledId.contains("|") ||
                    styledId.contains("["))
                mPatterns.put(styledId, Pattern.compile(styledId));
        });
    }

    public java.util.Map retrievePatterns() {
        return mPatterns;
    }
}


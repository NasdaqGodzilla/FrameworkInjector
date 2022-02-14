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

public class PointcutCollectionsList extends java.util.LinkedList<BasePointcutCollections> {
    public PointcutCollectionsList() {
        super();
    }

    public boolean containsStyledIdentifier(CharSequence identifier) {
        assert !Utils.isEmpty(identifier);

        for (BasePointcutCollections e : this)
            if (Utils.equals(identifier, e.getStyledIdentifier()))
                return true;
        return false;
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
}


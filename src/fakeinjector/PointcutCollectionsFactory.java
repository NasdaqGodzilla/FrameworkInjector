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
 * Filename     @ PointcutCollectionsFactory.java
 * Create date  @ 2021-12-25 10:20:11
 * Description  @
 * version      @ V1.0.0
 */

package peacemaker.frameworkinjector;

public class PointcutCollectionsFactory implements Factory {
    private final PointcutCollectionsList mCollectionsList;

    private PointcutCollectionsFactory() {
        mCollectionsList = new PointcutCollectionsList();
    }

    @Override
    public BasePointcutCollections add(BaseMethodPointcut e) {
        assert null != e;

        return mCollectionsList.add(e);
    }

    @Override
    public java.util.List add(java.util.List<BaseMethodPointcut> elements) {
        assert null != elements && 0 < elements.size();

        final java.util.HashSet ret = new java.util.HashSet<BasePointcutCollections>();

        elements.forEach(element -> {
            ret.add(add(element));
        });

        return new java.util.LinkedList<>(ret);
    }

    @Override
    public BaseMethodPointcut retrieveMethodPointcut(CharSequence target) {
        assert !Utils.isEmpty(target) : "Target is the target!";

        final String clzName = "", methodName = "";
        final String[] paramsType = new String[10];

        Utils.parsePointcutInfo(target, (c, m, p) -> {
            if (!Utils.isEmpty(c))
                clzName.concat(c);
            if (!Utils.isEmpty(m))
                methodName.concat(m);
            if (null != p && 0 < p.length)
                System.arraycopy(p, 0, paramsType, 0, p.length); // FIXME: 优化实现；当前实现限制参数个数
            Utils.message("TargetParse", String.format("Class: %s, Method: %s",
                        c, m) + p);
        });

        if (!Utils.isEmpty(clzName)) {
            return new ClassMethodPointcut(clzName, methodName, paramsType);
        } else if (!Utils.isEmpty(methodName)) {
            return new ClassMethodPointcut(Utils.通配符, methodName, paramsType);
        }

        return null;
    }

    @Override
    public java.util.List retrieveMethodPointcut(String[] targets) {
        assert null != targets;

        final java.util.LinkedList ret = new java.util.LinkedList<BaseMethodPointcut>();
        for (int i = 0; i < targets.length; ++i) {
            ret.add(retrieveMethodPointcut(targets[i]));
        }

        return ret;
    }

    public static PointcutCollectionsFactory getOrCreate(CharSequence styledIdentifier) {
        return get();
    }

    public static PointcutCollectionsFactory get() {
        return Instance.f;
    }

    private static class Instance {
        private static final PointcutCollectionsFactory f =
            new PointcutCollectionsFactory();
    }
}


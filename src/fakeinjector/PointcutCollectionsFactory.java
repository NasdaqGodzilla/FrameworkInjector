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

        class TargetInfo {
            public String clzName;
            public String methodName;
            public String[] paramsType;

            public TargetInfo() {
                paramsType = new String[10]; // TODO: 解决参数个数限制
            }
        }

        final TargetInfo targetInfo = new TargetInfo();

        Utils.parsePointcutInfo(target, (c, m, p) -> {
            if (!Utils.isEmpty(c))
                targetInfo.clzName = c;
            if (!Utils.isEmpty(m))
                targetInfo.methodName = m;
            if (null != p && 0 < p.length)
                System.arraycopy(p, 0, targetInfo.paramsType, 0, p.length); // FIXME: 优化实现；当前实现限制参数个数
            Utils.message("TargetParse", String.format("Class: %s, Method: %s, Args: ",
                        targetInfo.clzName, targetInfo.methodName)
                    + String.join(Utils.逗号符, targetInfo.paramsType));
        });

        if (!Utils.isEmpty(targetInfo.clzName)) {
            return new ClassMethodPointcut(targetInfo.clzName, targetInfo.methodName, targetInfo.paramsType);
        } else if (!Utils.isEmpty(targetInfo.methodName)) {
            return new ClassMethodPointcut(Utils.通配符, targetInfo.methodName, targetInfo.paramsType);
        }

        Utils.fatal("TargetParse", "Something went wrong: " + target);

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

    public void dump() {
        Utils.message("dump", dumpWorld());
    }

    public String dumpWorld() {
        final StringBuilder sb = new StringBuilder(getClass().getName());
        sb.append(String.format("\r\n\tPointcutCollecetionsList size: %d",
                    mCollectionsList.size()));
        mCollectionsList.forEach(e -> {
            sb.append("\r\n\t\t");
            sb.append(e.dumpWorld());
        });
        return sb.toString();
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


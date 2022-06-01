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
    public void prepare() {
        mCollectionsList.prepare();
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
    // TODO: Support insertBefore and insertAfter
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
                    + String.join(Utils.逗号符, Utils.getNonNullItems(targetInfo.paramsType)));
        });

        if (!Utils.isEmpty(targetInfo.clzName)) {
            return new ClassMethodPointcut(targetInfo.clzName, targetInfo.methodName,
                    targetInfo.paramsType, null, null);
        } else if (!Utils.isEmpty(targetInfo.methodName)) {
            return new ClassMethodPointcut(Utils.通配符, targetInfo.methodName,
                    targetInfo.paramsType, null, null);
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

    public static java.util.List retrieveMethodPointcutJson(String filepath) {
        if (Utils.isEmpty(filepath))
            return null;

        final PointcutPojo pojo = Utils.parsePointcutInfoJson(filepath);
        return fromPointcutPojo(pojo);
    }

    public static BaseMethodPointcut fromPointcut(Pointcut p) {
        if (null == p)
            return null;

        final java.util.List<String> listTargetParams = p.getTargetParams();
        return new ClassMethodPointcut(p.getTargetClass(), p.getTargetMethod(),
                null != listTargetParams && 0 < listTargetParams.size()
                        ? listTargetParams.toArray(new String[p.getTargetParams().size()])
                        : null,
                p.getInsertBefore(), p.getInsertAfter());
    }

    public static java.util.List fromPointcutPojo(PointcutPojo pojo) {
        if (null == pojo)
            return null;

        final java.util.LinkedList<BaseMethodPointcut> ret = new java.util.LinkedList<>();
        pojo.getPointcuts().stream().filter(p -> p.getEnable())
            .collect(java.util.stream.Collectors.toList())
            .forEach(p -> ret.add(fromPointcut(p)));

        return ret;
    }

    public static <T> T ifPointcutEnabled(Pointcut p,
            java.util.concurrent.Callable<T> call) throws Exception {
        if (null == p || null == call)
            return null;

        if (p.getEnable())
            return call.call();

        return null;
    }

    public void dump() {
        Utils.message("dump", dumpWorld());
        Utils.message("Proceed", "\r\n");
    }

    public String dumpWorld() {
        final StringBuilder sb = new StringBuilder(getClass().getName());
        sb.append(String.format("\tPointcutCollecetionsList Collections.size: %d, Pointcuts.size: %d",
                    mCollectionsList.size(), getElementsCount()));
        mCollectionsList.forEach(e -> {
            sb.append("\r\n\t\t");
            sb.append(e.dumpWorld());
        });
        final java.util.Map<String, java.util.regex.Pattern> patterns = mCollectionsList.retrievePatterns();
        sb.append(String.format("\r\n\r\n\tPointcutCollecetionsList Pattern.size: %d", patterns.size()));
        patterns.forEach((patternIdentifier, pattern) -> {
            sb.append("\r\n\t\t");
            sb.append(String.format("Pattern: %s", pattern.pattern()));
            sb.append("\r\n\t\t\t");
            sb.append(mCollectionsList.get(patternIdentifier).dumpWorld());
        });
        return sb.toString();
    }

    public int getElementsCount() {
        int[] ret = {0};
        mCollectionsList.forEach(e -> {
            ret[0] += e.getElementsCount();
        });
        return ret[0];
    }

    public PointcutCollectionsList retrievePointcutCollectionsList() {
        return mCollectionsList;
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


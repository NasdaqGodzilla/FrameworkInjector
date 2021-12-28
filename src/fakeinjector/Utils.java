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
 * Filename     @ Utils.java
 * Create date  @ 2021-12-22 15:47:51
 * Description  @
 * version      @ V1.0.0
 */

package peacemaker.frameworkinjector;

import java.util.regex.*;

public class Utils {

    public static final String LOG_TAG = "frameworkinjector";

    public static final String 分隔符 = "::";
    public static final String 通配符 = "@";
    public static final String 逗号符 = ",";
    public static final String 初始化方法表示 = "__init";
    public static final String 初始化方法 = "$init";

    private static final String regex双分隔 = String.format(".*%s.*%s.*", 分隔符, 分隔符);
    private static final String regex单分隔 = String.format(".*%s.*", 分隔符);
    private static final String regex无分隔 = String.format("^((?!%s).)*$", 分隔符);

    private static final String regex双分组 = String.format("(.*)%s(.*)%s(.*)", 分隔符, 分隔符);
    private static final String regex单分组 = String.format("(.*)%s(.*)", 分隔符);

    private static Pattern sPattern2;
    private static Pattern sPattern1;
    private static Pattern[] sPatterns;

    static {
        try {
            sPattern2 = Pattern.compile(regex双分组);
            sPattern1 = Pattern.compile(regex单分组);
            sPatterns = new Pattern[] {sPattern2, sPattern1};
        } catch (PatternSyntaxException e) {
            assert false : "Something went wrong. Check the pattern. " + e;
        } finally {
            Matcher m = sPattern2.matcher("ai.hello::12233::growth!");
            if (!m.matches())
                assert false : "It seems our patterns did not work correctly...";
        }
    }

    public static final boolean equals(CharSequence l, CharSequence r) {
        if (isEmpty(l) || isEmpty(r))
            assert false : "Catch u later";

        return l.toString().trim().equals(r.toString().trim());
    }

    public static boolean isEmpty(CharSequence cs) {
        return null == cs || 0 == cs.length();
    }

    public static void generateWorldThenDump(CharSequence targets) {
        generateWorld(targets);
        PointcutCollectionsFactory.get().dump();
    }

    public static void generateWorld(CharSequence t/*targets*/) {
        assert !isEmpty(t) : "U are destroying the world!";

        final String[] targets = t.toString().trim().split(逗号符);
        final int targetCnt = targets.length;

        assert 0 < targetCnt : "Invalid targets!";

        message("generateWorld",
                String.format("targets: %s; targetCnt: %d", t.toString(), targetCnt) + targets);

        final Factory factory = PointcutCollectionsFactory.getOrCreate("");
        factory.add(factory.retrieveMethodPointcut(targets));
    }

    @FunctionalInterface
    public interface PointcutInfoParsedCallback {
        void callback(String clzName, String methodName, String[] paramsType);
    }

    public static void parsePointcutInfo(CharSequence t, PointcutInfoParsedCallback cb) {
        assert !isEmpty(t) : "Target is the target!";

        final String target = t.toString().trim();
        regexParse(target, cb);
    }

    private static void regexParse(String target, PointcutInfoParsedCallback cb) {
        String clzName = null, methodName = null;
        String[] paramsType = null;

        try {
            for (int i = 0; i < sPatterns.length; ++i) {
                Pattern p = sPatterns[i];
                Matcher m = p.matcher(target);
                if (m.matches()) {
                    switch (m.groupCount()) {
                        case 3: {
                            clzName = m.group(1);
                            methodName = m.group(2);
                            paramsType = new String[1]; // TODO: 还未设计多参数的表示方案，暂时将全部参数装入
                            paramsType[0] = m.group(3);
                        } break;
                        case 2: {
                            clzName = m.group(1);
                            methodName = m.group(2);
                        } break;
                        case 1: {
                            methodName = target;
                        } break;
                        default: {
                            assert false : "Target is breaking against rules!";
                        } break;
                    }
                    break;
                }
            }
        } finally {
            cb.callback(clzName, methodName, paramsType);
        }
    }

    public static void message(String tag, String msg) {
        assert msg != null;

        final java.lang.StringBuilder sb = new StringBuilder("");
        sb.append("" + tag + ":\t");
        sb.append(msg);
        System.out.println(sb.toString());
    }

    public static void fatal(String tag, String msg) throws FatalMessageException {
        message(tag, msg);

        throw new FatalMessageException("Stop with fatal: " + tag + " " + msg);
    }

    public static void copyTo(String sourceFilePath, String to) throws java.io.IOException {
        java.nio.file.Files.copy(new java.io.File(sourceFilePath).toPath(), new java.io.File(to).toPath());
    }

    public static class FatalMessageException extends IllegalStateException {
        public FatalMessageException(String s) {
            super(s);
        }
    }
}


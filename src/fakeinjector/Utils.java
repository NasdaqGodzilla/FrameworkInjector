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
    public static final String 初始化方法表示 = "__init";
    public static final String 初始化方法 = "$init";

    private static final String regex双分隔 = String.format(".*%s.*%s.*", 分隔符, 分隔符);
    private static final String regex单分隔 = String.format(".*%s.*", 分隔符);
    private static final String regex无分隔 = String.format("^((?!%s).)*$", 分隔符);

    private static final String regex双分组 = String.format("(.*)%s(.*)%s(.*)", 分隔符, 分隔符);
    private static final String regex单分组 = String.format("(.*)%s(.*)", 分隔符);

    public static final boolean equals(CharSequence l, CharSequence r) {
        if (isEmpty(l) || isEmpty(r))
            assert false : "Catch u later";

        return l.toString().trim().equals(r.toString().trim());
    }

    public static boolean isEmpty(CharSequence cs) {
        return null == cs || 0 == cs.length();
    }

    @FunctionalInterface
    public interface PointcutInfoParsedCallback {
        void callback(String clzName, String methodName, String[] paramsType);
    }

    public static void parsePointcutInfo(CharSequence t, PointcutInfoParsedCallback cb) {
        assert !isEmpty(t) : "Target is the target!";

        final String target = t.toString().trim();

        String clzName = null, methodName = null;
        String[] paramsType = null;

        try {
            
        } finally {
            // cb.callback(clzName, methodName, paramsType);
        }
    }

    // private static void preParse(String target) {
    
    // }

    // private static void regexParse(String target) {
    
    // }

    // 匹配无分隔符：target声明为方法名
    // 匹配单分隔符：target声明为无参数类方法：className::methodName
    // 匹配双分隔符：target声明为带参数类方法：className::methodName::[]paramsType

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


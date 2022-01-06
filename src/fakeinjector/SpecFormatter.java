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
 * Filename     @ SpecFormatter.java
 * Create date  @ 2022-01-06 11:00:25
 * Description  @ 互相转换: com/android/server/wm/FactoryErrorDialog$1.class com.android.server.wm.FactoryErrorDialog$1 com.android.server.wm.FactoryErrorDialog.1
 * version      @ V1.0.0
 */

package peacemaker.frameworkinjector;

import java.util.regex.*;

/**
 * How java generate the final class name from the class??
 *
 * The generated class is added to the same package as the source class. If the class
 * is a nested class, the nested class names are joined with {@code "$"}. The suffix
 * is always added to the generated name. E.g.:
 * class {@code com.niko.example.Outer.Inner}, the generated class name will be
 * {@code com.niko.example.Outer$Inner$Companion}.
 *
 *  @NonNull
 *  private static ClassName generateClassName(@NonNull Model model) {
 *      final ClassName className = model.getClassName();
 *
 *      return ClassName.get(className.packageName(),
 *              String.join("$", className.simpleNames()) + GENERATED_CLASS_SUFFIX);
 *  }
 */
class SpecFormatter {
    static final String SLASH = java.io.File.separator;
    static final String POINT = ".";
    static final String DOLLAR = "$";
    static final String SHARP = "#";

    // Regex expresion to match class full name.
    // Feature: Auto detect splitor as POINT or SLASH.
    // 这个也成，这样分组看上去性能会差一些？猜的。"^(?<part>[a-zA-Z]+)(?<split>[.|/])[a-zA-Z]+\\k<split>*.*"
    // TODO: 将regex中的'/'替换为SLASH.
    // FIXME: 本匹配方案无法做到完全可靠，全限定名路径深度、命名方式都比较宽松，对于区域使用.*进行匹配；.*匹配的部分被下毒时无法检测出。
    // static final String sRegexMatchFullClassNamePoint = "^([a-zA-Z]+[.][a-zA-Z]+)[.]*.*"; // 匹配全限定名，通过POINT分隔
    // static final String sRegexMatchFullClassNameSlash = "^([a-zA-Z]+[/][a-zA-Z]+)[.]*.*"; // 同上，通过SLASH分隔
    private static final String sRegexMatchFullClassName = "^([a-zA-Z]+(?<split>[.|/])[a-zA-Z]+)\\k<split>*.*";

    private static Pattern sPatternFullClassName;

    static {
        try {
            sPatternFullClassName = Pattern.compile(sRegexMatchFullClassName);
        } catch (PatternSyntaxException e) {
            throw e;
        } finally {
            Matcher m = sPatternFullClassName.matcher("com/niko/example/Companion");
            assert m.matches() : "We apologize for the inconvenience, but this service is temporarily unavailable.";
        }
    }

    private ClassMethodSignatureBean signBean;

    public SpecFormatter() {}

    static SpecFormatter with(ClassMethodSignatureBean b) {
        SpecFormatter sf = new SpecFormatter();
        sf.signBean = b;
        return sf;
    }

    public boolean isInnerClass() {
        return !isOuterClass();
    }

    public static boolean isInnerClass(String s) {
        return !isOuterClass(s);
    }

    // INFO: 不可靠，因为部分框架/编码/库会出现使用DOLLAR进行命名的情况，对于Java规范来说是合法的，但对包含本方法在内的语义分析器是有毒的。
    public boolean isOuterClass() {
        return isOuterClass(signBean.getFullClassName());
    }

    public static boolean isOuterClass(String s) {
        return isValidFullName(s) && !s.contains(DOLLAR);
    }

    public boolean isValidFullName() {
        return isValidFullName(signBean.getFullClassName());
    }

    public static boolean isValidFullName(String s) {
        return sPatternFullClassName.matcher(s).matches();
    }

    public boolean isSplitByPoint() {
        return isSplitByPoint(signBean.getFullClassName());
    }

    public static boolean isSplitByPoint(String s) {
        return isValidFullName(s) && !s.contains(SLASH);
    }

    public boolean isSplitBySlash() {
        return !isSplitByPoint();
    }

    public static boolean isSplitBySlash(String s) {
        return !isSplitBySlash(s);
    }

    // TODO: 正则转换为点分表达，参数控制保留$
    // TODO: 正则转换为杠分表达，参数控制保留$
    // TODO: 生成Jar包ZipEntry风格的entryName: com/niko/example/ClazzName$Inner.class
}


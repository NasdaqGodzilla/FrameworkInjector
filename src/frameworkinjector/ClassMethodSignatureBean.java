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
 * Filename     @ ClassMethodSignatureBean.java
 * Create date  @ 2022-01-04 19:45:03
 * Description  @
 * version      @ V1.0.0
 */

package peacemaker.frameworkinjector;

public class ClassMethodSignatureBean implements java.io.Serializable {
    private String className; // Simple name.
    private String methodName;
    private String fullClassName; // Inner class shown with '$', Outter class split by '/'.
    // TODO: 添加参数列表

    private transient String classNameWithPoint; // Full class path shown with splitor '.'
    private transient String classNameWithSlash; // Splitor is '/', exclude inner class.

    public ClassMethodSignatureBean() {}

    public ClassMethodSignatureBean(String clz, String mtd,
            String full /* com/niko/example/Outer$Inner */ ) {
        className = clz;
        methodName = mtd;
        fullClassName = full;
    }

    public String getClassName() {
        return className;
    }

    public String getMethodName() {
        return methodName;
    }

    public String getFullClassName() {
        return fullClassName;
    }

    public String getClassNameWithPoint() {
        return classNameWithPoint;
    }

    public String getClassNameWithSlash() {
        return classNameWithSlash;
    }

    public void setClassName(CharSequence c) {
        className = c.toString();
    }

    public void setMethodName(CharSequence c) {
        methodName = c.toString();
    }

    public void setFullClassName(CharSequence c) {
        fullClassName = c.toString();
    }

    public void setClassNameWithPoint(CharSequence c) {
        classNameWithPoint = c.toString();
    }

    public void setClassNameWithSlash(CharSequence c) {
        classNameWithSlash = c.toString();
    }

    @Override
    public String toString() {
        return String.format("%s::%s", className, methodName);
    }
}


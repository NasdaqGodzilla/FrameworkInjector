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
 * Filename     @ Main.java
 * Filename     @ InjectorImpl.java
 * Create date  @ 2022-01-17 10:40:58
 * Description  @ 被设计为通过ClazzLoader管理CtClass，通过Translator实现AOP代码注入。
 * version      @ V1.0.0
 */

package peacemaker.frameworkinjector;

import java.io.InputStream;
import java.lang.ref.WeakReference;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

class InjectorImpl implements AutoCloseable {
    private static final String TAG = InjectorImpl.class.getSimpleName();

    private static ClassPool sClassPool;

    static class ClazzLoader {
        private static final java.util.Map<String, CtClass> sPrimitiveTypeMap = new java.util.HashMap<>(9);

        static {
            sPrimitiveTypeMap.put("boolean", CtClass.booleanType);
            sPrimitiveTypeMap.put("char", CtClass.charType);
            sPrimitiveTypeMap.put("byte", CtClass.byteType);
            sPrimitiveTypeMap.put("short", CtClass.shortType);
            sPrimitiveTypeMap.put("int", CtClass.intType);
            sPrimitiveTypeMap.put("long", CtClass.longType);
            sPrimitiveTypeMap.put("float", CtClass.floatType);
            sPrimitiveTypeMap.put("double", CtClass.doubleType);
            sPrimitiveTypeMap.put("void", CtClass.voidType);
        }

        static CtClassWrapper makeClass(ClassPool cp, InputStream is,
                CharSequence styledIdentifier /* ClassName */) {
            if (null == is || null == cp)
                fatal("ACCESS DENIED!");

            return CtClassWrapper.makeClassSilent(cp, is, styledIdentifier);
        }

        public static CtClass getClass(ClassPool cp, String classname) {
            if (sPrimitiveTypeMap.containsKey(classname))
                return sPrimitiveTypeMap.get(classname);

            if (Utils.isEmpty(classname) || null == cp)
                return null;

            try {
                return cp.get(classname);
            } catch (javassist.NotFoundException e) {
                fatal("Failed getClass " + classname + " " + e);
            }

            return null;
        }

        public static CtMethod getMethod(ClassPool cp, CtClass ct, String name, String[] params) {
            if (Utils.isEmpty(name) || null == ct)
                return null;

            if (ct.isPrimitive())
                fatal("Failed getMethod " + name + " with stupid " + ct);

            if (null == params || 0 == params.length) {
                try {
                    return ct.getDeclaredMethod(name);
                } catch (javassist.NotFoundException e) {
                    fatal("Failed getMethod " + name + " " + e);
                }
            }

            final java.util.List<CtClass> listParams = new java.util.LinkedList<>();
            for (String param : params) {
                listParams.add(getClass(cp, param));
            }
            try {
                return ct.getDeclaredMethod(name, listParams.toArray(new CtClass[listParams.size()]));
            } catch (javassist.NotFoundException e) {
                fatal("Failed getMethod " + name +
                        " " + String.join(" ", params) + " " + e);
            }

            return null;
        }
    }

    static class Translator {
        static Translator get() {
            return InjectorImpl.Instance.t;
        }

        private Translator() {}

        static boolean support(CtClass c) {
            return null != c &&
                !javassist.Modifier.isInterface(c.getModifiers());
        }

        static boolean support(CtMethod m) {
            return null != m &&
                    !javassist.Modifier.isNative(m.getModifiers()) &&
                    !javassist.Modifier.isAbstract(m.getModifiers());
        }

        static <T extends InjectTarget> CtClassWrapper performTargetInject(T target) {
            if (null == target || null == target.mClass.get() || null == target.mMethod.get()) {
                fatal("Titanic is sinking! \r\n\t- Class: " + target.mClass.get() + "\r\n\t\t- Method: " + target.mMethod.get());
                return null;
            }

            final javassist.CtMethod ctMethod = target.mMethod.get();

            message("Translator: performTargetInject for " + ctMethod.getLongName());
            int i = 0;

            try {
                if (!support(ctMethod)) {
                    message("Translator: DENY! It's not suprised that this guy does not support! " +
                            ctMethod.getLongName());
                    return target.mClass.get();
                }

                if (!Utils.isEmpty(target.mInsertBefore))
                    ctMethod.insertBefore(target.mInsertBefore);
                i = 1;
                if (!Utils.isEmpty(target.mInsertAfter))
                    ctMethod.insertAfter(target.mInsertAfter);
            } catch (javassist.CannotCompileException e) {
                message(String.format("Translator: abort due to [FAILED] insert<%s>: <%s: %s>: %s\n%s\n",
                            0 == i ? "Before" : "After",
                            target.mClass.get().getName(),
                            ctMethod.getName(),
                            "" + e,
                            0 == i ? target.mInsertBefore : target.mInsertAfter));
                fatal("" + e);
            } finally {
                message("Translator: performTargetInject finish " + ctMethod.getLongName());
            }

            return target.mClass.get();
        }
    }

    // mClass mMethod由InjectorImpl管理，可以考虑实现为InjectTarget自身进行管理。
    static class InjectTarget {
        final WeakReference<? extends CtClassWrapper> mClass;
        final WeakReference<? extends CtMethod> mMethod;
        final String[] mParams;
        String mInsertBefore;
        String mInsertAfter;

        InjectTarget(CtClassWrapper c, CtMethod m, String[] p, String before, String after) {
            if (null == c || null == m)
                fatal("We don't blame you even if you make a mistake");

            mClass = new WeakReference<>(c);
            mMethod = new WeakReference<>(m);
            mParams = p;
            mInsertBefore = before;
            mInsertAfter = after;
        }

        public InjectTarget setInsertBefore(CharSequence cs) {
            if (Utils.isEmpty(cs))
                mInsertBefore = null;
            else
                mInsertBefore = cs.toString();

            return this;
        }

        public InjectTarget setInsertAfter(CharSequence cs) {
            if (Utils.isEmpty(cs))
                mInsertAfter = null;
            else
                mInsertAfter = cs.toString();

            return this;
        }

        /*
         * @Description: 根据切点信息创建InjectTarget。
         *              CtClassWrapper的创建依赖jar输入流，且可能由不同的ClassPool管理，
         *              因此调用者需提供InjectTarget使用的CtClassWrapper。
         *              进一步的，由于CtClassWrapper是固定的，这意味着只能处理匹配CtClass
         *              的切点，不属于(即类名不同)该CtClass的pointcuts将被，也必须被忽略。
         */
        public static <E extends BaseMethodPointcut> java.util.List<InjectTarget>
            with(CtClassWrapper c, java.util.AbstractCollection<E> pointcuts) {
            final java.util.LinkedList<InjectTarget> ret = new java.util.LinkedList<>();

            if (null == c || null == pointcuts || 0 == pointcuts.size())
                return ret;

            final String cid = c.getName();
            message("InjectTarget: gen for " + cid);

            pointcuts.forEach((pointcut) -> {
                if (!Utils.equals(cid, pointcut.getStyledIdentifier())) {
                    message("Skip due to odd pointcut: " + pointcut.dumpWorld());
                    return;
                }

                ret.add(with(c, pointcut.getMethodName().toString(), pointcut.getParamTypes(),
                            pointcut.getInsertBefore(), pointcut.getInsertAfter()));
            });

            message(String.format("InjectTarget: finish [%s](%d) with input pointcuts(%d)",
                        cid, ret.size(), pointcuts.size()));

            return ret;
        }

        // TODO: 创建class时处理重载方法。
        public static InjectTarget with(CtClassWrapper c, String methodName, String[] p,
                CharSequence bf, CharSequence af) {
            return with(c,
                    ClazzLoader.getMethod(sClassPool, c.retrieveCtClass(), methodName, p),
                    p, bf, af);
        }

        public static InjectTarget with(CtClassWrapper c, CtMethod m, String[] p,
                CharSequence bf, CharSequence af) {
            return new InjectTarget(c, m, p, bf.toString(), af.toString());
        }
    }

    public static InjectorImpl get() {
        if (null == sClassPool)
            assignNewClassPool();

        return Instance.i;
    }

    public static ClassPool retrieveClassPool() {
        return sClassPool;
    }

    public static ClassPool assignNewClassPool() {
        return assignNewClassPool(ClassPool.getDefault());
    }

    public static ClassPool assignNewClassPool(ClassPool cp) {
        sClassPool = cp;
        return sClassPool;
    }

    private InjectorImpl() {
        sClassPool = ClassPool.getDefault();
    }

    private static class Instance {
        private static final InjectorImpl i = new InjectorImpl();
        private static final Translator t = new Translator();
    }

    @Override
    public void close() {
        message("State run-as closed.");

        sClassPool = null;
    }

    public static String dump() {
        return "// TODO";
    }

    private static void fatal(CharSequence cs) {
        Utils.fatal(TAG, cs.toString());
    }

    private static void message(CharSequence cs) {
        Utils.message(TAG, cs.toString());
    }
}


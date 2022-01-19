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
import javassist.CtMethod;

class InjectorImpl implements AutoCloseable {
    private static final String TAG = InjectorImpl.class.getSimpleName();

    private static ClassPool sClassPool;

    static class ClazzLoader {
        static CtClassWrapper makeClass(ClassPool cp, InputStream is,
                CharSequence styledIdentifier /* ClassName */) {
            if (null == is || null == cp)
                fatal("ACCESS DENIED!");

            return CtClassWrapper.makeClassSilent(cp, is, styledIdentifier);
        }
    }

    static class Translator {
        static Translator get() {
            return InjectorImpl.Instance.t;
        }

        private Translator() {}

        static <T extends InjectTarget> CtClassWrapper performTargetInject(T target) {
            if (null == target || null == target.mClass.get() || null == target.mMethod.get()) {
                fatal("Titanic is sinking!");
                return null;
            }

            message("Translator: performTargetInject for " + target.mMethod.get().getLongName());
            int i = 0;

            try {
                if (!Utils.isEmpty(target.mInsertBefore))
                    target.mMethod.get().insertBefore(target.mInsertBefore);
                i = 1;
                if (!Utils.isEmpty(target.mInsertAfter))
                    target.mMethod.get().insertAfter(target.mInsertAfter);
            } catch (javassist.CannotCompileException e) {
                message(String.format("Translator: abort due to [FAILED] insert<%s>: <%s: %s>: %s\n%s\n",
                            0 == i ? "Before" : "After",
                            target.mClass.get().getName(),
                            target.mMethod.get().getName(),
                            "" + e,
                            target.mInsertBefore));
                fatal("" + e);
            } finally {
                message("Translator: performTargetInject finish " + target.mMethod.get().getLongName());
            }

            return target.mClass.get();
        }
    }

    // mClass mMethod由InjectorImpl管理，可以考虑实现为InjectTarget自身进行管理。
    static class InjectTarget {
        final WeakReference<? extends CtClassWrapper> mClass;
        final WeakReference<? extends CtMethod> mMethod;
        String mInsertBefore;
        String mInsertAfter;

        InjectTarget(CtClassWrapper c, CtMethod m, String before, String after) {
            if (null == c || null == m)
                fatal("We don't blame you even if you make a mistake");

            mClass = new WeakReference<>(c);
            mMethod = new WeakReference<>(m);
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

                ret.add(with(c, pointcut.getMethodName().toString()));
            });

            message(String.format("InjectTarget: finish [%s](%d) with input pointcuts(%d)",
                        cid, ret.size(), pointcuts.size()));

            return ret;
        }

        public static InjectTarget with(CtClassWrapper c, String methodName) {
            try {
                return with(c, c.retrieveCtClass().getDeclaredMethod(methodName));
            } catch (javassist.NotFoundException e) {
                fatal("" + e);
            }

            return null;
        }

        public static InjectTarget with(CtClassWrapper c, CtMethod m) {
            return new InjectTarget(c, m, null, null);
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


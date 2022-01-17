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

import javassist.ClassPool;

class InjectorImpl implements AutoCloseable {
    private static final String TAG = InjectorImpl.class.getSimpleName();

    private static ClassPool sClassPool;

    static class ClazzLoader {
        static CtClassWrapper makeClass(ClassPool cp, InputStream is, CharSequence styledIdentifier) {
            if (null == is || null == cp)
                fatal("ACCESS DENIED!");

            return CtClassWrapper.makeClassSilent(cp, is, styledIdentifier);
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


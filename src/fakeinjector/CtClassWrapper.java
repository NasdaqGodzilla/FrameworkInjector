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
 * Filename     @ CtClassWrapper.java
 * Create date  @ 2022-01-14 16:00:25
 * Description  @
 * version      @ V1.0.0
 */

package peacemaker.frameworkinjector;

import java.io.IOException;

import javassist.ClassPool;
import javassist.CtClass;

class CtClassWrapper implements AutoCloseable {
    private CharSequence mStyledIdentifier;
    private CtClass mCtClass;

    static CtClassWrapper makeClassSilent(ClassPool cp, java.io.InputStream is, CharSequence cs) {
        try {
            return makeClass(cp, is, cs);
        } catch (IOException | RuntimeException e) {
            Utils.fatal(CtClassWrapper.class.getSimpleName(), "" + cs + " " + e);
        }
        return null;
    }

    /**
     * @Description:  Wrap CtClass that created from classfile input stream.
     */
    static CtClassWrapper makeClass(ClassPool cp, java.io.InputStream is, CharSequence cs) throws
            IOException, RuntimeException {
        if (null == cp)
            return null;

        return new CtClassWrapper(cp.makeClassIfNew(is), cs);
    }

    public CtClassWrapper(CtClass c, CharSequence cs) {
        mCtClass = c;
        mStyledIdentifier = cs;
    }

    CtClass retrieveCtClass() {
        return mCtClass;
    }

    String getName() {
        return mStyledIdentifier.toString();
    }

    String getCtName() {
        return mCtClass.getName();
    }

    @Override
    public void close() throws Exception {
        if (null != mCtClass)
            mCtClass.detach();

        mCtClass = null;
        mStyledIdentifier = null;
    }

    @Override
    public String toString() {
        return String.format("<%d, %s>: %s",
                Integer.toHexString(System.identityHashCode(this)),
                getName(),
                "" + mCtClass);
    }
}

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
 * Filename     @ BaseMethodPointcut.java
 * Create date  @ 2021-12-24 15:05:14
 * Description  @
 * version      @ V1.0.0
 */

package peacemaker.frameworkinjector;

public abstract class BaseMethodPointcut {
    protected final CharSequence methodName;
    protected final String[] paramTypes;

    protected final CharSequence insertBefore;
    protected final CharSequence insertAfter;

    public CharSequence getInsertBefore() {
        return insertBefore;
    }

    public CharSequence getInsertAfter() {
        return insertAfter;
    }

    public CharSequence getMethodName() {
        return methodName;
    }

    public String[] getParamTypes() {
        return paramTypes;
    }

    public CharSequence getStyledIdentifier() {
        return methodName;
    }

    public BaseMethodPointcut(CharSequence m) {
        this(m, null, null, null);
    }

    public BaseMethodPointcut(CharSequence m, String[] p,
            CharSequence bf, CharSequence af) {
        assert !Utils.isEmpty(m);

        methodName = m;
        paramTypes = p;
        insertBefore = bf;
        insertAfter = af;
    }

    public abstract String dumpWorld();
}


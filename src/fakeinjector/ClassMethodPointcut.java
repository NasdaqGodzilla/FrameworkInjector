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
 * Filename     @ ClassMethodPointcut.java
 * Create date  @ 2021-12-24 15:17:27
 * Description  @
 * version      @ V1.0.0
 */

package peacemaker.frameworkinjector;

public class ClassMethodPointcut extends BaseMethodPointcut {
    protected final CharSequence className;

    public CharSequence getClassName() {
        return className;
    }

    @Override
    public CharSequence getStyledIdentifier() {
        return className;
    }

    public ClassMethodPointcut(CharSequence c, CharSequence m) {
        this(c, m, null);
    }

    public ClassMethodPointcut(CharSequence c, CharSequence m, String[] p) {
        super(m, p);

        assert !Utils.isEmpty(c);
        assert !Utils.isEmpty(m);

        className = c;
    }

    @Override
    public String dumpWorld() {
        final StringBuilder sb = new StringBuilder("\t\t\t- " + getClass().getSimpleName());
        sb.append(String.format("<%s>: ", getStyledIdentifier()));
        sb.append(String.format("methodName: %s ", methodName) +
                String.join(Utils.逗号符, Utils.getNonNullItems(paramTypes)));
        return sb.toString();
    }
}


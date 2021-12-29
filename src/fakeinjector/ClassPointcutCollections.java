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
 * Filename     @ ClassPointcutCollections.java
 * Create date  @ 2021-12-24 16:40:00
 * Description  @ ArrayList wrapper，存储的切点均属于同一个类。
 * version      @ V1.0.0
 */

package peacemaker.frameworkinjector;

public class ClassPointcutCollections<T extends BaseMethodPointcut> extends BasePointcutCollections<T>
        implements BasePointcutCollections.Kitchen<T> {
    private static final String TAG = ClassPointcutCollections.class.getSimpleName();

    private final CharSequence mClassName;

    public ClassPointcutCollections(CharSequence clzName) {
        super(new java.util.ArrayList<T>());

        assert !Utils.isEmpty(clzName);
        assert null != mPoints;

        mClassName = clzName;
    }

    public final String getStyledIdentifier() {
        return mClassName.toString();
    }

    @Override
    public boolean smellsGood(T element) {
        assert null != element;

        return mClassName.toString().equals(element.getStyledIdentifier());
    }

    @Override
    public boolean ate(T element) {
        assert null != element;

        if (smellsGood(element)) {
            addSilent(element);
            return true;
        }
        return false;
    }

    @Override
    public boolean ate(java.util.List<T> elements) {
        assert null != elements;

        boolean[] ret = new boolean[1];
        ret[0] = false;

        elements.forEach(element -> {
            if (ate(element)) {
                ret[0] = true;
            }
        });

        Utils.message(TAG, "" + mClassName + " with mPoints: " +
                mPoints.size() + ": " + mPoints.toString());

        return ret[0];
    }

    @Override
    public String dumpWorld() {
        final StringBuilder sb = new StringBuilder(getClass().getName());
        sb.append(String.format("\r\n ClassPointcutCollections mPoints size: %d",
                    mPoints.size()));
        mPoints.forEach(e -> {
            sb.append("\r\n");
            sb.append(e.dumpWorld());
        });
        return sb.toString();
    }
}


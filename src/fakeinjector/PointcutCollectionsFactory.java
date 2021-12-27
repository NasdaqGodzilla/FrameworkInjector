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
 * Filename     @ PointcutCollectionsFactory.java
 * Create date  @ 2021-12-25 10:20:11
 * Description  @
 * version      @ V1.0.0
 */

package peacemaker.frameworkinjector;

public class PointcutCollectionsFactory implements Factory {
    private PointcutCollectionsFactory() {}

    @Override
    public BasePointcutCollections add(BaseMethodPointcut e) {
        return null;
    }

    @Override
    public BasePointcutCollections add(BaseMethodPointcut[] e) {
        return null;
    }

    @Override
    public BaseMethodPointcut retrieveMethodPointcut(CharSequence target) {
        return null;
    }

    @Override
    public java.util.List retrieveMethodPointcut(String[] target) {
        return null;
    }

    public static PointcutCollectionsFactory getOrCreate(CharSequence styledIdentifier) {
        return get();
    }

    public static PointcutCollectionsFactory get() {
        return Instance.f;
    }

    private static class Instance {
        private static final PointcutCollectionsFactory f =
            new PointcutCollectionsFactory();
    }
}


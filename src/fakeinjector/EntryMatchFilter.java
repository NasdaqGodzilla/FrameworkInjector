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
 * Filename     @ EntryMatchFilter.java
 * Create date  @ 2021-12-31 15:39:21
 * Description  @
 * version      @ V1.0.0
 */

package peacemaker.frameworkinjector;

import java.util.zip.ZipEntry;

public class EntryMatchFilter implements EntryFilter<ZipEntry> {
    private static final String TAG = EntryMatchFilter.class.getSimpleName();

    private final java.lang.ref.WeakReference<PointcutCollectionsList> mWeakList;

    public EntryMatchFilter(PointcutCollectionsList l) {
        mWeakList = new java.lang.ref.WeakReference<>(l);
    }

    private boolean matched(String s) {
        // Skip directories
        if (!s.endsWith(".class"))
            return false;

        final SpecFormatter sf =
            SpecFormatter.with(SpecFormatter.retrieveEntryNameWithoutSuffix(s));
        final String classFullNamePoint =
            sf.retrieveInnerSpecAs(sf.retrieveWithPoint(), ".");

        return mWeakList.get().containsStyledIdentifier(classFullNamePoint);
    }

    private void message(String m) {
        Utils.message(TAG, m);
    }

    // What does ZipEntry looks like? By ZipEntry.getName() called:
    // com/android/server/wm/ActivityTaskManagerService$Lifecycle.class
    // com/android/server/wm/WindowState$PowerManagerWrapper.class
    @Override
    public boolean test(ZipEntry t) {
        final boolean matched = matched(t.getName().trim());

        if (matched)
            message(String.format("%s matched.", t.getName()));

        return matched;
    }
}


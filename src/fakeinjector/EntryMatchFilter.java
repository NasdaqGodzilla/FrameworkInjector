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
        return mWeakList.get().containsStyledIdentifier(s);
    }

    private void message(String m) {
        Utils.message(TAG, m);
    }

    @Override
    public boolean test(ZipEntry t) {
        message(String.format("%s matched: %b",
                    t.getName(), matched(t.getName())));

        return null != t;
    }
}


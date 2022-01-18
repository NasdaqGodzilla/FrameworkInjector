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
 * Filename     @ EntryInjectConsumer.java
 * Create date  @ 2022-01-18 17:06:53
 * Description  @
 * version      @ V1.0.0
 */

package peacemaker.frameworkinjector;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

class EntryInjectConsumer extends EntryCopyConsumer {
    private static final String TAG = EntryInjectConsumer.class.getSimpleName();

    protected WeakReference<PointcutCollectionsList> mWeakList = null;

    // 计量注入次数
    private int mCntTheInject;

    public EntryInjectConsumer(ZipOutputStream zos) {
        super(zos);

        mWeakList =
            new WeakReference<>(InjectEngine.get().retrievePointcutCollectionsList());
        mCntTheInject = 0;
    }

    private boolean matched(String s) {
        return EntryMatchFilter.matched(s, mWeakList.get());
    }

    // For t/test: getPoints().forEach()
    protected BasePointcutCollections retrievePointcuts(String clzName) {
        final SpecFormatter sf =
            SpecFormatter.with(SpecFormatter.retrieveEntryNameWithoutSuffix(clzName));
        final String classFullNamePoint =
            sf.retrieveInnerSpecAs(sf.retrieveWithPoint(), ".");

        return mWeakList.get().get(classFullNamePoint);
    }

    /*
    @Override
    public void accept(ZipEntry newEntry, BufferedInputStream newEntryBis) {

    }
    */

    @Override
    public void close() {
        super.close();

        message(String.format("Finally done... mCntTheInject[%d]",
                    mCntTheInject));
    }

    private static final void message(CharSequence cs) {
        Utils.message(TAG, cs.toString());
    }

    private static final void fatal(CharSequence cs) {
        Utils.fatal(TAG, cs.toString());
    }
}


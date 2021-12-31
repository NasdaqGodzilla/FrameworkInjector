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
 * Filename     @ EntryCopyConsumer.java
 * Create date  @ 2021-12-31 11:20:02
 * Description  @ 一个总是将读入的entry不经任何修改地写入到ZipOutputStream的Consumer
 * version      @ V1.0.0
 */

package peacemaker.frameworkinjector;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class EntryCopyConsumer implements AutoCloseable, EntryConsumer<ZipEntry, BufferedInputStream> {
    private final String TAG = EntryCopyConsumer.class.getSimpleName();
    private final java.lang.ref.WeakReference<ZipOutputStream> mWeakZos;

    public EntryCopyConsumer(ZipOutputStream zos) {
        mWeakZos = new java.lang.ref.WeakReference<ZipOutputStream>(zos);
    }

    @Override
    public void accept(ZipEntry newEntry, BufferedInputStream newEntryBis) {
        final ZipOutputStream zos = mWeakZos.get();

        Utils.message(TAG, String.format("<%s> accept %s copying to output ",
                    Integer.toHexString(System.identityHashCode(this)), newEntry.getName())
                + zos);

        boolean crash = true;

        try {
            zos.putNextEntry(newEntry);
            while (newEntryBis.available() > 0)
                zos.write(newEntryBis.read());
            zos.closeEntry();
            newEntryBis.close();
            crash = !crash;
        } catch (IOException e) {
            Utils.fatal(TAG, "" + e);
        } finally {
            if (crash)
                Utils.fatal(TAG, String.format("<%s> accept %s something went wrong",
                            Integer.toHexString(System.identityHashCode(this)), newEntry.getName()));
        }
    }

    @Override
    public void close() {
        mWeakZos.clear(); /* Object not held by Class.this */
    }
}


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
import java.lang.ref.WeakReference;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

// accept方法的流和entry由本类所有并管理；ZipOutputStream由InjectEngine所有并管理
// 其中InjectEngine通过AutoClosable，借助try-with-resource为调用者提供自动生命周期管理。
public class EntryCopyConsumer implements AutoCloseable, EntryConsumer<ZipEntry, BufferedInputStream> {
    private static final String TAG = EntryCopyConsumer.class.getSimpleName();
    protected final java.lang.ref.WeakReference<ZipOutputStream> mWeakZos;

    // TODO: 需要重新实现ZipOutputStream的获取方式
    // INFO: 初始化目前传入null，原因是InjectorEngine会执行对zos的初始化，这里如果初始化会造成重复初始化。
    public EntryCopyConsumer(ZipOutputStream zos) {
        ZipOutputStream newZos = zos;
        if (null == newZos)
            newZos = InjectEngine.get().retrieveInjectorOutputStream();

        if (null == newZos)
            Utils.message(TAG, "Output stream from injector require NonNull. (Lateinit...will retry later.)");

        mWeakZos = new java.lang.ref.WeakReference<ZipOutputStream>(newZos);
    }

    @Override
    public void accept(ZipEntry newEntry, BufferedInputStream newEntryBis) {
        final ZipOutputStream zos;
        if (null != mWeakZos.get())
            zos = mWeakZos.get();
        else {
            zos = InjectEngine.get().retrieveInjectorOutputStream();
            // mWeakZos.refersTo(zos); // AOSP 10.0.0-r1 没有refersTo接口
        }

        if (null == zos)
            Utils.fatal(TAG, "Output stream from injector require NonNUll.");


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


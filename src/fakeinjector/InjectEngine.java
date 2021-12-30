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
 * Filename     @ InjectEngine.java
 * Create date  @ 2021-12-29 16:29:08
 * Description  @
 * version      @ V1.0.0
 */

package peacemaker.frameworkinjector;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class InjectEngine implements AutoCloseable {
    private ZipFileWrapper mInJar;
    private Injector mInjector;
    private PointcutCollectionsList mPointcutCollectionsList;

    public static class ZipFileWrapper extends ZipFile implements AutoCloseable {
        public ZipFileWrapper(String file) throws IOException {
            super(file);
        }

        public BufferedInputStream getBufferedInputStreamForEntry(ZipEntry entry) {
            try {
                return new BufferedInputStream(getInputStream(entry));
            } catch (IOException e) {
                Utils.fatal("ZipFileWrapper", "" + getName() + " failed open InputStream jar. " + e);
            }
            return null;
        }

        public Enumeration<? extends ZipEntry> getEntries() {
            return entries();
        }

        public void <T extends ZipEntry> forEachEntry(EntryConsumer<T> entryConsumer) {
            if (null == entryConsumer)
                Utils.fatal("ZipFileWrapper", "Happy Christmas!");

            return forEachEntryWithFilter("", null, entryConsumer);
        }

        public void <T extends ZipEntry> forEachEntryWithFilter(String entryType,
                    EntryFilter<T> entryFilter, EntryConsumer<T> entryConsumer) {
            if (null == entryConsumer)
                Utils.fatal("ZipFileWrapper", "Happy New Year!");
        }

        @Override
        public void close() {
            try {
                super.close();
                Utils.message("ZipFileWrapper", "" + getName() + " closed.");
            } catch (IOException e) {
                Utils.message("ZipFileWrapper", "" + getName() + ": " + e);
            };
        }
    }

    private static class Injector implements AutoCloseable {
        private String mOutFile;
        private ZipOutputStream mOutputStream;

        public Injector(String outFile) throws java.io.FileNotFoundException {
            mOutFile = outFile;
            mOutputStream = new ZipOutputStream(new FileOutputStream(outFile));
        }

        @Override
        public void close() {
            try {
                mOutputStream.finish();
                mOutputStream.close();
                Utils.message("Injector", "" + mOutFile + " closed.");
            } catch (IOException e) {
                Utils.message("Injector", "" + mOutFile + ": " + e);
            }
        }
    }

    public InjectEngine powerOn(String inJar, String outJar, PointcutCollectionsList pointcuts) {
        if (Utils.isEmpty(outJar) || Utils.isEmpty(inJar) || null == pointcuts || 0 >= pointcuts.size())
            Utils.fatal("InjectEngine", "InjectEngine unavailable due to something crash down.");

        workOn(inJar, outJar);
        mPointcutCollectionsList = pointcuts;
        return this;
    }

    public InjectEngine powerOff() {
        mInJar.close();
        mInjector.close();

        mInJar = null;
        mInjector = null;
        mPointcutCollectionsList = null;

        return this;
    }

    private InjectEngine workOn(String inJar, String outJar) {
        try {
            mInJar = new ZipFileWrapper(inJar);
            mInjector = new Injector(outJar);
        } catch (IOException e) {
            Utils.fatal("InjectEngine", "Failed ... " + e);
        }
        return this;
    }

    public static InjectEngine startEngine(String i, String o, PointcutCollectionsList l) {
        return get().powerOn(i, o, l);
    }

    public static InjectEngine get() {
        return Instance.i;
    }

    private InjectEngine() {}

    private static class Instance {
        private static final InjectEngine i = new InjectEngine();
    }

    @Override
    public void close() {
        powerOff();
    }
}


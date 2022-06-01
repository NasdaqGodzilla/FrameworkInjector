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
    private static final String TAG = InjectEngine.class.getSimpleName();

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

        public void forEachEntry(EntryConsumer<? extends ZipEntry, InputStream> entryConsumer) {
            if (null == entryConsumer)
                Utils.fatal("ZipFileWrapper", "Happy Christmas!");

            forEachEntryWithFilter("", null, entryConsumer, null);
        }

        /**
         * @author: Niko
         * @description: 遍历jar包，通过Consumer逐个返回指定后缀的且EntryFilter验证通过的entry
         * @param:  entryType 后缀；要求首字符必须是'.' String.startsWith('.')返回true; 或以空串表示接收所有
         * @param:  entryFilter 调用者实现的该接口返回flase时不通过entryConsumer返回entry
         * @param:  entryConsumer 向调用者逐个返回entry的接口
         * @param:  unmatchedConsumer 调用者实现该接口可以接收到不匹配的entry
         * @return: void
         */
        public void forEachEntryWithFilter(String entryType,
                    EntryFilter<? extends ZipEntry> entryFilter, EntryConsumer<? extends ZipEntry, InputStream> entryConsumer,
                    EntryConsumer<? extends ZipEntry, InputStream> unmatchedConsumer) {
            if (null == entryConsumer)
                Utils.fatal("ZipFileWrapper", "Happy New Year!");

            Utils.message("ZipFileWrapper",
                        String.format("%s: forEachEntryWithFilter entryType: %s entryFilter-entryConsumer: ",
                            getName(), entryType) +
                    entryFilter + " " + entryConsumer);

            final boolean entryTypeAll = Utils.isEmpty(entryType);
            final boolean entryFilterAll = null == entryFilter;

            final Enumeration<? extends ZipEntry> entries = getEntries();
            while (entries.hasMoreElements()) {
                final ZipEntry entry = entries.nextElement();
                final ZipEntry retEntry = new ZipEntry(entry.getName());
                final BufferedInputStream retEntryBis = getBufferedInputStreamForEntry(entry);

                final boolean entryTypeMatched = entryTypeAll || entry.getName().endsWith(entryType);
                final boolean entryFilterMatched = entryFilterAll || entryFilter.test(entry);

                if (entryTypeMatched && entryFilterMatched) {
                    entryConsumer.accept(retEntry, retEntryBis);
                } else if (null != unmatchedConsumer) {
                    unmatchedConsumer.accept(retEntry, retEntryBis);
                } else { /* Unmatched and unhandled */ }
            }
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

    public static class Injector implements AutoCloseable {
        private String mOutFile;
        private ZipOutputStream mOutputStream;

        public Injector(String outFile) throws java.io.FileNotFoundException {
            mOutFile = outFile;
            mOutputStream = new ZipOutputStream(new FileOutputStream(outFile));
        }

        public void performInject(ZipFileWrapper inJar, PointcutCollectionsList list, EntryFilter filter,
                EntryConsumer targetConsumer, EntryConsumer otherConsumer) {
            inJar.forEachEntryWithFilter(".class", filter, targetConsumer, otherConsumer);
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

    public InjectEngine workOnFilterModeOnce(String inJar, EntryFilter filter, EntryConsumer match, EntryConsumer other) throws IOException {
        if (Utils.isEmpty(inJar) || null == match || null == filter)
            Utils.fatal(TAG, "ACCESS DENIED! Hurtful parameters!");

        /*
        if (null != mInJar || null != mInjector) { // InjectEngine still power ON. Something is still working which may not well to interrupt.
            Utils.message(TAG, "workOnFilterMode may interrupt other routine which may run into bad ways.");
            powerOff();
        }
        */

        Utils.message(TAG, String.format("workOnFilterMode start %s", inJar));

        // try (mInJar = new ZipFileWrapper(inJar)) { // Assign mInJar is announce that we're using InjectEngine. There have messages when we were interrupted.
        try (final ZipFileWrapper jarFile = new ZipFileWrapper(inJar)) {
            jarFile.forEachEntryWithFilter(".class", filter, match, other);
        }

        Utils.message(TAG, String.format("workOnFilterMode quit %s", inJar));
        // mInJar = null;

        return this;
    }

    public InjectEngine powerOn(String inJar, String outJar, PointcutCollectionsList pointcuts) {
        Utils.message(TAG, String.format("<%s> powerOn.",
                    Integer.toHexString(System.identityHashCode(this))));

        if (Utils.isEmpty(outJar) || Utils.isEmpty(inJar))
            Utils.fatal(TAG, "InjectEngine unavailable due to something crash down.");

        if (null == pointcuts || 0 >= pointcuts.size())
            Utils.message(TAG, "InjectEngine working with empty PointcutsCollectionsList.");

        workOn(inJar, outJar);
        mPointcutCollectionsList = pointcuts;
        return this;
    }

    public InjectEngine powerOff() /* throws */ {
        Utils.message(TAG, String.format("<%s> powerOff.",
                    Integer.toHexString(System.identityHashCode(this))));

        if (null != mInJar)
            mInJar.close();

        if (null != mInjector)
            mInjector.close();

        mInJar = null;
        mInjector = null;
        mPointcutCollectionsList = null;

        return this;
    }

    private InjectEngine workOn(String inJar, String outJar) {
        if (null != mInJar || null != mInjector) {
            Utils.message(TAG, "workOn: It seems that something eariler jobs does not finished.");
            powerOff(); // Call it will cause crash(double close) maybe. On the contrary, will cause resources leak maybe.
        }

        try {
            mInJar = new ZipFileWrapper(inJar);
            mInjector = new Injector(outJar);
        } catch (IOException e) {
            Utils.fatal(TAG, "Failed ... " + e);
        }
        return this;
    }

    PointcutCollectionsList retrievePointcutCollectionsList() {
        return mPointcutCollectionsList;
    }

    public ZipFileWrapper retrieveInJar() {
        return mInJar;
    }

    public Injector retrieveInjector() {
        return mInjector;
    }

    public ZipOutputStream retrieveInjectorOutputStream() {
        if (null != mInjector)
            return mInjector.mOutputStream;

        return null;
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


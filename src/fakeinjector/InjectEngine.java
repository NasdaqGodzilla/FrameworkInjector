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

public class InjectEngine {
    private ZipFileWrapper mInJar;
    private Injector mInjector;

    public static class ZipFileWrapper extends ZipFile implements AutoCloseable {
        public ZipFileWrapper(String file) {
            super(file);
        }

        public BufferedInputStream getBufferedInputStreamForEntry(ZipEntry entry) {
            return new BufferedInputStream(getInputStream(entry));
        }

        public Enumeration<? extends ZipEntry> getEntries() {
            return entries();
        }

        // TODO: forEachEntry

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

        public Injector(String outFile) {
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

    public InjectEngine workOn(String inJar, String outJar) {
        mInJar = new ZipFileWrapper(inJar);
        mInjector = new Injector(outJar);
        return this;
    }

    public static InjectEngine get() {
        return Instance.i;
    }

    private InjectEngine() {}

    private static class Instance {
        private static final InjectEngine i = new InjectEngine();
    }
}


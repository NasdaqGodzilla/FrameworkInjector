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
 * Create date  @ 2021-12-22 16:13:21
 * Description  @
 * version      @ V1.0.0
 */

package peacemaker.frameworkinjector;

public class Main {
    public static void main(String[] args) throws EarlyExitException {
        Utils.message("frameworkinjector", "frameworkinjector work!");

        String inJar = null;
        String outJar = null;

        for (int i = 0; i < args.length; i++) {
            if ("-i".equals(args[i].trim())) {
                i++;
                inJar = args[i].trim();
            } else if ("-o".equals(args[i].trim())) {
                i++;
                outJar = args[i].trim();
            }
        }

        Utils.message("fakeinjector", "inJar-outJar: " + inJar + " " + outJar);

        try {
            Utils.copyTo(inJar, outJar);
        } catch (java.io.IOException e) {
            Utils.fatal("fakeinjector", "" + e);
        }
    }

    public static class EarlyExitException extends RuntimeException {
        public EarlyExitException(String s) {
            super(s);
        }
    }
}


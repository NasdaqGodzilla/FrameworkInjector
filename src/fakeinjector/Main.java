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
    private static final String exp = "\r\n==========================================\r\n";

    public static void main(String[] args) throws EarlyExitException {
        Utils.message("frameworkinjector", "frameworkinjector work!");
        Utils.message("frameworkinjector", exp);

        String inJar = null;
        String outJar = null;
        String pointcuts = null;

        for (int i = 0; i < args.length; i++) {
            final String arg_ = args[i].trim();
            if ("-i".equals(arg_)) {
                inJar = args[++i].trim();
            } else if ("-o".equals(arg_)) {
                outJar = args[++i].trim();
            } else if ("-pointcuts".equals(arg_)) {
                pointcuts = args[++i].trim();
            }
        }

        Utils.message("frameworkinjector", "inJar-outJar: " + inJar + " " + outJar +
                " pointcuts: " + pointcuts);
        Utils.message("frameworkinjector", exp);

        Utils.generateWorldThenDump(pointcuts);

        try {
            Utils.startInjectAsCopy(inJar, outJar);
        } catch (java.io.IOException e) {
            Utils.fatal("frameworkinjector", "" + e);
        }

        /*
        try {
            Utils.copyTo(inJar, outJar);
        } catch (java.io.IOException e) {
            Utils.fatal("frameworkinjector", "" + e);
        }
        */
    }

    public static class EarlyExitException extends RuntimeException {
        public EarlyExitException(String s) {
            super(s);
        }
    }
}


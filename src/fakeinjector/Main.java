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
        String cplist = null;

        for (int i = 0; i < args.length; i++) {
            final String arg_ = args[i].trim();
            if ("-i".equals(arg_)) {
                inJar = args[++i].trim();
            } else if ("-o".equals(arg_)) {
                outJar = args[++i].trim();
            } else if ("-pointcuts".equals(arg_)) {
                pointcuts = args[++i].trim();
            } else if ("-cplist".equals(arg_)) {
                cplist = args[++i].trim();
            }
        }

        Utils.message("frameworkinjector", "inJar-outJar: " + inJar + " " + outJar +
                " pointcuts: " + pointcuts);
        Utils.message("frameworkinjector", exp);

        Utils.generateWorldThenDump(pointcuts);

        try {
            /* 测试Filter及其机制工作正常
            Utils.startInjectAsFilter(inJar,
                    new EntryMatchFilter(PointcutCollectionsFactory.get().retrievePointcutCollectionsList()),
                    (e, s) ->
                        Utils.message("Main.startInjectAsFilter", "接收到匹配项: " + ((java.util.zip.ZipEntry) e).getName()),
                        null);
            */

            // 不进行注入，直接拷贝(通过Inject consumer机制)。
            // Utils.startInjectAsCopy(inJar, outJar);

            InjectorImpl.get().retrieveClassPool().appendPathList(cplist);

            final PointcutCollectionsList pointcutList =
                PointcutCollectionsFactory.get().retrievePointcutCollectionsList();
            Utils.startInjectWithConsumer(inJar, outJar, pointcutList,
                    new EntryMatchFilter(pointcutList),
                    new EntryInjectConsumer(null),
                    new EntryCopyConsumer(null));

        } catch (javassist.NotFoundException e) {
            Utils.fatal("frameworkinjector", "" + e.toString());
        }

        /* 不进行注入，直接拷贝(通过Java NIO机制)。
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


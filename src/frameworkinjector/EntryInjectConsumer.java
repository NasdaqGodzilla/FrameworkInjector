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

import java.io.InputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

class EntryInjectConsumer extends EntryCopyConsumer {
    private static final String TAG = EntryInjectConsumer.class.getSimpleName();

    protected WeakReference<PointcutCollectionsList> mWeakList = null;

    public EntryInjectConsumer(ZipOutputStream zos) {
        super(zos);

        mWeakList = new WeakReference<>(
                PointcutCollectionsFactory.get().retrievePointcutCollectionsList());
    }

    // 验证PointcutCollectionsList包含对应该Entry的切点
    private boolean matched(String s /* ZipEntry.getName() */) {
        return EntryMatchFilter.matched(s, mWeakList.get());
    }

    protected BasePointcutCollections retrievePointcuts(String entryName) {
        return mWeakList.get().get(toNameWithPoint(entryName));
    }

    protected java.util.Collection<? extends BasePointcutCollections> retrieveMatchedPointcuts(String entryName) {
        return mWeakList.get().getMatched(toNameWithPoint(entryName));
    }

    static String toNameWithPoint(String entryName) {
        final SpecFormatter sf =
            SpecFormatter.with(SpecFormatter.retrieveEntryNameWithoutSuffix(entryName));
        final String classFullNamePoint =
            sf.retrieveInnerSpecAs(sf.retrieveWithPoint(), ".");

        return classFullNamePoint;
    }

    @Override
    public void accept(ZipEntry newEntry, InputStream newEntryBis) {
        final String newEntryName = newEntry.getName().trim();
        final String clzNameWithPoint = toNameWithPoint(newEntryName);

        if (!matched(newEntryName)) {
            message("Not accept due to unmatched: " + newEntryName);
            super.accept(newEntry, newEntryBis);
            return;
        }

        final BasePointcutCollections pointcuts = retrievePointcuts(newEntryName);
        final java.util.Collection<? extends BasePointcutCollections> matchedPointcuts = retrieveMatchedPointcuts(newEntryName);
        final boolean noPointcuts =
            null == pointcuts || 0 == pointcuts.getPoints().size();
        final boolean noMatchedPointcuts =
            null == matchedPointcuts || 0 == matchedPointcuts.size();
        final boolean emptyPointcuts = noPointcuts && noMatchedPointcuts;

        if (emptyPointcuts) {
            fatal("Trying accept matched entry but failed to retrieve pointcuts. " +
                    newEntryName);
            return;
        }

        try (final InjectorImpl injector = InjectorImpl.get();
            final CtClassWrapper ctClassWrapper = InjectorImpl.ClazzLoader
                .makeClass(injector.retrieveClassPool(), newEntryBis, clzNameWithPoint);) {
            // TODO: Assert CtClassWrapper class name equals with clzNameWithPoint;
            final String loadedClassName = ctClassWrapper.getCtName();
            message("Accept: ClazzLoader success load " + loadedClassName);

            final boolean skip = !InjectorImpl.Translator.support(ctClassWrapper.retrieveCtClass());
            if (skip)
                message("Accept: Skip " + loadedClassName);

            if (!skip && !noMatchedPointcuts) {
                final javassist.CtMethod[] methods = ctClassWrapper.retrieveCtClass().getDeclaredMethods();
                final java.util.List<InjectorImpl.InjectTarget> injectTargets = new java.util.LinkedList<>();

                matchedPointcuts.forEach(c -> {
                    // An unnessary identifier matched check.
                    // if (!matched) return;

                    if (null == c || 0 == c.getPoints().size())
                        return;

                    // Traversing the methods of class to generate InjectTarget that matched.
                    c.getPoints().forEach(p -> {
                        final BaseMethodPointcut pointcut = (BaseMethodPointcut) p;
                        final Pattern pattern = Pattern.compile(pointcut.getMethodName().toString());
                        int index = -1;
                        for (javassist.CtMethod method : methods) {
                            ++index;

                            if (Main.DEBUG)
                                message("\t- Method " + method.getName() + " checking by " + pattern.pattern());

                            if (!pattern.matcher(method.getName()).matches())
                                continue;

                            message("\tAccept: Method matched " + method.getName());

                            injectTargets.add(InjectorImpl.InjectTarget.with(ctClassWrapper,
                                        methods[index], null,
                                        pointcut.getInsertBefore(),
                                        pointcut.getInsertAfter()));
                        }
                    });
                });

                injectTargets.forEach(InjectorImpl.Translator::performTargetInject);
            }

            if (!skip && !noPointcuts) {
                final java.util.List<? extends InjectorImpl.InjectTarget> injectTargets =
                    InjectorImpl.InjectTarget.with(ctClassWrapper, pointcuts.getPoints());
                if (0 == injectTargets.size())
                    fatal("Trying accept matched and loaded class but failed to retrieve inject targets. " +
                            " Pointcuts size: " + pointcuts.getPoints().size());

                injectTargets.forEach(InjectorImpl.Translator::performTargetInject);
            }

            boolean flag = false;
            try (final java.io.ByteArrayInputStream bais =
                    new java.io.ByteArrayInputStream(ctClassWrapper.retrieveCtClass().toBytecode());) {
                super.accept(newEntry, bais);
                flag = !flag;
            } catch (javassist.CannotCompileException | IOException e) {
                fatal("Accept: Cannot generate injected class. " + e);
            } finally {
                message(String.format("Accept: finally finished: %s [%s]",
                            clzNameWithPoint, flag ? "SUCCESS" : "FAILED"));
            }
        } catch (Utils.FatalMessageException e) {
            fatal("Accept: fatal error... " + e);
        }
    }

    @Override
    public void close() {
        super.close();
    }

    private static final void message(CharSequence cs) {
        Utils.message(TAG, cs.toString());
    }

    private static final void fatal(CharSequence cs) {
        Utils.fatal(TAG, cs.toString());
    }
}


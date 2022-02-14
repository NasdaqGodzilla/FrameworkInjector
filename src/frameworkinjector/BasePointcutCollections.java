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
 * Filename     @ BasePointcutCollections.java
 * Create date  @ 2021-12-24 16:05:43
 * Description  @ 保存切点的集合的基类。
 * version      @ V1.0.0
 */

package peacemaker.frameworkinjector;

public abstract class BasePointcutCollections<E extends BaseMethodPointcut> {
    protected final java.util.AbstractCollection<E> mPoints;

    // 子类根据业务逻辑[选择]将集合内的元素[add]到自己的mPoints内
    public interface Kitchen<E> { // 厨房
        // [选择]闻起来挺香，子类业务逻辑允许[add]，返回true
        boolean smellsGood(E e);
        // [add]吃了该元素，若子类将e加入mPoints返回true
        boolean ate(E e);
        // 添加list内所有smellsGood的元素到mPoints内，若滴水未进则返回false
        boolean ate(java.util.List<E> list);
    }

    public BasePointcutCollections(java.util.AbstractCollection<E> c) {
        mPoints = c;
    }

    public String getStyledIdentifier() {
        assert false : "Code here would never run.";

        return "";
    }

    public java.util.AbstractCollection<E> getPoints() {
        return mPoints;
    }

    public final void addSilent(E e) {
        try {
            add(e);
        } catch (java.lang.RuntimeException ex) {
            Utils.message("", "" + ex);
        }
    }

    public final void add(E e) throws java.lang.RuntimeException {
        mPoints.add(e);
    }

    public final boolean containsSilent(E e) {
        try {
            return contains(e);
        } catch (java.lang.RuntimeException ex) {
            Utils.message("", "" + ex);
        }
        return false;
    }

    public final boolean contains(E e) throws java.lang.RuntimeException {
        return mPoints.contains(e);
    }

    public abstract String dumpWorld();

    public abstract int getElementsCount();
}


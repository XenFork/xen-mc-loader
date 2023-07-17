package union.xenfork.xenmc.util;

import cn.hutool.core.map.BiMap;

import java.util.Map;

public class XenBiMap<K, V> extends BiMap<K, V> {
    /**
     * 构造
     *
     * @param raw 被包装的Map
     */
    public XenBiMap(Map<K, V> raw) {
        super(raw);
    }


    public K getKeyOrDefault(V value, K defaultValue) {
        return getInverse().getOrDefault(value, defaultValue);
    }
}

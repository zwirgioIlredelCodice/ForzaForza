package connectx.ForzaForza;

public class CacheTable <K, V> {
    private static final int DEFAULT_SIZE = 10000;
    
    private int size;
    private Data <K, V>[] table;

    public CacheTable() {
        this(DEFAULT_SIZE);
    }

    public CacheTable(int size) {
        this.size = size;
        this.table = new Data[size];
    }

    private int hash(K key) {
        return key.hashCode() % size;
    }

    public void put(K key, V value) {
        int index = hash(key);
        Data <K, V> data = table[index];
        data.key = key;
        data.value = value;
    }

    public V get(K key) {
        int index = hash(key);
        Data <K, V> data = table[index];
        if (data.key.equals(key)) {
            return data.value;
        }
        return null;
    }
}

class Data <K, V> {
    public K key;
    public V value;

    public Data() {
        this(null, null);
    }

    public Data(K key, V value) {
        this.key = key;
        this.value = value;
    }
}

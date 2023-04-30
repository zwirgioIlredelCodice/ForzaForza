package connectx.ForzaForza;

public class CacheTable <K, V> {
    private static final int DEFAULT_SIZE = 50000;
    
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
        int hc = key.hashCode();
        int rem = hc % size;
        return Math.abs(rem);
    }

    public void put(K key, V value) {
        int index = hash(key);
        Data <K, V> data = new Data <K, V> (key, value);
        table[index] = data;
    }

    public V get(K key) {
        int index = hash(key);
        Data <K, V> data = table[index];
        
        if (data != null) {
            if (data.key.equals(key)) {
                return data.value;
            }
            return null;
        }
        return null;
    }

    public void reset() {
        this.table = new Data[size];
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

package connectx.ForzaForza;

import java.math.BigInteger;

public class CacheTable {
    private static final int DEFAULT_SIZE = 50000;
    
    private int size;
    private Data [] table;

    public CacheTable() {
        this(DEFAULT_SIZE);
    }

    public CacheTable(int size) {
        this.size = size;
        this.table = new Data[size];
    }

    private int hash(BigInteger key) {
        int hc = key.hashCode();
        int rem = hc % size;
        return Math.abs(rem);
    }

    public void put(BigInteger key, Score value) {
        int index = hash(key);
        Data data = new Data(key, value);
        table[index] = data;
    }

    public Score get(BigInteger key) {
        int index = hash(key);
        Data data = table[index];
        
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

class Data {
    public BigInteger key;
    public Score value;

    public Data() {
        this(null, null);
    }

    public Data(BigInteger key, Score value) {
        this.key = key;
        this.value = value;
    }
}

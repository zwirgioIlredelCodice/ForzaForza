package connectx.ForzaForza;


/**
 * Lo scopo di questa classe è di salvare il punteggio delle 
 * scaccheire già calcolate in modo da avere immediatamente il loro punteggio
 * se richiesto.
 * 
 * il salvataggio dei punteggi viene effetuato in una struttura dati simile
 * ad una tabella ad accesso e una tabella hash diretto dove però la grandezza 
 * della tabella è fissata.
 * In caso di collisione si sovrascrive il dato con il più recente in modo da
 * tenere i punteggi delle scacchiere più recenti, questa tabella emula il funzionamento 
 * delle memorie cache dei processori.
 */
public class CacheTable {

    /**
     * grandezza di default della tabella, occupa circa 1.3GB di ram su Linux
     */
    private static final int DEFAULT_SIZE = 100000;
    
    private int size;
    private Data [] table;

    public CacheTable() {
        this(DEFAULT_SIZE);
    }

    public CacheTable(int size) {
        this.size = size;
        this.table = new Data[size];
    }

    /**
     * metodo che data una chiave rappresentante una scacchiera 
     * ritorna la posizione in cui si potrebbe trovare il punteggio 
     * ad essa associata nella tabella
     */
    private int hash(long key) {
        int rem = (int) (key % size);
        return Math.abs(rem);
    }

    /**
     * aggiunge una posizione di gioco alla tabella
     * @param key la chiave della posizione di gioco
     * @param value il valore calcolato per quella posizione
     */
    public void put(long key, Score value) {
        int index = hash(key);
        Data data = new Data(key, value);
        table[index] = data;
    }

    /**
     * @param key la chiave della posizione di gioco
     * @return il valore associato a quella posizione, se non è presente restituisce null
     */
    public Score get(long key) {
        int index = hash(key);
        Data data = table[index];
        
        if (data != null) {
            if (data.key == key) {
                return data.value;
            }
            return null;
        }
        return null;
    }

    /**
     * effettua il reset della tabella
     */
    public void reset() {
        this.table = new Data[size];
    }
}

class Data {
    public long key;
    public Score value;

    public Data() {
        this(0, null);
    }

    public Data(long key, Score value) {
        this.key = key;
        this.value = value;
    }
}

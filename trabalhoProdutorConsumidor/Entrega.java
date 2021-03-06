import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.Semaphore;

public class Entrega extends Thread {

    private char id_transp;
    private Produto[] entrega;
    private int[] entrega_idx = { 0, 0 };
    private Semaphore mtx_entrega, itens_entrega, espacos_entrega;

    public Entrega(char id_transp, Produto[] delivery_array, int[] index, Semaphore mutex, Semaphore itens,
            Semaphore espacos_entrega) {
        /**
         * @param delivery_array: delivery output array
         * @param mutex:          consumer mutex;
         * @param itens:          consumer semaphore for available item
         * @param espacos:        consumer semphore for available index in
         *                        delivery_array
         */
        this.id_transp = id_transp;
        this.entrega = delivery_array;
        this.entrega_idx = index;
        this.mtx_entrega = mutex;
        this.itens_entrega = itens;
        this.espacos_entrega = espacos_entrega;
    }

    public void run() {
        try {
            while (true) {
                itens_entrega.acquire();
                mtx_entrega.acquire();
                Produto k = entrega[entrega_idx[0]];
                entrega_idx[0] = (entrega_idx[0] + 1) % entrega.length;
                mtx_entrega.release();
                Timer.deliverytimer(id_transp);
                long currentTime = System.currentTimeMillis() - k.getstartTime();
                escrever(currentTime);
                espacos_entrega.release();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void escrever(long tempoEntrega) throws IOException {
        FileWriter fw = new FileWriter("tempoEntrega.txt", true);
        BufferedWriter bw = new BufferedWriter(fw);
        PrintWriter pw = new PrintWriter(bw);
        pw.println(tempoEntrega);
        pw.flush();
        pw.close();
        bw.close();
        fw.close();

    }

}
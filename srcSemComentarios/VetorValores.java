import java.util.Random;

public class VetorValores {
    private int[] valores;
    private int tamanho;

    public VetorValores(int tamanho){
        this.tamanho = tamanho;
        valores = new int[tamanho];

        int min = 100000000;
        int max = 999999999;
        Random rand = new Random(987654321);

        for(int i = 0; i < tamanho; i++){
            valores[i] = rand.nextInt(max - min + 1) + min;
        }
    }
    public int[] getVetor(){
        return valores;
    }

    public int getTamanho(){
        return tamanho;
    }
}

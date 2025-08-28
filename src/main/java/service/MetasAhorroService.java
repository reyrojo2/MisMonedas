package service;
import model.MetasAhorro;

public class MetasAhorroService {

	
    public double calcularProgreso(MetasAhorro meta) {
        
        return (meta.getMontoActual() / meta.getMontoObjetivo()) * 100;
    }
    
    public boolean metaCumplida(MetasAhorro meta) {
        return calcularProgreso(meta) >= 100;
    }
}

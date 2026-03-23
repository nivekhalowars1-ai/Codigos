
package simulacionred;

import java.util.Random;

/**
 *
 * @author KEN
 */
public class SimulacionRed {

    
    public static void main(String[] args) {
        
        Random aleatorio = new Random();
        
        // --- 1. CONFIGURACIÓN (Ajustes de la Red) ---
        int numSimul = 10000;    // MONTE CARLO: Cuántas veces se repite el experimento.
        int mediaPaq = 40;       // POISSON: El tráfico promedio de paquetes * simulación.
        
        int posibilidadPaq = 21; // AZAR: Rango de variación (del 0 al X + 1) para el tráfico.
        int limiteRed = 41;      // CAPACIDAD: Si llegan más de X paquetes, la red se satura.
        
        double fallo = 0.10;     // BERNOULLI: X% de probabilidad EN DECIMAL de que un paquete se dañe. ej. 0.20 = 20%

        // --- 2. CONTADORES (Resultados acumulados) ---
        int totalEnviados = 0;
        int totalPerdidos = 0;
        int totalCongestion = 0;

        // --- 3. SIMULACIÓN (El paso del tiempo) ---
        for (int seg = 0; seg < numSimul; seg++) { 
            
            // POISSON: Calcula el tráfico en X simulación (entre 30 y 50 paquetes).
            // nextInt(posibilidadPaq es igual a X valor que pongamos) - 10 [EL -10 es para tener
            // un cálculo uniforme de la media.
            int paqEnEstaSIM = mediaPaq + (aleatorio.nextInt(posibilidadPaq) - 10);
            totalEnviados += paqEnEstaSIM;

            // CONGESTIÓN: Lo que el tráfico de esta simulación superó lo que la red soporta.
            if (paqEnEstaSIM > limiteRed) {
                totalCongestion++;
            }

            // BERNOULLI: Revisa la calidad del paquete por paquete.
            for (int paq = 0; paq < paqEnEstaSIM; paq++) {
                // Si el número al azar es menor a 0.10, el paquete se pierde.
                if (aleatorio.nextDouble() < fallo) {
                    totalPerdidos++;
                }
            }
        }

        // --- 4. CÁLCULOS FINALES ---
        // Multiplicamos por 100.0 para obtener decimales y calcular porcentajes.
        double tasaPerdida = (totalPerdidos * 100.0) / totalEnviados;
        double tasaCongestion = (totalCongestion * 100.0) / numSimul;
        
        // Eficiencia: Restamos tanto los paquetes dañados como los bloqueos por saturación.
        double eficiencia = 100.0 - tasaPerdida - tasaCongestion;

        // --- 5. EL FINAL ---
        System.out.println("====== REPORTE DE RENDIMIENTO DE LA RED ========");
        System.out.println("Número de simulaciones: " + numSimul);
        System.out.println("Total de paquetes procesados: " + totalEnviados);
        System.out.println("Simulaciones con saturación: " + totalCongestion);
        System.out.printf("Pérdida por calidad: %.2f%%\n", tasaPerdida);
        System.out.printf("Pérdida por saturación: %.2f%%\n", tasaCongestion);
        System.out.printf("Eficiencia Total: %.2f%%\n", eficiencia);
        System.out.println("================================================");
    }
}

package sistemapanelessolares.app;

public class main {

    public static void main(String[] args) {
        Casa casa = new Casa("Calle 10 # 5–15", "Valledupar", 400, 0.0, 0.0);
        PanelSolar panel = new PanelSolar("300W Mono", 300, 0.19, 250);
        CalculadoraPanels calc = new CalculadoraPanels(casa, panel, 1200);

        System.out.println(calc.generarResumen());
    }
}

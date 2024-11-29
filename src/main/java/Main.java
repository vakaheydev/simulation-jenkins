import lombok.extern.slf4j.Slf4j;
import simulation.Simulation;
import simulation.concurrent.ConcurrentSimulation;

@Slf4j
public class Main {
    public static void main(String[] args) {
        log.info("<--- Start of simulation --->");
        getSimulation().start();
        log.info("<--- End of simulation --->");
    }

    public static Simulation getSimulation() {
        return new ConcurrentSimulation();
    }
}

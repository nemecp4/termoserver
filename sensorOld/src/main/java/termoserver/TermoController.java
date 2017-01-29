package termoserver;

/**
 * Controller (think M-V-C) for TermoServer
 *
 */
public interface TermoController {

	void update();

	void setActiveGraph(GraphTiming timing);

	void updateGraph(GraphTiming hour5);

}

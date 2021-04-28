package it.polito.tdp.artsmia.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.artsmia.db.ArtsmiaDAO;

public class Model {

	private Graph<ArtObject, DefaultWeightedEdge> grafo;
	private ArtsmiaDAO dao;
	//Identity Map 
	private Map<Integer, ArtObject> idMap;
	
	public Model() {
		dao = new ArtsmiaDAO();
		idMap = new HashMap<Integer, ArtObject>();
	}
	
	public void creaGrafo() {
		//meglio qua perchè se no tutte le volte creo grafo da altro grafo
		grafo = new SimpleWeightedGraph<ArtObject,DefaultWeightedEdge>(DefaultWeightedEdge.class);
		
		//Aggiunger vertici
		dao.listObjects(idMap);
		Graphs.addAllVertices(grafo, idMap.values());
		
		//Aggiungere gli archi
		//APPROCCIO 1 -> doppio ciclo for per vedere se due vertici si devono collegare
		/*for(ArtObject a1: this.grafo.vertexSet()) {
			for(ArtObject a2: this.grafo.vertexSet()) {
				if(!a1.equals(a2) && !this.grafo.containsEdge(a1,a2)){
					//oggetti che siano diversi e che non ci sia già un arco
					int pesoArco = dao.getPeso(a1, a2);
					if(pesoArco > 0)
						Graphs.addEdge(this.grafo, a1, a2, pesoArco);
				}
			}
		}*/
		//Stampa tra 620 giorni => l'approccio 1 in questo caso non va bene
		
		//APPROCCIO 3 -> faccio fare gli archi al database
		for(Adiacenza a: dao.getAdiacenze())
			Graphs.addEdge(this.grafo, idMap.get(a.getId1()), idMap.get(a.getId2()), a.getPeso());
		//recupero l'oggetto da idMap dall'id
		
		System.out.println("GRAFO CREATO");
		System.out.println("#Vertici " + grafo.vertexSet().size());
		System.out.println("#Archi " + grafo.edgeSet().size());
	}
	
}

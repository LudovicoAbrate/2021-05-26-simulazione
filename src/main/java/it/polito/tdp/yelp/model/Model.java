package it.polito.tdp.yelp.model;

import java.time.Year;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.yelp.db.YelpDao;

public class Model {
	
	YelpDao dao;
	Graph<Business,DefaultWeightedEdge> grafo;
	List<Business> listaVertici ;
	Map<String,Business> idMap;
	
	public Model() {
		this.dao = new YelpDao();
		this.listaVertici = new ArrayList<>();
		this.idMap= new HashMap<>();
	}
	
	public void creaGrafo(Year anno,String citta) {
		
		this.dao = new YelpDao();
		this.listaVertici = new ArrayList<>();
		this.idMap= new HashMap<>();
		
		idMap.clear();
		listaVertici.clear();
		for(Business v: dao.getVertici(anno, citta)) {
			idMap.put(v.getBusinessId(), v);
			listaVertici.add(v);
		}
		
		this.grafo = new SimpleDirectedWeightedGraph<Business,DefaultWeightedEdge>(DefaultWeightedEdge.class);
		
		Graphs.addAllVertices(this.grafo, listaVertici);
		
		for (Adiacenza a : dao.getAdiacenze(anno, citta)) {
			
			Business b1 = idMap.get(a.s1);
			Business b2 = idMap.get(a.s2);
			
			
			
			
			if(a.peso > 0 ) 
				Graphs.addEdgeWithVertices(this.grafo, b2, b1, Math.abs(a.peso));
			if(a.peso<0 )
				Graphs.addEdgeWithVertices(this.grafo, b1, b2, Math.abs(a.peso));
			
			
		}
		}
	
	public Business getLocaleMigliore(){
		
		
		Business b = null;
		double migliore = 0.0;
		
		for( Business b1: this.grafo.vertexSet()) {
			
			double entranti = 0.0;
			double uscenti = 0.0;
			double somma = 0.0;
			
			for(DefaultWeightedEdge e : this.grafo.outgoingEdgesOf(b1)) {
				
				uscenti = uscenti + this.grafo.getEdgeWeight(e);
			}
			for (DefaultWeightedEdge e : this.grafo.incomingEdgesOf(b1)) {
				
				entranti = entranti + this.grafo.getEdgeWeight(e);
			}
			
			somma = entranti - uscenti;
			
			if(somma > migliore) {
				
				migliore = somma;
				b= b1;
			}
			
			
			
		}
		
		
		return b ;
		
		
	}

	
	
	public List<String> getCities(){
		
	return dao.getCities();
		
	}
	
	public int nArchi() {
		return this.grafo.edgeSet().size();
	}
	public int nVertici() {
		return this.grafo.vertexSet().size();
	}
}


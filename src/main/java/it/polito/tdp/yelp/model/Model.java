package it.polito.tdp.yelp.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import com.javadocmd.simplelatlng.LatLngTool;
import com.javadocmd.simplelatlng.util.LengthUnit;

import it.polito.tdp.yelp.db.YelpDao;

public class Model {
	
	private Graph<Business, DefaultWeightedEdge> grafo;
	private YelpDao dao;
	private List<Business> listaBusiness;
	private List<String> listaCitta;
	private Double distanzaMax;
	private Set<Business> setMigliore;
	
	public Model() {
		this.dao = new YelpDao();
	}
	
	public void creaGrafo(String citta) {
		
		this.grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		this.listaBusiness = new ArrayList<>(this.dao.getBusinessInCity(citta));
		Graphs.addAllVertices(this.grafo, this.listaBusiness);
		for(Business b1 : this.listaBusiness) {
			for(Business b2 : this.listaBusiness) {
				if(b1.compareTo(b2) > 0) {
					Double km = Math.abs(LatLngTool.distance(b1.getCoordinate(), b2.getCoordinate(), LengthUnit.KILOMETER));
					Graphs.addEdge(this.grafo, b1, b2, km);
				}
			}
		}
		
	}
	
	public Business getLocaleDistante(Business business) {
		Business distante = null;
		List<Business> listaAdiacenti = Graphs.neighborListOf(this.grafo, business);
		this.distanzaMax = 0.0;
		for(Business b : listaAdiacenti) {
//			Double km = Math.abs(LatLngTool.distance(business.getCoordinate(), b.getCoordinate(), LengthUnit.KILOMETER));
			Double km = this.grafo.getEdgeWeight(this.grafo.getEdge(business, b));
			
			if(km > this.distanzaMax) {
				this.distanzaMax = km;
				distante = b;
			}
		}
		return distante;
	}
	
	public Set<Business> trovaSequenza(Business b1, Business b2, Double soglia) {
		
		this.setMigliore = new LinkedHashSet<>();
		//calcolo distanza solo alla fine, il parametro e' size
		Set<Business> setAttuale = new LinkedHashSet<>();
		Set<Business> setDepurato = new HashSet<>(this.grafo.vertexSet());
		setDepurato.remove(b1);
		setDepurato.remove(b2); //lo aggiungero alla fine
		
		setAttuale.add(b1);
		cerca(setAttuale, setDepurato, soglia);
		this.setMigliore.add(b2);
		
		return setMigliore;
	}
	
	
	
	private void cerca(Set<Business> setAttuale, Set<Business> setDepurato, Double soglia) {
		//genero solo soluzioni valide
		if(setAttuale.size() > this.setMigliore.size()) {
			this.setMigliore = new LinkedHashSet<>(setAttuale);
		}
		
		for(Business b : setDepurato) {
			if( /* !setAttuale.contains(b) && */ b.getStars() > soglia) {
				//alleggerisco set ricorsivo, evitando il contains
				Set<Business> depuratoRicorsivo = new HashSet<>(setDepurato);
				depuratoRicorsivo.remove(b);
				//aggiungo, esploro e rimuovo
				setAttuale.add(b);
				cerca(setAttuale, depuratoRicorsivo, soglia);
				setAttuale.remove(b);
			}
		}
		
	}

	public double getDistanzaMax() {
		return this.distanzaMax;
	}

	public List<Business> getListaBusiness(){
		return this.listaBusiness;
	}
	
	public int getNumeroVertici() {
		return this.grafo.vertexSet().size();
	}
	
	public int getNumeroArchi() {
		return this.grafo.edgeSet().size();
	}
	
	public List<String> getAllCities(){
		if(this.listaCitta == null) {
			this.listaCitta = new ArrayList<>(this.dao.getAllCities());
		}
		return this.listaCitta;
	}

	public Double getDistanzaTotale(Set<Business> sequenzaMigliore) {
		Double tot = 0.0;
		List<Business> listaMigliore = new ArrayList<>(sequenzaMigliore);
		
		for(int i=1; i<listaMigliore.size(); i++) {
			Business b1 = listaMigliore.get(i-1);
			Business b2 = listaMigliore.get(i);
			DefaultWeightedEdge e = this.grafo.getEdge(b1, b2);
			tot+=this.grafo.getEdgeWeight(e);
		}
		
		return tot;
	}
	
	
}

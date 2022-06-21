package it.polito.tdp.yelp.model;

import com.javadocmd.simplelatlng.LatLng;

public class Business implements Comparable<Business>{
	private String businessId;
	private String businessName;
	private String city;
	private double stars;
	private LatLng coordinate;
	
	
	public Business(String businessId, String businessName, String city, double stars, double latitude, double longitude) {
		super();
		this.businessId = businessId;
		this.businessName = businessName;
		this.city = city;
		this.stars = stars;
		this.coordinate = new LatLng(latitude, longitude);
	}
	
	

	public String getBusinessId() {
		return businessId;
	}



	public String getCity() {
		return city;
	}



	public String getBusinessName() {
		return businessName;
	}



	public double getStars() {
		return stars;
	}



	public LatLng getCoordinate() {
		return coordinate;
	}



	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((businessId == null) ? 0 : businessId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Business other = (Business) obj;
		if (businessId == null) {
			if (other.businessId != null)
				return false;
		} else if (!businessId.equals(other.businessId))
			return false;
		return true;
	}



	@Override
	public String toString() {
		return businessName;
	}



	@Override
	public int compareTo(Business o) {
		// TODO Auto-generated method stub
		return this.businessId.compareTo(o.getBusinessId());
	}


	
}

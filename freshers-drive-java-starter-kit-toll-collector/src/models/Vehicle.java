package models;
import java.util.Objects;

public class Vehicle {
	
	private String type;
	private String licence;
	
	public Vehicle(String type, String licence) {
		super();
		this.type = type;
		this.licence = licence;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(licence);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Vehicle other = (Vehicle) obj;
		return Objects.equals(licence, other.licence);
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getLicence() {
		return licence;
	}

	public void setLicence(String licence) {
		this.licence = licence;
	}

}

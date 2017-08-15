package CentralObjects;

import java.util.List;

public class GeoSearchDTO {
	public String id;
	public String URL;
	public String place_type;
	public String name;
	public String full_name;
	public String country_code;
	public String country;
	public List<ContainedWithinTwitter> AreaContained;
	public double[] centroid;
	public BoundingBoxTwitter bounding_box;
}

package portfolioManagement;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class Portfolio {

    private String id;
    private String name;
    private String admin;

    private ArrayList<SupplyPoint> supplyPoints = new ArrayList<>();

    public Portfolio(JSONObject portfolio){
        id = portfolio.getString("id");
        name = portfolio.getString("name");
        admin = portfolio.getString("admin");
        JSONArray supplyPointsArray = portfolio.getJSONArray("portfolio");
        Iterator<Object> it = supplyPointsArray.iterator();
        while (it.hasNext()){
            JSONObject supplyPoint = (JSONObject) it.next();
            supplyPoints.add(new SupplyPoint(supplyPoint.getString("ean"),supplyPoint.getString("supplier")));
        }
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAdmin() {
        return admin;
    }

    public ArrayList<SupplyPoint> getSupplyPoints() {
        return supplyPoints;
    }
}
